package unical.progetto.igpe.net;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JTextField;

public class ClientManager implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter printer;
	private String name;
	private JTextField map;
	private String difficult;
	private ServerGameManager server;
	private Socket socket;
	
	public ClientManager(Socket socket, ServerGameManager server) {
		this.socket = socket;
		this.server = server;
		this.map=new JTextField();
		try {
			socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(final String message) {
		if (printer != null && message != null) {
			printer.println(message);
			printer.flush();
		}
	}

	@Override
	public void run() {
		try {
			server.setReady(this);
			String string = reader.readLine();
			while (string!=null) {
//				string+=":"+System.currentTimeMillis();
				server.received(string);
			string = reader.readLine();
			}
			socket.close();
		} catch (final IOException e) {
			server.disconnetctedClient(name);
		}
	}
	
	String setup(String string) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printer = new PrintWriter(socket.getOutputStream(), true);
		String s = reader.readLine();
		String[] split = s.split(":");
		name=string;
		server.getName().put(split[0], name);
		if(split.length>1){
			System.out.println("clientmanager "+split[1]+" "+split[2]);
			map.setText(split[1]);
			difficult=split[2];
		}	
		server.dispatch(server.getConnectedClientNames());
		return split[0];
	}
	
	public String getName() {
		return name;
	}
	
	public JTextField getMap() {
		return map;
	}

	public void setMap(JTextField map) {
		this.map = map;
	}

	public String getDifficult() {
		return difficult;
	}

	public void setDifficult(String difficult) {
		this.difficult = difficult;
	}
}
