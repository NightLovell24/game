package com.archers.controller.net.client;

import java.util.List;

import com.archers.controller.net.DataReceiver;
import com.archers.model.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientDataReceiver implements DataReceiver {
	private Client client;

	public ClientDataReceiver(Client client) {
		this.client = client;
	}

	@Override
	public void receive(String data, String ip) {
		 ObjectMapper mapper = new ObjectMapper();
	        try {
	            List<PlayerData> playerData = mapper.readValue(data, new TypeReference<List<PlayerData>>() {});
	            client.drawPlayers(playerData);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
		
	}
}
