package com.archers.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;

//	private int id;
	private Vector2 velocity = new Vector2();

	private float speed = 0.2f;
	private float gravity = 1.8f;
	private float step = 2f;

	public Player(Sprite sprite) {

		super(sprite);
	}

	@Override
	public void draw(Batch batch) {

		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W: {
			upPressed = true;

			break;
		}
		case Keys.S: {
			downPressed = true;
			break;
		}
		case Keys.A: {
			leftPressed = true;
			break;
		}
		case Keys.D: {
			rightPressed = true;
			break;
		}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W: {
			upPressed = false;

			break;
		}
		case Keys.S: {
			downPressed = false;
			break;
		}
		case Keys.A: {
			leftPressed = false;
			break;
		}
		case Keys.D: {
			rightPressed = false;
			break;
		}
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	private void update(float delta) {

		if (leftPressed) {
			velocity.add(-step, 0);
		}
		if (rightPressed) {
			velocity.add(step, 0);
		}
		if (upPressed) {
			velocity.add(0, step);
		}
		if (downPressed) {
			velocity.add(0, -step);
		}
		move();

	}
	private void move()
	{
//		System.out.println("X: " +velocity.x);
//		System.out.println("Y:" +velocity.y);
		this.setX(velocity.x);
		this.setY(velocity.y);
	}

}
