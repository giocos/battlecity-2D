package unical.progetto.igpe.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
	
	private final int DIM = 3;
	private final int posY = 70;
	private final int posX = 120;
	private int cursorPosition;
	private PanelSwitcher switcher;
	private final ArrayList<JButton> buttons;
	private MyListener myListener;

	public PlayerPanel(final int w, final int h, PanelSwitcher switcher) {
		
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
		setSwitcher(switcher);
		setCursorPosition(1);
		
		myListener = new MyListener();
		buttons = new ArrayList<>();
		createButton();
	}

	public void createButton() {
		
		for(int i = 0; i < DIM; i++) {
			
			final int curRow = i;
			
			buttons.add(new JButton());
			buttons.get(i).setBorder(null);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment( SwingConstants.LEFT );
			buttons.get(i).addMouseListener(myListener);
			buttons.get(i).addMouseMotionListener(myListener);
			
			if(i == 0)
				buttons.get(i).setFont(MainFrame.customFontM);
			else
				buttons.get(i).setFont(MainFrame.customFontB);
			
			setBoundAndText(i);
			buttons.get(i).addKeyListener(new KeyAdapter() {
	               
			@Override
	        public void keyPressed(KeyEvent e) {
				
			boolean enter = false;
				
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
          	         ((JButton) e.getComponent()).doClick();
                  }
                  else 
                	  if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                		  enter=true;
                		  setCursorPosition(1);
                		  getSwitcher().showMenu();
                  }
                  else 
                	  if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
                	  enter=true;
                		  if(curRow < 1) {
	                        
                			  buttons.get(buttons.size() - 1).requestFocus();
                			  setCursorPosition(buttons.size() - 1); 
                			  repaint();
		                  }        
		                  else {
		                    	 
	                    	 buttons.get(curRow - 1).requestFocus();
	                    	 setCursorPosition(curRow - 1);
	                    	 repaint();
		                  }
                	}
                	else 
 	                	if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
 	                		 enter=true;
                		  buttons.get((curRow + 1) % buttons.size()).requestFocus();
                		  setCursorPosition((curRow + 1) % buttons.size()); 
                		  repaint();
 	                	} 
				 if(enter)
					 SoundsProvider.playBulletHit1();
				}   
			});	
			
			addActionListener(i);	
			add(buttons.get(i));
	    }
	}
   
	public void addActionListener(int j) {
		
		switch (j) {
		case 0:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {	
					SoundsProvider.playBulletHit1();
					setCursorPosition(1);
					getSwitcher().showMenu();
					repaint();
				}
			});
			break;
		case 1:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					getSwitcher().showFirstStage("./maps/career/singleplayer");
					setCursorPosition(1);
					
				}
			});
			break;
		case 2:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					getSwitcher().showFirstStage("./maps/career/multiplayer");
					setCursorPosition(1);
				}				
			});
			break;
		default:
			break;
		}
	}
					
	public void setBoundAndText(int j) {
	
		switch (j) {
		case 0:
			buttons.get(j).setBounds(20, 20, 70, 30);
			buttons.get(j).setText("Back");
			break;
		case 1:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY, 260, 40);
			buttons.get(j).setText("Singleplayer");
			break;
		case 2:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 , 260, 40);
			buttons.get(j).setText("Multiplayer");
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(getCursorPosition() == 0)
			g.drawImage(ImageProvider.getCursorLeft(), 
					buttons.get(cursorPosition).getX() + 90,buttons.get(cursorPosition).getY() - 8, this);
		else
			g.drawImage(ImageProvider.getCursorRight(), 
					buttons.get(cursorPosition).getX() - 65,buttons.get(cursorPosition).getY() - 5, this);
	}
	
	public int getCursorPosition() {
		return cursorPosition;
	}
	
	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public JButton getButton(int i) {
		return buttons.get(i);
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
	
	private class MyListener extends MouseInputAdapter {

		@Override
		public void mousePressed(MouseEvent e) {

			if(e.getComponent().getY() == buttons.get(0).getY())
				cursorPosition = 0;
			else
				if(e.getComponent().getY() == buttons.get(1).getY())
					cursorPosition = 1;
			else
				if(e.getComponent().getY() == buttons.get(2).getY())
					cursorPosition = 2;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		
			if(e.getComponent().getY() == buttons.get(0).getY())
				cursorPosition = 0;
			else
				if(e.getComponent().getY() == buttons.get(1).getY())
					cursorPosition = 1;
			else
				if(e.getComponent().getY() == buttons.get(2).getY())
					cursorPosition = 2;
			repaint();
		}
	}
	
}