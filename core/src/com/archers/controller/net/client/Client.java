package com.archers.controller.net.client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.archers.controller.net.Connection;
import com.archers.controller.net.DataReceiver;
import com.archers.controller.net.TCPConnection;
import com.archers.model.PlayerData;
import com.archers.view.screen.netmap.PlayScreen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

	private Connection connection;
	private String nickname;
	private static final int port = 24120;

	private PlayScreen screen;

	public Client(String ip, String nickname, PlayScreen screen) throws IOException {

		this.screen = screen;

		Socket socket = new Socket(ip, port);
		DataReceiver receiver = new ClientDataReceiver(this);
		connection = new TCPConnection(socket, receiver);
		PlayerData data = new PlayerData();
		data.setNickname(nickname);
		data.setX(0);
		data.setY(0);
		sendPlayerDataToServer(data);

	}

	public Client(String ip, String nickname) throws IOException {

		Socket socket = new Socket(ip, port);
		DataReceiver receiver = new ClientDataReceiver(this);
		connection = new TCPConnection(socket, receiver);
		PlayerData data = new PlayerData();
		data.setNickname(nickname);
		data.setX(0);
		data.setY(0);
		sendPlayerDataToServer(data);

	}
   public void setScreen(PlayScreen screen)
   {
	   this.screen = screen;
   }
	public String getNickname() {
		return nickname;
	}

	public void drawPlayers(List<PlayerData> data) {
		screen.updateRemotePlayers(data);
	}

	public void sendPlayerDataToServer(PlayerData data) {
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			System.out.println("Cannot send JSON to server.");
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		if (json != null) {
			connection.dispatchData(json);
		}
	}

}
