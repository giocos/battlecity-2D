package unical.progetto.igpe.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JTextField;

import unical.progetto.igpe.core.BrickWall;
import unical.progetto.igpe.core.GameManager;
import unical.progetto.igpe.core.PlayerTank;
import unical.progetto.igpe.core.PowerUp;
import unical.progetto.igpe.core.Rocket;
import unical.progetto.igpe.core.SteelWall;
import unical.progetto.igpe.gui.ImageProvider;
import unical.progetto.igpe.gui.MainFrame;
import unical.progetto.igpe.gui.SoundsProvider;
import unical.progetto.igpe.gui.TranslucentWindow;

public class ConnectionManager implements Runnable {

	private MainFrame mainFrame;
	private final String name;
	private String nameOfGame;
	private BufferedReader br;
	private PrintWriter pw;
	private final Socket socket;
	private JTextField map;
	private String difficult;
	
	private boolean soundsPaused;

	//COSTRUTTORE PER P1
	public ConnectionManager(final Socket socket, final String name, MainFrame mainFrame, String stage, String difficult) {
		this.socket = socket;
		this.name = name;
		this.mainFrame = mainFrame;
		this.map=new JTextField("./maps/career/multiplayer/" + stage + ".txt");
		this.difficult=difficult;
		try {
			socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.soundsPaused=false;
	}

	public void close() {
		try {
			socket.close();
		} catch (final IOException e) {
		}
	}

	public void dispatch(final String message) {
		pw.println(message);
		pw.flush();
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void run() {
		try {

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(name+":"+map.getText()+":"+difficult);
			pw.flush();
			
			String buffer = br.readLine();

			while (!buffer.equals("#START")) {

				final String[] split = buffer.split(";");
				if (split.length != 0) {
					for (final String name : split) {
						String[] split1 = name.split(":");
						if (this.name.equals(split1[1])) {
							nameOfGame = split1[0];

						}
					}
				}
				buffer = br.readLine();
			}
			System.out.println("NOME DATO DAL SERVER " + nameOfGame);
			final GameManager gameManager = mainFrame.showNetwork(this, map, difficult);
			buffer = br.readLine();
			while (buffer != null) {
				if (buffer.contains("CLOSE") || gameManager.isExit() || ((gameManager.getPlayersArray().get(0).toString().equals(nameOfGame)
						&& gameManager.getPlayersArray().get(0).isExitOnline())
						|| (gameManager.getPlayersArray().get(1).toString().equals(nameOfGame)
								&& gameManager.getPlayersArray().get(1).isExitOnline()))) {

					buffer = null;

					mainFrame.setTransparent(true);
					mainFrame.getGamePanel().repaint();
					mainFrame.getFullGamePanel().repaint();

					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();

					if (gameManager.getNumbersOfEnemiesOnline() == 0) {
						SoundsProvider.playStageComplete();
						new TranslucentWindow(mainFrame, null, ImageProvider.getStageComplete());
					} else if ((((gameManager.getPlayersArray().get(0).getResume() < 0
							&& gameManager.getPlayersArray().get(1).getResume() < 0) || GameManager.flag.isHit())
							&& !gameManager.getPlayersArray().get(0).isExitOnline()
							&& !gameManager.getPlayersArray().get(1).isExitOnline())
							|| (gameManager.getPlayersArray().get(0).getResume() < 0
									&& gameManager.getPlayersArray().get(0).isExitOnline()
									&& !gameManager.getPlayersArray().get(1).isExitOnline()
									&& GameManager.flag.isHit())
							|| (gameManager.getPlayersArray().get(1).getResume() < 0
									&& gameManager.getPlayersArray().get(1).isExitOnline()
									&& !gameManager.getPlayersArray().get(0).isExitOnline()
									&& GameManager.flag.isHit())) {
						SoundsProvider.playGameOver();
						new TranslucentWindow(mainFrame, null, ImageProvider.getGameOver());
					} 
					mainFrame.setTransparent(false);
					mainFrame.showLobby(false);
					close();
				} else {
					gameManager.parseStatusFromString(buffer);
					playSounds(gameManager);
					mainFrame.getGamePanel().repaint();
					mainFrame.getFullGamePanel().repaint();
					
					buffer = br.readLine();
				}
			}
			System.out.println("EXIT------> CONNECTIONMANAGER CLOSE");
		} catch (final IOException e) {
			System.out.println("Connessione chiusa");
			SoundsProvider.cancelMove();
			SoundsProvider.cancelStop();
			mainFrame.showNetwork();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	private void playSounds(GameManager game) {
		
		restoreBoolean(game);
	
		if(game.isPaused() && !soundsPaused){
			SoundsProvider.playPause();
			soundsPaused=true;
		}
		
		if (game.isSoundPowerUp()) {
			SoundsProvider.playPowerUpAppear();
		}

		if (game.isExplosion()) {
			SoundsProvider.playExplosion1();
			SoundsProvider.playExplosion2();
		}

		// players
		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (game.getPlayersArray().get(a).isShot()) {
				SoundsProvider.playBulletShot();
			}
			
			if(game.getPlayersArray().get(a).getNext() instanceof PowerUp){
				SoundsProvider.playPowerUpPick();
			}
			
			//TODO DA GUARDARE QUESTO CANGO PER IL SUONO!!!
//				if(!game.getPlayersArray().get(a).canGo){
//					SoundsProvider.playHitForCanGo();
//				}
			
			if(nameOfGame.equals(game.getPlayersArray().get(a).toString())){
				if(!game.getPlayersArray().get(a).isDied()) {
					if(game.isPaused()){
						SoundsProvider.cancelMove();
						SoundsProvider.cancelStop();
					}else if (!game.getPlayersArray().get(a).isPressed())
						SoundsProvider.playStop();
					else
						SoundsProvider.playMove();
				}
			}
		}

		// rockets
//		for (int a = 0; a < game.getRocket().size(); a++) {
//			if (game.getRocket().get(a).getTank() instanceof PlayerTank) {
//				if (game.getRocket().get(a).getNext() instanceof BrickWall){
//					SoundsProvider.playBulletHit2();
//				}
//				else if (game.getRocket().get(a).getNext() instanceof SteelWall
//						|| game.getRocket().get(a).isOnBorder()) {
//					SoundsProvider.playBulletHit1();
//				}
//			}
//		}
		
		for(int a=0;a<game.getEffects().size();a++) {
			
			if(game.getEffects().get(a) instanceof Rocket && 
					((Rocket)game.getEffects().get(a)).getTank() instanceof PlayerTank &&
					((Rocket)game.getEffects().get(a)).isOneTimeSound()) {
				if (((Rocket)game.getEffects().get(a)).getNext() instanceof BrickWall) {
					SoundsProvider.playBulletHit2();
					
				}
				else if (((Rocket)game.getEffects().get(a)).getNext() instanceof SteelWall
						|| ((Rocket)game.getEffects().get(a)).isOnBorder()) {
					SoundsProvider.playBulletHit1();
					
				}
				((Rocket)game.getEffects().get(a)).setOneTimeSound(false);
			}
		}
	}

	private void restoreBoolean(GameManager game) {
		if(!game.isPaused()){
			soundsPaused=false;
		}	
	}

	public String getNameOfGame() {
		return nameOfGame;
	}
}
