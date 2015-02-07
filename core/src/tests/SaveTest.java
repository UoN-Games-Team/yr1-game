package tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.djammr.westernknights.Assets;
import com.djammr.westernknights.util.input.controllers.mappings.XboxOne;
import com.djammr.westernknights.util.input.keybindings.GameActions;

/**
 * Test saving and loading a key map
 */
public class SaveTest implements ApplicationListener {

    //Map<Integer, Integer> keyMap = new HashMap<Integer, Integer>();
    //Map<Integer, Integer> controllerMap = new HashMap<Integer, Integer>();
    IntMap<Integer> keyMap = new IntMap<Integer>();
    IntMap<Integer> controllerMap = new IntMap<Integer>();


    @Override
    public void create() {
        keyMap.put(Input.Keys.A, GameActions.PLAYER_LEFT);
        keyMap.put(Input.Keys.D, GameActions.PLAYER_RIGHT);
        controllerMap.put(XboxOne.AXIS_LEFT_X, GameActions.PLAYER_LEFT);
        controllerMap.put(XboxOne.BUTTON_A, GameActions.PLAYER_JUMP);

        System.out.println("To save: ");
        printMap(keyMap);
        printMap(controllerMap);

        // Save the maps
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        FileHandle file = Gdx.files.local(Assets.keyMapDefault);
        file.writeString(json.prettyPrint(keyMap), false);

        FileHandle cfile = Gdx.files.local(Assets.controllerMapDefault);
        //cfile.writeString(Base64Coder.encodeString(json.prettyPrint(controllerMap)), false);
        cfile.writeString(json.prettyPrint(controllerMap), false);

        keyMap.clear();
        controllerMap.clear();

        // Load the maps
        keyMap = json.fromJson(IntMap.class, file.readString());
        //controllerMap = json.fromJson(IntMap.class, Base64Coder.decodeString(cfile.readString()));
        controllerMap = json.fromJson(IntMap.class, cfile.readString());

        System.out.println("Loaded: ");
        printMap(keyMap);
        printMap(controllerMap);
    }

    public void printMap(IntMap<Integer> keyMap) {
        IntMap<Integer> map = new IntMap<Integer>(keyMap);
        for (IntMap.Entry entry : map.entries()) {
            System.out.println(entry.key + ": " + GameActions.toString((int)entry.value));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
