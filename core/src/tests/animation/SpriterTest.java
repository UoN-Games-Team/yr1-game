package tests.animation;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;
import com.djammr.westernknights.WKGame;
import com.djammr.westernknights.util.spriter.LibGdxDrawer;
import com.djammr.westernknights.util.spriter.LibGdxLoader;


/** From https://github.com/Trixt0r/spriter/blob/master/SpriterTests/src/com/brashmonkey/spriter/tests/backend/LibGdxTest.java */
public class SpriterTest implements ApplicationListener {

    SpriteBatch batch;
    ShapeRenderer renderer;
    OrthographicCamera camera;
    // Spriter
    Player spriterPlayer;
    Drawer<Sprite> spriterDrawer;
    LibGdxLoader spriterLoader;


    @Override
    public void create() {
        camera = new OrthographicCamera(32, 18);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        FileHandle handle = Gdx.files.internal("test/spriter/spriter_character.scml");
        Data data = new SCMLReader(handle.read()).getData();

        spriterLoader = new LibGdxLoader(data);
        spriterLoader.load(handle.file());

        spriterDrawer = new LibGdxDrawer(spriterLoader, batch, renderer);
        spriterPlayer = new Player(data.getEntity(0));
        spriterPlayer.setScale(WKGame.PIXELS_TO_METERS);
        spriterPlayer.speed = 4;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        spriterPlayer.update();
        batch.begin();
        {
            spriterDrawer.draw(spriterPlayer);
        }
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        spriterLoader.dispose();
    }
}
