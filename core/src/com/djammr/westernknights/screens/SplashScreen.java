package com.djammr.westernknights.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.WKGame;


public class SplashScreen extends WKScreen{

	private Sprite libGDXLogo;
	private Sprite studioLogo;
	private Sprite[] splashes;
	private SpriteBatch batch;

	private float timer;
	/** Time to display each logo for in seconds */
	private float logoTime = 3;
	/** Current splash being displayed, corresponds to splashes index */
	private int currentSplash = 0;
	/** Total splashes to display, starting from 0 */
	private int totalSplashes = 1;
	/** Current alpha of the current splash */
	private float splashAlpha = 0;


	public SplashScreen(WKGame game) {
        super(game);
    }

	@Override
	public void load() {
		libGDXLogo = new Sprite(new Texture(Assets.libGDXLogo));
		studioLogo = new Sprite(new Texture(Assets.studioLogo));
		splashes = new Sprite[]{studioLogo, libGDXLogo};
		batch = new SpriteBatch();

		libGDXLogo.setPosition(Gdx.graphics.getWidth() / 2 - libGDXLogo.getWidth() / 2, Gdx.graphics.getHeight() / 2 - libGDXLogo.getHeight() / 2);
		studioLogo.setPosition(Gdx.graphics.getWidth() / 2 - studioLogo.getWidth() / 2, Gdx.graphics.getHeight() / 2 - studioLogo.getHeight() / 2);

		for (Sprite splash : splashes) splash.setAlpha(0);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		timer += delta;
		if (timer >= logoTime) {
			// Fade splash out
			splashAlpha -= delta;
			if (splashAlpha <= 0) {
				splashAlpha = 0;
				currentSplash++;
				if (currentSplash > totalSplashes) {
					game.getScreens().setScreen("main_menu", false);
				} else {
					timer = 0;
				}
			}
		} else {
			// Fade splash in
			splashAlpha += delta;
		}
		if (splashAlpha > 1) splashAlpha = 1;
		else if (splashAlpha < 0) splashAlpha = 0;
		if (currentSplash <= totalSplashes) splashes[currentSplash].setAlpha(splashAlpha);

		batch.begin();
		for (Sprite splash : splashes) splash.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
