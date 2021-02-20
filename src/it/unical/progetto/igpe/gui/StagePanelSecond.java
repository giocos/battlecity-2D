package it.unical.progetto.igpe.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class StagePanelSecond extends JPanel {

	private int posY;
	private int posX;
	private final int DIM = 12;
	private int cursorPosition;
	private String path;
	private File file;
	private JFileChooser fileChooser;
	private JLabel labelStage;
	private JButton arrowLeft;
	private ArrayList<JButton> maps;
	private PanelSwitcher switcher;
	private MyListener myListener;
	
	public StagePanelSecond(final int w, final int h, PanelSwitcher switcher) {
	
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.GRAY);
		this.setLayout(null);
	
		posY = 50;
		posX = 225;
		cursorPosition = 0;
		
		path = "";
		myListener = new MyListener();
		arrowLeft = new JButton();
		maps = new ArrayList<>();
		
		file = new File(path);
		fileChooser = new JFileChooser();
		
		setSwitcher(switcher);
		createArrowButton();
		createButton();
	}
	
	public void createButton() {
		for(int i = 0; i < DIM; i++) {
			final int curRow = i;

			maps.add(new JButton());
			setBounds(i);
			maps.get(i).setBorder(null);
			maps.get(i).setContentAreaFilled(false);
			maps.get(i).setBorderPainted(false);
			maps.get(i).setFocusPainted(false);
			maps.get(i).addMouseListener(myListener);
			maps.get(i).addMouseMotionListener(myListener);
			maps.get(i).addKeyListener(new KeyAdapter() {

				@Override
	            public void keyPressed(KeyEvent e) {
					boolean enter = false;
					 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	          	           ((JButton) e.getComponent()).doClick();
	                  }
	                  else
	                  	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	                	  enter = true;

	                		  if(curRow < 1) {
	                			  maps.get(maps.size() - 1).requestFocus();
	                			  cursorPosition = maps.size() - 1;
	                		  }
	                		  else {
	                			  if (curRow == 4) {
	                				  arrowLeft.requestFocus();
	                				  cursorPosition = - 1;
	                			  } else {
		                			  maps.get(curRow - 1).requestFocus();
		                			  cursorPosition = curRow - 1;
	                			  }
	                		  }
	                   }
	                   else
	                   	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                		   enter = true;
	                		   maps.get((curRow + 1) % maps.size()).requestFocus();
	                		   cursorPosition = (curRow + 1) % maps.size();

	                   } else
	                   		if (e.getKeyCode() == KeyEvent.VK_UP) {
	                		   	enter = true;
								int tmp = (curRow - 4);
	                			if(tmp < 0) {
									tmp = maps.size() + tmp;
								}
								maps.get(tmp).requestFocus();
								cursorPosition = tmp;
	                	}
	                	else
	                		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
								 enter = true;
								 int tmp = (curRow + 4) % maps.size();
								 maps.get(tmp).requestFocus();
								 cursorPosition = tmp;
	                    }
					    repaint();
					    if (enter) {
							SoundsProvider.playBulletHit1();
						}
				}
	   		});
		addActionListener(i);	
		this.add(maps.get(i));
		}
	}
	
	public void addActionListener(int j) {
			maps.get(j).addActionListener(e -> {
				SoundsProvider.playBulletHit1();
				int tmp = 0;
				if (path.contains("single")) {
					tmp = ((MainFrame) switcher).getUnlockedMapsP1();
				} else {
					tmp = ((MainFrame) switcher).getUnlockedMapsP2();
				}
				if (j + DIM <= tmp) {

					fileChooser.setCurrentDirectory(file);

					JTextField fileNameMap = new JTextField();
					fileNameMap.setText(path + "/stage" + (j + DIM + 1) + ".txt");

					JTextField directory = new JTextField();
					directory.setText(fileChooser.getCurrentDirectory().toString());
					getSwitcher().showSlideStage(fileNameMap, true, null, null);
					cursorPosition = 0;
				}
			});
	}
			
	public void setBounds(int j) {
						
			if (j == 4 || j == 8) {
				posX = 225;
				posY += posX;
			}
			int k = (j + 13) - 1;
			setLabel(k + 1);
			maps.get(j).setBounds(posX, posY, ImageProvider.getMapsP1().get(k)
					.getWidth(null), ImageProvider.getMapsP1().get(k).getHeight(null));
			posX += 245;
	}
	
	public void setLabel(int j){
		labelStage = new JLabel();
		labelStage.setText("Stage " + j);
		labelStage.setFont(MainFrame.customFontM);
		labelStage.setBackground(Color.GRAY);
		labelStage.setForeground(Color.BLACK);
		labelStage.setBounds(posX + 35, posY + 190, 110, 25);
		this.add(labelStage);
	}
	
	public void createArrowButton() {
				
		arrowLeft.setBounds(25 , (int) this.getPreferredSize().getHeight() / 2 - 15,
				ImageProvider.getArrowLeft().getWidth(null), ImageProvider.getArrowLeft().getHeight(null));
		arrowLeft.setBorder(null);
		arrowLeft.setIcon(new ImageIcon(ImageProvider.getArrowLeft()));
		arrowLeft.setContentAreaFilled(false);
		arrowLeft.setBorderPainted(false);
		arrowLeft.setFocusPainted(false);
		arrowLeft.addMouseListener(myListener);
		arrowLeft.addMouseMotionListener(myListener);
		arrowLeft.addKeyListener(new KeyAdapter() {
            
			@Override
            public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					 SoundsProvider.playBulletHit1();
          	           ((JButton) e.getComponent()).doClick();
                 }
				 else 
					 if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(4).requestFocus();
						 cursorPosition = 4;	
				 }
				 else 
					 if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(3).requestFocus();
						 cursorPosition = 3;
			     }
			}
		});
		arrowLeft.addActionListener(e -> {
			getSwitcher().showFirstStage(path);
			cursorPosition = 0;
		});
		this.add(arrowLeft);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//cursor
		if (cursorPosition == -1) {
			g.drawImage(ImageProvider.getCursorLeft(), 85, 
					(int) this.getPreferredSize().getHeight() / 2 - 8, this);
		} else {
			g.drawImage(ImageProvider.getSelectMap(), 
					maps.get(cursorPosition).getX() - 5, maps.get(cursorPosition).getY() - 5, this);
		}
		
		if (path.contains("single")) {
			
			for (int i = 0; i < maps.size(); i++) {
				if ((i + DIM) < ((MainFrame) switcher).getUnlockedMapsP1()) {
					maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP1().get(i + DIM)));
				} else {
					maps.get(i).setIcon(new ImageIcon(ImageProvider.getLocked()));
				}
			}
		} else {
			for (int i = 0; i < maps.size(); i++) {
				
				if ((i + DIM) < ((MainFrame)switcher).getUnlockedMapsP2()) {
					maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP2().get(i + DIM)));
				} else {
					maps.get(i).setIcon(new ImageIcon(ImageProvider.getLocked()));
				}
			}
		}
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public JButton getButton(int i) {
		return maps.get(i);
	}
	
	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}
	
	private class MyListener extends MouseInputAdapter {

		private boolean flag = false;
		
		@Override
		public void mousePressed(MouseEvent e) {

			for(int i = 0; i < DIM; i++) {
			
				if(e.getComponent().getX() == maps.get(i).getX()) {
					
					cursorPosition = i;
					flag = true;
					break;
				}
			}
			
			if(!flag)
				cursorPosition = -1;
			
			repaint();
			
			flag = false;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		
			for(int i = 0; i < DIM; i++) {
				
				if(e.getComponent().getX() == maps.get(i).getX()) {
					
					cursorPosition = i;
					flag = true;
					break;
				}
			}
		
			if(!flag)
				cursorPosition = -1;
			
			repaint();
			
			flag = false;
		}
	}	
}

