package it.unical.progetto.igpe.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import it.unical.progetto.igpe.core.SteelWall;
import it.unical.progetto.igpe.core.Tree;
import it.unical.progetto.igpe.core.AbstractDynamicObject;
import it.unical.progetto.igpe.core.AbstractStaticObject;
import it.unical.progetto.igpe.core.ArmorTank;
import it.unical.progetto.igpe.core.BasicTank;
import it.unical.progetto.igpe.core.BrickWall;
import it.unical.progetto.igpe.core.Direction;
import it.unical.progetto.igpe.core.EnemyTank;
import it.unical.progetto.igpe.core.FastTank;
import it.unical.progetto.igpe.core.Flag;
import it.unical.progetto.igpe.core.GameManager;
import it.unical.progetto.igpe.core.Ice;
import it.unical.progetto.igpe.core.PlayerTank;
import it.unical.progetto.igpe.core.Power;
import it.unical.progetto.igpe.core.PowerTank;
import it.unical.progetto.igpe.core.PowerUp;
import it.unical.progetto.igpe.core.Rocket;
import it.unical.progetto.igpe.core.Tank;
import it.unical.progetto.igpe.core.Water;
import it.unical.progetto.igpe.net.ConnectionManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private static double tempFPS;

	private int tile;
	private int shift;
	private long start;
	private double end;
	private int exitDelay;
	private int cursorPositionDialog;
	private long limit = 120;

	private Long longTime;
	private JDialog dialog;
	private JButton[] buttons;
	private GameManager game;
	private String difficult;
	private String playerName;

	private Timer timer;
	private PanelSwitcher switcher;
	private FullGamePanel fullGamePanel;
	private ConnectionManager connectionManager;
	private List<Color> labelForNameOfPlayersColor;

	// online
	public GamePanel(PanelSwitcher switcher, String difficult) {
		this.tile = 35;
		exitDelay = 500;
		this.dialog = new JDialog(((MainFrame) switcher));
		this.setBackground(Color.BLACK);
		this.longTime = 0L;
		tempFPS = 1.5d;
		this.shift = 17;
		this.difficult = difficult;
		this.setSwitcher(switcher);
		this.labelForNameOfPlayersColor = new ArrayList<>();
		for (int a = 0; a < 2; a++) {
			labelForNameOfPlayersColor.add(chooseColorOfLabel());
		}

		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {

				if (!SoundsProvider.stageStartClip.isActive()) {
					if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (!game.isPauseOptionDialog() && !game.isExit()) {
							((MainFrame) getSwitcher()).setTransparent(true);
							game.setPauseOptionDialog(true);
							option();
						}
					} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
						if (!game.isPaused() && !game.isExit()) {
							if (GameManager.offline)
								SoundsProvider.playPause();
							game.setPaused(true);
						} else {
							game.setPaused(false);
						}
					}

				}

				int keyCode = event.getKeyCode();
				// MULTIPLAYER
				if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
					if (!game.isPauseOptionDialog()) {
						if (!game.getPlayersArray().get(0).getKeyBits().get(keyCode)
								&& !game.getPlayersArray().get(0).isPressed())
							game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
					}
				}
				game.getPlayersArray().get(0).getKeyBits().set(keyCode);
				connectionManager
						.dispatch(getUpdateMessage(event, "YES", game.getPlayersArray().get(0).getKeyPressedMillis(),
								game.getPlayersArray().get(0).isReleaseKeyRocket(), game.isPauseOptionDialog(),
								game.isPaused()));
			}

			@Override
			public void keyReleased(final KeyEvent event) {

				int keyCode = event.getKeyCode();

				if (keyCode == 32) {
					game.getPlayersArray().get(0).setReleaseKeyRocket(false);
				}

				game.getPlayersArray().get(0).getKeyBits().clear(keyCode);
				connectionManager
						.dispatch(getUpdateMessage(event, "NO", game.getPlayersArray().get(0).getKeyPressedMillis(),
								game.getPlayersArray().get(0).isReleaseKeyRocket(), game.isPauseOptionDialog(),
								game.isPaused()));
			}
		});

		new Thread("TIME") {
			public void run() {
				boolean run = true;
				while (run) {
					if (connectionManager != null)
						connectionManager.dispatch(getUpdateTime(System.currentTimeMillis()));
					if (game != null && game.isExit()) {
						run = false;
					}
					try {
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	// offline
	public GamePanel(final int w, final int h, PanelSwitcher switcher, GameManager game) {

		this.setPreferredSize(new Dimension(w, h));
		this.setGame(game);
		this.shift = 17;
		exitDelay = 500;
		this.tile = 35;
		this.dialog = new JDialog(((MainFrame) switcher));
		this.setSwitcher(switcher);
		this.setBackground(Color.BLACK);
		this.longTime = 0L;
		cursorPositionDialog = 0;
		tempFPS = 1.5d;

		if (game.getPlayersArray().size() == 1) {

			loadScoreSingle();
		} else
			loadScoreMulti();

		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {

				if (!SoundsProvider.stageStartClip.isActive()) {
					if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (!game.isPauseOptionDialog() && !game.isExit()) {
							((MainFrame) getSwitcher()).setTransparent(true);
							game.setPauseOptionDialog(true);
							if (game.getPlayersArray().size() > 1) {
								game.getPlayersArray().get(0).getKeys().clear();
								game.getPlayersArray().get(1).getKeys().clear();
								game.getPlayersArray().get(1).getKeyBits().clear();
							} else {
								game.getPlayersArray().get(0).getKeys().clear();
							}
							game.getPlayersArray().get(0).getKeyBits().clear();

							option();
						}
					} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {

						if (!game.isPaused() && !game.isExit()) {

							SoundsProvider.playPause();
							if (game.getPlayersArray().size() > 1) {
								game.getPlayersArray().get(0).getKeys().clear();
								game.getPlayersArray().get(1).getKeys().clear();
								game.getPlayersArray().get(1).getKeyBits().clear();
							} else {
								game.getPlayersArray().get(0).getKeys().clear();
							}
							game.getPlayersArray().get(0).getKeyBits().clear();

							game.setPaused(true);
						} else {
							game.setPaused(false);
						}
					}

				}

				int keyCode = event.getKeyCode();
				// MULTIPLAYER
				if (game.getPlayersArray().size() > 1 && GameManager.offline) {

					if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
						if (!game.isPauseOptionDialog()) {
							if (!game.getPlayersArray().get(0).getKeyBits().get(keyCode)
									&& !game.getPlayersArray().get(0).isPressed())
								game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
						}
					}

					if (game.getPlayersArray().get(1).getDefaultKeysPlayer().contains(keyCode) && keyCode != 17) {
						if (!game.isPauseOptionDialog()) {
							if (!game.getPlayersArray().get(1).getKeyBits().get(keyCode)
									&& !game.getPlayersArray().get(1).isPressed())
								game.getPlayersArray().get(1).setKeyPressedMillis(System.currentTimeMillis());
						}
					}
					game.getPlayersArray().get(0).getKeyBits().set(keyCode);
					game.getPlayersArray().get(1).getKeyBits().set(keyCode);

				} else { // SINGLEPLAYER
					if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
						if (!game.isPauseOptionDialog()) {
							if (!game.getPlayersArray().get(0).getKeyBits().get(keyCode)
									&& !game.getPlayersArray().get(0).isPressed())
								game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
						}
					}
					game.getPlayersArray().get(0).getKeyBits().set(keyCode);
				}
			}

			@Override
			public void keyReleased(final KeyEvent event) {

				int keyCode = event.getKeyCode();

				if (game.getPlayersArray().size() > 1) {
					if (keyCode == 17) {
						game.getPlayersArray().get(1).setReleaseKeyRocket(false);
					}
					if (keyCode == 32) {
						game.getPlayersArray().get(0).setReleaseKeyRocket(false);
					}

					game.getPlayersArray().get(0).getKeyBits().clear(keyCode);
					game.getPlayersArray().get(1).getKeyBits().clear(keyCode);
				} else {
					if (keyCode == 32) {
						game.getPlayersArray().get(0).setReleaseKeyRocket(false);
					}
					game.getPlayersArray().get(0).getKeyBits().clear(keyCode);
				}

			}
		});

		new Thread() {

			@Override
			public void run() {
				gameLoop();
			}
		}.start();
	}

	// ------------------------------- GAMELOOP ------------------------------------ //

	public void gameLoop() {
		while (!game.isExit()) {
			if (!game.isPaused()) {
				if (GameManager.offline)
					start = System.nanoTime();

				logic();
				graphic();
				if (!GameManager.offline) {
					game.getRunnable().run();
				}
				if (GameManager.offline) {
					longTime = (System.nanoTime() - start);
					end = (double) (longTime.doubleValue() / 1000000);
				}

			} else if (game.isPaused() || game.isPauseOptionDialog()) {
				if (GameManager.offline) { // IL SERVER NON DEVE RIPRODURRE IL SOUND
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
				}
				if (!GameManager.offline) {
					game.getRunnable().run();
				}
			}

			// if(GameManager.offline) {
			repaint();
			if (fullGamePanel != null) {
				fullGamePanel.repaint();
			}
			if (!GameManager.offline) {
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		repaint();
		if (fullGamePanel != null) {
			fullGamePanel.repaint();
		}
		if (GameManager.offline) {
			SoundsProvider.cancelMove();
			SoundsProvider.cancelStop();
			repaint();

			((MainFrame) switcher).setCurrentResumeP1(3);
			((MainFrame) switcher).setCurrentLevelP1(0);

			if (game.getPlayersArray().size() > 1) {
				((MainFrame) switcher).setCurrentResumeP2(3);
				((MainFrame) switcher).setCurrentLevelP2(0);
			}
			dialog.dispose();
			game.getTimer().cancel();
			game.getTimer2().cancel();
		}
	}

	private void removeEffect() {
		game.getLockEffect().lock();
		synchronized (this) {
			for (int i = 0; i < game.getEffects().size(); i++) {
				if ((game.getEffects().get(i) instanceof PlayerTank
						&& ((PlayerTank) (game.getEffects().get(i))).getInc() > 5)
						|| (game.getEffects().get(i) instanceof EnemyTank
								&& ((EnemyTank) (game.getEffects().get(i))).getInc() > 12)
						|| (game.getEffects().get(i) instanceof Rocket
								&& ((Rocket) (game.getEffects().get(i))).getInc() > 3)
						|| (game.getEffects().get(i) instanceof PowerUp
								&& ((PowerUp) (game.getEffects().get(i))).getInc() > 12)
						|| game.getEffects().get(i) instanceof Flag
								&& ((Flag) (game.getEffects().get(i))).getInc() > 5) {
					game.getEffects().remove(i--);
				}
			}
		}
		game.getLockEffect().unlock();
	}

	public void logic() {
		removeEffect();
		// RESET DELLE BOOLEANE PER IL SUONO
		resetBooleanForSounds();

		// MANAGE KEYS
		keyPresses();

		// UPDATE ROCKETS
		rockets();

		// UPDATE ENEMIES
		enemies();

		// UPDATE PLAYERS
		players();

		// MANAGE SOUNDS
		sounds();

		// GAME OVER / WIN
		gameOverOrWin();
	}

	public void graphic() {
		synchronized (this) {
			// ANIMATION ROCKET
			for (int a = 0; a < game.getRocket().size(); a++) {
				Rocket rocket = game.getRocket().get(a);

				if (game.collision(rocket)) {
					game.destroyRocket(rocket);
				} else {
					if (!game.getRocket().get(a).isUpdateObject() && game.getRocket().get(a).isRocketForPlayer()) {

						game.getRocket().get(a)
								.setCont(contFPS(game.getRocket().get(a), game.getRocket().get(a).getDirection(),
										game.getRocket().get(a).getCont(),
										game.returnSpeed(game.getRocket().get(a).getTank().getSpeedShot(),
												game.getRocket().get(a)),
										end));
					}

					game.getRocket().get(a).updateRect();

					if (!game.getRocket().get(a).getRect().intersects(game.getRocket().get(a).getTank().getRect())) {
						game.getRocket().get(a).setFirstAnimationNo(false);
					}

					if (game.getRocket().get(a).getCont() >= tile) {
						// prima di creare il secondo rocket devo
						// aver finito l animazione del primo
						if (game.getRocket().get(a).getTank() instanceof PlayerTank
								&& ((PlayerTank) game.getRocket().get(a).getTank()).isEnter()
								&& ((PlayerTank) game.getRocket().get(a).getTank()).getLevel() >= 1) {
							((PlayerTank) game.getRocket().get(a).getTank()).setFinish(true);
						}
						game.getRocket().get(a).FPS();
						if (!game.getRocket().get(a).isOnBorder())
							game.getRocket().get(a).setUpdateObject(true);
					}
				}
			}
		}

		// ANIMATION ENEMY
		for (int a = 0; a < game.getEnemy().size(); a++) {
			((Tank) game.getEnemy().get(a)).updateRect();
			if (game.getEnemy().get(a).isAppearsInTheMap() && !game.getEnemy().get(a).isNoUpdateG()
					&& !game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).canGo
					&& !game.getEnemy().get(a).isStopEnemyGraphic()) {

				game.getEnemy().get(a)
						.setCont(contFPS(game.getEnemy().get(a), game.getEnemy().get(a).getDirection(),
								game.getEnemy().get(a).getCont(),
								game.returnSpeed(game.getEnemy().get(a).getSpeed(), game.getEnemy().get(a)), end));
			}
			if ((game.getEnemy().get(a).getCont() >= tile || game.getEnemy().get(a).isNoUpdateG()
					|| !game.getEnemy().get(a).canGo)

					&& game.getEnemy().get(a).isAppearsInTheMap()) {

				if (game.getEnemy().get(a).isStopEnemy()) {
					game.getEnemy().get(a).setStopEnemyGraphic(true);
				}

				game.getEnemy().get(a).FPS();
				game.getEnemy().get(a).setUpdateObject(true);
			}
		}

		// ANIMATION PLAYER
		for (int a = 0; a < game.getPlayersArray().size(); a++) {

			// System.out.println(game.getPlayersArray().get(a).toString() + ":
			// "+ game.getPlayersArray().get(a).getKeyPressLength());

			if (noIntersectPlayerWithEnemy(a)) {
				((Tank) game.getPlayersArray().get(a)).updateRect();
				if (game.getPlayersArray().get(a).isPressed() && game.getPlayersArray().get(a).getKeyPressLength() != 0
						&& game.getPlayersArray().get(a).getKeyPressLength() > limit) {
					if (!game.getPlayersArray().get(a).isOldDirection()) {
						game.getPlayersArray().get(a)
								.setCont(contFPS(game.getPlayersArray().get(a),
										game.getPlayersArray().get(a).getTmpDirection(),
										game.getPlayersArray().get(a).getCont(),
										game.returnSpeed(game.getPlayersArray().get(a).getSpeed(),
												game.getPlayersArray().get(a)),
										end));
					} else {
						game.getPlayersArray().get(a)
								.setCont(contFPS(game.getPlayersArray().get(a), game.getPlayersArray().get(a).getDirection(),
										game.getPlayersArray().get(a).getCont(),
										game.returnSpeed(game.getPlayersArray().get(a).getSpeed(),
												game.getPlayersArray().get(a)),
										end));
					}
				}

				if (game.getPlayersArray().get(a).getCont() >= tile) {
					game.getPlayersArray().get(a).setxGraphics(game.getPlayersArray().get(a).getX() * tile);
					game.getPlayersArray().get(a).setyGraphics(game.getPlayersArray().get(a).getY() * tile);
					game.getPlayersArray().get(a).FPS();
					if (game.getPlayersArray().get(a).isOldDirection())
						game.getPlayersArray().get(a).setOldDirection(false);
					game.getPlayersArray().get(a).setOld(game.getPlayersArray().get(a).getTmpDirection());
					game.getPlayersArray().get(a).setPressed(false);
				}
			} 
		}

	}

	// --------------------------------- ONLINE ---------------------------------------- //

	protected String getUpdateMessage(KeyEvent code, String string, long getKeyPressedMillis,
			boolean isReleaseKeyRocket, boolean pauseOptionDialog, boolean paused) {
		return playerName + ":" + code.getKeyCode() + ":" + string + ":" + getKeyPressedMillis + ":"
				+ isReleaseKeyRocket + ":" + pauseOptionDialog + ":" + paused;
	}

	protected String getUpdatePaintComponent(boolean isWaitToExit) {
		return "PAINT" + ":" + isWaitToExit;
	}

	protected String getUpdateOptionPanel(String getNameOfGame) {
		return "EXIT" + ":" + getNameOfGame;
	}

	protected String getUpdateTime(long time) {
		return "TIME" + ":" + playerName + ":" + time;
	}

	private Color chooseColorOfLabel() {
		int R = (int) (Math.random() * 256);
		int G = (int) (Math.random() * 256);
		int B = (int) (Math.random() * 256);
		Color color = new Color(R, G, B);
		return color;
	}

	// SlideStage
	protected GameManager startNetwork(ConnectionManager connectionManager, JTextField filename) {
		this.connectionManager = connectionManager;
		playerName = connectionManager.getNameOfGame();
		System.out.println("GamePanel.startNetwork() " + playerName);
		requestFocus();
		game = new GameManager(filename, playerName);
		return game;
	}

	// ---------------------------------- OTHER ---------------------------------------- //
	private void changeRotationForIce(Tank t) {

		if (t.getDirection() == Direction.UP) {
			t.setRotateDegrees(0);
		} else if (t.getDirection() == Direction.DOWN) {
			t.setRotateDegrees(180);
		} else if (t.getDirection() == Direction.LEFT) {
			t.setRotateDegrees(270);
		} else if (t.getDirection() == Direction.RIGHT) {
			t.setRotateDegrees(90);
		}
		t.setOldDirection(true);
	}

	public void option() {
		dialog.setPreferredSize(new Dimension(250, 280));

		JPanel fullpanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (cursorPositionDialog == 0) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 82, this);
				} else if (cursorPositionDialog == 1) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 128, this);
				} else if (cursorPositionDialog == 2) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 174, this);
				} else {
					g.drawImage(ImageProvider.getCursorRight(), 30, 217, this);
				}
			}
		};

		JPanel text = new JPanel();
		JPanel buttonspanel = new JPanel(new GridLayout(4, 1));
		String[] buttonTxt = { "Retry", "Restart", "Menu", "Lobby" };
		JLabel label = new JLabel("Option");

		fullpanel.setPreferredSize(new Dimension(250, 350));
		fullpanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		fullpanel.setBackground(Color.BLACK);
		buttons = new JButton[buttonTxt.length];
		label.setFont(MainFrame.customFontB);
		label.setForeground(Color.RED);
		label.setBorder(null);
		text.add(label);
		text.setPreferredSize(new Dimension(200, 70));
		text.setMaximumSize(new Dimension(200, 70)); // set max = pref
		text.setBackground(Color.BLACK);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);

		for (int i = 0; i < buttonTxt.length; i++) {

			final int curRow = i;
			buttons[i] = new JButton(buttonTxt[i]);
			buttons[i].setFont(MainFrame.customFontM);
			buttons[i].setBackground(Color.BLACK);
			buttons[i].setBorder(null);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setFocusPainted(false);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setBorderPainted(false);
			buttons[i].setFocusPainted(false);
			buttons[i].addMouseListener(new MouseInputAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {

					if (e.getComponent().getY() == buttons[curRow].getY()) {
						cursorPositionDialog = curRow;
						repaint();
					}
				}
			});

			if (GameManager.offline) {
				if (i == 3) {
					buttons[i].setForeground(Color.DARK_GRAY);
				} else {
					buttons[i].setForeground(Color.WHITE);
				}
			}
			if (!GameManager.offline) {
				if (i == 1 || i == 2) {
					buttons[i].setForeground(Color.DARK_GRAY);
				} else {
					buttons[i].setForeground(Color.WHITE);
				}
			}

			buttons[i].addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						((MainFrame) getSwitcher()).setTransparent(false);
						game.setPauseOptionDialog(false);
						cursorPositionDialog = 0;
						dialog.dispose();
					}

					else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						SoundsProvider.playBulletHit1();
						if (curRow < 1) {
							buttons[buttons.length - 1].requestFocus();
							cursorPositionDialog = buttons.length - 1;

						} else {
							buttons[curRow - 1].requestFocus();
							cursorPositionDialog = curRow - 1;

						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						SoundsProvider.playBulletHit1();
						buttons[(curRow + 1) % buttons.length].requestFocus();
						cursorPositionDialog = (curRow + 1) % buttons.length;
					}
				}
			});

			buttonspanel.add(buttons[i]);
			optionActionListener(i);
		}

		buttonspanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonspanel.setPreferredSize(new Dimension(100, 180));
		buttonspanel.setBackground(Color.BLACK);
		fullpanel.add(text);
		fullpanel.add(buttonspanel);
		dialog.setContentPane(fullpanel);
		dialog.setUndecorated(true);
		// dialog.setResizable(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(((MainFrame) switcher));
		dialog.setVisible(true);

	}

	public void optionActionListener(int j) {

		switch (j) {
		case 0: // RETRY
			buttons[j].addActionListener(e -> {
				SoundsProvider.playBulletHit1();
				((MainFrame) getSwitcher()).setTransparent(false);
				game.setPauseOptionDialog(false);
				dialog.dispose();
			});
			break;
		case 1:// RESTART
			buttons[j].addActionListener(e -> {

				if (GameManager.offline) {
					SoundsProvider.playStageStart();
					SoundsProvider.playBulletHit1();
					((MainFrame) getSwitcher()).setTransparent(false);
					game.setExit(true);
					dialog.dispose();
					getSwitcher().showSlideStage(game.getFilename(), true, null, null);
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
					game.getTimer().cancel();
					game.getTimer2().cancel();
				}
			});
			break;
		case 2: // MENU
			buttons[j].addActionListener(e -> {

				if (GameManager.offline) {
					SoundsProvider.playBulletHit1();
					((MainFrame) getSwitcher()).setTransparent(false);
					game.setPauseOptionDialog(false);
					game.setExit(true);
					dialog.dispose();
					getSwitcher().showMenu();
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
					game.getTimer().cancel();
					game.getTimer2().cancel();
				}
			});
			break;
		case 3: // LOBBY
			buttons[j].addActionListener(e -> {

				if (!GameManager.offline) {

					SoundsProvider.playBulletHit1();
					((MainFrame) getSwitcher()).setTransparent(false);
					game.setExit(true);
					dialog.dispose();
					if (getGameManager().getPlayersArray().get(1).isDied()) {
						connectionManager.dispatch(getUpdateOptionPanel(connectionManager.getNameOfGame()));
					} else {
						connectionManager.dispatch(getUpdateOptionPanel(connectionManager.getNameOfGame()));
					}
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
				}
			});
			break;
		default:
			break;
		}
	}

	private void rotation(AbstractDynamicObject ob, Direction d) {
		if (d == Direction.UP) {
			ob.setRotateDegrees(0);
		} else if (d == Direction.DOWN) {
			ob.setRotateDegrees(180);
		} else if (d == Direction.LEFT) {
			ob.setRotateDegrees(270);
		} else if (d == Direction.RIGHT) {
			ob.setRotateDegrees(90);
		}
	}

	private double contFPS(AbstractStaticObject object, Direction dir, double contFPSobject, double pixel,
			double delta) {

		// ONLINE
		if (GameManager.offline)
			pixel = ((pixel / tempFPS) * delta);

		contFPSobject += pixel;

		if (dir == Direction.LEFT) {
			object.setyGraphics(object.getyGraphics() - pixel);
		} else if (dir == Direction.RIGHT) {
			object.setyGraphics(object.getyGraphics() + pixel);
		} else if (dir == Direction.UP) {
			object.setxGraphics(object.getxGraphics() - pixel);
		} else if (dir == Direction.DOWN) {
			object.setxGraphics(object.getxGraphics() + pixel);
		}

		return contFPSobject;
	}

	private void extend(Direction d, PlayerTank player) {

		if (!player.isPressed()) {
			if (player.getKeyPressLength() > limit) {
				player.setPressed(true);
				player.setDirection(d);
				player.setTmpDirection(d);
				if (player.getNext() instanceof Ice) {
					changeRotationForIce(player);
					player.setDirection(player.getDirection());
				}
			} else {
				player.setTmpDirection(d);
				if (player.getNext() instanceof Ice)
					player.setOld(d);
			}
		}
	}

	private void keyPresses() {

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (GameManager.offline) {
				game.getPlayersArray().get(a).setKeyPressLength(
						System.currentTimeMillis() - game.getPlayersArray().get(a).getKeyPressedMillis());
			} else {
				game.getPlayersArray().get(a).setKeyPressLength(game.getPlayersArray().get(a).getCurrentTimeMillis()
						- game.getPlayersArray().get(a).getKeyPressedMillis());
			}
		}

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (!game.getPlayersArray().get(a).isDied()) {
				for (int i = 0; i < game.getPlayersArray().get(a).getKeyBits().size(); i++) {
					if (game.getPlayersArray().get(a).getKeyBits().get(i)
							&& firstTime(i, game.getPlayersArray().get(a).getKeys())
							&& ((a == 0 && game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(i))
									|| (a == 1 && game.getPlayersArray().get(1).getDefaultKeysPlayer().contains(i)))) {
						game.getPlayersArray().get(a).getKeys().add(i);
					}
				}
			}
		}

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (!game.getPlayersArray().get(a).getKeys().isEmpty()) {
				isKeyPressed(
						game.getPlayersArray().get(a).getKeys().get(game.getPlayersArray().get(a).getKeys().size() - 1),
						a);
				if (a == 0 && game.getPlayersArray().get(a).getKeys()
						.get(game.getPlayersArray().get(a).getKeys().size() - 1) == 32) { // contemporanemrnate
					// cammini e spari
					// rimani bloccato
					game.getPlayersArray().get(a).getKeys().remove(game.getPlayersArray().get(a).getKeys().size() - 1);
					game.getPlayersArray().get(a).getKeyBits().clear(32);
				} else if (a == 1 && game.getPlayersArray().get(a).getKeys()
						.get(game.getPlayersArray().get(a).getKeys().size() - 1) == 17) { // contemporanemrnate
					// cammini e spari
					// rimani bloccato
					game.getPlayersArray().get(a).getKeys().remove(game.getPlayersArray().get(a).getKeys().size() - 1);
					game.getPlayersArray().get(a).getKeyBits().clear(17);
				}
				for (int i = 0; i < game.getPlayersArray().get(a).getKeys().size(); i++) {
					if (!game.getPlayersArray().get(a).getKeyBits()
							.get(game.getPlayersArray().get(a).getKeys().get(i))) {
						game.getPlayersArray().get(a).getKeys().remove(i);
					}
				}
			}
		}
	}

	public void isKeyPressed(final int keyCode, int i) {
		// SINGLEPLAYER | MULTIPLAYER ONLINE
		if (game.getPlayersArray().size() == 1 || !GameManager.offline) {
			if (keyCode == 37) {
				extend(Direction.LEFT, game.getPlayersArray().get(i));
			} else if (keyCode == 38) {
				extend(Direction.UP, game.getPlayersArray().get(i));
			} else if (keyCode == 39) {
				extend(Direction.RIGHT, game.getPlayersArray().get(i));
			} else if (keyCode == 40) {
				extend(Direction.DOWN, game.getPlayersArray().get(i));
			}
			if (keyCode == 32 && !game.getPlayersArray().get(i).isReleaseKeyRocket()) {

				if (game.getPlayersArray().get(i).getContRocket() == 0) {
					game.createRocketTank(game.getPlayersArray().get(i).getTmpDirection(),
							game.getPlayersArray().get(i));

					if (game.getPlayersArray().get(i).getLevel() >= 1
							&& game.getPlayersArray().get(i).getContRocket() > 0)
						game.createRocketTank(game.getPlayersArray().get(i).getTmpDirection(),
								game.getPlayersArray().get(i));

					game.getPlayersArray().get(i).setEnter(true);
					game.getPlayersArray().get(i).setReleaseKeyRocket(true);

				}
			}
		}
		// MULTIPLAYER OFFLINE
		else {
			if (keyCode == 65) {
				extend(Direction.LEFT, game.getPlayersArray().get(1));
			} else if (keyCode == 87) {
				extend(Direction.UP, game.getPlayersArray().get(1));
			} else if (keyCode == 68) {
				extend(Direction.RIGHT, game.getPlayersArray().get(1));
			} else if (keyCode == 83) {
				extend(Direction.DOWN, game.getPlayersArray().get(1));
			}

			if (keyCode == 17 && !game.getPlayersArray().get(1).isReleaseKeyRocket()) {
				if (game.getPlayersArray().get(1).getContRocket() == 0) {
					game.createRocketTank(game.getPlayersArray().get(1).getTmpDirection(),
							game.getPlayersArray().get(1));

					if (game.getPlayersArray().get(1).getLevel() >= 1
							&& game.getPlayersArray().get(1).getContRocket() > 0)
						game.createRocketTank(game.getPlayersArray().get(1).getTmpDirection(),
								game.getPlayersArray().get(1));
					game.getPlayersArray().get(1).setEnter(true);
					game.getPlayersArray().get(1).setReleaseKeyRocket(true);

				}
			}

			if (keyCode == 37) {
				extend(Direction.LEFT, game.getPlayersArray().get(0));
			} else if (keyCode == 38) {
				extend(Direction.UP, game.getPlayersArray().get(0));
			} else if (keyCode == 39) {
				extend(Direction.RIGHT, game.getPlayersArray().get(0));
			} else if (keyCode == 40) {
				extend(Direction.DOWN, game.getPlayersArray().get(0));
			}
			if (keyCode == 32 && !game.getPlayersArray().get(0).isReleaseKeyRocket()) {

				if (game.getPlayersArray().get(0).getContRocket() == 0) {
					game.createRocketTank(game.getPlayersArray().get(0).getTmpDirection(),
							game.getPlayersArray().get(0));

					if (game.getPlayersArray().get(0).getLevel() >= 1
							&& game.getPlayersArray().get(0).getContRocket() > 0)
						game.createRocketTank(game.getPlayersArray().get(0).getTmpDirection(),
								game.getPlayersArray().get(0));

					game.getPlayersArray().get(0).setEnter(true);
					game.getPlayersArray().get(0).setReleaseKeyRocket(true);

				}
			}
		}
	}

	private boolean firstTime(int key, ArrayList<Integer> keys) {
		for (int i = 0; i < keys.size(); i++)
			if (key == keys.get(i))
				return false;
		return true;
	}

	private void powerUpPickUp(Tank t, PowerUp p) {
		SoundsProvider.playPowerUpPick();

		if (!game.isPresent(t, p)) {
			p.setTank(t);
			p.setActivate(true);
			p.setTime(p.getDuration());
			game.usePowerUp(p);
			if (!game.getEffects().contains(p))
				game.getEffects().add(p);

		} else {

			if (p.getPowerUp() == Power.TIMER)
				game.stopEnemies();

			game.sumPowerUp(t, p);
			game.getPower().remove(p);
		}
		t.setNext(null);
		t.setCurr(null); // da vedere
	}

	public void gameOverOrWin() {

		if(!game.isExit()){
			// SINGLEPLAYER
			if (GameManager.singlePlayer) {
				if (game.getFlag().isHit() || game.getPlayersArray().get(0).getResume() <= 0) {
					gameOver();
				} else if (game.getEnemy().size() == 0)
					win();
			}
			// MULTIPLAYER
			else if (!GameManager.singlePlayer) {
	
				for (int a = 0; a < game.getPlayersArray().size(); a++) {
					if (game.getPlayersArray().get(a).getResume() < 0 && !game.isExit()) {
						game.getPlayersArray().get(a).setExitOnline(true);
					}
				}
	
				if (((game.getPlayersArray().size() > 1 && (game.getPlayersArray().get(0).getResume() <= 0
						&& game.getPlayersArray().get(1).getResume() <= 0)))
						|| game.getPlayersArray().size() == 1 && ((game.getPlayersArray().get(0).getResume() <= 0
								|| game.getPlayersArray().get(1).getResume() <= 0))
						|| game.getFlag().isHit()) {
					gameOver();
				} else if (game.getEnemy().size() == 0)
					win();
	
			}
		}
	}

	private void gameOver() {
		game.setExit(true);
		((MainFrame) getSwitcher()).setTransparent(true);
		((MainFrame)getSwitcher()).setSlide(true);
		SoundsProvider.playGameOver();
		new TranslucentWindow(getSwitcher(), game.getFilename(), ImageProvider.getGameOver());
		
		timer = new Timer(3000, e -> game.setExit(true));
		
		timer.setRepeats(false);
		timer.start();
	}

	private void win() {
		game.setExit(true);
		
		if (GameManager.offline) {

			SoundsProvider.cancelMove();
			SoundsProvider.cancelStop();
			((MainFrame) getSwitcher()).setTransparent(true);
			repaint();

			game.getPlayersArray().get(0).setCurrentResume(game.getPlayersArray().get(0).getResume());
			((MainFrame) switcher).setCurrentResumeP1(game.getPlayersArray().get(0).getCurrentResume());
			game.getPlayersArray().get(0).setCurrentLevel(game.getPlayersArray().get(0).getLevel());
			((MainFrame) switcher).setCurrentLevelP1(game.getPlayersArray().get(0).getCurrentLevel());

			if (game.getPlayersArray().size() > 1) {
				game.getPlayersArray().get(1).setCurrentResume(game.getPlayersArray().get(1).getResume());
				((MainFrame) switcher).setCurrentResumeP1(game.getPlayersArray().get(1).getCurrentResume());
				game.getPlayersArray().get(1).setCurrentLevel(game.getPlayersArray().get(1).getLevel());
				((MainFrame) switcher).setCurrentLevelP1(game.getPlayersArray().get(1).getCurrentLevel());
			}

			try {
				Thread.sleep(exitDelay);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			dialog.dispose();
			SoundsProvider.playStageComplete();
			new TranslucentWindow(getSwitcher(), game.getFilename(), ImageProvider.getStageComplete());

			game.getTimer().cancel();
			game.getTimer2().cancel();

		}

	}

	private void sounds() {

		if (GameManager.offline) {
			if (game.isSoundPowerUp()) {
				SoundsProvider.playPowerUpAppear();
				game.setSoundPowerUp(false);
			}

			if (game.isExplosion()) {
				SoundsProvider.playExplosion1();
				SoundsProvider.playExplosion2();
				game.setExplosion(false);
			}

			synchronized (this) {
				for (int a = 0; a < game.getEffects().size(); a++) {

					if (game.getEffects().get(a) instanceof Rocket
							&& ((Rocket) game.getEffects().get(a)).getTank() instanceof PlayerTank
							&& ((Rocket) game.getEffects().get(a)).isOneTimeSound()) {
						if (((Rocket) game.getEffects().get(a)).getNext() instanceof BrickWall) {
							SoundsProvider.playBulletHit2();

						} else if (((Rocket) game.getEffects().get(a)).getNext() instanceof SteelWall
								|| ((Rocket) game.getEffects().get(a)).isOnBorder()) {
							SoundsProvider.playBulletHit1();

						}
						((Rocket) game.getEffects().get(a)).setOneTimeSound(false);
					}
				}
			}

			// players
			for (int a = 0; a < game.getPlayersArray().size(); a++) {

				if (!SoundsProvider.stageStartClip.isActive()) {
					if (game.getPlayersArray().get(a).isShot()) {
						SoundsProvider.playBulletShot();
						game.getPlayersArray().get(a).setShot(false);
					}

					// SINGLEPLAYER OFFLINE

					if (GameManager.singlePlayer && a == 0) {
						if (!game.getPlayersArray().get(0).isPressed())
							SoundsProvider.playStop();
						else
							SoundsProvider.playMove();
					} else if (!GameManager.singlePlayer) {
						// MULTIPLAYER OFFLINE

						if (!game.getPlayersArray().get(0).isDied() && !game.getPlayersArray().get(1).isDied()) {

							if (!game.getPlayersArray().get(0).isPressed())
								SoundsProvider.playStop();
							else
								SoundsProvider.playMove();
						} else if (!game.getPlayersArray().get(a).isDied()) {
							if (!game.getPlayersArray().get(a).isPressed())
								SoundsProvider.playStop();
							else
								SoundsProvider.playMove();
						}
					}
				}
			}
		}
	}

	private void resetBooleanForSounds() {
		if (!GameManager.offline) {
			game.setSoundPowerUp(false);
			game.setExplosion(false);
			for (int a = 0; a < game.getPlayersArray().size(); a++) {
				game.getPlayersArray().get(a).setShot(false);
			}

			for (int a = 0; a < game.getEffects().size(); a++) {
				if (game.getEffects().get(a) instanceof Rocket)
					((Rocket) game.getEffects().get(a)).setOneTimeSound(false);
			}
		}
	}

	private void players() {
		// LOGIC
		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (!game.getPlayersArray().get(a).isDied()) {
				if (game.getPlayersArray().get(a).getKeyPressLength() > limit)
					game.getPlayersArray().get(a).update();

				if (game.getPlayersArray().get(a).isReadyToSpawn() && game.getPlayersArray().get(a).isFirst()) {
					game.getPlayersArray().get(a).setSpawnTime((GameManager.currentTime + 4) % 60);
					game.getPlayersArray().get(a).setFirst(false);
				}
				// SE CE UN OSTACOLO APPLICHI MINI EFFETTO
				if (!game.getPlayersArray().get(a).canGo) {
					if (game.getPlayersArray().get(a).getNext() instanceof Tank)
						game.getPlayersArray().get(a).setCont(35);
					else
						game.getPlayersArray().get(a).setCont(32);
					SoundsProvider.playHitForCanGo();
				}

				if (game.getPlayersArray().get(a).getNext() instanceof PowerUp) {
					game.getPlayersArray().get(a).getStatistics()
							.calculate(((PowerUp) game.getPlayersArray().get(a).getNext()));
					powerUpPickUp(game.getPlayersArray().get(a), ((PowerUp) game.getPlayersArray().get(a).getNext()));

				}
			}
		}
	}

	private void enemies() {
		// SPAWN ENEMY
		game.spawnEnemy();

		// LOGIC
		for (int a = 0; a < game.getEnemy().size(); a++) {
			if (game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).isAppearsInTheMap()
					&& !game.getEnemy().get(a).isStopEnemy()) {

				game.getEnemy().get(a).setxGraphics(game.getEnemy().get(a).getX() * tile);
				game.getEnemy().get(a).setyGraphics(game.getEnemy().get(a).getY() * tile);

				if ((GameManager.offline && SettingsPanel.easy) || (!GameManager.offline && difficult.equals("easy")))
					game.getEnemy().get(a).easy();
				else if ((GameManager.offline && SettingsPanel.normal)
						|| (!GameManager.offline && difficult.equals("normal")))
					game.getEnemy().get(a).medium();
				else if ((GameManager.offline && SettingsPanel.hard)
						|| (!GameManager.offline && difficult.equals("hard"))) {
					game.getEnemy().get(a).difficult(GameManager.flag.getX(), GameManager.flag.getY());

					if (!game.getEnemy().get(a).isHasApath()) {
						PlayerTank player = game.getPlayersArray().get(game.getEnemy().get(a).getRandomObject());
						game.getEnemy().get(a).difficult(player.getX(), player.getY());
					}
				}

				game.getEnemy().get(a).setTmpDirection(game.getEnemy().get(a).getDirection());

			}
		}
		for (int a = 0; a < game.getEnemy().size(); a++) {
			if (game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).isAppearsInTheMap()
					&& !game.getEnemy().get(a).isStopEnemy()) {
				// TODO in prova
				if (game.getEnemy().get(a).getNext() instanceof PowerUp
						&& ((PowerUp) game.getEnemy().get(a).getNext()).getPowerUp() == Power.HELMET) {
					powerUpPickUp(game.getEnemy().get(a), ((PowerUp) game.getEnemy().get(a).getNext()));
				}
				game.getEnemy().get(a).update();

				if (game.isShotEnabled() && game.getEnemy().get(a).getShotTimeEverySecond() == 1) {
					game.createRocketTank(game.getEnemy().get(a).getDirection(), game.getEnemy().get(a));
					game.getEnemy().get(a).setShotTimeEverySecond(0);
				}

				game.getMatrix().getWorld()[game.getEnemy().get(a).getX()][game.getEnemy().get(a).getY()] = game
						.getEnemy().get(a);
				game.getEnemy().get(a).setUpdateObject(false);
			}
		}

	}

	private void rockets() {
		synchronized (this) {
			// LOGIC
			for (int a = 0; a < game.getRocket().size(); a++) {
				if (game.getRocket().get(a).getTank() instanceof PlayerTank
						&& (((PlayerTank) game.getRocket().get(a).getTank()).isFinish()
								|| game.getRocket().get(a).getTank().getContRocket() == 1)
						&& ((PlayerTank) game.getRocket().get(a).getTank()).isEnter()
						&& ((PlayerTank) game.getRocket().get(a).getTank()).getLevel() >= 1) {
					if (!game.getRocket().get(a).isUpdateObject()) {
						game.getRocket().get(a).setUpdateObject(true);
						game.getRocket().get(a).setRocketForPlayer(true);
						((PlayerTank) game.getRocket().get(a).getTank()).setFinish(false);
						((PlayerTank) game.getRocket().get(a).getTank()).setEnter(false);
					}
				}

				if (game.getRocket().get(a).isUpdateObject()) {
					game.updateRocket(game.getRocket().get(a));
				}
			}
		}
	}

	private boolean noIntersectPlayerWithEnemy(int a) {
		for (int x = 0; x < game.getEnemy().size(); x++) {
			if (game.getEnemy().get(x).isAppearsInTheMap()
					&& game.getEnemy().get(x).getRect().intersects(game.getPlayersArray().get(a).getRect())) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------- PAINT ------------------------------------------ //
	private boolean powerUpShovelActive(int a, int b) {
		int x = game.getFlag().getX();
		int y = game.getFlag().getY();
		boolean trovato = false;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == a && j == b) {
					trovato = true;
					break;
				}
			}
		}

		if (trovato) {
			for (int a1 = 0; a1 < game.getPower().size(); a1++) {
				if (game.getPower().get(a1).getPowerUp() == Power.SHOVEL && game.getPower().get(a1).isActivate()) {
					return true;
				}
			}
		}
		return false;
	}

	private void paintPowerUp(Graphics g, PowerUp power) {

		if (power != null) {
			int b = power.getY();
			int a = power.getX();

			if (!(power.getBefore() instanceof Water)) {
				if (power.getPowerUp() == Power.GRENADE) {
					g.drawImage(ImageProvider.getGrenade(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.HELMET) {
					g.drawImage(ImageProvider.getHelmet(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.SHOVEL) {
					g.drawImage(ImageProvider.getShovel(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.STAR) {
					g.drawImage(ImageProvider.getStar(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.TANK) {
					g.drawImage(ImageProvider.getTank(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.TIMER) {
					g.drawImage(ImageProvider.getTimer(), b * tile, a * tile, null);
				}
			}

			else // ALTRIMENTI 80 % DENTRO L'ACQUA e 20 % FUORI
			{

				// CASO PARTICOLARE PER DOMANDE CHIEDERE ALOIA
				if (power.getBeforeWater() instanceof BrickWall) {
					g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof SteelWall) {
					g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof Tree) {
					g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof Ice) {
					g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
				}

				int y = power.getBefore().getY() * tile;
				int x = power.getBefore().getX() * tile;

				if (power.getDropDirection() == Direction.LEFT)
					y -= 7;

				if (power.getDropDirection() == Direction.RIGHT)
					y += 7;

				if (power.getDropDirection() == Direction.UP)
					x -= 7;

				if (power.getDropDirection() == Direction.DOWN)
					x += 10;

				if (power.getPowerUp() == Power.GRENADE) {
					g.drawImage(ImageProvider.getGrenade(), y, x, null);
				}
				if (power.getPowerUp() == Power.HELMET) {
					g.drawImage(ImageProvider.getHelmet(), y, x, null);
				}
				if (power.getPowerUp() == Power.SHOVEL) {
					g.drawImage(ImageProvider.getShovel(), y, x, null);
				}
				if (power.getPowerUp() == Power.STAR) {
					g.drawImage(ImageProvider.getStar(), y, x, null);
				}
				if (power.getPowerUp() == Power.TANK) {
					g.drawImage(ImageProvider.getTank(), y, x, null);
				}
				if (power.getPowerUp() == Power.TIMER) {
					g.drawImage(ImageProvider.getTimer(), y, x, null);
				}
			}
		}
	}

	private void paintTrees(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().getObjectStatic()[a][b] instanceof Tree && !powerUpShovelActive(a, b)) {
					g.drawImage(ImageProvider.getTree(), b * tile, a * tile, null);
				}
			}
		}
	}

	private void paintEnemy(Graphics g, Graphics2D g2d) {

		for (int i = 0; i < game.getEnemy().size(); i++) {

			// EFFETTO NASCITA ENEMY
			if (game.getEnemy().get(i).isReadyToSpawn() && !game.getEnemy().get(i).isAppearsInTheMap()) {

				if (game.getEnemy().get(i).getCountdown() == 0)
					g.drawImage(ImageProvider.getAppear1(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else if (game.getEnemy().get(i).getCountdown() == 1)
					g.drawImage(ImageProvider.getAppear2(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else if (game.getEnemy().get(i).getCountdown() == 2)
					g.drawImage(ImageProvider.getAppear3(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else
					g.drawImage(ImageProvider.getAppear4(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
			}

			if (game.getEnemy().get(i).isAppearsInTheMap()) {
				AffineTransform at = AffineTransform.getTranslateInstance(game.getEnemy().get(i).getyGraphics(),
						game.getEnemy().get(i).getxGraphics());
				rotation(game.getEnemy().get(i), game.getEnemy().get(i).getTmpDirection());
				at.rotate(Math.toRadians(game.getEnemy().get(i).getRotateDegrees()), tile / 2, tile / 2);

				if (game.getEnemy().get(i).getNext() instanceof PowerUp
						&& !(((PowerUp) game.getEnemy().get(i).getNext()).getPowerUp() == Power.HELMET)
						&& ((PowerUp) game.getEnemy().get(i).getNext()).getBefore() instanceof Ice) {
					g.drawImage(ImageProvider.getIce(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				}

				if (game.getEnemy().get(i).getNext() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getEnemy().get(i).getNext());
				}

				///
				long time = 0;

				if (!((MainFrame) getSwitcher()).isTransparent())
					time = (System.currentTimeMillis() / 200) % 2;
				///

				if (game.getEnemy().get(i) instanceof ArmorTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getArmorA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getArmorPowerUpB(), at, null);
						else
							g2d.drawImage(ImageProvider.getArmorB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof FastTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getFastA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getFastPowerUpA(), at, null);
						else
							g2d.drawImage(ImageProvider.getFastB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof BasicTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getBasicA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getBasicPowerUpA(), at, null);
						else
							g2d.drawImage(ImageProvider.getBasicB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof PowerTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getPowerA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getPowerPowerUpB(), at, null);
						else
							g2d.drawImage(ImageProvider.getPowerB(), at, null);
					}
				}

				// g.drawRect((int)game.getEnemy().get(i).rect.getY(),(int)
				// game.getEnemy().get(i).rect.getX(),
				// (int)game.getEnemy().get(i).rect.getWidth(),
				// (int)game.getEnemy().get(i).rect.getHeight());

				if (game.getEnemy().get(i).getNext() instanceof PowerUp
						&& !(((PowerUp) game.getEnemy().get(i).getNext()).getPowerUp() == Power.HELMET)
						&& ((PowerUp) game.getEnemy().get(i).getNext()).getBefore() instanceof Tree) {
					g.drawImage(ImageProvider.getTree(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				}

				// EFFETTO PROTEZIONE ENEMY
				if (game.getEnemy().get(i).isProtection()) {
					if (game.getEnemy().get(i).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getEnemy().get(i).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}
			}
		}
	}

	private void paintIce(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().getObjectStatic()[a][b] instanceof Ice && !powerUpShovelActive(a, b))
					g.drawImage(ImageProvider.getIce(), b * tile, a * tile, null);
			}
		}
	}

	private void paintRocket(Graphics g, Graphics2D g2d) {

		synchronized (this) {
			for (int i = 0; i < game.getRocket().size(); i++) {

				if (GameManager.offline && game.getRocket().get(i).getNext() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getRocket().get(i).getNext());
				}

				if (GameManager.offline && game.getRocket().get(i).getCurr() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getRocket().get(i).getCurr());
				}

				if (!game.getRocket().get(i).isFirstAnimationNo()) {
					AffineTransform at = AffineTransform.getTranslateInstance(game.getRocket().get(i).getyGraphics(),
							game.getRocket().get(i).getxGraphics());
					rotation(game.getRocket().get(i), game.getRocket().get(i).getDirection());
					at.rotate(Math.toRadians(game.getRocket().get(i).getRotateDegrees()), tile / 2, tile / 2);
					g2d.drawImage(ImageProvider.getRocket(), at, null);

					// g.drawRect((int)game.getRocket().get(i).getRect().getY(),(int)
					// game.getRocket().get(i).getRect().getX(),
					// (int)game.getRocket().get(i).getRect().getWidth(),
					// (int)game.getRocket().get(i).getRect().getHeight());

				}

				// DA SEMPRE PROBLEMI DI ECCEZZIONI
				if (GameManager.offline && game.getRocket().get(i).getNext() instanceof PowerUp)
					if (((PowerUp) game.getRocket().get(i).getNext()).getBefore() instanceof Tree) {
						g.drawImage(ImageProvider.getTree(), game.getRocket().get(i).getY() * tile,
								game.getRocket().get(i).getX() * tile, null);
					}
			}
		}
	}

	private void paintWater(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().getObjectStatic()[a][b] instanceof Water && !powerUpShovelActive(a, b)) {
					if (GameManager.currentTime % 2 == 0)
						g.drawImage(ImageProvider.getWaterA(), b * tile, a * tile, null);
					else
						g.drawImage(ImageProvider.getWaterB(), b * tile, a * tile, null);
				}
			}
		}
	}

	private void paintEffects(Graphics g, Graphics g2d) {

		synchronized (this) {
			for (int i = 0; i < game.getEffects().size(); i++) {
				int X, Y, pixel, inc;
				X = (int) game.getEffects().get(i).getxGraphics();
				Y = (int) game.getEffects().get(i).getyGraphics();

				// ROCKET boom
				if (game.getEffects().get(i) instanceof Rocket) {
					inc = ((Rocket) game.getEffects().get(i)).getInc();
					pixel = 10;
					switch (((Rocket) game.getEffects().get(i)).getDirection()) {
					case UP:
						X += pixel;
						break;
					case DOWN:
						X -= pixel;
						break;
					case LEFT:
						Y += pixel;
						break;
					case RIGHT:
						Y -= pixel;
						break;
					default:
						break;
					}

					if (inc == 1)
						g.drawImage(ImageProvider.getRocketExplosion1(), Y, X, null);
					else if (inc == 2)
						g.drawImage(ImageProvider.getRocketExplosion2(), Y, X, null);
					else if (inc == 3)
						g.drawImage(ImageProvider.getRocketExplosion3(), Y, X, null);
					// else if (inc > 3) {
					// game.getEffects().remove(game.getEffects().get(i));
					// i--;
					// }
				}

				// FLAG boom
				else if (game.getEffects().get(i) instanceof Flag) {

					inc = ((Flag) game.getEffects().get(i)).getInc();
					pixel = 17;

					if (inc == 1)
						g.drawImage(ImageProvider.getBigExplosion1(), Y - pixel, X - pixel, null);
					else if (inc == 2)
						g.drawImage(ImageProvider.getBigExplosion2(), Y - pixel, X - pixel, null);
					else if (inc == 3)
						g.drawImage(ImageProvider.getBigExplosion3(), Y - pixel, X - pixel, null);
					else if (inc == 4)
						g.drawImage(ImageProvider.getBigExplosion4(), Y - pixel, X - pixel, null);
					else if (inc == 5)
						g.drawImage(ImageProvider.getBigExplosion5(), Y - pixel, X - pixel, null);
					// else if (inc > 5) {
					// game.getEffects().remove(game.getEffects().get(i));
					// i--;
					// }
				}

				// ENEMY & PLAYER boom
				else if (game.getEffects().get(i) instanceof Tank) {

					inc = ((Tank) game.getEffects().get(i)).getInc();
					pixel = 17;

					if (inc == 1)
						g.drawImage(ImageProvider.getBigExplosion1(), Y - pixel, X - pixel, null);
					else if (inc == 2)
						g.drawImage(ImageProvider.getBigExplosion2(), Y - pixel, X - pixel, null);
					else if (inc == 3)
						g.drawImage(ImageProvider.getBigExplosion3(), Y - pixel, X - pixel, null);
					else if (inc == 4)
						g.drawImage(ImageProvider.getBigExplosion4(), Y - pixel, X - pixel, null);
					else if (inc == 5)
						g.drawImage(ImageProvider.getBigExplosion5(), Y - pixel, X - pixel, null);

					// ONLY PLAYER
					// else if (inc > 5 && game.getEffects().get(i) instanceof
					// PlayerTank) {
					// game.getEffects().remove(game.getEffects().get(i));
					// i--;
					// }
					// ONLY ENEMY POINTS
					else if (game.getEffects().get(i) instanceof EnemyTank) {
						if (((EnemyTank) game.getEffects().get(i)).getInc() > 5
								&& ((EnemyTank) game.getEffects().get(i)).getInc() < 12) {
							if (game.getEffects().get(i) instanceof BasicTank) {
								g.drawImage(ImageProvider.getPoints100(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof PowerTank) {
								g.drawImage(ImageProvider.getPoints300(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof ArmorTank) {
								g.drawImage(ImageProvider.getPoints400(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof FastTank) {
								g.drawImage(ImageProvider.getPoints200(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							}
						}
						// else if (((EnemyTank)
						// game.getEffects().get(i)).getInc() >= 12 ) {
						// game.getEffects().remove(game.getEffects().get(i));
						// i--;
						// }
					}
				}

				// PowerUp points
				else if (game.getEffects().get(i) instanceof PowerUp) {
					if (((PowerUp) game.getEffects().get(i)).getInc() > 5
							&& ((PowerUp) game.getEffects().get(i)).getInc() < 12) {
						g.drawImage(ImageProvider.getPoints500(), game.getEffects().get(i).getY() * tile,
								game.getEffects().get(i).getX() * tile, null);
					}
					// if (((PowerUp) game.getEffects().get(i)).getInc() >= 12)
					// {
					// game.getEffects().remove(game.getEffects().get(i));
					// i--;
					// }
				}
			}
		}
	}

	private void paintPlayer(Graphics g, Graphics2D g2d) {

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (!game.getPlayersArray().get(a).isDied()) {
				AffineTransform at = AffineTransform.getTranslateInstance(game.getPlayersArray().get(a).getyGraphics(),
						game.getPlayersArray().get(a).getxGraphics());
				rotation(game.getPlayersArray().get(a), game.getPlayersArray().get(a).getTmpDirection());
				at.rotate(Math.toRadians(game.getPlayersArray().get(a).getRotateDegrees()), tile / 2, tile / 2);

				// OFFLINE
				if (game.getPlayersArray().size() > 1 && GameManager.offline) {

					// PLAYER1
					if (a == 0) {

						if (game.getPlayersArray().get(a).getCont() < 15) {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1A_s3(), at, null);
						} else {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1B_s3(), at, null);
						}

					}

					// PLAYER2
					if (a == 1)
						if (game.getPlayersArray().get(a).getCont() < 15) { // MULTIPLAYER

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2A_s3(), at, null);
						} else {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2B_s3(), at, null);
						}
				} else {
					// SINGLEPLAYER | ONLINE
					// PLAYER1
					if (game.getPlayersArray().get(a).toString().equals("P1")) {
						if (game.getPlayersArray().get(a).getCont() < 15) {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1A_s3(), at, null);
						} else {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1B_s3(), at, null);
						}
					}

					if (game.getPlayersArray().get(a).toString().equals("P2")) {
						if (game.getPlayersArray().get(a).getCont() < 15) { // MULTIPLAYER

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2A_s3(), at, null);
						} else {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2B_s3(), at, null);
						}
					}
				}

				// g.drawRect((int)game.getPlayersArray().get(a).rect.getY(),(int)
				// game.getPlayersArray().get(a).rect.getX(),
				// (int)game.getPlayersArray().get(a).rect.getWidth(),
				// (int)game.getPlayersArray().get(a).rect.getHeight());

				// EFFETTO SPAWN E PROTEZIONE
				if (game.getPlayersArray().get(a).isReadyToSpawn() || game.getPlayersArray().get(a).isProtection()) {
					if (game.getPlayersArray().get(a).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getPlayersArray().get(a).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}

				// EFFETTO SPAWN E PROTEZIONE
				if (game.getPlayersArray().get(a).isReadyToSpawn() || game.getPlayersArray().get(a).isProtection()) {
					if (game.getPlayersArray().get(a).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getPlayersArray().get(a).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}
			}

			if (!GameManager.offline && game.getPlayersArray().get(a).getNameOfPlayerTank() != null) {
				if (!game.getPlayersArray().get(a).isDied()) {
					g.setColor(labelForNameOfPlayersColor.get(a));
					g.setFont(MainFrame.customFontS);

					int size = game.getPlayersArray().get(a).getNameOfPlayerTank().length();
					if (size <= 3) {
						size = size * 3;
					} else if (size < 9) {
						size = size * 4;
					} else {
						size = size * 5;
					}
					g.drawString(game.getPlayersArray().get(a).getNameOfPlayerTank(),
							(int) game.getPlayersArray().get(a).getyGraphics() + 15 - size,
							(int) game.getPlayersArray().get(a).getxGraphics() - 15);
				}
			}
		}

	}

	private void paintFlagBrickSteelPower(Graphics g) {

		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {

				// paintFlag
				if (game.getMatrix().getWorld()[a][b] instanceof Flag) {
					if (!game.getFlag().isHit() && game.getPlayersArray().size() > 0)
						g.drawImage(ImageProvider.getFlag(), b * tile, a * tile, null);
					else {
						g.drawImage(ImageProvider.getFlag_destroyed(), b * tile, a * tile, null);
					}
				}

				// paintBrickWall
				if (game.getMatrix().getWorld()[a][b] instanceof BrickWall) {

					if (((BrickWall) game.getMatrix().getWorld()[a][b]).getBefore() == null) {
						g.drawImage(ImageProvider.getBrick(), b * tile, a * tile, null);
					} else { // altrimenti disegno powerUp
						PowerUp power = ((PowerUp) ((BrickWall) game.getMatrix().getWorld()[a][b]).getBefore());

						if (power.isBlink()) {
							if ((System.currentTimeMillis() / 400) % 2 == 0)
								paintPowerUp(g, power);
							else
								g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile, null);
						} else {
							paintPowerUp(g, (PowerUp) ((BrickWall) game.getMatrix().getWorld()[a][b]).getBefore());
						}
					}
				}

				// paintSteelWall
				if (game.getMatrix().getWorld()[a][b] instanceof SteelWall) {
					if (((SteelWall) game.getMatrix().getWorld()[a][b]).getBefore() == null) {
						g.drawImage(ImageProvider.getSteel(), b * tile, a * tile, null);
					} else { // altrimenti disegno powerUp
						PowerUp power = ((PowerUp) ((SteelWall) game.getMatrix().getWorld()[a][b]).getBefore());

						if (power.isBlink()) {
							if ((System.currentTimeMillis() / 400) % 2 == 0)
								paintPowerUp(g, power);
							else
								g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
						} else {
							paintPowerUp(g, (PowerUp) ((SteelWall) game.getMatrix().getWorld()[a][b]).getBefore());
						}
					}
				}
			}
		}

		// paintPowerUp
		for (int a = 0; a < game.getPower().size(); a++) {
			if (!game.getPower().get(a).isActivate()) {
				PowerUp power = game.getPower().get(a);

				if (power.isBlink()) {
					if ((System.currentTimeMillis() / 400) % 2 == 0) {
						paintPowerUp(g, power);
					} else {
						// ice
						if (power.getBefore() instanceof Ice)
							g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
						// steel
						else if (power.getBefore() instanceof SteelWall)
							g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
						// tree
						else if (power.getBefore() instanceof Tree) {
							g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile, null);
						} else if (power.getBefore() instanceof Water) {

							if (power.getBeforeWater() instanceof BrickWall) {
								g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile, null);
							} else if (power.getBeforeWater() instanceof SteelWall) {
								g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
							} else if (power.getBeforeWater() instanceof Tree) {
								g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile, null);
							} else if (power.getBeforeWater() instanceof Ice) {
								g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
							}
						}

					}
				} else {
					paintPowerUp(g, power);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void printLines(Graphics g, Graphics2D g2d) {
		if (!((MainFrame) getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
		for (int i = 0; i <= game.getHeight(); i++) {

			g.drawLine(0, i * tile, game.getWidth() * tile, i * tile);
			g.drawLine(i * tile, 0, i * tile, game.getHeight() * tile);

		}
		if (!((MainFrame) getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	private void paused(Graphics g, Graphics2D g2d) {
		if (game.isPaused() && (System.currentTimeMillis() / 400) % 2 == 0) {
			g2d.drawImage(ImageProvider.getPause(), this.getWidth() / 2 - (70 + shift), getHeight() / 2 - (45 + shift),
					null);
		}
	}

	private void stroke(Graphics g, Graphics2D g2d) {
		if (((MainFrame) getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g.setColor(Color.GRAY);
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(shift * 2 - 2));
		g2d.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 2);
		g2d.setStroke(oldStroke);
		if (((MainFrame) getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (((MainFrame) getSwitcher()).isTransparent()) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2d.setColor(getBackground());
			g2d.fill(getBounds());
		}

		stroke(g, g2d);

		g.translate(shift, shift);

		// printLines(g, g2d);

		paintWater(g);

		paintIce(g);

		if (!GameManager.offline)
			game.getGlobaLock().lock();
		paintFlagBrickSteelPower(g);
		if (!GameManager.offline)
			game.getGlobaLock().unlock();

		if (!GameManager.offline)
			game.getGlobaLock().lock();
		paintEnemy(g, g2d);
		if (!GameManager.offline)
			game.getGlobaLock().unlock();

		paintPlayer(g, g2d);

		if (!GameManager.offline)
			game.getGlobaLock().lock();
		paintRocket(g, g2d);
		if (!GameManager.offline)
			game.getGlobaLock().unlock();

		paintTrees(g);

		if (!GameManager.offline)
			game.getGlobaLock().lock();
		paintEffects(g, g2d);
		if (!GameManager.offline)
			game.getGlobaLock().unlock();

		paused(g, g2d);

	}

	// ------------------------ SCORE MULTIPLAYER ------------------------- //
	private void loadScoreSingle() {

		BufferedReader reader = null;
		String line = null;

		final int DIM = 5;
		String values[] = new String[DIM];

		try {

			reader = new BufferedReader(new FileReader("./score/singleCareer.txt"));
			line = reader.readLine();

			int i = 0;

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, "");
				String tmp = null;

				while (st.hasMoreTokens()) {

					tmp = st.nextToken();

					if (tmp.matches("^[0-9]+") && i < values.length)
						values[i++] = tmp;
				}

				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		game.getPlayersArray().get(0).setCurrentResume(((MainFrame) switcher).getCurrentResumeP1());
		game.getPlayersArray().get(0).setResume(game.getPlayersArray().get(0).getCurrentResume());
		game.getPlayersArray().get(0).setCurrentLevel(((MainFrame) switcher).getCurrentLevelP1());
		game.getPlayersArray().get(0).setLevel(game.getPlayersArray().get(0).getCurrentLevel());

	}

	private void loadScoreMulti() {

		BufferedReader reader = null;
		String line = null;

		final int DIM = 7;
		String values[] = new String[DIM];

		try {

			reader = new BufferedReader(new FileReader("./score/multiCareer.txt"));
			line = reader.readLine();

			int i = 0;

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, "");
				String tmp = null;

				while (st.hasMoreTokens()) {

					tmp = st.nextToken();

					if (tmp.matches("^[0-9]+") && i < values.length)
						values[i++] = tmp;
				}

				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		((MainFrame) switcher).setCurrentResumeP1(Integer.parseInt(values[1]));
		((MainFrame) switcher).setCurrentLevelP1(Integer.parseInt(values[2]));

		((MainFrame) switcher).setCurrentResumeP2(Integer.parseInt(values[4]));
		((MainFrame) switcher).setCurrentLevelP2(Integer.parseInt(values[5]));

		// ASSEGNAZIONE VALORI AD ENTRAMBI I PLAYER

		game.getPlayersArray().get(0).setCurrentResume(((MainFrame) switcher).getCurrentResumeP1());
		game.getPlayersArray().get(0).setResume(game.getPlayersArray().get(0).getCurrentResume());
		game.getPlayersArray().get(0).setCurrentLevel(((MainFrame) switcher).getCurrentLevelP1());
		game.getPlayersArray().get(0).setLevel(game.getPlayersArray().get(0).getCurrentLevel());

		game.getPlayersArray().get(1).setCurrentResume(((MainFrame) switcher).getCurrentResumeP2());
		game.getPlayersArray().get(1).setResume(game.getPlayersArray().get(1).getCurrentResume());
		game.getPlayersArray().get(1).setCurrentLevel(((MainFrame) switcher).getCurrentLevelP2());
		game.getPlayersArray().get(1).setLevel(game.getPlayersArray().get(1).getCurrentLevel());

	}

	// ----------------------- GET & SET --------------------------//
	public GameManager getGame() {
		return game;
	}

	public void setGame(GameManager game) {
		this.game = game;
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public GameManager getGameManager() {
		return game;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public FullGamePanel getFullGamePanel() {
		return fullGamePanel;
	}

	public void setFullGamePanel(FullGamePanel fullGamePanel) {
		this.fullGamePanel = fullGamePanel;
	}

}
