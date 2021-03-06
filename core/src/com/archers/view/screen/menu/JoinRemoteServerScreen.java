package com.archers.view.screen.menu;



import com.archers.controller.net.client.PacketDispatcher;
import com.archers.main.Starter;
import com.archers.model.PlayerData;
import com.archers.view.screen.netmap.PlayScreen;
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

public class JoinRemoteServerScreen implements Screen {
	private SpriteBatch batch;
	private Starter game;
	private Stage stage;

	private Viewport viewport;
	private OrthographicCamera camera;
	private Skin skin;
	private final float WIDTH = 640;
	private final float HEIGHT = 480;

	public JoinRemoteServerScreen(SpriteBatch batch, Starter game) {
		this.batch = batch;
		this.game = game;
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		camera = new OrthographicCamera();

		viewport = new ExtendViewport(WIDTH, HEIGHT);
		// viewport.apply();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

		stage = new Stage(viewport, batch);
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
		TextField address = new TextField("", skin);
		Label wrongIpLabel = new Label("Cannot connect to the given address.",
				new Label.LabelStyle(new BitmapFont(), Color.RED));
		wrongIpLabel.setVisible(false);
		Label nicknameUsedLabel = new Label("This nickname is already used. Please take another.",
				new Label.LabelStyle(new BitmapFont(), Color.RED));
		nicknameUsedLabel.setVisible(false);

		connectButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				PacketDispatcher packetDispatcher = new PacketDispatcher(address.getText(), 24120);
				boolean joined = packetDispatcher.join(new PlayerData(nickname.getText()));
				if (joined) {
					game.setScreen(new PlayScreen(batch, nickname.getText(),  packetDispatcher));
				} else {
					wrongIpLabel.setVisible(true);
				}
			}
		});
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setMainScreen();
			}
		});
		mainTable.add(nicknameLabel);

		mainTable.add(nickname);
		mainTable.row();
		mainTable.add(adressLabel);
		mainTable.add(address);
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
