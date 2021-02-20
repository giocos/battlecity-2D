package it.unical.progetto.igpe.net;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

	private boolean flag;

	private Server server;
	private Socket socket;
	private String nameSocket;

	public ServerThread(Server server, Socket socket) {
		this.flag = false;
		this.server = server;
		this.socket = socket;
		this.start();
	}

	public void run() {
		try {
			DataInputStream din = new DataInputStream(socket.getInputStream());

			while (true) {
				String message = din.readUTF();
//				System.out.println(".... "+message);
				String[] elements = message.split(" ");
				if (elements[0].equals("EXITALL")) {
					server.sendToAll("EXITALL");
					server.removeConnection(socket);
					server.setExitChat(true);
					server.closeServer();
					break;
				} else if (elements[0].equals("EXIT")) {
					String client = elements[1];
					server.removeConnection(client);
					String[] clients = server.getOnlineNames().split(" ");
					server.setOnlineNames("");
					for (String s : clients) {
						if (!s.equals(client)) {
							server.setOnlineNames(server.getOnlineNames() +s);
							server.setOnlineNames(server.getOnlineNames() + " ");
						}
					}
					server.sendToAll("EXIT " + client);
				} else {
					boolean name = true;
					int len = message.length();

					for (int i = 0; i < 6; i++) {
						if (!(message.charAt(len - i - 1) == '^')) {
							name = false;
							break;
						}
					}

					if (name) {
						nameSocket = searchName(message);
						int cont = 0;

						String[] clients = server.getOnlineNames().split(" ");
						for (String s : clients) {
							if (s.equals(nameSocket)) {
								cont++;
							}
						}

						if (cont < 1) {
							server.setOnlineNames(server.getOnlineNames() + nameSocket + " " );
							server.sendToAll(message);
							server.sendToSocket(socket, "OKNAME");
						} else {
							server.sendToSocket(socket, "ERRORNAME");
							server.removeConnection(socket);
							flag = true;
							break;
						}
					}
					if (!flag){
						server.getClient().put(socket, nameSocket);
						if(!name)
							server.sendToAll(message);
					}
				}
			}
		} catch (EOFException e) {
			System.err.println(e.getMessage());

		} catch (IOException e) {
			System.out.println("SOCKET CLOSE");
		} finally {
			if (socket.isConnected()) {
				server.removeConnection(socket);
			}
		}
	}

	private String searchName(String message) {
		String name1 = "";
		int i = 0;
		while (!(message.charAt(i) == ':' && message.charAt(i + 1) == ':')) {
			name1 = name1 + message.charAt(i);
			i++;
		}
		return name1;
	}
}
