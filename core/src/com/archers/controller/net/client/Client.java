package com.archers.controller.net.client;

import com.archers.model.PlayerData;

public class Client {

	private PlayerData data;

	private String ip;
	private int port;
	private boolean connectionRefreshed;

	public Client(PlayerData data, String ip, int port) {

		this.data = data;
		this.ip = ip;
		this.port = port;
		connectionRefreshed = true;
	}

	public PlayerData getData() {
		return data;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public boolean isConnectionRefreshed() {
		return connectionRefreshed;
	}

	public void setConnectionRefreshed(boolean connectionRefreshed) {
		this.connectionRefreshed = connectionRefreshed;
	}

}
