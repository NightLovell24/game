package com.archers.controller.net.server;

import com.archers.controller.net.DataReceiver;
import com.archers.model.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerDataReceiver implements DataReceiver {

	private Server server;

	public ServerDataReceiver(Server server) {
		this.server = server;
	}

	@Override
	public void receive(String data, String ip) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			PlayerData playerData = mapper.readValue(data, PlayerData.class);
			server.updatePlayerData(playerData, ip);
		} catch (JsonProcessingException e) {
			System.out.println("Incorrect JSON from client.");
			e.printStackTrace();
		}
	}

}
