package com.archers.main;

import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Starter extends Game {
	public SpriteBatch batch;
	private static PlayScreen screen;

	public static PlayScreen getPlayScreen() {
		return screen;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		screen = new PlayScreen(batch);
		setScreen(screen);
	}

}
