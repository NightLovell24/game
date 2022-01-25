package com.archers.entities;

import com.archers.inputadapters.PlayerAdapter;
import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

	private enum State {
		STANDING, RUNNING
	}

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

	public Player(Sprite sprite) {
		super(sprite);

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

		if (adapter.leftPressed) {

			if (location.x > PlayScreen.MIN_X) {
				location.add(-step, 0);
				isStanding = false;
			}
		}
		if (location.x < PlayScreen.MAX_X)
			if (adapter.rightPressed) {
				location.add(step, 0);
				isStanding = false;
			}
		if (location.y < PlayScreen.MAX_Y) {
			if (adapter.upPressed) {
				location.add(0, step);
				isStanding = false;
			}
		}
		if (location.y > PlayScreen.MIN_Y) {
			if (adapter.downPressed) {
				location.add(0, -step);
				isStanding = false;
			}
		}

		setCoords(location.x, location.y);

		if (isStanding) {
			currentState = State.STANDING;
		} else {
			currentState = State.RUNNING;
		}

		if (currentState == State.STANDING) {
			standingTime += delta;
			this.setRegion(standing.getKeyFrame(standingTime, true));
		}
		if (currentState == State.RUNNING) {
			runningTime += delta;
			this.setRegion(running.getKeyFrame(runningTime, true));
		}

	}

}
