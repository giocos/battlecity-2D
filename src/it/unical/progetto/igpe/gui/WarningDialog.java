package it.unical.progetto.igpe.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class WarningDialog extends JDialog {
	
	private JPanel panel;
	private JLabel label;
	private MainFrame mainframe;
	
	public WarningDialog(String text, TypeMatrix[][] matrix, MainFrame mainframe) {
		super(mainframe);
		this.mainframe = mainframe;
		init(text);

		Timer timer = new Timer(3000, e -> {
			dispose();
			matrix[0][0]=matrix[0][(matrix[0].length)/2]=matrix[0][(matrix[0].length)-1]=TypeMatrix.EMPTY;
			getMainframe().setTransparent(false);
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
	}
	
	public WarningDialog(String text, MainFrame mainframe){
		this.mainframe = mainframe;
		init(text);
		Timer timer = new Timer(3000, e -> {
			dispose();
			getMainframe().setTransparent(false);
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
	}
	
	private void init(String text){
		
		panel = new JPanel();
		label = new JLabel(text);
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createLineBorder(Color.RED));
		label.setForeground(Color.WHITE);
		label.setFont(MainFrame.customFontM);
		panel.add(label);
		
		add(panel);
		setModal(true);
		setUndecorated(true);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(mainframe);
	}
	
	public MainFrame getMainframe() {
		return mainframe;
	}

	public void setMainframe(MainFrame mainframe) {
		this.mainframe = mainframe;
	}
}
