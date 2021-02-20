package it.unical.progetto.igpe.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unical.progetto.igpe.core.GameManager;
import it.unical.progetto.igpe.core.PlayerTank;
import it.unical.progetto.igpe.core.Power;

@SuppressWarnings("serial")
public class FullGamePanel extends JPanel {
	
	private int x;
	private int y;
	private int shift = 17;

	private PanelSwitcher switcher;
	private String valueMap;
	private JLabel labelValueMap;
	private GamePanel gamePanel;
	private GameManager gameManager;
	private PanelPlayersInfo[] panelOfInfo;

	public FullGamePanel(final int WIDTH, int HEIGHT, int gameWidth, int gameHeight, PanelSwitcher switcher,GamePanel gamePanel) {

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		setBackground(Color.GRAY);
		this.switcher = switcher;
		
		this.setGamePanel(gamePanel);
		this.gameManager = gamePanel.getGame();
		gamePanel.setFocusable(true);
//		gamePanel.setBounds(322 - shift, 32 - shift, gameWidth + shift * 2 + 3, gameHeight + shift * 2);
		gamePanel.setBounds(282 - shift, 20 - shift, gameWidth + shift * 2 + 3, gameHeight + shift * 2);
		
		if (GameManager.offline)
			valueMap = gameManager.getFilename().getText().replaceAll("[^0-9]", "");	
		
		createInfoLabel();

		labelValueMap = new JLabel();
		labelValueMap.setBounds(1175, 660, 30, 30);
		labelValueMap.setPreferredSize(new Dimension(30, 30));
		labelValueMap.setForeground(Color.BLACK);
		labelValueMap.setFont(MainFrame.customFontM);
		labelValueMap.setText(valueMap);
		
		createInfoPlayer();
		
		add(labelValueMap);
		add(gamePanel);
		
	}
	
	public void createInfoPlayer() {
		panelOfInfo = new PanelPlayersInfo[2];
		int position = 100;
		for (int a = 0; a < gameManager.getPlayersArray().size(); a++) {
			panelOfInfo[a] = new PanelPlayersInfo(gameManager.getPlayersArray().get(a).toString());
			panelOfInfo[a].setForeground(Color.BLACK);
			panelOfInfo[a].setBackground(Color.GRAY);
			panelOfInfo[a].setBounds(14, position, 250, 300);
			add(panelOfInfo[a]);
			position += 320;
		}
	}
	
	public void createInfoLabel() {

		JLabel label1 = new JLabel("Press <ENTER> for 'PAUSE'");
		label1.setForeground(Color.BLACK);
		label1.setFont(MainFrame.customFontS);
		label1.setBounds(35, 57, 220, 20);
		this.add(label1);
		
		JLabel label2 = new JLabel("Press <ESC> for 'MENU'");
		label2.setForeground(Color.BLACK);
		label2.setFont(MainFrame.customFontS);
		label2.setBounds(35, 27, 200, 20);
		this.add(label2);
	}
	
	private class PanelPlayersInfo extends JPanel {
		private String player;
		private String p;
		private JLabel PlayerLabel;
		private int x;
		private int y;

		public PanelPlayersInfo(String player) {
			
			if(player.equals("P1"))
				this.player = "Player 1";
			else
				this.player = "Player 2";
			this.p = player;
			this.setLayout(null);
			this.setPreferredSize(new Dimension(250, 300));
			this.setBackground(Color.GRAY);
			PlayerLabel = new JLabel(this.player);
			PlayerLabel.setPreferredSize(new Dimension(120, 35));
			PlayerLabel.setFont(MainFrame.customFontM);
			PlayerLabel.setBounds(70, 10, 120, 35);
			PlayerLabel.setForeground(Color.BLACK);
			this.add(PlayerLabel);
			
//			JLabel l = new JLabel("PowerUp:");
//			l.setPreferredSize(new Dimension(120, 35));
//			l.setFont(((MainFrame)getSwitcher()).customFontS);
//			l.setBounds(30, 120, 120, 35);
//			l.setForeground(Color.BLACK);
//			this.add(l);
			
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			String lives = null;
			
			g.fillRect(10, 150, 230, 140);
			
			int X  = 100, Y = 50, X1 = 40, Y1 = 40;
			
			for (int a = 0; a < gameManager.getPlayersArray().size(); a++) {
				if (gameManager.getPlayersArray().get(a).toString().equals("P1") && player.equals("Player 1")) {
					if (gameManager.getPlayersArray().get(a).getResume() <= 0) {
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
					}
					if (gameManager.getPlayersArray().get(a).getLevel() == 0) {
						g.drawImage(ImageProvider.getPlayer1A(), X, Y, X1, Y1, null);
					} else if (gameManager.getPlayersArray().get(a).getLevel() == 1) {
						g.drawImage(ImageProvider.getPlayer1A_s1(), X, Y, X1, Y1, null);
					} else if (gameManager.getPlayersArray().get(a).getLevel() == 2) {
						g.drawImage(ImageProvider.getPlayer1A_s2(), X, Y, X1, Y1, null);
					} else if (gameManager.getPlayersArray().get(a).getLevel() == 3) {
						g.drawImage(ImageProvider.getPlayer1A_s3(), X, Y, X1, Y1, null);
					}

					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

					if (gameManager.getPlayersArray().get(a).getResume() > 0)
						lives = Integer.toString(gameManager.getPlayersArray().get(a).getResume());
				} else if (gameManager.getPlayersArray().get(a).toString().equals("P2") && player.equals("Player 2")) {
					if (gameManager.getPlayersArray().size() > 1) {

						if (gameManager.getPlayersArray().get(a).getResume() <= 0) {
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
						}

						if (gameManager.getPlayersArray().get(a).getLevel() == 0) {
							g.drawImage(ImageProvider.getPlayer2A(), X, Y, X1, Y1, null);
						} else if (gameManager.getPlayersArray().get(a).getLevel() == 1) {
							g.drawImage(ImageProvider.getPlayer2A_s1(), X, Y, X1, Y1, null);
						} else if (gameManager.getPlayersArray().get(a).getLevel() == 2) {
							g.drawImage(ImageProvider.getPlayer2A_s2(), X, Y, X1, Y1, null);
						} else if (gameManager.getPlayersArray().get(a).getLevel() == 3) {
							g.drawImage(ImageProvider.getPlayer2A_s3(), X, Y, X1, Y1, null);
						}

						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

						if (gameManager.getPlayersArray().get(a).getResume() > 0)
							lives = Integer.toString(gameManager.getPlayersArray().get(a).getResume());
					}
				}
			}
			g.setFont(MainFrame.customFontM);
			if (lives != null)
				g.drawString(lives, 140, 100);

			x = 25;
			y = 80;
			int cont = 0;

			if (!GameManager.offline)
				gameManager.getGlobaLock().lock();

			g.setColor(Color.WHITE);
			for (int a = 0; a < gameManager.getPower().size(); a++) {
				if (gameManager.getPower().get(a).isActivate()
						&& gameManager.getPower().get(a).getTank() instanceof PlayerTank
						&& (gameManager.getPower().get(a).getTank().toString().equals(p)
								|| (gameManager.getPower().get(a).getPowerUp() == Power.SHOVEL)
								|| (gameManager.getPower().get(a).getPowerUp() == Power.TIMER))) {
					if (cont % 3 == 0) {
						x = 34;
						y += 100;
					}
					String time = Integer.toString((int) gameManager.getPower().get(a).getTime());

					if (gameManager.getPower().get(a).getPowerUp() == Power.HELMET) {
						g.drawImage(ImageProvider.getHelmetx(), x, y, null);
						g.drawString(time, x+10, y + 80);
					} else if (gameManager.getPower().get(a).getPowerUp() == Power.TIMER) {
						g.drawImage(ImageProvider.getTimerx(), x, y, null);
						g.drawString(time, x+10, y + 80);
					} else if (gameManager.getPower().get(a).getPowerUp() == Power.SHOVEL) {
						g.drawImage(ImageProvider.getShovelx(), x, y, null);
						g.drawString(time, x+10, y + 80);
					}
					x += 65;
					cont++;
				}
			}
			if (!GameManager.offline)
				gameManager.getGlobaLock().unlock();
		}

	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.GRAY);
		g.fillRect(1000, 18, 291, 703);
		x = 1109;
		y = 15;
		for (int i = 0; i < gameManager.getNumbersOfEnemiesOnline(); i++) {

			if (i % 3 == 0) {

				x = 1109;
				y += 40;
			}
			g.drawImage(ImageProvider.getIconEnemy(), x, y, null);

			x += 40;
		}
		g.drawImage(ImageProvider.getIconFlag(), 1150, 640, null);
	}

	public String getValueMap() {
		return valueMap;
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
}
