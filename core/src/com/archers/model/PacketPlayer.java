package com.archers.model;

import com.archers.controller.net.client.PacketType;

public class PacketPlayer {

	public PacketPlayer(PlayerData data, PacketType type) {
		this.data = data;
		this.type = type;
	}
	public PacketPlayer()
	{
		
	}

	public PlayerData getData() {
		return data;
	}

	public PacketType getType() {
		return type;
	}

	private PlayerData data;
	private PacketType type;

}
