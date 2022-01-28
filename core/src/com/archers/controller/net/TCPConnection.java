package com.archers.controller.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPConnection extends Thread implements Connection {
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	private DataReceiver dispatcher;
	private String ipAdress;

	public String getIpAdress() {
		return ipAdress;
	}

	public TCPConnection(Socket socket, DataReceiver dispatcher) {
		ipAdress = socket.getInetAddress().toString();
		this.socket = socket;
		this.dispatcher = dispatcher;
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			close();
			e.printStackTrace();
		}

		this.start();
	}

	@Override
	public void run() {

		while (socket.isConnected()) {
			String data = receiveData();
			if (data != null) {
				dispatcher.receive(data, ipAdress);
			}
		}
	}

	@Override
	public String receiveData() {
		String data = null;
		try {
			data = input.readLine();
		} catch (IOException e) {
			close();
			e.printStackTrace();
		}
		return data;

	}

	@Override
	public void dispatchData(String data) {

		try {
			output.write(data + "\r\n");
			output.flush();
		} catch (IOException e) {
			close();
			e.printStackTrace();
		}

	}

	public boolean isConnected() {
		return !socket.isClosed() && socket.isConnected();
	}

	public void close() {
		try {
			this.stop();
			socket.close();
			input.close();
			output.close();
		} catch (IOException e) {
			System.out.println("An error occurred while closing connection.");
			e.printStackTrace();
		}
	}

}
