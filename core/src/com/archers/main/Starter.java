package com.archers.main;

import com.archers.view.screen.menu.MainScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Starter extends Game {
	private SpriteBatch batch;
	private MainScreen mainScreen;

	
	public void setMainScreen()
	{
		this.setScreen(mainScreen);
	}


	@Override
	public void create() {
		batch = new SpriteBatch();
		mainScreen = new MainScreen(batch, this);
		setScreen(mainScreen);
	}

}
