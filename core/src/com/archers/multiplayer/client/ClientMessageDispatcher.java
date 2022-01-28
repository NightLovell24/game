package com.archers.multiplayer.client;

import com.archers.multiplayer.MessageDispatcher;
import com.archers.multiplayer.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageDispatcher implements MessageDispatcher {
    private final Client client;

    public ClientMessageDispatcher(Client client) {
        this.client = client;
    }

    @Override
    public void dispatch(String message, String ip) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<PlayerData> playerData = mapper.readValue(message, new TypeReference<List<PlayerData>>() {});
            client.drawPlayers(playerData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
