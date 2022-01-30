package com.archers.view.screen.netmap;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.archers.controller.net.client.PacketDispatcher;
import com.archers.controller.net.client.PacketType;
import com.archers.model.PacketPlayer;
import com.archers.model.PlayerData;
import com.archers.view.characters.Character;
import com.archers.view.entities.LocalPlayer;
import com.archers.view.entities.RemotedPlayer;
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

	private String nickname;
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private LocalPlayer localPlayer;
	private PlayerInputAdapter inputAdapter;
	private Map<String, RemotedPlayer> players; //
	private PacketDispatcher packetDispatcher;

	public PlayScreen(SpriteBatch batch, String nickname, PacketDispatcher packetDispatcher) {
		this.nickname = nickname;
		this.batch = batch;
		this.packetDispatcher = packetDispatcher;
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("firstmap.tmx");
		players = new ConcurrentHashMap<>();
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(worldWidth / 4, worldHeight / 4, camera);

		inputAdapter = new PlayerInputAdapter();
		localPlayer = new LocalPlayer(Character.ELF, this.inputAdapter, nickname);

		new Thread(() -> {
			try {
				packetDispatcher.proccessPacket(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}) {

		}.start();
		Gdx.input.setInputProcessor(this.inputAdapter);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setView(camera);
		renderer.render();

		batch.begin();
		packetDispatcher.dispatchMessage(new PacketPlayer(localPlayer.getData(), PacketType.CHECK));
		packetDispatcher.dispatchMessage(new PacketPlayer(localPlayer.getData(), PacketType.MOVE));
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

	public void updatePlayer(PlayerData data) {
		RemotedPlayer player = players.get(data.getNickname());
		if (player != null) {
			player.setLocation(new Vector2(data.getX(), data.getY()));
		}
	}

	public void joinPlayer(PlayerData data) {
		RemotedPlayer player = new RemotedPlayer(Character.ELF, data.getNickname());
		players.put(data.getNickname(), player);
	}

	public void removePlayer(String nickname) {
		players.remove(nickname);
	}

	private void drawPlayers() {
		localPlayer.draw(batch);
		for (RemotedPlayer player : players.values()) {
			player.draw(batch);
		}
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