package tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class WKTest {

    public static void run (String title, ApplicationListener target) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = title;
        cfg.useGL30 = false;
        cfg.width = 1280;
        cfg.height = 720;
        cfg.fullscreen = false;

        new LwjglApplication(target, cfg);
    }
}