package com.archers.controller.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.archers.controller.net.Connection;
import com.archers.controller.net.TCPConnection;
import com.archers.model.PlayerData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {
	private static final int PORT = 24120;
	private static final int MAX_PLAYERS = 8;
	private ServerSocket serverSocket;

	private Map<String, PlayerData> ipToPlayerData;
	private List<TCPConnection> connections;

	public static void main(String[] args) {
		new Server();
	}

	public Server() {

		connections = new ArrayList<>();
		ipToPlayerData = new HashMap<>();
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server is successfully started.");
			acceptConnections();
		} catch (IOException e) {
			System.out.println("An error occurred while starting server.");
			e.printStackTrace();
		}

	}

	private void acceptConnections() {
		while (true) {

			try {
				if (connections.size() < MAX_PLAYERS) {

					Socket client = serverSocket.accept();
					ServerDataReceiver dispatcher = new ServerDataReceiver(this);
					TCPConnection connection = new TCPConnection(client, dispatcher);
					connections.add(connection);
					System.out.println("New client connected : " + client.getInetAddress().toString());
				} else {
					// some logica
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void updatePlayerData(PlayerData data, String ip) {
//		if (isValid(data, ip)) {
			ipToPlayerData.put(ip, data);
//		}
		sendDataToClients();
	}

	public void sendDataToClients() {
		cleanConnections();

		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(ipToPlayerData.values());
		} catch (JsonProcessingException e) {
			System.out.println("Cannot send JSON to clients.");
			e.printStackTrace();
		}

		if (json != null) {
			for (Connection connection : connections) {
				connection.dispatchData(json);
			}
		}
	}

	private void cleanConnections() {
		connections.stream().filter(c -> !c.isConnected()).forEach(c -> {
			ipToPlayerData.remove(c.getIpAdress());
			System.out.println(c.getIpAdress() + " left our server.");
			connections.remove(c);
		});
	}

//	private boolean isValid(PlayerData data, String ip) {
//		PlayerData oldData = ipToPlayerData.get(ip);
//		if (oldData != null) {
//
//		} else {
//			if (ipToPlayerData.values().stream().map(PlayerData::getNickname)
//					.anyMatch(name -> name.equals(data.getNickname()))) {
//				return false;
//			}
//		}
//
//		return true;
//	}

}
