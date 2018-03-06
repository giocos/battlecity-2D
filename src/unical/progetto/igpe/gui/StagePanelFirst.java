package unical.progetto.igpe.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class StagePanelFirst extends JPanel {

	private int posY;
	private int posX;
	private final int DIM = 13;
	private int cursorPosition;
	private String path;
	private File file;
	private JFileChooser fileChooser;
	private JLabel labelStage;
	private JButton arrowRight;
	private ArrayList<JButton> maps;
	private PanelSwitcher switcher;
	private MyListener myListener;
	
	public StagePanelFirst(final int w, final int h, PanelSwitcher switcher) {
	
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.GRAY);
		this.setLayout(null);
		
		path = "";
		posY = 50;
		posX = 225;
		cursorPosition = 1;
		
		myListener = new MyListener();
		arrowRight = new JButton();
		maps = new ArrayList<>();
		
		file = new File(path);
		fileChooser = new JFileChooser();
		
		setSwitcher(switcher);
		createButton();
		createArrowButton();
		loadMaps();
	}
	
	private void loadMaps() {
		
		String directory = null;
		
		if(path.contains("single"))
			directory = "./values/singleCareer.txt";
		else
			directory = "./values/multiCareer.txt";
		
		BufferedReader reader = null;
		String line = null;
		String value = null;
	
		try {

			reader = new BufferedReader(new FileReader(directory));
			line = reader.readLine();

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, "");
				String tmp = null;

				while (st.hasMoreTokens()) {

					tmp = st.nextToken();
			
					if(tmp.matches("^[0-9]+")) {
						
						int m = Integer.parseInt(tmp);
						
						if (m >= 1 && m <= 24)
							value = tmp;
					}
				}

				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(path.contains("single"))
			((MainFrame)switcher).setUnlockedMapsP1(Integer.parseInt(value));
		else
			((MainFrame)switcher).setUnlockedMapsP2(Integer.parseInt(value));
	}
	
	public void createButton() {
		
		for(int i = 0; i < DIM; i++) {
			
			final int curRow = i;
			
			maps.add(new JButton());	

			if(i == 0) {
				
				maps.get(i).setBorder(null);
				maps.get(i).setContentAreaFilled(false);
				maps.get(i).setBorderPainted(false);
				maps.get(i).setFocusPainted(false);
				maps.get(i).setFont(MainFrame.customFontM);
				maps.get(i).setForeground(Color.BLACK);
				maps.get(i).setHorizontalAlignment( SwingConstants.LEFT);
			}
			
			maps.get(i).addMouseListener(myListener);
			maps.get(i).addMouseMotionListener(myListener);
			setBoundAndText(i);	
			maps.get(i).addKeyListener(new KeyAdapter() {
	               
				@Override
	            public void keyPressed(KeyEvent e) {
					
					boolean enter = false;
					
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	          	           ((JButton) e.getComponent()).doClick();
	                }
					else 
	                	if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                		enter = true;
	                		cursorPosition = 1;
	                		getSwitcher().showPlayer();
	                  }
	                  else 
	                	  if(e.getKeyCode() == KeyEvent.VK_LEFT) {
	                		  enter = true;
	                	  
	                		  if(curRow < 1) {
	  	                        
	                			  maps.get(maps.size() - 1).requestFocus();
	                			  cursorPosition = maps.size() - 1;
	                		
	                		  }        
	                		  else {
		                    	 
	                			  maps.get(curRow - 1).requestFocus();
	                			  cursorPosition = curRow - 1;
	                		  }
	                   }
	                   else 
	                	   if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                		   enter = true;
	                		  
	                		   if(curRow == 8) {
	                		   
	                			   arrowRight.requestFocus();
	                			   cursorPosition = -1;
	                		   }
	                		   else {
	                		   
	                			   maps.get((curRow + 1) % maps.size()).requestFocus();
	                			   cursorPosition = (curRow + 1) % maps.size();
	                		   }
	                	   }   
	                	   else
	                		   if(e.getKeyCode() == KeyEvent.VK_UP) {
	                			   enter = true;
	                			   int tmp = (curRow - 4);
		                		
	                			   if(tmp < 0)
	                				   tmp = maps.size() + tmp;
		                	
	                			   if(curRow == 0)
	                				   tmp = maps.size() - 1;
	                			
		                			maps.get(tmp).requestFocus();
		                			cursorPosition = tmp;
		                		
	                	  }
	                	  else 
	                		  if(e.getKeyCode() == KeyEvent.VK_DOWN){
	                			  enter = true;
	                			int tmp = (curRow + 4) % maps.size();
		                		
	                			if(curRow + 4 > maps.size() - 1)
		                			tmp++;
		                		
	                			if(curRow == 0)
		                			tmp = curRow + 1;
	                			
									maps.get(tmp).requestFocus();
									cursorPosition = tmp;
	    	    		}
					 repaint();
					 if(enter)
						 SoundsProvider.playBulletHit1();
				}
	       });
			
		addActionListener(i);	
		this.add(maps.get(i));
		}
	}
	
	public void addActionListener(int j) {
		
		switch (j) {
		case 0:// BACK
			maps.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					getSwitcher().showPlayer();
					cursorPosition = 1;
				}
			});
			break;
		default: // Tutte le mappe
			maps.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					int tmp = 0;
					
					if(path.contains("single"))
						tmp = ((MainFrame)switcher).getUnlockedMapsP1();
					else
						tmp = ((MainFrame)switcher).getUnlockedMapsP2();
							
					if(j <= tmp) {
					
						fileChooser.setCurrentDirectory(file);
						
						JTextField fileNameMap = new JTextField();
	                    fileNameMap.setText(path + "/stage" + j + ".txt");
	                    
	                    JTextField directory = new JTextField();
	                    directory.setText(fileChooser.getCurrentDirectory().toString());
	                    getSwitcher().showSlideStage(fileNameMap, true, null, null);
	                    cursorPosition = 1;
					}
				}
			});
			break;
		}
	}
			
	public void setBoundAndText(int j) {
				
		if(j == 0) {
			
			maps.get(j).setBounds(20, 20, 70, 30);
			maps.get(j).setText("Back");
		}
		else {
		
			if(j == 5 || j == 9) {
				
				posX = 225;
				posY += posX;
			}
			setLabel(j);
			maps.get(j).setBounds(posX, posY, 
					ImageProvider.getMapsP1().get(j - 1).getWidth(null), ImageProvider.getMapsP1().get(j - 1).getHeight(null));
					
			posX += 245;
		}
	}
	
	public void setLabel(int j){

		labelStage = new JLabel();
		labelStage.setText("Stage " + j);
		labelStage.setFont(MainFrame.customFontM);
		labelStage.setBackground(Color.GRAY);
		labelStage.setForeground(Color.BLACK);
		labelStage.setBounds(posX + 35, posY + 190, 105, 25);
		this.add(labelStage);
	}
	
	public void createArrowButton() {
				
		arrowRight.setBounds((int) this.getPreferredSize().getWidth() - 80 , (int) this.getPreferredSize().getHeight() / 2 - 15,
				ImageProvider.getArrowRight().getWidth(null), ImageProvider.getArrowRight().getHeight(null));
		arrowRight.setBorder(null);
		arrowRight.setIcon(new ImageIcon(ImageProvider.getArrowRight()));
		arrowRight.setContentAreaFilled(false);
		arrowRight.setBorderPainted(false);
		arrowRight.setFocusPainted(false);
		arrowRight.addMouseListener(myListener);
		arrowRight.addMouseMotionListener(myListener);
		arrowRight.addKeyListener(new KeyAdapter() {
            
			@Override
            public void keyPressed(KeyEvent e) {
				
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					 SoundsProvider.playBulletHit1();
          	           ((JButton) e.getComponent()).doClick();
                 }
				 else 
					 if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(8).requestFocus();
						 cursorPosition = 8;
			     } 
				 else 
					 if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(9).requestFocus();
						 cursorPosition = 9;	
				}
			}
		});
		
		arrowRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				getSwitcher().showSecondStage(path);
				cursorPosition = 1;
			}
		});
		
		this.add(arrowRight);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//cursor + select
		if(cursorPosition > 0 ) {
			g.drawImage(ImageProvider.getSelectMap(), 
					maps.get(cursorPosition).getX() - 5, maps.get(cursorPosition).getY() - 5, null);
		}
		else 
			if(cursorPosition == -1) {
				g.drawImage(ImageProvider.getCursorRight(), (int) this.getPreferredSize().getWidth() - 130 , 
					(int) this.getPreferredSize().getHeight() / 2 - 8, null);
		}
		else {
			g.drawImage(ImageProvider.getCursorLeft(), 
					maps.get(cursorPosition).getX() + 80,maps.get(cursorPosition).getY() - 8, this);	
		}
		
		if(path.contains("single")) { 
			
			for(int i = 0;i < maps.size(); i++) {
			
				if(i != 0) {
					
					if(i <= ((MainFrame)switcher).getUnlockedMapsP1())
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP1().get(i - 1)));
					
					else
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getLocked()));
				}
			}
		}
		else {
			
			for(int i = 0;i < maps.size(); i++) {
				
				if(i != 0) {
					
					if(i <= ((MainFrame)switcher).getUnlockedMapsP2())
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP2().get(i - 1)));
					
					else
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
