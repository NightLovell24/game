package com.archers.screens;

import com.archers.entities.Player;
import com.archers.main.Starter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayScreen implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private FitViewport viewport;
	private Player player;
	private Music music;

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("firstmap.tmx");
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.play();
		renderer = new OrthogonalTiledMapRenderer(map);
		viewport = new FitViewport(Starter.WIDTH, Starter.HEIGHT, camera);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		player = new Player(new Sprite(new Texture("knight1.png")));
		Gdx.input.setInputProcessor(player);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
//		MapProperties prop = map.getProperties();
//
//		int mapWidth = prop.get("width", Integer.class);
//		int mapHeight = prop.get("height", Integer.class);
//		player.setX(mapWidth/2);
//		player.setY(mapHeight/2);
		player.draw(renderer.getBatch());
		camera.position.set(player.getX(), player.getY(), 0);
//		camera.position.set(viewport.getScreenWidth()/2, viewport.getScreenHeight()/2, 0);

		camera.update();
		renderer.getBatch().end();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 3;
		camera.viewportHeight = height / 3;

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
		music.dispose();

	}

}
