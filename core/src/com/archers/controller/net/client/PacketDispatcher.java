package com.archers.controller.net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

import com.archers.model.PacketPlayer;
import com.archers.model.PlayerData;
import com.archers.view.screen.netmap.PlayScreen;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PacketDispatcher {
	private String ip;
	private int port;
	private ObjectMapper mapper;
	private static final int COUNT_OF_COPIES = 3;

	private static final int TIMEOUT = 5;
	private byte[] buffer = new byte[1024];
	private DatagramSocket socket;

	public PacketDispatcher(String ip, int port) {
		this.ip = ip;
		this.port = port;
		mapper = new ObjectMapper();
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean join(PlayerData playerData) {
		try {
			InetAddress address = InetAddress.getByName(ip);

			playerData.setDate(new Date());
			PacketPlayer packetPlayer = new PacketPlayer(playerData, PacketType.JOIN);
			String message = mapper.writeValueAsString(packetPlayer);
			byte[] buf = message.getBytes();
			DatagramPacket dataPacket = new DatagramPacket(buf, buf.length, address, port);
			for (int i = 0; i < COUNT_OF_COPIES; i++) {
				socket.send(dataPacket);
			}
			dataPacket = new DatagramPacket(buf, buf.length);
			socket.setSoTimeout(TIMEOUT * 1000);
			try {
				socket.receive(dataPacket);
				return true;
			} catch (SocketTimeoutException e) {
				return false;
			}

		} catch (UnknownHostException e) {
			return false;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void processPacket(PlayScreen screen) throws IOException {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {
			socket.receive(packet);

			String received = new String(packet.getData(), 0, packet.getLength());
//			System.out.println(received);
			PacketPlayer packetPlayer = mapper.readValue(received, PacketPlayer.class);
			switch (packetPlayer.getType()) {
			case MOVE:
				movePlayer(packetPlayer, screen);
				break;
			case JOIN:
				joinPlayer(packetPlayer, screen);
				break;
			case LEAVE:
				leavePlayer(packetPlayer, screen);
				break;
			}
		}
	}

	private void movePlayer(PacketPlayer packetPlayer, PlayScreen screen) {
		screen.updatePlayer(packetPlayer.getData());
	}

	private void leavePlayer(PacketPlayer packetPlayer, PlayScreen screen) {
		screen.removePlayer(packetPlayer.getData().getNickname());
	}

	private void joinPlayer(PacketPlayer packetPlayer, PlayScreen screen) {
		screen.joinPlayer(packetPlayer.getData());
	}

	public void dispatchMessage(PacketPlayer packetPlayer) {
		try {
			String message = mapper.writeValueAsString(packetPlayer);
			InetAddress address = InetAddress.getByName(ip);

			byte[] buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

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
