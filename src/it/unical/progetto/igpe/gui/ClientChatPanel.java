package it.unical.progetto.igpe.gui;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unical.progetto.igpe.gui.MainFrame;
import it.unical.progetto.igpe.gui.SoundsProvider;
import it.unical.progetto.igpe.gui.WarningDialog;
import it.unical.progetto.igpe.net.ConnectionManager;

import java.io.*;
import java.util.List;

@SuppressWarnings("serial")
public class ClientChatPanel extends JPanel implements Runnable {

	private int count;
	private int port;
	private int portChat;
	private String clientName;
	private String host;
	private String difficult;
	private String stage;

	private TextField tf1;
	private TextField tf2;
	private JDialog dialog;
	private TextArea ta;
	//Server Communication
	private Socket socket;
	private DataOutputStream dout;
	private DataInputStream din;

	private MainFrame mainFrame;

	private List<String> nameOfClientsOnline;
	private boolean readyP1;
	private boolean readyP2;
	private boolean exitThrad;
	private boolean showInChat;

	private String points;
	private int updateStageRealTime;
	private String updateDifficultRealTime;

	public ClientChatPanel(String name, String host, int portChat, MainFrame mainFrame) {

		this.count = 0;
		this.readyP1 = false;
		this.readyP2 = false;
		this.exitThrad=false;
		this.showInChat = true;
		this.points = "........................................";
		this.updateStageRealTime = 1; //stage 1 di default
		this.updateDifficultRealTime = "easy";
		
		this.host = host;
		this.clientName = name;
		this.setPortChat(portChat);
		this.setNameOfClientsOnline(new ArrayList<>());
		this.mainFrame = mainFrame;
		this.dialog = new JDialog(dialog, "ERROR");	
		this.tf1 = new TextField(name + ":");
		this.setSize(new Dimension(500, 300));
		this.tf1.setEditable(false);
		this.tf2 = new TextField();
		this.ta = new TextArea();
		this.ta.setEditable(false);
		this.tf1.setBackground(Color.black);
		this.tf2.setBackground(Color.black);
		this.ta.setBackground(Color.black);
		this.tf1.setForeground(Color.white);
		this.tf2.setForeground(Color.white);
		this.ta.setForeground(Color.white);
		this.tf1.setFont(MainFrame.customFontS);
		this.tf2.setFont(MainFrame.customFontS);
		this.ta.setFont(MainFrame.customFontS);
		this.tf2.requestFocus();
		this.setLayout(new BorderLayout());
		this.add("North", tf1);
		this.add("South", tf2);
		this.add("Center", ta);
		this.tf2.addActionListener(ae -> processMessage(ae.getActionCommand()));

		try {
			socket = new Socket(host, Integer.valueOf(portChat));
			System.out.println("Connessione per " + clientName);
			din = new DataInputStream(socket.getInputStream());
			setDout(new DataOutputStream(socket.getOutputStream()));
			new Thread(this).start();
			processMessage(tf1.getText() + "^^^^^^");

		} catch (IOException e) {
			showDialog();
		}
			
	}

	private void processMessage(String message) {
		try {
			getDout().writeUTF(tf1.getText() + ":" + message);
			tf2.setText(" ");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			while (!exitThrad) {
				String message = din.readUTF();
				String[] elements = message.split(" ");

				// -----------------------------------------------

				System.out.println("-> " + message);

				if (elements[0].equals("EXIT")) {
					String client = elements[1];
					nameOfClientsOnline.remove(client);
//					to.setText("People Online: \n");
//					for (int a = 0; a < nameOfClientsOnline.size(); a++) {
//						to.append(nameOfClientsOnline.get(a) + "\n");
//					}
				} else if(elements[0].equals("EXITALL")){
					exitThrad = true;
					mainFrame.showNetwork();	
				} else if (elements.length == 2) {
					if (elements[0].equals("p2") && elements[1].equals("true")) {
						readyP2 = true;
						showInChat = false;
					} else if (elements[0].equals("p2") && elements[1].equals("false")) {
						readyP2 = false;
						showInChat = false;
					} else if (elements[0].equals("p1") && elements[1].equals("true")) {
						readyP1 = true;
						showInChat = false;
					} else if (elements[0].equals("p1") && elements[1].equals("false")) {
						readyP1 = false;
						showInChat = false;
					}
				} else if (elements.length == 4) {
					if (elements[0].equals("connect")) {
						port = Integer.parseInt(elements[1]);
						stage = elements[2];
						difficult = elements[3];
						showInChat = false;
					}
				}
				
				if (elements[0].equals("OKNAME")){
					mainFrame.showLobby(false);
					showInChat = false;
				}
			
				if (elements[0].equals("ERRORNAME")){
					exitThrad = true;
					mainFrame.setTransparent(true);
					new WarningDialog(clientName + " is already present in the Lobby. Try again!", mainFrame);
					mainFrame.setTransparent(false);
					mainFrame.showNetwork();
				}
				
				if (elements.length == 1 && elements[0].equals(points + "StartGame") && readyP1 && readyP2) { // entrambi si connettono
					readyP1 = false;
					readyP2 = false;
					try {
						connectoToServer();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				
				if(elements[0].equals("#PLAY#START")){
					SoundsProvider.playOnlineStart();
					showInChat=false;
				}
				
				else if(elements[0].equals("#PLAY#END")){
					SoundsProvider.playOnlineEnd();
					showInChat=false;
				}
				
				if(elements[0].equals("#stage#")) {
					setUpdateStageRealTime(Integer.valueOf(elements[1]));
					showInChat=false;
				}
				
				else if(elements[0].equals("#difficult#")) {
					updateDifficultRealTime=(elements[1]);
					showInChat=false;
				}

				// ------------------------------------------------

				if(showInChat) {
					if (count == 0 && !(message.equals(null))) {
						String[] names = message.split(" ");
						int i = 0;
	
						while (i < names.length) {
							if (!names[i].equals("")) {
	//							to.append(names[i] + "\n");
								nameOfClientsOnline.add(names[i]);
							}	
							i++;
						}
						count++;
					} else {
						boolean name = true;
						int len = message.length();
	
						for (int i = 0; i < 6; i++) {
							if (!(message.charAt(len - i - 1) == '^')) {
								name = false;
//								System.out.println(message.charAt(len - i - 1));
								break;
							}
						}
	
						if (name == false) {
							ta.append(message + "\n");
						} else {
							String name1 = "";
							int i = 0;
							while (!(message.charAt(i) == ':' && message.charAt(i + 1) == ':')) {
								name1 = name1 + message.charAt(i);
								i++;
							}
							nameOfClientsOnline.add(name1);
							if(nameOfClientsOnline.get(0).equals(clientName)){
								dout.writeUTF("#difficult# "+updateDifficultRealTime);
								dout.writeUTF("#stage# "+String.valueOf(getUpdateStageRealTime()));
							}
//							to.append(name1 + "\n");
						}
					}
				}
				showInChat=true;
			}

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	private void showDialog() {

		JLabel label = new JLabel("Impossible to connect to " + host );
		label.setFont(MainFrame.customFontS);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.RED);
		label.setHorizontalAlignment(JLabel.CENTER);

		JPanel panel = new JPanel(new GridLayout(2, 0));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createLineBorder(Color.RED));

		JButton ok = new JButton("OK");
		ok.setBorder(null);
		ok.setContentAreaFilled(false);
		ok.setBorderPainted(false);
		ok.setFocusPainted(false);
		ok.setFont(MainFrame.customFontS);
		ok.setBackground(Color.BLACK);
		ok.setForeground(Color.WHITE);
		ok.addActionListener(e -> {
			SoundsProvider.playBulletHit1();
			dialog.dispose();
			mainFrame.showNetwork();
		});

		panel.add(label);
		panel.add(ok);
		panel.setPreferredSize(new Dimension(300, 100));
		dialog.setContentPane(panel);
		dialog.setUndecorated(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}	

	protected void connectoToServer() throws Exception {
		Socket socket = new Socket(host, port);
		ConnectionManager connectionManager = null;
		connectionManager = new ConnectionManager(socket, clientName, mainFrame, stage, difficult);
		new Thread(connectionManager, "Connection Manager").start();
	}
	
	public boolean isPresentInTheArrayOfClientOnline() {
		for (String s : nameOfClientsOnline) {
			if (s.equals(clientName)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getNameOfClientsOnline() {
		return nameOfClientsOnline;
	}

	public void setNameOfClientsOnline(ArrayList<String> nameOfClientsOnline) {
		this.nameOfClientsOnline = nameOfClientsOnline;
	}

	public String getClientName() {
		return clientName;
	}

	public boolean isReadyP1() {
		return readyP1;
	}

	public void setReadyP1(boolean readyP1) {
		this.readyP1 = readyP1;
	}

	public boolean isReadyP2() {
		return readyP2;
	}

	public void setReadyP2(boolean readyP2) {
		this.readyP2 = readyP2;
	}

	public int getPortChat() {
		return portChat;
	}

	public void setPortChat(int portChat) {
		this.portChat = portChat;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public int getUpdateStageRealTime() {
		return updateStageRealTime;
	}

	public void setUpdateStageRealTime(int updateStageRealTime) {
		this.updateStageRealTime = updateStageRealTime;
	}

	public String getUpdateDifficultRealTime() {
		return updateDifficultRealTime;
	}

	public void setUpdateDifficultRealTime(String updateDifficultRealTime) {
		this.updateDifficultRealTime = updateDifficultRealTime;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isExitThrad() {
		return exitThrad;
	}

	public void setExitThrad(boolean exitThrad) {
		this.exitThrad = exitThrad;
	}

	public DataOutputStream getDout() {
		return dout;
	}

	public void setDout(DataOutputStream dout) {
		this.dout = dout;
	}
}
