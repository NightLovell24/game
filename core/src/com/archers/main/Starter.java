package com.archers.main;


import com.archers.screens.PlayScreen;
import com.badlogic.gdx.Game;



public class Starter extends Game {
public static final int WIDTH = 1200;
public static final int HEIGHT = 1400;

	@Override
	public void create() {
	
		setScreen(new PlayScreen());
		
	}
	
}
