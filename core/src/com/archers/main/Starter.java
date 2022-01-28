package com.archers.main;

import com.archers.screens.MainScreen;

import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Starter extends Game {
	public SpriteBatch batch;
	private MainScreen mainScreen;
	private PlayScreen playScreen;

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public PlayScreen getPlayScreen() {
		return playScreen;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		mainScreen = new MainScreen(batch, this);
		playScreen = new PlayScreen(batch);
		setScreen(mainScreen);
	}

}
