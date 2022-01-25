package com.archers.screens;

import com.archers.entities.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayScreen implements Screen {
	public static final int MIN_X = 0;
	public static final int MAX_X = 50 * 16;

	public static final int MIN_Y = 0;
	public static final int MAX_Y = 50 * 16;

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private Player player;

	private float worldWidth = 50 * 16;
	private float worldHeight = 50 * 16;

	public PlayScreen(SpriteBatch batch) {
		this.batch = batch;

	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("firstmap.tmx");

		renderer = new OrthogonalTiledMapRenderer(map, batch);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(worldWidth / 4, worldHeight / 4, camera);

//		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//?
		player = new Player(new Sprite(new Texture("knight1.png")));
		Gdx.input.setInputProcessor(player.adapter);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setView(camera);
		renderer.render();

		batch.begin();
		player.draw(batch);
//		camera.position.set(player.location, 0);
		cameraGo();
		camera.update();

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);

	}

	private void cameraGo() {
		Vector2 playerPos = player.location;

		float cameraX = Math.min(Math.max(playerPos.x, camera.viewportWidth / 2.0f),
				worldWidth - camera.viewportWidth / 2.0f);
		float cameraY = Math.min(Math.max(playerPos.y, camera.viewportHeight / 2.0f),
				worldHeight - camera.viewportHeight / 2.0f);
		camera.position.set(cameraX, cameraY, 0);
		camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();

	}

}