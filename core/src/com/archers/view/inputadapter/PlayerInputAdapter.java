package com.archers.view.inputadapter;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class PlayerInputAdapter implements InputProcessor {
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean upPressed = false;
	public boolean downPressed = false;
	private Vector2 mouseAngle = new Vector2();

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
		mouseAngle.set(screenX, screenY);
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	public Vector2 getMouseAngle() {
		return mouseAngle;
	}
}
