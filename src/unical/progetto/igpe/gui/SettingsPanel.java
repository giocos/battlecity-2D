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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {

	private boolean hide;
	protected static float soundValue=0.0f;
	public static boolean easy=true, normal=false, hard=false;
	
	private float currValue;
	private int cursorPosition;
	private final int DIM = 4;
	private PanelSwitcher switcher;
	private JSlider sound;
	private ArrayList<JRadioButton> level;
	private ArrayList<JButton> buttons;
	private ButtonGroup group;
	private JDialog dialogKeyBoard;

	public SettingsPanel(final int w, final int h, PanelSwitcher switcher) {

		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		this.dialogKeyBoard = new JDialog(((MainFrame)switcher));
		hide = false;
		cursorPosition = 1;
		buttons = new ArrayList<>();
		level = new ArrayList<>();
		group =  new ButtonGroup();
		setSwitcher(switcher);
		setButtons();
		setSlider();
		createRadioButtons();
	}
	
	public void setButtons() {
		
		for(int i = 0; i < DIM; i++) {
		
			final int curRow = i;
			
			buttons.add(new JButton());
			
			if(i == 0)
				buttons.get(i).setFont(MainFrame.customFontM);
			else
				buttons.get(i).setFont(MainFrame.customFontB);
				
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setHorizontalAlignment(SwingConstants.LEFT);
			setBoundsAndText(i);
			addActionListener(i);
			buttons.get(i).addMouseListener(new MouseInputAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
				
					if(e.getComponent().getY() == buttons.get(curRow).getY()) {
						cursorPosition = curRow;
						repaint();
					}
				}
			});
			buttons.get(i).addKeyListener(new KeyAdapter() {
			
				@Override
				public void keyPressed(KeyEvent e) {
					
					boolean enter = false;
					
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	         	        
						hide = false; 
	         	        repaint();
						((JButton) e.getComponent()).doClick();
	                }
					else 
	              	  if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	              		
	              		SoundsProvider.playBulletHit1();
	              		hide = false;
	              		repaint();
	              		cursorPosition = 1;
	              		getSwitcher().showMenu();
	              	}
	              	else 
	              		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {

	              			enter = true;
	              			hide = false;

						if(curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							cursorPosition = buttons.size() - 1;
							repaint();
							
						} else {

							buttons.get(curRow - 1).requestFocus();
							cursorPosition = curRow - 1;
							repaint();
						}
					} else 
						if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {

							enter = true;
							hide = false;
							buttons.get((curRow + 1) % buttons.size()).requestFocus();
							cursorPosition = (curRow + 1) % buttons.size();
							repaint();
					}
					
					if(enter) 
						SoundsProvider.playBulletHit1();
				}
			});
			
				this.add(buttons.get(i));
			}
	}
	
	public void setBoundsAndText(int j) {
		
		switch(j) {
		case 0:
			buttons.get(j).setBounds(20, 20, 100, 30);
			buttons.get(j).setText("Back");
			break;
		case 1:
			buttons.get(j).setBounds(265, 270, 200, 100);
			buttons.get(j).setText("Sound");
			break;
		case 2:
			buttons.get(j).setBounds(265, 370, 200, 100);
			buttons.get(j).setText("Level");
			break;
		case 3:
			buttons.get(j).setBounds(265, 470, 200, 100);
			buttons.get(j).setText("Keys");
		default:
			break;
		}
	}
	
	public void addActionListener(int j) {
		
		switch(j) {
		case 0:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
					cursorPosition = 1;
					getSwitcher().showMenu();
				}
			});
			break;
		case 1:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
				}
			});
			break;
		case 2:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
				}
			});
			break;
		case 3:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					SoundsProvider.playBulletHit1();
					cursorPosition = j;
					repaint();
					keyboard();
					
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void keyboard() {

		dialogKeyBoard.setPreferredSize(new Dimension(1236, 530));
		
		JPanel p = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				
				g.drawImage(ImageProvider.getKeyboard(), 0, 0, null);
			}
		};
		p.setBackground(Color.BLACK);
		p.setLayout(null);
		p.setBorder(BorderFactory.createLineBorder(Color.RED));
		JButton b = new JButton("Close");
		b.setFont(MainFrame.customFontM);
		b.setBackground(Color.BLACK);
		
		b.setBorder(null);
		b.setOpaque(false);
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setForeground(Color.WHITE);
		b.setBackground(Color.BLACK);
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.setBounds(1160, 10, 80, 35);
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundsProvider.playBulletHit1();
				dialogKeyBoard.dispose();
	
			}
		});
		b.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				SoundsProvider.playBulletHit1();
					dialogKeyBoard.dispose();
			}
		});
		p.add(b);
		
		dialogKeyBoard.add(p);
		dialogKeyBoard.setUndecorated(true);
		dialogKeyBoard.setModal(true);
		dialogKeyBoard.pack();
		dialogKeyBoard.setLocationRelativeTo(this);
		dialogKeyBoard.setVisible(true);

	}

	public void createRadioButtons() {

		for(int i = 0; i < DIM; i++) {
			
			level.add(new JRadioButton());
			level.get(i).setBackground(null);
			
			if(i == 0)
				level.get(i).setSelected(easy);
			
			level.get(i).addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					
					hide = true;
					
					if(level.get(0).isSelected()) {						
						
						easy = true;
						normal = hard = false;
					}
					else
						if(level.get(1).isSelected()) {
							
							normal = true;
							easy = hard = false;						
						}
						else
							if(level.get(2).isSelected()) {
								
								hard = true;
								easy = normal = false;
							}
					
					repaint();
					}
			});	
			
			setBoundRadioButton(i);
			group.add(level.get(i));
			this.add(level.get(i));
	    }
	}

	public void setBoundRadioButton(int j) {
		
		switch (j) {
		case 0:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 - 100),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		case 1:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 + 35),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		case 2:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 + 170),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		default:
			break;
		}
	}
	
	public void setSlider() {

		sound = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		sound.setBounds((int) (this.getPreferredSize().getWidth() / 2 - 105),
				(int) (this.getPreferredSize().getHeight() / 2 - 65), 300, 50);
		sound.setBackground(null);
		sound.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				hide = true;
				repaint();
				
				if(sound.getValue() != 0) {
				
					currValue = (sound.getMaximum() - sound.getValue()) / 2;
					soundValue = currValue / 2;
				}
				else
					soundValue = currValue;
			}
		});

		this.add(sound);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		if(!hide) {
		
			if(cursorPosition == 0)
				g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 100,buttons.get(cursorPosition).getY() - 8, this);
			else
				g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 35, buttons.get(cursorPosition).getY() + 25, this);
		}
		
		g.drawImage(ImageProvider.getEasy(), level.get(0).getX() - 17, level.get(0).getY() + 25, null);
		g.drawImage(ImageProvider.getNormal(),level.get(1).getX() - 27, level.get(1).getY() + 25, null);
		g.drawImage(ImageProvider.getHard(), level.get(2).getX() - 17, level.get(2).getY() + 25, null);
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
	
	public int getCursorPosition() {
		return cursorPosition;
	}
}
