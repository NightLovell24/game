package com.archers.view.entities;

import com.archers.model.PlayerData;
import com.archers.view.characters.Character;
import com.archers.view.inputadapter.PlayerInputAdapter;
import com.archers.view.screen.netmap.PlayScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Player {
    private PlayerData data;
    private final PlayerInputAdapter adapter;
    private final Stage stage;
    private Label nicknameLabel;
    private Sprite entitySprite;

    private static final float STEP = 0.8f;
    private static final Texture BASIC_ELF = new Texture("ElfBasic.png");
    private static final Texture INVISIBLE = new Texture("invisible.png");

    public Player(Character character, PlayerInputAdapter adapter, String nickname, Stage stage) {
        this.adapter = adapter;
        this.stage = stage;
        data = new PlayerData(nickname);
        centerLocation = new Vector2();
    }

    public void draw(Batch batch) {
        updateLabel();
        updateSprite();
        updateCoords();
        updateButtons();
        batch.draw(entitySprite, data.getX(), data.getY());
    }

    private void updateSprite() {
        if (entitySprite == null) {
            entitySprite = new Sprite(BASIC_ELF);
        }
        if (data.getCurrentState() == PlayerData.State.HIDING) {
            entitySprite.setTexture(INVISIBLE);
            nicknameLabel.setVisible(false);
        } else {
            entitySprite.setTexture(BASIC_ELF);
            nicknameLabel.setVisible(true);
        }
    }

    private void updateLabel() {
        if (nicknameLabel == null) {
            nicknameLabel = new Label(data.getNickname(), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            nicknameLabel.setVisible(true);
            stage.addActor(nicknameLabel);
        }
        nicknameLabel.setX(data.getX());
        nicknameLabel.setY(data.getY() + PlayScreen.PIXELS);
    }

    private void updateCoords() {
        float dx = 0;
        float dy = 0;
        if (adapter.leftPressed) {
            dx -= STEP;
        }
        if (adapter.rightPressed) {
            dx += STEP;
        }
        if (adapter.upPressed) {
            dy += STEP;
        }
        if (adapter.downPressed) {
            dy -= STEP;
        }

        if (dx * dx + dy * dy > 1.5 * STEP * STEP) {
            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }

        data.setX(data.getX() + dx);
        data.setY(data.getY() + dy);
    }

    private void updateButtons() {
        data.setStopped(false);
        data.setUpPressed(adapter.upPressed);
        data.setDownPressed(adapter.downPressed);
        data.setLeftPressed(adapter.leftPressed);
        data.setRightPressed(adapter.rightPressed);
        data.setMouseAngleX(adapter.getMouseAngle().x);
        data.setMouseAngleY(adapter.getMouseAngle().y);
    }

    public PlayerData getData() {
        return data;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

    private final Vector2 centerLocation;
    public Vector2 getCenterLocation() {
        centerLocation.x = data.getX() + 16;
        centerLocation.y = data.getY() + 16;
        return centerLocation;
    }

    public String getNickname() {
        return data.getNickname();
    }

    public void updateAdapter() {
        adapter.upPressed = data.isUpPressed();
        adapter.downPressed = data.isDownPressed();
        adapter.leftPressed = data.isLeftPressed();
        adapter.rightPressed = data.isRightPressed();
        adapter.getMouseAngle().x = data.getMouseAngleX();
        adapter.getMouseAngle().y = data.getMouseAngleY();
    }
}
