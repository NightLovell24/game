package com.archers.multiplayer.server;

import com.archers.multiplayer.Connection;
import com.archers.multiplayer.MessageDispatcher;
import com.archers.multiplayer.PlayerData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 7777;
    private ServerSocket socket;
    private List<Connection> connections;
    private Map<String, PlayerData> ipToPlayerData;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            socket = new ServerSocket(PORT);
            connections = new ArrayList<>();
            ipToPlayerData = new HashMap<>();
            System.out.println("Server is successfully started.");
            acceptConnections();
        } catch (Exception e) {
            System.out.println("An error occurred while starting server.");
            e.printStackTrace();
        }
    }

    public void acceptConnections() {
        while (true) {
            try {
                Socket newClient = socket.accept();
                MessageDispatcher dispatcher = new ServerMessageDispatcher(this);
                Connection newConnection = new Connection(newClient, dispatcher);
                connections.add(newConnection);
                System.out.println(newClient.getInetAddress() + " has been connected.");
            } catch (Exception e) {
                System.out.println("An error occurred while connecting a new client.");
                e.printStackTrace();
            }
        }
    }

    public void updatePlayerData(PlayerData data, String ip) {
        if (isValid(data, ip)) {
            ipToPlayerData.put(ip, data);
        } else {
            throw new IllegalArgumentException();
        }
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
                connection.send(json);
            }
        }
    }

    private void cleanConnections() {
        connections.stream().filter(c -> !c.isConnected()).
                forEach(c -> {
                    ipToPlayerData.remove(c.getIpAddress());
                    System.out.println(c.getIpAddress() + " left our server.");
                    connections.remove(c);
                });
    }

    private boolean isValid(PlayerData data, String ip) {
        PlayerData oldData = ipToPlayerData.get(ip);
        if (oldData != null) {

        } else {
            if (ipToPlayerData.values().stream().
                    map(PlayerData::getUsername).anyMatch(name -> name.equals(data.getUsername()))) {
                return false;
            }
        }

        return true;
    }
}
