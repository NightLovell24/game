package com.archers.view.entities;

import com.archers.model.PlayerData;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RemotedPlayer {
	private PlayerData data;
	private Vector2 centerLocation;
	private Vector2 location;

	private Sprite entitySprite;

	public RemotedPlayer(String nickname) {
		data = new PlayerData(nickname);
		centerLocation = new Vector2();
		location = new Vector2();
	}

	public void setCoords(float x, float y) {
		data.setX(x);
		data.setY(y);
	}
	
	private void setCoords() {
		location.x = data.getX();
		location.y = data.getY();
		centerLocation.x = data.getX() + 16;
		centerLocation.y = data.getY() + 16;
	}

	public void draw(SpriteBatch batch) {
		setCoords();
		batch.draw(entitySprite, location.x, location.y);

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

	

	public void setEntitySprite(Sprite entitySprite) {
		this.entitySprite = entitySprite;
	}

	public Sprite getEntitySprite() {
		return entitySprite;
	}

}
