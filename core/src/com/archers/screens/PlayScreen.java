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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Iterator;

public class PlayScreen implements Screen {
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final int PIXELS = 16;

	public static final int MIN_X = 0;
	public static final int MAX_X = (WIDTH - 1) * PIXELS;

	public static final int MIN_Y = 0;
	public static final int MAX_Y = (HEIGHT - 1) * PIXELS;

	private TiledMap map;
	private TiledMapTileLayer objectLayer;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private Player player;

	private float worldWidth = WIDTH * PIXELS;
	private float worldHeight = HEIGHT * PIXELS;

	public PlayScreen(SpriteBatch batch) {
		this.batch = batch;
	}

	public boolean isInsideWorld(float x, float y) {
		return x > MIN_X && x < MAX_X && y > MIN_Y && y < MAX_Y;
	}

	public boolean isInsideObstacle(float x, float y) {
		TiledMapTileLayer.Cell cell1 = objectLayer.getCell((int) (x / PIXELS), (int) y / PIXELS);
		TiledMapTileLayer.Cell cell2 = objectLayer.getCell((int) (x / PIXELS) + 1, (int) y / PIXELS);
		return (cell1 != null && cell1.getTile().getProperties().containsKey("blocked")) ||
				(cell2 != null && cell2.getTile().getProperties().containsKey("blocked"));
	}

	public boolean isInsideShelter(float x, float y) {
		TiledMapTileLayer.Cell cell1 = objectLayer.getCell((int) (x / PIXELS), (int) y / PIXELS);
		TiledMapTileLayer.Cell cell2 = objectLayer.getCell((int) (x / PIXELS) + 1, (int) y / PIXELS);
		return (cell1 != null && cell1.getTile().getProperties().containsKey("shelter")) ||
				(cell2 != null && cell2.getTile().getProperties().containsKey("shelter"));
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("firstmap.tmx");
		objectLayer = (TiledMapTileLayer) map.getLayers().get(1);

		renderer = new OrthogonalTiledMapRenderer(map, batch);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(worldWidth / 8, worldHeight / 8, camera);

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

	}

	@Override
	public void resume() {

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