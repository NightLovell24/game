package com.archers.main;

import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Starter extends Game {
	public SpriteBatch batch;


	@Override
	public void create() {
batch = new SpriteBatch();
		setScreen(new PlayScreen(batch));

	}

}
