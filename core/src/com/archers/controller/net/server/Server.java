package com.archers.controller.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.archers.controller.net.client.Client;
import com.archers.controller.net.client.PacketType;
import com.archers.model.PacketPlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {

	private DatagramSocket socket;
	private static final int port = 24120;
	private static final int COUNT_OF_COPIES = 3;
	private static final int TIMEOUT = 5;
	private Map<String, Client> players;

	private byte[] buffer = new byte[1024];
	private ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		try {
			players = new ConcurrentHashMap<>();

			socket = new DatagramSocket(port);
			System.out.println("Server is started!");
			listenPackets();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listenPackets() throws IOException {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {

			socket.receive(packet);

			String received = new String(packet.getData(), 0, packet.getLength());
//			System.out.println(received);
			processPacket(received, packet.getAddress().getHostAddress(), packet.getPort());
		}
	}

	private void processPacket(String message, String ip, int port)
			throws JsonMappingException, JsonProcessingException {
		PacketPlayer packetPlayer = mapper.readValue(message, PacketPlayer.class);
		switch (packetPlayer.getType()) {
		case JOIN:
			joinPlayer(packetPlayer, ip, port);
			break;

		case LEAVE: // IF PLAYER PRESSED THE DISCONNECT BUTTON (WILL BE IMPLEMENTED IN THE FUTURE)
			leavePlayer(packetPlayer);
			break;

		case MOVE:

			movePlayer(packetPlayer);
			break;
		case CHECK:

			checkPlayer(packetPlayer.getData().getNickname());
			break;

		}

	}

	private void checkPlayer(String nickname) {
		Client client = players.get(nickname);
		client.setConnectionRefreshed(true);
	}

	private void joinPlayer(PacketPlayer packetPlayer, String ip, int port) {
		if (players.get(packetPlayer.getData().getNickname()) == null) {
			players.put(packetPlayer.getData().getNickname(), new Client(packetPlayer.getData(), ip, port));
			System.out.println(packetPlayer.getData().getNickname() + " " + ip + ":" + port + " joined to the server!");

			new ClientListener(packetPlayer.getData().getNickname());
			dispatchMessage(packetPlayer);
		}
	}

	private void leavePlayer(PacketPlayer packetPlayer) {
		String nickname = packetPlayer.getData().getNickname();
		if (players.get(nickname) != null) {
			players.remove(nickname);

			System.out.println(nickname + " leaved from the server!");
			dispatchMessage(packetPlayer);
		}
	}

	class ClientListener extends Thread {

		private String nickname;

		public ClientListener(String nickname) {
			this.nickname = nickname;
			this.start();
		}

		@Override
		public void run() {

			while (players.get(nickname).isConnectionRefreshed()) {
				Client client = players.get(nickname);

				PacketPlayer packetPlayer = new PacketPlayer(client.getData(), PacketType.CHECK);
				for (int i = 0; i < COUNT_OF_COPIES; i++) {
					dispatchMessage(packetPlayer);
//					System.out.println("Dispatched..");
				}
				client.setConnectionRefreshed(false);
				try {
					Thread.sleep(TIMEOUT * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!client.isConnectionRefreshed()) {

					leavePlayer(new PacketPlayer(client.getData(), PacketType.LEAVE));
					return;
				}

			}

		}

	}

	private void movePlayer(PacketPlayer packetPlayer) {
// validation
		dispatchMessage(packetPlayer);

	}

	private void dispatchMessage(PacketPlayer packetPlayer) {
		for (Client client : players.values()) {
			if (packetPlayer.getType() == PacketType.JOIN) {
				if (packetPlayer.getData().getNickname().equals(client.getData().getNickname())) {
					continue;
				}
			}
			try {
				String message = mapper.writeValueAsString(packetPlayer);
				InetAddress adress = InetAddress.getByName(client.getIp());
				int port = client.getPort();
				
				byte[] buf = message.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, adress, port);
				socket.send(packet);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
