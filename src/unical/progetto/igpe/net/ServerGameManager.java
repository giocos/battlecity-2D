package unical.progetto.igpe.net;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextField;

import unical.progetto.igpe.core.GameManager;
import unical.progetto.igpe.gui.GamePanel;

public class ServerGameManager {
	
	private final ArrayList<ClientManager> clients;
	private final Set<ClientManager> readyClients;
	private HashMap<String,String>name;
	private GameManager gameManager;
	private GamePanel gamePanel;
	private String difficult;
	private JTextField map;
	private Server server;
	private String moderatorNameOfGame;

	public ServerGameManager(Server server) {
		this.server=server;
		this.clients = new ArrayList<>();
		this.readyClients = new HashSet<ClientManager>();
		this.name=new HashMap<>();
		this.map=new JTextField();
	}
	
	public void add(final ClientManager cm) {
		clients.add(cm);
		System.out.println("connesso." );
	}

	public void dispatch(final String message) {
		for (final ClientManager cm : clients) {
			if (cm != null) {
				cm.dispatch(message);
			}
		}
	}

	public String getConnectedClientNames() {
		final StringBuilder sb = new StringBuilder();
		for (final ClientManager cm : clients) {
			if (cm.getName() != null) {
				sb.append(cm.getName()+":");
				for(Map.Entry<String, String> entry : name.entrySet()) {
					String key = entry.getKey();
					String names= entry.getValue();	
					if(names.equals(cm.getName())){
						sb.append(key);
					}
				}
				sb.append(";");
			}
		}	
		return sb.toString();
	}

	public void received(final String buffer) {
		final String[] split = buffer.split(":");
				
		if(gameManager!=null){
			if(split[0].equals("EXIT")){
				disconnetctedClient(split[1]);
//				gameManager.setExit(Boolean.parseBoolean(split[2]));
			}else if(split[0].equals("TIME")){
				for(int a=0; a<gameManager.getPlayersArray().size(); a++){
					if(gameManager.getPlayersArray().get(a).toString().equals(split[1])){
						gameManager.getPlayersArray().get(a).setCurrentTimeMillis(Long.parseLong(split[2]));
					}
				}
			}else{
				//BOOLEANE DI SISTEMA
				gameManager.setPauseOptionDialog(Boolean.parseBoolean(split[5]));
				gameManager.setPaused(Boolean.parseBoolean(split[6]));
				
				for(int a=0; a<gameManager.getPlayersArray().size(); a++){
					if(gameManager.getPlayersArray().get(a).toString().equals(split[0])){
						if(split[2].equals("YES")){
							gameManager.getPlayersArray().get(a).getKeyBits().set(Integer.valueOf(split[1]));
							gameManager.getPlayersArray().get(a).setKeyPressedMillis(Long.parseLong(split[3]));
						}
						else if(split[2].equals("NO")){
							gameManager.getPlayersArray().get(a).getKeyBits().clear(Integer.valueOf(split[1]));
							gameManager.getPlayersArray().get(a).setReleaseKeyRocket(Boolean.parseBoolean(split[4]));
						}
					}
				}
			}
		}
	}
	
	public void setReady(final ClientManager clientManager) {
		synchronized (readyClients) {
			readyClients.add(clientManager);
			
			if (readyClients.size() == 2) {
				dispatch("#START");
				System.out.println("ServerGame PRONTO!");
			}
		}
	}
	
	public void setupClient() throws IOException {
		ArrayList<String>nameOfPlayers=new ArrayList<>();
		nameOfPlayers.add("P1");
		nameOfPlayers.add("P2");
		final List<String> names = new ArrayList<>();
		for (final ClientManager cm : clients) {
			String nameTmp=cm.setup(nameOfPlayers.remove(0));
			if(nameTmp.equals(server.getModeratorServerGame())){
				moderatorNameOfGame=cm.getName();
			}
			new Thread(cm, cm.getName()).start();
			names.add(cm.getName());
		}	
	}

	public void startGame() throws IOException {
		for (final ClientManager cm : clients) {
			map.setText(cm.getMap().getText());
			difficult=cm.getDifficult();
			break;
		}
		
		gameManager = new GameManager(new Runnable() {
			@Override
			public void run() {
				final String statusToString = gameManager.statusToString();
				dispatch(statusToString);
			}
		}, name, map);
		
		gamePanel=new GamePanel(null, difficult);
		gamePanel.setGame(gameManager);
		
		new Thread() {
			@Override
			public void run() {
				gamePanel.gameLoop();
				gameManager.getTimer().cancel();
				gameManager.getTimer2().cancel();
				System.out.println("CHIUSO_SERVERGAME");
				dispatch("CLOSE");
				try {
					server.getServerSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void disconnetctedClient(String name) {
		if(name.equals(moderatorNameOfGame)){
			gameManager.setExit(true);	
		}else{
		
			int cont = 0;
			
			for(int a=0; a<gameManager.getPlayersArray().size(); a++){
				if(gameManager.getPlayersArray().get(a).toString().equals(name)){
					gameManager.getPlayersArray().get(a).setResume(0);
					gameManager.destroyPlayerTank(gameManager.getPlayersArray().get(a));
					gameManager.getPlayersArray().get(a).setExitOnline(true);
				}
				if(gameManager.getPlayersArray().get(a).getResume()<0){
					cont++;
				}
			}
			if(cont==2){
				gamePanel.gameOverOrWin();
			}
		}
		System.out.println("CLIENTE DISCONNESSO: " + name);
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public HashMap<String, String> getName() {
		return name;
	}

	public void setName(HashMap<String, String> name) {
		this.name = name;
	}

	public String getModeratorNameOfGame() {
		return moderatorNameOfGame;
	}

	public void setModeratorNameOfGame(String moderatorNameOfGame) {
		this.moderatorNameOfGame = moderatorNameOfGame;
	}
}
