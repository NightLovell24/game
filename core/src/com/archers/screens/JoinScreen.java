package com.archers.screens;

import com.archers.main.Starter;

import com.archers.multiplayer.client.Client;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;

public class JoinScreen implements Screen {

	private SpriteBatch batch;
	private Starter game;
	private Stage stage;

	private Viewport viewport;
	private OrthographicCamera camera;
	private Skin skin;
	private final float WIDTH = 640;
	private final float HEIGHT = 480;

	public JoinScreen(SpriteBatch batch, Starter game) {
		this.batch = batch;
		this.game = game;
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		camera = new OrthographicCamera();

		viewport = new ExtendViewport(WIDTH, HEIGHT);
		// viewport.apply();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, this.batch);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.top();

		
		Label nicknameLabel = new Label("Nickname: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		Label adressLabel = new Label("Adress: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		TextButton connectButton = new TextButton("Connect", skin);
		TextButton menuButton = new TextButton("Menu", skin);
		TextField nickname = new TextField("", skin);
		TextField adress = new TextField("", skin);

		Label wrongIpLabel = new Label("Cannot connect to the given address.",
				new Label.LabelStyle(new BitmapFont(), Color.RED));
		wrongIpLabel.setVisible(false);
		Label nicknameUsedLabel = new Label("This nickname is already used. Please take another.",
				new Label.LabelStyle(new BitmapFont(), Color.RED));
		nicknameUsedLabel.setVisible(false);


		connectButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String nick = nickname.getText();
				String ip = adress.getText();

				if (nick == null || nick.trim().isEmpty()) return;

				Client client;
				try {
					client = new Client(ip, nick, game.getPlayScreen());
				} catch (IOException e) {
					nicknameUsedLabel.setVisible(false);
					wrongIpLabel.setVisible(true);
					client = null;
				} catch (IllegalArgumentException e) {
					wrongIpLabel.setVisible(false);
					nicknameUsedLabel.setVisible(true);
					client = null;
				}

				if (client != null) {
					game.getPlayScreen().setClient(client);
					game.setScreen(game.getPlayScreen());
				}
			}
		});
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getMainScreen());
			}
		});
		mainTable.add(nicknameLabel);
		
		mainTable.add(nickname);
		mainTable.row();
		mainTable.add(adressLabel);
		mainTable.add(adress);
		mainTable.row();
		mainTable.add(connectButton);
		mainTable.row();
		mainTable.add(menuButton);
		mainTable.row();
		mainTable.add(wrongIpLabel);
		mainTable.row();
		mainTable.add(nicknameUsedLabel);

		stage.addActor(mainTable);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);

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
		skin.dispose();
		stage.dispose();
	}

}
