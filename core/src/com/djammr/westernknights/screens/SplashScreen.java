package com.djammr.westernknights.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.WKWorld;
import com.djammr.westernknights.entity.systems.InputSystem;
import com.djammr.westernknights.ui.DebugUI;
import com.djammr.westernknights.ui.PlayerHUD;
import com.djammr.westernknights.util.controllers.DebugController;
import com.djammr.westernknights.util.controllers.PlayerHUDController;
import com.djammr.westernknights.util.controllers.UIController;
import com.djammr.westernknights.util.input.InputMapper;

public class SplashScreen extends WKScreen{

	Sprite libgdxlogoSprite;
	Sprite studiologoSprite;

	public SplashScreen(WKGame game) {
        super(game);
    }

	@Override
	public void load() {

	}

	@Override
	public void loadComplete() {
		super.loadComplete();
	}

	@Override
	public void show() {

		libgdxlogoSprite.setPosition(Gdx.graphics.getWidth()/2 - libgdxlogoSprite.getWidth()/2, Gdx.graphics.getHeight()/2 - libgdxlogoSprite.getHeight()/2);
		studiologoSprite.setPosition(Gdx.graphics.getWidth()/2 - studiologoSprite.getWidth()/2, Gdx.graphics.getHeight()/2 - studiologoSprite.getHeight()/2);
	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void dispose() {

	}
}
