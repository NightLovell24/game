package com.archers.controller.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.archers.controller.net.client.Client;
import com.archers.controller.net.client.PacketType;
import com.archers.model.PacketPlayer;

import com.archers.model.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {

	private DatagramSocket socket;
	private static final int port = 24120;
	private MapChecker mapChecker;

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

			serverMessage("Server is started!");
			listenPackets();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void serverMessage(String msg) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.println("["+format.format(date) + "]" + " " + msg);
	}
	
	private void listenPackets() throws IOException {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			processPacket(received, packet.getAddress().getHostAddress(), packet.getPort());
		}
	}

	private void processPacket(String message, String ip, int port) throws JsonProcessingException {
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
		}
	}

	private void refreshPlayer(String nickname) {
		Client client = players.get(nickname);
		client.setConnectionRefreshed(true);
	}

	private void joinPlayer(PacketPlayer packetPlayer, String ip, int port) {
		if (players.get(packetPlayer.getData().getNickname()) == null) {
			players.put(packetPlayer.getData().getNickname(), new Client(packetPlayer.getData(), ip, port));
			serverMessage(packetPlayer.getData().getNickname() + " " + ip + ":" + port + " joined the server!");
			new ClientListener(packetPlayer.getData().getNickname());
			dispatchMessageAll(packetPlayer);
		}
	}

	private void leavePlayer(PacketPlayer packetPlayer) {
		String nickname = packetPlayer.getData().getNickname();
		if (players.get(nickname) != null) {
			players.remove(nickname);
			serverMessage(nickname + " left the server!");
			dispatchMessageAll(packetPlayer);
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
				client.setConnectionRefreshed(false);
				try {
					Thread.sleep(TIMEOUT * 1000);
				} catch (InterruptedException e) {
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
		if (isValid(packetPlayer)) {
			PlayerData data = packetPlayer.getData();
			players.get(data.getNickname()).setData(data);
			refreshPlayer(data.getNickname());
			dispatchMessageAll(packetPlayer);
		} else {
			if (packetPlayer != null) {
				PlayerData data = packetPlayer.getData();
				if (data != null) {
					Client client = players.get(data.getNickname());
					if (client != null) {
						refreshPlayer(data.getNickname());
						dispatchMessageAll(new PacketPlayer(client.getData(), PacketType.MOVE));
					}
				}
			}
		}


	}

	private static final double SPEED_LIMIT = 0.055;

	private boolean isValid(PacketPlayer packetPlayer) {
		if (packetPlayer == null) return false;

		if (packetPlayer.getType() == PacketType.MOVE) {
			PlayerData newData = packetPlayer.getData();
			if (newData == null) return false;

			Client client =  players.get(newData.getNickname());
			if (client == null) return false;

			PlayerData oldData = client.getData();

			double distance = Math.
					sqrt(Math.pow(oldData.getX() - newData.getX(), 2) + Math.pow(oldData.getY() - newData.getY(), 2));
			long time = newData.getDate().getTime() - oldData.getDate().getTime();

			if (distance / time > SPEED_LIMIT) return false;

			if (mapChecker != null) {
				if (!mapChecker.isInsideWorld(newData.getX(), newData.getY())) return false;
				if (mapChecker.isInsideObstacle(newData.getX(), newData.getY())) return false;
				if (mapChecker.isInsideShelter(newData.getX(), newData.getY())) {
					newData.setCurrentState(PlayerData.State.HIDING);
				}
			}
		} else if (packetPlayer.getType() == PacketType.JOIN) {

		}

		return true;
	}

	private void dispatchMessageAll(PacketPlayer packetPlayer) {
		byte[] buf = null;
		try {
			String message = mapper.writeValueAsString(packetPlayer);
			buf = message.getBytes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (buf != null) {
			for (Client client : players.values()) {
				try {
					InetAddress address = InetAddress.getByName(client.getIp());
					int port = client.getPort();

					DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
					socket.send(packet);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
