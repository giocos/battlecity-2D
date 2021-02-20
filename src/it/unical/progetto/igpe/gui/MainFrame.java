package it.unical.progetto.igpe.gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;

import it.unical.progetto.igpe.core.Flag;
import it.unical.progetto.igpe.core.GameManager;
import it.unical.progetto.igpe.net.ConnectionManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PanelSwitcher {

	private static final String TITLE = "BATTLE CITY UNICAL";
	private static final int WIDTH = 1300;
	private static final int HEIGHT = 740;

	private final int gameWidth = WIDTH - 565;
  	private final int gameHeight = HEIGHT - 40;
 
	public static Font customFontM;
	public static Font customFontB;
	public static Font customFontS; 
	 
	private boolean transparent;
	private boolean slide = true; 
	private int highScoreP1;
	private int highScoreP2;
	private int unlockedMapsP1;
	private int unlockedMapsP2;
	private int resumeP1;
	private int resumeP2;
	private int levelP1;
	private int levelP2;

	private JComponent component;

	//FullScreen
	private GraphicsEnvironment graphicscEnvironment;
	private GraphicsDevice device;
	private boolean pressF11 = false;
	private boolean fullscreen;

	//Core
	private GameManager gameManager;
	private Flag flag;

	//Panels
	private GamePanel gamePanel;
	private LoadPanel loadPanel;
	private MenuPanel menuPanel;
	private ScorePanel scorePanel;
	private LobbyPanel lobbyPanel;
	private EditorPanel editorPanel;
	private PlayerPanel playerPanel;
	private NetworkPanel networkPanel;
	private SettingsPanel settingsPanel;
	private FullGamePanel fullGamePanel;
	private StagePanelFirst firstStagePanel;
	private StagePanelSecond secondStagePanel;

	//Slide Panels
	private SlideStage slideStage;
	private SlideContainer slideContainer;

	public MainFrame() {
		this.setLayout(new BorderLayout());
		this.setTitle(TITLE);
		this.setSize(new Dimension(WIDTH, HEIGHT));
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(e -> {
				if (e.getKeyCode() == KeyEvent.VK_F11 && !pressF11){
					if(!fullscreen) {
						mainScreenTurnOn();
					} else {
						mainScreenTurnOff();
					}
					pressF11 = true;
				} else {
				  pressF11 = false;
				}
				return false;
		  });
		
		loadPanel = new LoadPanel(WIDTH, HEIGHT, this);
		component = loadPanel;
		this.add(loadPanel);
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * @author Giovanni
	 * Application Main
	 */
	public static void main(String[] args) {
		MainFrame main = new MainFrame();
		main.instantiate();
	}

	//------------------------------------FULLSCREEN-----------------------------------
	public void mainScreenTurnOn() {
		dispose(); 
		transparent = false;
		setUndecorated(true);
		device.setFullScreenWindow(this);
		fullscreen = true;
	
		this.validate();
		this.repaint();
		component.transferFocusBackward();
		selectFocusButton(component);
	}
	
	public void mainScreenTurnOff() {
		fullscreen = false;
		transparent = false;
		device.setFullScreenWindow(null);
		dispose();
		setUndecorated(false);
		setVisible(true);
		this.validate();
		this.repaint();
		component.transferFocusBackward();
		selectFocusButton(component);
	}
	
	protected void processWindowEvent(WindowEvent e) {
	    if (e.getID() == WindowEvent.WINDOW_DEACTIVATED) {
	       return;
	    }
	    super.processWindowEvent(e);        
	}  
	
	//---------------------------------------------------------------------------------
	private void instantiate() {
		//Static Resources
		new ImageProvider();
		new SoundsProvider();

		Timer timer = new Timer(8000, e -> showMenu());
		graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphicscEnvironment.getDefaultScreenDevice();
		transparent = false;
		timer.setRepeats(false);
		timer.start();
		setSlide(true);

		setFont();
		
		networkPanel = new NetworkPanel(WIDTH, HEIGHT, this);
		menuPanel = new MenuPanel(WIDTH, HEIGHT, this);
		menuPanel.drawScore();
		playerPanel = new PlayerPanel(WIDTH, HEIGHT, this);
		firstStagePanel = new StagePanelFirst(WIDTH, HEIGHT, this);
		secondStagePanel = new StagePanelSecond(WIDTH, HEIGHT, this);
		editorPanel = new EditorPanel(WIDTH, HEIGHT, this, flag);
		settingsPanel = new SettingsPanel(WIDTH, HEIGHT, this);
	}
	
	public void switchTo(JComponent component) {
		if(component instanceof SlideContainer) {
			this.component = menuPanel;
		} else {
			this.component = component;
		}
		this.getContentPane().removeAll();
		this.add(component);
		this.validate();
		this.repaint();
		component.transferFocus();
		selectFocusButton(component);
	}

	private void selectFocusButton(JComponent component) {
		if (component instanceof MenuPanel) {
			((MenuPanel) component).getButton(menuPanel.getCursorPosition()).requestFocus();
		} else if (component instanceof PlayerPanel) {
			((PlayerPanel) component).getButton(playerPanel.getCursorPosition()).requestFocus();
		} else if (component instanceof StagePanelFirst) {
			((StagePanelFirst) component).getButton(firstStagePanel.getCursorPosition()).requestFocus();
		} else if (component instanceof StagePanelSecond) {
			((StagePanelSecond) component).getButton(secondStagePanel.getCursorPosition()).requestFocus();
		} else if (component instanceof EditorPanel) {
			((EditorPanel) component).getButton(editorPanel.getCursorPosition()).requestFocus();
		} else if (component instanceof SettingsPanel) {
			((SettingsPanel) component).getButton(settingsPanel.getCursorPosition()).requestFocus();
		} else if (component instanceof NetworkPanel) {
			((NetworkPanel) component).getButton(networkPanel.getCursorPosition()).requestFocus();
		}
	}

	private void setFont() {
		try {
			customFontM = (Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f));
			customFontB = (Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f));
			customFontS = (Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(16f));
			graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicscEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	} 
	
	private void manageLooby() {
		lobbyPanel = new LobbyPanel(WIDTH, HEIGHT, this);
		lobbyPanel.setClient(networkPanel.getClient());
		lobbyPanel.setNameTextField(networkPanel.getNameTextField());
		lobbyPanel.setIpTextField(networkPanel.getIpTextField());
		lobbyPanel.setPortTextField(networkPanel.getPortTextField());
		//while(!lobby.getClient().isPresentInTheArrayOfClientOnline()){
		//	System.out.println("ok");
		//}
		lobbyPanel.createChat(lobbyPanel.getClient());
		lobbyPanel.createOnlinePanel();
		lobbyPanel.createDifficultPanel();
		lobbyPanel.createMapsPanel();
		
	}
	
	// -----------------------------OVERRIDE METHODS-----------------------------------
	@Override
	public void showMenu() {
		GameManager.offline = false;
		if (isSlide()) {
			slideContainer = new SlideContainer(WIDTH, HEIGHT);
			slideContainer.add(menuPanel);
			switchTo(slideContainer);
			setSlide(false);
		} else
			switchTo(menuPanel);
	}

	@Override
	public void showPlayer() {
		switchTo(playerPanel);
	}

	@Override
	public void showGame() {
		switchTo(fullGamePanel);
	}

	@Override
	public void showFirstStage(String path) {
		firstStagePanel.setPath(path);
		firstStagePanel.repaint();
		switchTo(firstStagePanel);
	}

	@Override
	public void showSecondStage(String path) {
		secondStagePanel.setPath(path);
		secondStagePanel.repaint();
		switchTo(secondStagePanel);
	}

	@Override
	public void showScores(JTextField filename) {
		scorePanel = new ScorePanel(WIDTH, HEIGHT, this, gameManager, filename);
		switchTo(scorePanel);
	}

	@Override
	public void showConstruction() {
		switchTo(editorPanel);
	}

	@Override
	public void showSlideStage(JTextField filename, boolean offline, ConnectionManager connectionManager, String difficult) {
		if(offline) {
			slideStage = new SlideStage(WIDTH, HEIGHT, this, filename);
		} else {
			slideStage = new SlideStage(WIDTH, HEIGHT, this, filename, connectionManager, difficult);
		}
		switchTo(slideStage);
	}

	@Override
	public void showSettings() {
		switchTo(settingsPanel);
	}

	@Override
	public void showNetwork() {
		networkPanel.getButton(1).setEnabled(true);
		switchTo(networkPanel);
	}
	
	@Override
	public void showLobby(boolean gamePanelExit){
		if (!gamePanelExit){
			manageLooby();
		}
		switchTo(lobbyPanel);
		lobbyPanel.revalidate();		//va messo perch� quando faccio il passaggio da un pannello ad un'altro io aggiungo dopo un'altro pannello di sopra
	}

	public GameManager showNetwork(ConnectionManager connectionManager, JTextField filename, String difficult) {
		showSlideStage(filename, false, connectionManager, difficult);
		return gameManager;
	}
	
	//---------------------------------SET & GET---------------------------------------------
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	public int getCurrentLevelP1() {
		return levelP1;
	}

	public void setCurrentLevelP1(int levelP1) {
		this.levelP1 = levelP1;
	}

	public int getCurrentLevelP2() {
		return levelP2;
	}

	public void setCurrentLevelP2(int levelP2) {
		this.levelP2 = levelP2;
	}
	
	public int getCurrentResumeP2() {
		return resumeP2;
	}

	public void setCurrentResumeP2(int resumeP2) {
		this.resumeP2 = resumeP2;
	}
	
	public int getCurrentResumeP1() {
		return resumeP1;
	}

	public void setCurrentResumeP1(int resumeP1) {
		this.resumeP1 = resumeP1;
	}
	
	public int getUnlockedMapsP2() {
		return unlockedMapsP2;
	}

	public void setUnlockedMapsP2(int unlockedMapsP2) {
		this.unlockedMapsP2 = unlockedMapsP2;
	}

	public int getUnlockedMapsP1() {
		return unlockedMapsP1;
	}
	
	public void setUnlockedMapsP1(int unlockedMapsP1) {
		this.unlockedMapsP1 = unlockedMapsP1;
	}
	
	public int getHighScoreP2() {
		return highScoreP2;
	}

	public void setHighScoreP2(int highScoreP2) {
		this.highScoreP2 = highScoreP2;
	}

	public int getHighScoreP1() {
		return highScoreP1;
	}

	public void setHighScoreP1(int highScoreP1) {
		this.highScoreP1 = highScoreP1;
	}
	
	public boolean isSlide() {
		return slide;
	}

	public void setSlide(boolean slide) {
		this.slide = slide;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public FullGamePanel getFullGamePanel() {
		return fullGamePanel;
	}

	public void setFullGamePanel(FullGamePanel fullGamePanel) {
		this.fullGamePanel = fullGamePanel;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}
}
