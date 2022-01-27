package com.archers.main;

import com.archers.screens.MainScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Starter extends Game {
	public SpriteBatch batch;
	private MainScreen mainScreen;

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		mainScreen = new MainScreen(batch, this);
		setScreen(mainScreen);
	}

}
