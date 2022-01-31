package com.archers.view.entities;

import com.archers.model.PlayerData;
import com.archers.view.characters.Character;
import com.archers.view.inputadapter.PlayerInputAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class LocalPlayer extends Sprite {

	private PlayerData data;

	
	private Vector2 centerLocation;
	private Vector2 location;
	

	private PlayerInputAdapter adapter;
	private float step = 0.8f;

	public LocalPlayer(Character character, PlayerInputAdapter adapter, String nickname) {
		super(new Texture("ElfBasic.png"));
		this.adapter = adapter;
		
		data = new PlayerData(nickname);
		centerLocation = new Vector2();
		location = new Vector2();
	}

	@Override
	public void draw(Batch batch) {
		updateCoords(data);
		setCoords();
		super.draw(batch);
	}

	private void updateCoords(PlayerData data) {
		if (adapter.leftPressed) {
			data.setX(data.getX() - step);
		}
		if (adapter.rightPressed) {
			data.setX(data.getX() + step);
		}
		if (adapter.upPressed) {
			data.setY(data.getY() + step);
		}
		if (adapter.downPressed) {
			data.setY(data.getY() - step);
		}
	}

	private void setCoords() {
		this.setX(data.getX());
		this.setY(data.getY());

		location.x = data.getX();
		location.y = data.getY();
		centerLocation.x = data.getX() + 16;
		centerLocation.y = data.getY() + 16;

	}

	
	public PlayerData getData() {
		return data;
	}

	public Vector2 getCenterLocation() {
		return centerLocation;
	}

	public Vector2 getLocation() {
		return location;
	}

	public String getNickname() {
		return data.getNickname();
	}
	public void setLocation(Vector2 location) {
		this.location = location;
	}

}
