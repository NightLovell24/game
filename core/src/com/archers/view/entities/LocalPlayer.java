package com.archers.view.entities;

import com.archers.model.PlayerData;
import com.archers.view.characters.Character;
import com.archers.view.inputadapter.PlayerInputAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class LocalPlayer extends Sprite {
	private final PlayerData data;
	private final Vector2 centerLocation;

	private final PlayerInputAdapter adapter;
	private static final float STEP = 0.8f;

	public LocalPlayer(Character character, PlayerInputAdapter adapter, String nickname) {
		super(new Texture("ElfBasic.png"));
		this.adapter = adapter;
		
		data = new PlayerData(nickname);
		centerLocation = new Vector2();
	}

	@Override
	public void draw(Batch batch) {
		updateCoords();
		setCoords();
		updateButtons();
		super.draw(batch);
	}

	private void updateCoords() {
		float dx = 0;
		float dy = 0;
		if (adapter.leftPressed) {
			dx -= STEP;
		}
		if (adapter.rightPressed) {
			dx += STEP;
		}
		if (adapter.upPressed) {
			dy += STEP;
		}
		if (adapter.downPressed) {
			dy -= STEP;
		}

		if (dx * dx + dy * dy > 1.5 * STEP * STEP) {
			dx /= Math.sqrt(2);
			dy /= Math.sqrt(2);
		}

		data.setX(data.getX() + dx);
		data.setY(data.getY() + dy);
	}

	private void setCoords() {
		this.setX(data.getX());
		this.setY(data.getY());

		centerLocation.x = data.getX() + 16;
		centerLocation.y = data.getY() + 16;
	}

	private void updateButtons() {
		data.setUpPressed(adapter.upPressed);
		data.setDownPressed(adapter.downPressed);
		data.setLeftPressed(adapter.leftPressed);
		data.setRightPressed(adapter.rightPressed);
		data.setMouseAngle(adapter.getMouseAngle());
	}

	
	public PlayerData getData() {
		return data;
	}

	public Vector2 getCenterLocation() {
		return centerLocation;
	}

	public String getNickname() {
		return data.getNickname();
	}

	public void setLocation(float x, float y) {
		data.setX(x);
		data.setY(y);
	}

}
