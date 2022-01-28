package com.archers.view.screen.netmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.archers.controller.net.client.Client;
import com.archers.model.PlayerData;
import com.archers.view.characters.Character;
import com.archers.view.entities.Player;
import com.archers.view.inputadapter.PlayerInputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayScreen implements Screen {

	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;

	public static final int PIXELS = 16;
	public static final int CHARACTER_PIXELS = 32;

	public static final int MIN_X = 0;
	public static final int MAX_X = (WIDTH - 1) * PIXELS;

	public static final int MIN_Y = 0;
	public static final int MAX_Y = (HEIGHT - 1) * PIXELS;

	private float worldWidth = WIDTH * PIXELS;
	private float worldHeight = HEIGHT * PIXELS;

	private TiledMap map;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Client client;
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private Player localPlayer;
	private PlayerInputAdapter inputAdapter;
	private Map<String, Player> players; //

	public PlayScreen(SpriteBatch batch) {
		this.batch = batch;
	}

	public PlayScreen(SpriteBatch batch, Client client) {
		this.batch = batch;
		this.client = client;
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("firstmap.tmx");
		players = new HashMap<>();
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(worldWidth / 4, worldHeight / 4, camera);

		inputAdapter = new PlayerInputAdapter();
		localPlayer = new Player(Character.ELF);

		Gdx.input.setInputProcessor(this.inputAdapter);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setView(camera);
		renderer.render();

		batch.begin();
		sendInfoToServer(localPlayer);
		drawPlayers();
		cameraGo();
		camera.update();

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	private void cameraGo() {

		Vector2 playerPos = localPlayer.getCenterLocation();

		float cameraX = Math.min(Math.max(playerPos.x, camera.viewportWidth / 2.0f),
				worldWidth - camera.viewportWidth / 2.0f);
		float cameraY = Math.min(Math.max(playerPos.y, camera.viewportHeight / 2.0f),
				worldHeight - camera.viewportHeight / 2.0f);
		camera.position.set(cameraX, cameraY, 0);

		camera.update();

	}

	public void updateRemotePlayers(List<PlayerData> data) {
		Set<String> activePlayersUsernames = new HashSet<>();
		for (PlayerData playerData : data) {
			if (!playerData.getNickname().equals(client.getNickname())) {
				activePlayersUsernames.add(playerData.getNickname());
			}
		}
		Set<String> usernames = players.keySet();
		for (String username : usernames) {
			if (!activePlayersUsernames.contains(username)) {
				players.remove(username);
			}
		}

		for (PlayerData playerData : data) {
			if (!playerData.getNickname().equals(client.getNickname())) {
				Player player = players.get(playerData.getNickname());
				if (player == null) {
					player = new Player(Character.ELF);
					players.put(playerData.getNickname(), player);
				}
				Vector2 location = player.getLocation();
				location.x = playerData.getX();
				location.y = playerData.getY();
			}
		}
	}

	public void drawPlayers() {
		localPlayer.draw(batch);
		for (Player player : players.values()) {
			player.draw(batch);
		}
	}

	private void sendInfoToServer(Player player) {
		PlayerData data = new PlayerData();
		data.setNickname(client.getNickname());
		data.setX(player.getX());
		data.setY(player.getY());
		client.sendPlayerDataToServer(data);
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

	public OrthographicCamera getCamera() {
		return camera;
	}
}