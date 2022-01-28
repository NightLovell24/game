package com.archers.multiplayer.client;

import com.archers.multiplayer.Connection;
import com.archers.multiplayer.MessageDispatcher;
import com.archers.multiplayer.PlayerData;
import com.archers.screens.PlayScreen;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Client {
    private static final int PORT = 7777;
    private final String nickname;
    private final PlayScreen screen;
    private final Connection serverConnection;

    public Client(String ip, String nickname, PlayScreen screen) throws IOException {
        Socket socket = new Socket(ip, PORT);
        this.nickname = nickname;
        this.screen = screen;
        MessageDispatcher dispatcher = new ClientMessageDispatcher(this);
        serverConnection = new Connection(socket, dispatcher);
        PlayerData data = new PlayerData();
        data.setUsername(nickname);
        data.setX(0);
        data.setY(0);
        sendPlayerDataToServer(data);
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
            serverConnection.send(json);
        }
    }
}
