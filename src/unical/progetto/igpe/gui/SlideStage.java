package unical.progetto.igpe.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import unical.progetto.igpe.core.GameManager;
import unical.progetto.igpe.net.ConnectionManager;

@SuppressWarnings("serial")
public class SlideStage extends JLayeredPane {

	private final int DELAY = 5;
	private final int DELTA_Y = 2;
	private Component oldComponent;
	private boolean flag;
	private JLabel label;
	private PanelSwitcher switcher;
	private JTextField filename;
	private ArrayList<JPanel> panels = new ArrayList<>();

	// ONLINE
	public SlideStage(int w, int h, PanelSwitcher switcher, JTextField filename, ConnectionManager connectionManager,
			String difficult) {
		init(w, h, filename, switcher);

		instantiateOnline(difficult, connectionManager);
		
		add(panels.get(0), panels.get(1));
	}

	// OFFLINE
	public SlideStage(final int w, final int h, PanelSwitcher switcher, JTextField filename) {
		init(w, h, filename, switcher);

		instantiateOffline();
		
		add(panels.get(0), panels.get(1));

	}

	private void init(int w, int h, JTextField filename, PanelSwitcher switcher) {
		SoundsProvider.playStageStart();
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(w, h));
		this.setLayout(null);
		this.setOpaque(true);
		this.setSwitcher(switcher);
		flag = false;
		this.filename = filename;

		setLabel();

		for (int i = 0; i < 2; i++) {

			panels.add(new JPanel());
			panels.get(i).setPreferredSize(new Dimension(w, h));
			panels.get(i).setBackground(Color.GRAY);
		}
	}

	private void setLabel() {

		label = new JLabel();
		label.setFont(MainFrame.customFontB);

		if(filename.getText().contains("career"))
			label.setText("Stage " + filename.getText().
					subSequence(filename.getText().indexOf("stage") + 5, filename.getText().length() - 4));
		else
			label.setText("Custom Map");

		label.setBounds((int) (getPreferredSize().getWidth() / 2 - 50), (int) getPreferredSize().getHeight() / 2 - 50,
				300, 100);
		label.setBackground(Color.GRAY);
		label.setForeground(Color.BLACK);
		this.add(label);
	}

	private void instantiateOnline(String difficult, ConnectionManager connectionManager) {
		((MainFrame) switcher).setGamePanel(new GamePanel(switcher, difficult));
		((MainFrame) switcher)
				.setGameManager(((MainFrame) switcher).getGamePanel().startNetwork(connectionManager, filename));
		((MainFrame) switcher).getGamePanel().setGame(((MainFrame) switcher).getGameManager());
		((MainFrame) switcher).setFullGame(new FullGamePanel(WIDTH, HEIGHT, ((MainFrame) switcher).getGameWidth(),
				((MainFrame) switcher).getGameHeight(), getSwitcher(), ((MainFrame) switcher).getGamePanel()));
	}

	// BISOGNA FARLI DURANTE IL CARICAMENTO
	public void instantiateOffline() {

		((MainFrame) switcher).setGameManager(new GameManager(filename));
		((MainFrame) switcher).setFlag(((MainFrame) switcher).getGameManager().getFlag());
//		if (SettingsPanel.normal)
//			((MainFrame) switcher).getGameManager().setMedium(true);

		((MainFrame) switcher).setGamePanel(
				new GamePanel(((MainFrame) switcher).getGameWidth(), ((MainFrame) switcher).getGameHeight(),
						((MainFrame) switcher), ((MainFrame) switcher).getGameManager()));
		((MainFrame) switcher).setFullGame(new FullGamePanel(WIDTH, HEIGHT, ((MainFrame) switcher).getGameWidth(),
				((MainFrame) switcher).getGameHeight(), ((MainFrame) switcher), ((MainFrame) switcher).getGamePanel()));
		((MainFrame) switcher).getGamePanel().setFullGamePanel(((MainFrame) switcher).getFullGame());
	}

	public void add(Component component1, Component component2) {

		this.add(component1);
		this.add(component2);

		SlideStage.putLayer((JComponent) component1, JLayeredPane.DRAG_LAYER);
		SlideStage.putLayer((JComponent) component2, JLayeredPane.DRAG_LAYER);

		component1.setSize(getPreferredSize());
		component1.setLocation(0, -getPreferredSize().height);
		component2.setSize(getPreferredSize());
		component2.setLocation(0, getPreferredSize().height);

		slideTopAndBottom(component1, component2);
	}

	public void slideTopAndBottom(final Component component1, final Component component2) {

		final int tmp = -(getPreferredSize().height / 2);

		new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int y1 = component1.getY();
				int y2 = component2.getY();

				if (y1 >= tmp) {

					component1.setLocation(0, y1);
					component2.setLocation(0, y2);
					putLayer((JComponent) component1, JLayeredPane.DEFAULT_LAYER);
					putLayer((JComponent) component2, JLayeredPane.DEFAULT_LAYER);

					if (oldComponent != null)
						remove(oldComponent);

					((Timer) e.getSource()).stop();

					try {
						Thread.sleep(500);
						getSwitcher().showGame();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				} else {

					y1 += DELTA_Y;
					y2 -= DELTA_Y;
					component1.setLocation(0, y1);
					component2.setLocation(0, y2);
				}

				repaint();
			}
		}).start();
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}