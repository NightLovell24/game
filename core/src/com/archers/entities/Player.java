package com.archers.entities;

import com.archers.inputadapters.PlayerAdapter;
import com.archers.main.Starter;
import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

	private enum State {
		STANDING, RUNNING, HIDING
	}

	private PlayScreen screen;
	public Vector2 location = new Vector2();
	private float step = 0.5f;
	public PlayerAdapter adapter;
	private final float FRAME_RATE_STANDING = 1 / 10f;
	private final float FRAME_RATE_RUNNING = 1 / 15f;
	private State currentState;
	private TextureAtlas atlas;
	private Animation<TextureRegion> standing;
	private Animation<TextureRegion> running;
	private float standingTime;
	private float runningTime;
	private Sprite invisible = new Sprite(new Texture("invisible.png"));

	public Player(Sprite sprite) {
		super(sprite);

		screen = Starter.getPlayScreen();
		adapter = new PlayerAdapter();
		currentState = State.STANDING;
		atlas = new TextureAtlas("elf.pack");
		standing = new Animation<TextureRegion>(FRAME_RATE_STANDING, atlas.findRegions("standing"));
		running = new Animation<TextureRegion>(FRAME_RATE_RUNNING, atlas.findRegions("running"));
		standing.setFrameDuration(FRAME_RATE_STANDING);
		running.setFrameDuration(FRAME_RATE_RUNNING);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void setCoords(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	private void update(float delta) {
		boolean isStanding = true;
		float x = location.x;
		float y = location.y;

		if (adapter.leftPressed) {
			x -= step;
			isStanding = false;
		}
		if (adapter.rightPressed)
			x += step;
			isStanding = false;
		if (adapter.upPressed) {
			y += step;
			isStanding = false;
		}
		if (adapter.downPressed) {
			y -= step;
			isStanding = false;
		}

		if (screen.isInsideWorld(x, y) && !screen.isInsideObstacle(x, y)) {
			location.x = x;
			location.y = y;
			setCoords(x, y);
		}

		if (isStanding) {
			currentState = State.STANDING;
		} else {
			currentState = State.RUNNING;
		}

		if (screen.isInsideShelter(x, y)) {
			currentState = State.HIDING;
		}

		switch (currentState) {
			case STANDING:
				standingTime += delta;
				this.setRegion(standing.getKeyFrame(standingTime, true));
				break;
			case RUNNING:
				runningTime += delta;
				this.setRegion(running.getKeyFrame(runningTime, true));
				break;
			case HIDING:
				this.set(invisible);
				break;
		}
	}

}
