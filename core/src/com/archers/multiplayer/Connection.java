package com.archers.multiplayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection extends Thread {
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private String ipAddress;
    private MessageDispatcher dispatcher;

    public Connection(Socket socket, MessageDispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
        try {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream());
            ipAddress = socket.getInetAddress().toString();
        } catch (Exception e) {
            System.out.println("An error occurred while creating connection.");
            e.printStackTrace();
            close();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (isConnected()) {
            String message = accept();
            if (message != null) {
                dispatcher.dispatch(message, ipAddress);
            }
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void send(String message) {
        output.println(message);
        output.flush();
    }

    public String accept() {
        String message = null;
        if (input.hasNextLine()) {
            message = input.nextLine();
        }
        return message;
    }

    public boolean isConnected() {
        return !socket.isClosed() && socket.isConnected();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("An error occurred while closing connection.");
            e.printStackTrace();
        }
    }
}
