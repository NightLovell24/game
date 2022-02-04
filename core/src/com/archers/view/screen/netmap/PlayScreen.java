package com.archers.view.screen.netmap;

import java.io.IOException;

import java.util.Date;
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayScreen implements Screen {

	public static int WIDTH;
	public static int HEIGHT;

	public static int PIXELS;

	private float worldWidth;
	private float worldHeight;
	private TiledMap map;

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private String nickname;
	private SpriteBatch batch;
	private ExtendViewport viewport;

	private Stage stage;

	private PlayerInputAdapter inputAdapter;
	private PacketDispatcher packetDispatcher;
	private LocalPlayer localPlayer;
	private Map<String, RemotedPlayer> players;

	public PlayScreen(SpriteBatch batch, String nickname, PacketDispatcher packetDispatcher) {
		this.nickname = nickname;
		this.batch = batch;
		this.packetDispatcher = packetDispatcher;
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("map.tmx");

		MapProperties prop = map.getProperties();
		WIDTH = prop.get("width", Integer.class);
		HEIGHT = prop.get("height", Integer.class);
		PIXELS = prop.get("tilewidth", Integer.class);
		worldWidth = WIDTH * PIXELS;
		worldHeight = HEIGHT * PIXELS;

		renderer = new OrthogonalTiledMapRenderer(map, batch);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(worldWidth / 4, worldHeight / 4, camera);

		stage = new Stage(viewport, batch);



		players = new ConcurrentHashMap<>();
		inputAdapter = new PlayerInputAdapter();
		localPlayer = new LocalPlayer(Character.ELF, this.inputAdapter, nickname, stage);
		new Thread(() -> {

				try {
					packetDispatcher.processPacket(this);
				} catch (IOException e) {
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

		stage.act();
		stage.draw();

		batch.begin();

		localPlayer.getData().setDate(new Date());
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
		if (data.getNickname().equals(localPlayer.getNickname())) {

		} else {
			RemotedPlayer player = players.get(data.getNickname());
			if (player == null) {
				joinPlayer(data);
			}
			player = players.get(data.getNickname());
			player.setCoords(data.getX(), data.getY());
		}
	}

	public void joinPlayer(PlayerData data) {
		RemotedPlayer player = new RemotedPlayer(data.getNickname());
//		System.out.println(data);
		players.put(data.getNickname(), player);
	}

	public void removePlayer(String nickname) {
		players.remove(nickname);
	}

	private void drawPlayers() {
//		System.out.println("count remoted on client " + localPlayer.getNickname() + " " + players.values().size());
		localPlayer.draw(batch);
		for (RemotedPlayer player : players.values()) {
			if (player.getEntitySprite() == null) {
				Sprite newSprite = new Sprite(new Texture("ElfBasic.png"));
				player.setEntitySprite(newSprite);
			}
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
		stage.dispose();
		map.dispose();
		renderer.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}