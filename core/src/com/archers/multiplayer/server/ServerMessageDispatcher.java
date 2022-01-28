package com.archers.multiplayer.server;

import com.archers.multiplayer.MessageDispatcher;
import com.archers.multiplayer.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerMessageDispatcher implements MessageDispatcher {
    private final Server server;

    public ServerMessageDispatcher(Server server) {
        this.server = server;
    }

    @Override
    public void dispatch(String message, String ip) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            PlayerData data = mapper.readValue(message, PlayerData.class);
            server.updatePlayerData(data, ip);
        } catch (JsonProcessingException e) {
            System.out.println("Incorrect JSON from client.");
            e.printStackTrace();
        }
    }
}
