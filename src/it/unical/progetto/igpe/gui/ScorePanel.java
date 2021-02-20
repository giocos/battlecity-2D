
package it.unical.progetto.igpe.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import it.unical.progetto.igpe.core.GameManager;
import it.unical.progetto.igpe.core.PlayerTank;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {

	private static final int OBJECT = 5;
	private static final int SLEEP = 150;
	private static final int SHOW = 500;

	private int totalP1;
	private int totalP2;
	private int highScoreP1;
	private int highScoreP2;
	private int currScoreP1;
	private int currScoreP2;
	private int x, y;
	private boolean complete;
	private int occurrence[][];

	private PanelSwitcher switcher;	
	private GameManager game;
	private String value;
	private String path;

	private JTextField filename;
	private Timer timer;
	
	public ScorePanel(final int w, final int h, PanelSwitcher switcher, GameManager game, JTextField filename) {

		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);

		complete = false;

		if (!filename.getText().contains("editor")) {
			value = (String) filename.getText().
					subSequence(filename.getText().indexOf("stage") + 5, filename.getText().length() - 4);
		} else {
			value = (String) filename.getText().subSequence(
					filename.getText().lastIndexOf("/") + 1, filename.getText().length());
			value = (String) value.subSequence(0, value.length() - 4);
		}
		path = (String) filename.getText().subSequence(0, filename.getText().lastIndexOf("/"));
		this.game = game;
		this.filename = filename;
		setSwitcher(switcher);

		occurrence = new int[game.getPlayersArray().size()][OBJECT];
		
		if (game.getPlayersArray().size() == 1) {
			setUp1Player();
		} else {
			setUp2Players();
		}
	}
	
	public void setUp1Player() {
		x = 805;
		y = 315;

		game.getPlayersArray().getFirst().getStatistics().setNewRecord();
		highScoreP1 = game.getPlayersArray().getFirst().getStatistics().getHighScore();
		currScoreP1 = game.getPlayersArray().getFirst().getStatistics().getCurrScore();
		totalP1 = game.getPlayersArray().getFirst().getStatistics().getTotalOccurr();
	
		for (int i = 0 ; i < occurrence.length; i++) {
			PlayerTank p = game.getPlayersArray().get(i);
			for (int j = 0; j < occurrence[i].length; j++) {
				setOccurrence(p, i, j);
			}
		}
		updateHighScore();
		drawLabelP1();
		activeTimer();
	}
	
	public void setUp2Players() {
		x = 385;
		y = 295;
		
		game.getPlayersArray().getFirst().getStatistics().setNewRecord();
		highScoreP1 = game.getPlayersArray().getFirst().getStatistics().getHighScore();
		currScoreP1 = game.getPlayersArray().getFirst().getStatistics().getCurrScore();
		totalP1 = game.getPlayersArray().getFirst().getStatistics().getTotalOccurr();
		
		game.getPlayersArray().getLast().getStatistics().setNewRecord();
		highScoreP2 = game.getPlayersArray().getLast().getStatistics().getHighScore();
		currScoreP2 = game.getPlayersArray().getLast().getStatistics().getCurrScore();
		totalP2 = game.getPlayersArray().getLast().getStatistics().getTotalOccurr();
		
		for (int i = 0 ; i < occurrence.length; i++) {
			PlayerTank p = game.getPlayersArray().get(i);
			for (int j = 0; j < occurrence[i].length; j++) {
				setOccurrence(p, i, j);
			}
		}
		updateHighScore();
		drawLabelP2();		
		activeTimer();
	}
		
	private void activeTimer() {
		timer = new Timer(500, e -> {
				try {
					if (complete) {
						 ((Timer)e.getSource()).stop();
						 if(!path.contains("editor")) {
							if(((MainFrame)getSwitcher()).isSlide()) {

								writeScore(0);
								Thread.sleep(SHOW);
								getSwitcher().showMenu();
							} else {
								writeScore(Integer.parseInt(value) + 1);
								filename.setText(path + "/stage" + String.valueOf(Integer.parseInt(value) + 1) + ".txt");
								Thread.sleep(SHOW);
								getSwitcher().showSlideStage(filename,true, null, null);
							}
						 } else {
							Thread.sleep(SHOW);
							getSwitcher().showConstruction();
		//					getSwitcher().showMenu();
						}
				} //if
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		});
		timer.start();
	}

	public void setOccurrence(PlayerTank p, int j, int k) {
		switch(k) {
		case 0:
			occurrence[j][k] = p.getStatistics().getBasicTankOcc();
			break;
		case 1:
			occurrence[j][k] = p.getStatistics().getFastTankOcc();
			break;
		case 2:
			occurrence[j][k] = p.getStatistics().getPowerTankOcc();
			break;
		case 3:
			occurrence[j][k] = p.getStatistics().getArmorTankOcc();
			break;
		case 4:
			occurrence[j][k] = p.getStatistics().getPowerUpOcc();
		default:
			break;
		}	
	}
	
	private void drawLabelP1() {
		JLabel stage = new JLabel();
		
		stage.setFont(MainFrame.customFontB);
		stage.setBackground(Color.BLACK);
		stage.setForeground(Color.WHITE);
		if (value.matches("^[0-9]+")) {
			stage.setText("Stage " + value);
		} else {
			stage.setText(value);
		}
		stage.setBounds(555, 185, 300, 100);
		
		this.add(stage);
		for (int i = 0, y = 280; i < OBJECT; i++, y += 65) {
		
			JLabel pts = new JLabel();
			JLabel text = new JLabel();
			text.setFont(MainFrame.customFontB);
			
			if (i == 1) {
				JLabel score = new JLabel();
				score.setFont(text.getFont());
				score.setBackground(Color.BLACK);
				score.setForeground(Color.ORANGE);
				score.setText(String.valueOf(currScoreP1));
				score.setBounds(545, 95, 300, 100);
				this.add(score);
				
				text.setForeground(Color.RED);
				text.setText("I-Player");
				text.setBounds(365, 95, 300, 100);
			} else
				if(i == 2) {
					
					JLabel hiScore = new JLabel();
					hiScore.setFont(text.getFont());
					hiScore.setBackground(Color.BLACK);
					hiScore.setForeground(Color.ORANGE);
					hiScore.setText(String.valueOf(highScoreP1));
					hiScore.setBounds(895, 95, 300, 100);
					this.add(hiScore);
					
					text.setForeground(Color.RED);
					text.setText("Hi-Score");
					text.setBounds(685, 95, 300, 100);
			} else
				if(i == 3) {

					text.setBackground(Color.BLACK);
					text.setForeground(Color.WHITE);
					text.setText("Total");
					text.setBounds(545, 635, 300, 100);
				}
				pts.setFont(text.getFont());
				pts.setBackground(Color.BLACK);
				pts.setForeground(Color.WHITE);
				pts.setText("pts");
				pts.setBounds(560, y, 300, 100);
			
				this.add(pts);
				this.add(text);
		}
		
		//LABEL OCCORRENZE ENEMIES UCCISI
		new Thread() {
			@Override
			public void run() {
				try {
					int positionX = 675;
					int positionY = 310;
					
					for (int i = 0, j = 0; j < OBJECT; j++, positionY += 65) {
					
						JLabel occur = new JLabel();
						JLabel points = new JLabel();
						
						points.setFont(MainFrame.customFontB);
						points.setBackground(Color.BLACK);
						points.setForeground(Color.WHITE);
						
						occur.setFont(MainFrame.customFontB);
						occur.setBackground(Color.BLACK);
						occur.setForeground(Color.WHITE);
						
						add(occur);
						add(points);
						
						int currValue = 0;
						
						while (currValue <= occurrence[i][j]) {
							
							Thread.sleep(SLEEP);
							SoundsProvider.playScore();
							
							points.setText(String.valueOf(currValue * 100 * (j + 1)));
							
							if (currValue == 0) {
								points.setBounds(positionX - 199, positionY, 45, 45);
							} else {
								points.setBounds(positionX - 245, positionY, 120, 45);
							}
							occur.setText(String.valueOf(currValue));
							occur.setBounds(positionX, positionY, 95, 45);
							currValue++;
						}
					}
					
					JLabel total = new JLabel();
					
					total.setFont(MainFrame.customFontB);
					total.setBackground(Color.BLACK);
					total.setForeground(Color.WHITE);
					add(total);
					
					for (int j = 0; j <= totalP1; j++) {
						
						Thread.sleep(SLEEP);
						SoundsProvider.playScore();
						total.setText(String.valueOf(j));
						total.setBounds(positionX, positionY + 28, 95, 45);
					}
					complete = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void drawLabelP2() {
		int k = 0;
		
		for (int i = 0; i < game.getPlayersArray().size(); i++) {
			for (int j = 0, posY = 260; j < OBJECT; j++, posY += 65) {
			
				JLabel pts = new JLabel();
				JLabel text = new JLabel();
				text.setFont(MainFrame.customFontB);
				if (j == 1) {

					JLabel score = new JLabel();

					score.setFont(text.getFont());
					score.setBackground(Color.BLACK);
					score.setForeground(Color.ORANGE);

					if (i == 0) {
						score.setText(String.valueOf(currScoreP1));
						text.setText("I-Player");
					} else {
						score.setText(String.valueOf(currScoreP2));
						text.setText("II-Player");
					}
					score.setBounds(385 + k, 95, 300, 100);
					this.add(score);
					text.setForeground(Color.RED);
					text.setBounds(185 + k, 95, 300, 100);
				} else
					if(j == 2) {
						JLabel hiScore = new JLabel();

						hiScore.setFont(text.getFont());
						hiScore.setBackground(Color.BLACK);
						hiScore.setForeground(Color.ORANGE);

						if (i == 0) {
							hiScore.setText(String.valueOf(highScoreP1));
							hiScore.setBounds(385 + k, 175, 300, 100);
						} else {
							hiScore.setText(String.valueOf(highScoreP2));
							hiScore.setBounds(385 + k, 175, 300, 100);
						}
						this.add(hiScore);

						text.setForeground(Color.RED);
						text.setText("Hi-Score");
						text.setBounds(185 + k, 175, 300, 100);
					} else
						if (j == 3) {
							text.setBackground(Color.BLACK);
							text.setForeground(Color.WHITE);
							text.setText("Total");
							text.setBounds(150 + k, 605, 200, 100);
						}
				pts.setFont(MainFrame.customFontB);
				pts.setBackground(Color.BLACK);
				pts.setForeground(Color.WHITE);
				pts.setText("pts");
				pts.setBounds(185 + k, posY, 100, 100);
				
				this.add(pts);
				this.add(text);
			}
			k += 595;
		}
		
		//LABEL OCCORRENZE ENEMIES UCCISI
		new Thread() {
		
			@Override
			public void run() {
				try {
					int positionX = 270;
					int positionY = 340;
					int positionK = 865;
					
					for (int i = 0; i < occurrence.length; i++) {
						positionY = 330;
						for (int j = 0; j < occurrence[i].length; j++, positionY += 65) {
							JLabel occur = new JLabel();
							JLabel points = new JLabel();
							
							points.setFont(MainFrame.customFontB);
							points.setBackground(Color.BLACK);
							points.setForeground(Color.WHITE);
							
							occur.setFont(MainFrame.customFontB);
							occur.setBackground(Color.BLACK);
							occur.setForeground(Color.WHITE);
							
							add(occur);
							add(points);
							
							int currValue = 0;
							int currPosition = 0;
							
							if (i == 0) {
								currPosition = positionX;
							} else {
								currPosition = positionK;
							}
							while (currValue <= occurrence[i][j]) {
								
								Thread.sleep(SLEEP);
								SoundsProvider.playScore();
								points.setText(String.valueOf(currValue * 100 * (j + 1)));
								
								if (currValue == 0) {
									points.setBounds(currPosition - 138, positionY - 40, 45, 45);
								} else {
									points.setBounds(currPosition - 183, positionY - 40, 105, 45);
								}
								occur.setText(String.valueOf(currValue));
								occur.setBounds(currPosition, positionY - 40, 85, 45);
								currValue++;
								
							}
						}
					}
					JLabel total1P = new JLabel();
					total1P.setFont(MainFrame.customFontB);
					total1P.setBackground(Color.BLACK);
					total1P.setForeground(Color.WHITE);
					add(total1P);
					
					for (int j = 0; j <= totalP1; j++) {
						
						Thread.sleep(SLEEP);
						SoundsProvider.playScore();
						total1P.setText(String.valueOf(j));
						total1P.setBounds(positionX, positionY - 22, 95, 45);
					}
					
					JLabel total2P = new JLabel();
					total2P.setFont(MainFrame.customFontB);
					total2P.setBackground(Color.BLACK);
					total2P.setForeground(Color.WHITE);
					add(total2P);
					
					
					for (int j = 0; j <= totalP2; j++) {
						
						Thread.sleep(SLEEP);
						SoundsProvider.playScore();
						total2P.setText(String.valueOf(j));
						total2P.setBounds(positionK, positionY - 22, 95, 45);
					}
					complete = true;
				}
				catch (InterruptedException e) {
						e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void writeScore(int value) {
		try {
			if (path.contains("singleplayer")) {
				writeSingle(value);
			} else
				if (path.contains("multiplayer")) {
					writeMulti(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeMulti(int value) throws IOException {
		
		PrintWriter writer = new PrintWriter("./score/multiCareer.txt");
		BufferedWriter buffer = new BufferedWriter(writer);
		
		if (((MainFrame)switcher).getUnlockedMapsP2() < value) {
			((MainFrame) switcher).setUnlockedMapsP2(value);
		}
		buffer.write("P1:\n");
		buffer.write("HI-SCORE\n");
		buffer.write(String.valueOf(highScoreP1) + "\n");
		buffer.write("LIVES\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentResumeP1()) + "\n");
		buffer.write("LEVEL\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentLevelP1()) + "\n");
		buffer.write("P2:\n");
		buffer.write("HI-SCORE\n");
		buffer.write(String.valueOf(highScoreP2) + "\n");
		buffer.write("LIVES\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentResumeP2()) + "\n");
		buffer.write("LEVEL\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentLevelP2()) + "\n");
		buffer.write("MAPS\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getUnlockedMapsP2()) + "\n");
		
		buffer.flush();
		buffer.close();
	}

	private void writeSingle(int value) throws IOException {
		
		PrintWriter writer = new PrintWriter("./score/singleCareer.txt");
		BufferedWriter buffer = new BufferedWriter(writer);
		
		if(((MainFrame)switcher).getUnlockedMapsP1() < value) {
			((MainFrame) switcher).setUnlockedMapsP1(value);
		}
		buffer.write("SCORE\n");
		buffer.write(String.valueOf(currScoreP1) + "\n");
		buffer.write("HI-SCORE\n");
		buffer.write(String.valueOf(highScoreP1) + "\n");
		buffer.write("LIVES\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentResumeP1()) + "\n");
		buffer.write("LEVEL\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getCurrentLevelP1()) + "\n");
		buffer.write("MAPS\n");
		buffer.write(String.valueOf(((MainFrame)switcher).getUnlockedMapsP1()));
		
		buffer.flush();
		buffer.close();
	}

	private void updateHighScore() {
		try {
			if (path.contains("singleplayer")) {
				updateSingle();
			} else
				if (path.contains("multiplayer")) {
					updateMulti();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateMulti() throws IOException {
		if (((MainFrame)switcher).getHighScoreP1() > highScoreP1) {
			highScoreP1 = ((MainFrame) switcher).getHighScoreP1();
		}
		if (((MainFrame)switcher).getHighScoreP2() > highScoreP2) {
			highScoreP1 = ((MainFrame) switcher).getHighScoreP2();
		}
	}

	private void updateSingle() throws IOException {
		if (((MainFrame)switcher).getHighScoreP1() > highScoreP1) {
			highScoreP1 = ((MainFrame) switcher).getHighScoreP1();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int positionX = x;
		for (int i = 0; i < occurrence.length; i++, positionX += 595) {
			int positionY = y;
			for (int j = 0; j < occurrence[i].length; j++, positionY += 65) {
				if (j == 0) {
					g.drawImage(ImageProvider.getBasicA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				} else if (j == 1) {
					g.drawImage(ImageProvider.getFastA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);

				} else if (j == 2) {
					g.drawImage(ImageProvider.getPowerA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);

				} else if (j == 3) {
					g.drawImage(ImageProvider.getArmorA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);

				}		
				else if (j == 4) {
					g.drawImage(ImageProvider.getStar(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				}
			}
			g.drawImage(ImageProvider.getBar(), positionX - 145, positionY - 15, null);
		}
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
}
