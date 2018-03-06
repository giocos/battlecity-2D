package unical.progetto.igpe.gui;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFileChooserClass {

	private JFileChooser chooser;
	private JTextField filename;
	private JTextField dir;
	private File file;
	private static boolean online;
	private File[] filesInDirectory;
	private FileNameExtensionFilter filter;
	private MainFrame m;
	private JOptionPane tmp;
	
	@SuppressWarnings("static-access")
	public JFileChooserClass(MainFrame m, boolean online) {
		this.m=m;
		this.chooser = new JFileChooser();
		this.filename = new JTextField();
		this.dir = new JTextField();
		this.setOnline(online);
		if (!online) {
			this.file = new File("./maps/editor");
		} else {
			this.file = new File("./maps/career/multiplayer");
		}
		this.filter = new FileNameExtensionFilter(".txt", "txt");
		this.chooser.setFileFilter(filter);
		setFileChooserFont(chooser.getComponents());
		disableButton(chooser, "FileChooser.homeFolderIcon");
		disableButton(chooser, "FileChooser.upFolderIcon");
		disableButton(chooser, "FileChooser.newFolderIcon");
	}

	@SuppressWarnings("static-access")
	public boolean functionSaveFile(int p1, int p2) {
		if (p1 == 1 && p2 == 1) {
			this.file = new File("./maps/editor/multiplayer");
		} else {
			this.file = new File("./maps/editor/singleplayer");
		}
		chooser.setCurrentDirectory(file);
		int value = chooser.showSaveDialog(m);
		if (value == JFileChooser.APPROVE_OPTION) {
			dir.setText(chooser.getCurrentDirectory().toString());
			filename.setText(chooser.getSelectedFile().getName());
			filename.setText(searchExtensionTxt(filename));

			filesInDirectory = chooser.getCurrentDirectory().listFiles();

			if (file.exists() && existFile(filesInDirectory, filename)) {
				int result =tmp.showConfirmDialog(m, "The file name exists. Please change name",
						"New File Name",JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.CANCEL_OPTION) {
					chooser.cancelSelection();
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean functionLoadFile() {
		chooser.setCurrentDirectory(file);
		int value = chooser.showOpenDialog(m);
		file = chooser.getCurrentDirectory();
		filesInDirectory = chooser.getCurrentDirectory().listFiles();
		if (value == JFileChooser.APPROVE_OPTION) {
			dir.setText(chooser.getCurrentDirectory().toString());
			filename.setText(chooser.getSelectedFile().getName());
			filename.setText(searchExtensionTxt(filename));

			if (!existFile(filesInDirectory, filename)) {
				int result = JOptionPane.showConfirmDialog(m, "IL FILE NON ESISTE", "New File Name",
						JOptionPane.CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	private static void disableButton(final Container c, final String iconString) {
		int len = c.getComponentCount();
		for (int i = 0; i < len; i++) {
			Component comp = c.getComponent(i);
			if (comp instanceof JButton) {
				JButton b = (JButton) comp;
				Icon icon = b.getIcon();
				if (icon != null && icon == UIManager.getIcon(iconString)) {
					b.setEnabled(false);
				}
			} else if (comp instanceof Container) {
				disableButton((Container) comp, iconString);
			}
		}
	}

	private static void setFileChooserFont(Component[] comp) {
		for (int x = 0; x < comp.length; x++) {
			if (comp[x] instanceof Container)
				setFileChooserFont(((Container) comp[x]).getComponents());
			try {
				comp[x].setFont(MainFrame.customFontS);
			} catch (Exception e) {
			}
		}
	}

	private String searchExtensionTxt(JTextField filename) {
		String string = filename.getText();
		int point = string.length();
		String tmp = string;
		while (tmp.contains(".txt")) {
			point = tmp.lastIndexOf(".");
			tmp = string.substring(0, point);
		}
		return string.substring(0, point);
	}

	private boolean existFile(File[] filesInDirectory, JTextField filename) {
		for (File file : filesInDirectory) {
			if (file.getName().equals(filename.getText() + ".txt")) {
				return true;
			}
		}
		return false;
	}

	public JTextField getFilename() {
		return filename;
	}

	public JTextField getDir() {
		return dir;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public static boolean isOnline() {
		return online;
	}

	public static void setOnline(boolean online) {
		JFileChooserClass.online = online;
	}

}
