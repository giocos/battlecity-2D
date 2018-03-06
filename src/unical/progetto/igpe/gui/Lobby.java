package unical.progetto.igpe.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import unical.progetto.igpe.net.ClientChat;
import unical.progetto.igpe.net.Server;


@SuppressWarnings("serial")
public class Lobby extends JPanel {

	private int width;
	private int height;
	private PanelSwitcher switcher;
	private int DIM;
	private int cursorPosition;
	private String difficult;
	private String stage;
	private ClientChat client;
	private int countDown;
	private Timer timer;
	private TimerTask task;
	private ButtonGroup group;
	private JTextField ipTextField;
	private ArrayList<JLabel> labels;
	private JTextField nameTextField;
	private JTextField portTextField;
	private JButton arrowLeft, arrowRight;
	private ArrayList<JRadioButton> level;
	private final ArrayList<JButton> buttons;
	
	public Lobby(int w, int h, PanelSwitcher switcher) {
		
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		setSwitcher(switcher);
		width = w;
		height = h;
		difficult = "easy";
		stage = "stage1";
		DIM = 2;
		cursorPosition = 0;
		countDown = 5;

		arrowLeft = new JButton();
		arrowRight = new JButton();
		buttons = new ArrayList<>();
		level = new ArrayList<>();
		labels = new ArrayList<>();
		group = new ButtonGroup();

		createButton();
		buttons.get(1).setForeground(Color.YELLOW);

		JLabel chat = new JLabel("Chat: ");
		chat.setFont(MainFrame.customFontM);
		chat.setBackground(Color.BLACK);
		chat.setForeground(Color.WHITE);
		chat.setBounds(80, height - 260, 100, 40);
		add(chat);

		JLabel label = new JLabel("Maps: ");
		label.setFont(MainFrame.customFontM);
		label.setForeground(Color.WHITE);
		label.setBounds(width - 440, 90, 100, 40);
		add(label);


		JLabel online = new JLabel("Online: ");
		online.setFont(MainFrame.customFontM);
		online.setForeground(Color.WHITE);
		online.setBounds(80, 90, 100, 40);
		add(online);

		createOnlinePanel();

		JLabel difficult = new JLabel("Difficult: ");
		difficult.setFont(MainFrame.customFontM);
		difficult.setForeground(Color.WHITE);
		difficult.setBounds(520, 90, 130, 40);
		add(difficult);

		
	}

	public class MyTask extends TimerTask {

		public void run() {
	
			if(client.isReadyP1() && client.isReadyP2() && client.getClientName().equals(client.getNameOfClientsOnline().get(0))) {
				try {
					client.getDout().writeUTF(String.valueOf(client.getPoints() + countDown--));	 // <- "..............5"
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					client.getDout().writeUTF("#PLAY#START");	
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			if (countDown < 0) {
				try {
					client.getDout().writeUTF(String.valueOf(client.getPoints() + "StartGame"));	 // <- "..............5"
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					client.getDout().writeUTF("#PLAY#END");	
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.cancel();
			}
		}
	}
	
	public void createDifficultPanel() {
		
		JPanel difficultPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				if((client.isReadyP1() && client.isReadyP2()) || client.getNameOfClientsOnline().size() > 1 && client.getClientName().equals(client.getNameOfClientsOnline().get(1))) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					
					if(client.getUpdateDifficultRealTime().equals("easy")) {
						level.get(0).setSelected(true);
						level.get(1).setSelected(false);
						level.get(2).setSelected(false);
					}else if(client.getUpdateDifficultRealTime().equals("normal")) {
						level.get(1).setSelected(true);
						level.get(0).setSelected(false);
						level.get(2).setSelected(false);
						
					}else {
						level.get(2).setSelected(true);
						level.get(0).setSelected(false);
						level.get(1).setSelected(false);
					}
				}	
			}
		};
		
		difficultPanel.setLayout(null);
		difficultPanel.setBounds(520, 130, 200, 200);
		difficultPanel.setBackground(Color.DARK_GRAY);
		add(difficultPanel);

		for (int i = 0; i < 3; i++) {

			labels.add(new JLabel());
			level.add(new JRadioButton());
			level.get(i).setBackground(null);

			if (i == 0) {
				level.get(i).setBounds(50, 40, 20, 20);
				labels.get(i).setText("easy");
				labels.get(i).setBounds(75, 40, 70, 20);
			} else if (i == 1) {
				level.get(i).setBounds(50, 90, 20, 20);
				labels.get(i).setText("normal");
				labels.get(i).setBounds(75, 90, 70, 20);
			} else {
				level.get(i).setBounds(50, 140, 20, 20);
				labels.get(i).setText("hard");
				labels.get(i).setBounds(75, 140, 70, 20);
			}
			labels.get(i).setForeground(Color.BLACK);
			labels.get(i).setFont(MainFrame.customFontS);

			group.add(level.get(i));
			difficultPanel.add(level.get(i));
			difficultPanel.add(labels.get(i));
			
			if((!client.isReadyP1() || !client.isReadyP2()) && client.getNameOfClientsOnline().size() > 0 && client.getClientName().equals(client.getNameOfClientsOnline().get(0)))
				level.get(i).addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!client.isReadyP1() || !client.isReadyP2()) {
						if(client.getClientName().equals(client.getNameOfClientsOnline().get(0)))
							if (level.get(0).isSelected()) {
								difficult = "easy";
							} else if (level.get(1).isSelected()) {
								difficult = "normal";
							} else if (level.get(2).isSelected()) {
								difficult = "hard";
							}
							repaint();
							
							try {
								client.getDout().writeUTF("#difficult# "+difficult);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							SoundsProvider.playBulletHit1();
						
					}
						}
				});
	
			if((client.isReadyP1() && client.isReadyP2()) || client.getNameOfClientsOnline().size() > 1 && client.getClientName().equals(client.getNameOfClientsOnline().get(1))) {
		
				level.get(i).setBorder(null);
				level.get(i).setOpaque(false);
				level.get(i).setContentAreaFilled(false);
				level.get(i).setBorderPainted(false);
				level.get(i).setFocusPainted(false);
			}
		}
		level.get(0).setSelected(true);

	}

	public void createOnlinePanel() {

		JPanel onlinePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				int cont = 0;
				int y = 40;
				while (cont < client.getNameOfClientsOnline().size()) {
					
						if (client.getClientName().equals(client.getNameOfClientsOnline().get(cont)))
							g.setColor(Color.YELLOW);
						else
							g.setColor(Color.BLACK);
						
						g.setFont(MainFrame.customFontM);
						g.drawString(client.getNameOfClientsOnline().get(cont), 32, y);
						
						
						//scritta ready
						if(client.isReadyP1() && cont == 0) {
							g.setFont(MainFrame.customFontS);
							g.setColor(Color.GREEN);
							g.drawString("ready", 245, y);
						}
						else if(client.isReadyP2() && cont == 1) {
							g.setFont(MainFrame.customFontS);
							g.setColor(Color.GREEN);
							g.drawString("ready", 245, y);
						}
						
						//Scritta Moderatore
						if(cont == 0) {
							g.setFont(MainFrame.customFontS);
							g.setColor(Color.YELLOW);
							g.setFont(g.getFont().deriveFont(10.0f));
							g.drawRect(2, y-18, 12, 12);
							g.drawString("M", 5, y-8);
						}
						
						y += 40;
					
					cont++;
				}

			}
		};
	
		
		onlinePanel.setLayout(null);
		onlinePanel.setBounds(80, 130, 300, 300);
		onlinePanel.setBackground(Color.DARK_GRAY);
		add(onlinePanel);
	}

	public void createChat(ClientChat client) {

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(null);
		chatPanel.setBounds(80, height - 220, 800, 190);
		chatPanel.setBackground(Color.DARK_GRAY);

		client.setSize(new Dimension(800, 190));
		chatPanel.add(client);
		add(chatPanel);
		
		
		
		// solo moderatore avvia timer che manda messaggi
		if(client.getClientName().equals(client.getNameOfClientsOnline().get(0))){
			timer =  new Timer();
			task = new MyTask();
			timer.schedule(task, 1000, 1000);
		}
	}

	public void createMapsPanel() {

		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				if((client.isReadyP1() && client.isReadyP2()) || client.getNameOfClientsOnline().size() > 1 && client.getClientName().equals(client.getNameOfClientsOnline().get(1))) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				}	
		
				g.drawImage(ImageProvider.getMapsP2().get(client.getUpdateStageRealTime() - 1), 45, 20, this);
				g.setColor(Color.BLACK);
				g.setFont(MainFrame.customFontM);
				stage = "stage" + client.getUpdateStageRealTime();
				g.drawString("Stage " + client.getUpdateStageRealTime(), 90, 238);
			}
		};

		panel.setLayout(null);
		panel.setBounds(width - 440, 130, 260, 260);
		panel.setBackground(Color.DARK_GRAY);
		add(panel);

		arrowLeft.setBorder(null);
		arrowLeft.setIcon(new ImageIcon(ImageProvider.getArrowLeftsmall()));
		arrowLeft.setBounds(30, 210, 30, 40);
		arrowLeft.setContentAreaFilled(false);
		arrowLeft.setBorderPainted(false);
		arrowLeft.setFocusPainted(false);

	if((!client.isReadyP1() || !client.isReadyP2()) && client.getNameOfClientsOnline().size() > 0 && client.getClientName().equals(client.getNameOfClientsOnline().get(0)))
		arrowLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!client.isReadyP1() || !client.isReadyP2()) {
				SoundsProvider.playBulletHit1();
				if (client.getUpdateStageRealTime() == 1) {
					client.setUpdateStageRealTime(24);
				}
				else {
					client.setUpdateStageRealTime(client.getUpdateStageRealTime() - 1);
				}
				repaint();

				try {
					client.getDout().writeUTF("#stage# "+String.valueOf(client.getUpdateStageRealTime()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				}
			}
		});
		panel.add(arrowLeft);

		arrowRight.setBorder(null);
		arrowRight.setIcon(new ImageIcon(ImageProvider.getArrowRightsmall()));
		arrowRight.setBounds(202, 210, 30, 40);
		arrowRight.setContentAreaFilled(false);
		arrowRight.setBorderPainted(false);
		arrowRight.setFocusPainted(false);
	
	if((!client.isReadyP1() || !client.isReadyP2()) && client.getNameOfClientsOnline().size() > 0 && client.getClientName().equals(client.getNameOfClientsOnline().get(0)))
		arrowRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!client.isReadyP1() || !client.isReadyP2()) {
				SoundsProvider.playBulletHit1();
				if(client.getUpdateStageRealTime() < 23)
					client.setUpdateStageRealTime((client.getUpdateStageRealTime() + 1) % 24);
				else
					client.setUpdateStageRealTime(1);
				repaint();
				
				try {
					client.getDout().writeUTF("#stage# "+String.valueOf(client.getUpdateStageRealTime()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				}
			}
		});
		
		panel.add(arrowRight);
		
		if(client.getNameOfClientsOnline().size() > 1 && client.getClientName().equals(client.getNameOfClientsOnline().get(1))) {
			arrowLeft.setEnabled(false);
			arrowRight.setEnabled(false);
		}

	}

	public void createButton() {

		for (int i = 0; i < DIM; i++) {

			final int curRow = i;

			buttons.add(new JButton());
			buttons.get(i).setBorder(null);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment(SwingConstants.LEFT);

			if (i == 0)
				buttons.get(i).setFont(MainFrame.customFontM);
			else
				buttons.get(i).setFont(MainFrame.customFontB);

			setBoundAndText(i);
			buttons.get(i).addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					
					if(!client.isReadyP1() || !client.isReadyP2()) {

					boolean enter = false;

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						enter = true;
						SoundsProvider.playBulletHit1();
						setCursorPosition(1);

						// chiude tutto
						if (client.getClientName().equals(client.getNameOfClientsOnline().get(0))) {
							timer.cancel();
							try {
								client.getDout().writeUTF("EXITALL");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						} else {
							try {
								client.getDout().writeUTF("EXIT "+client.getClientName());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						}
						getSwitcher().showNetwork();
						
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						enter = true;
						if (curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							setCursorPosition(buttons.size() - 1);
							repaint();
						} else {

							buttons.get(curRow - 1).requestFocus();
							setCursorPosition(curRow - 1);
							repaint();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						enter = true;
						buttons.get((curRow + 1) % buttons.size()).requestFocus();
						setCursorPosition((curRow + 1) % buttons.size());
						repaint();
					}
					if (enter)
						SoundsProvider.playBulletHit1();
				}
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
					if(!client.isReadyP1() || !client.isReadyP2()) {
					SoundsProvider.playBulletHit1();
					setCursorPosition(0);

					// chiude tutto
					if (client.getClientName().equals(client.getNameOfClientsOnline().get(0))) {
						timer.cancel();
						try {
							client.getDout().writeUTF("EXITALL");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					
					} else {
						try {
							client.getDout().writeUTF("EXIT "+client.getClientName());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					
					}
					getSwitcher().showNetwork();
					repaint();
				}
				}
			});
			break;
		case 1:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
			

					if(!client.isReadyP1() || !client.isReadyP2()) {
						SoundsProvider.playBulletHit1();
					// se sono P1
					if (client.getClientName().equals(client.getNameOfClientsOnline().get(0))) {
						if (client.isReadyP2()) {
							
						
						//  1) creo il server
							
							final Server server1 = new Server(1234);
							new Thread(server1, "game").start();
							
							server1.setModeratorServerGame(client.getNameOfClientsOnline().get(0));
							
							
						//	2) mando il messaggio a tutti 
							try {
								client.getDout().writeUTF("p1 true");
							} catch (IOException e2) {
								e2.printStackTrace();
							}
										
						//  3) do il via libero al connect ( tutti i client )
							try {
								client.getDout().writeUTF("connect"+" "+portTextField.getText()+" "+stage+" "+difficult);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						
						}
					}

					// se sono P2
					else if (client.getClientName().equals(client.getNameOfClientsOnline().get(1))) {
						if (!client.isReadyP2()) {
							try {
								client.getDout().writeUTF("p2 true");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else {
							try {
								client.getDout().writeUTF("p2 false");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}

				}
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
			buttons.get(j).setBounds(width - 250, height - 150, 100, 50);
			buttons.get(j).setText("Start");
			break;

		default:
			break;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		if (cursorPosition == 0)
			g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 85,
					buttons.get(cursorPosition).getY() - 6, this);
		else
			g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 60,
					buttons.get(cursorPosition).getY(), this);

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

	public JTextField getIpTextField() {
		return ipTextField;
	}

	public void setIpTextField(JTextField ipTextField) {
		this.ipTextField = ipTextField;
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(JTextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public JTextField getPortTextField() {
		return portTextField;
	}

	public void setPortTextField(JTextField portTextField) {
		this.portTextField = portTextField;
	}

	public void setClient(ClientChat client) {
		this.client = client;
	}
	
	public ClientChat getClient() {
		return client;
	}

}
