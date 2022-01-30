package com.archers.view.entities;

import com.archers.model.PlayerData;
import com.archers.view.characters.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class RemotedPlayer extends Sprite {

	private PlayerData data;

	private Vector2 centerLocation;
	private Vector2 location;

	

	public RemotedPlayer(Character character, String nickname) {
		super(new Texture("ElfBasic.png"));

		data = new PlayerData(nickname);
		centerLocation = new Vector2();
		location = new Vector2();
	}

	@Override
	public void draw(Batch batch) {

		setCoords();
		super.draw(batch);
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
