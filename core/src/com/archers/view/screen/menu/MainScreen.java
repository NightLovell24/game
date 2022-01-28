package com.archers.view.screen.menu;

import com.archers.main.Starter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen {
	private SpriteBatch batch;
	private Starter game;
	private Stage stage;

	private Viewport viewport;
	private OrthographicCamera camera;
	private Skin skin;
	private final float WIDTH = 640;
	private final float HEIGHT = 480;
	private Music music;

	public MainScreen(SpriteBatch batch, Starter game) {
		this.batch = batch;
		this.game = game;
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		music = Gdx.audio.newMusic(Gdx.files.internal("mainmusic.mp3"));
		music.setLooping(true);
		music.play();

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

		TextButton joinButton = new TextButton("Join to the server", skin);
		TextButton createButton = new TextButton("Create the server", skin);
		TextButton optionsButton = new TextButton("Options", skin);
		TextButton exitButton = new TextButton("Exit", skin);

		joinButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new JoinScreen(batch, game));
			}
		});
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//new Server();
				//open a screen with a nickname input
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				game.setScreen(new CreateScreen());
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		mainTable.add(joinButton);
		mainTable.row();
		mainTable.add(createButton);
		mainTable.row();
		mainTable.add(optionsButton);
		mainTable.row();
		mainTable.add(exitButton);

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
		

	}

	@Override
	public void dispose() {
		skin.dispose();
		music.dispose();
		stage.dispose();

	}

}
