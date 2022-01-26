package com.archers.entities;

import com.archers.inputadapters.PlayerAdapter;
import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite {

	private enum State {
		STANDING, RUNNING, HIDING
	}

	private enum Facing {
		LEFT, RIGHT, DOWN, UP
	}

	private PlayScreen screen;
	private Vector2 location = new Vector2(); // its the bottom left coords of sprite
	private Vector2 centerLocation = new Vector2(); // its a center of sprite. We need it for camera centering or for
													// pvp calculations

	public Vector2 getCenterLocation() {
		return centerLocation;
	}

	private float step = 0.7f;
	public PlayerAdapter adapter;
	private final float FRAME_RATE_STANDING = 1 / 8f;
	private final float FRAME_RATE_RUNNING = 1 / 15f;
	private State currentState;
	private Facing currentFacing;
	private TextureAtlas atlas;
	private Animation<TextureRegion> rightStanding;
	private Animation<TextureRegion> rightRunning;
	private Animation<TextureRegion> leftStanding;
	private Animation<TextureRegion> leftRunning;
	private Animation<TextureRegion> downStanding;
	private Animation<TextureRegion> downRunning;
	private float rightStandingTime;
	private float rightRunningTime;
	private float leftStandingTime;
	private float leftRunningTime;
	private float downStandingTime;
	private float downRunningTime;
	private Texture invisible = new Texture("invisible.png");

	public Player(Sprite sprite, PlayScreen screen) {

		super(sprite);

		this.screen = screen;
		adapter = new PlayerAdapter();
		currentState = State.STANDING;
		currentFacing = Facing.RIGHT;
		atlas = new TextureAtlas("Elf.pack");
		rightStanding = new Animation<TextureRegion>(FRAME_RATE_STANDING, atlas.findRegions("standingRight"));
		rightRunning = new Animation<TextureRegion>(FRAME_RATE_RUNNING, atlas.findRegions("runningRight"));
		leftStanding = new Animation<TextureRegion>(FRAME_RATE_STANDING, atlas.findRegions("standingLeft"));
		leftRunning = new Animation<TextureRegion>(FRAME_RATE_RUNNING, atlas.findRegions("runningLeft"));
		downStanding = new Animation<TextureRegion>(FRAME_RATE_STANDING, atlas.findRegions("standingDown"));
		downRunning = new Animation<TextureRegion>(FRAME_RATE_RUNNING, atlas.findRegions("runningDown"));

		rightStanding.setFrameDuration(FRAME_RATE_STANDING);
		rightRunning.setFrameDuration(FRAME_RATE_RUNNING);
		leftStanding.setFrameDuration(FRAME_RATE_STANDING);
		leftRunning.setFrameDuration(FRAME_RATE_RUNNING);
		downStanding.setFrameDuration(FRAME_RATE_STANDING);
		downRunning.setFrameDuration(FRAME_RATE_RUNNING);

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

		float x = location.x;
		float y = location.y;
		centerLocation.x = location.x + 16;
		centerLocation.y = location.y + 16;

		updateState(x, y);
		updateFacing(centerLocation.x, centerLocation.y);
		updateSprite(delta);
	}

	private void updateFacing(float centerX, float centerY) {

		Vector3 tempVector = screen.getCamera().unproject(new Vector3(adapter.getMouseAngle(), 0));
		float mouseAngleX = tempVector.x;
		float mouseAngleY = tempVector.y;

//		if (mouseAngleY > centerY && Math.abs(mouseAngleX - centerX) <= 16) {
//			currentFacing = Facing.UP;
//		}
		if (mouseAngleY < centerY && Math.abs(mouseAngleX - centerX) <= 16) {
			currentFacing = Facing.DOWN;
		}
		if (mouseAngleX > centerX && Math.abs(mouseAngleY - centerY) <= 16) {
			currentFacing = Facing.RIGHT;
		}
		if (mouseAngleX < centerX && Math.abs(mouseAngleY - centerY) <= 16) {
			currentFacing = Facing.LEFT;
		}

	}

	private void updateSprite(float delta) {
		switch (currentState) {
			case STANDING:
				if (currentFacing == Facing.RIGHT) {
					rightStandingTime += delta;
					this.setRegion(rightStanding.getKeyFrame(rightStandingTime, true));
				}
				if (currentFacing == Facing.LEFT) {
					leftStandingTime += delta;
					this.setRegion(leftStanding.getKeyFrame(leftStandingTime, true));
				}
				if (currentFacing == Facing.DOWN) {
					downStandingTime += delta;
					this.setRegion(downStanding.getKeyFrame(downStandingTime, true));
				}
				break;
			case RUNNING:
				if (currentFacing == Facing.RIGHT) {
					rightRunningTime += delta;
					this.setRegion(rightRunning.getKeyFrame(rightRunningTime, true));
				}
				if (currentFacing == Facing.LEFT) {
					leftRunningTime += delta;
					this.setRegion(leftRunning.getKeyFrame(leftRunningTime, true));
				}
				if (currentFacing == Facing.DOWN) {
					downRunningTime += delta;
					this.setRegion(downRunning.getKeyFrame(downRunningTime, true));
				}
				break;
			case HIDING:
				this.setTexture(invisible);
				break;
			}
	}

	private void updateState(float x, float y) {
		boolean isStanding = true;
		if (adapter.leftPressed) {
			x -= step;
			isStanding = false;
		}
		if (adapter.rightPressed) {
			x += step;
			isStanding = false;
		}
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

	}

}
