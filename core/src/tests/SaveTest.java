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

import java.util.HashMap;
import java.util.Map;

/**
 * Test saving and loading a key map
 */
public class SaveTest implements ApplicationListener {

    Map<String, Float> keyMap = new HashMap<String, Float>();
    Map<String, Float> controllerMap = new HashMap<String, Float>();
    //IntMap<Integer> keyMap = new IntMap<Integer>();
    //IntMap<Integer> controllerMap = new IntMap<Integer>();


    @Override
    public void create() {
        keyMap.put("btn-"+Input.Keys.A, (float)GameActions.PLAYER_LEFT);
        keyMap.put("btn-"+Input.Keys.D, (float)GameActions.PLAYER_RIGHT);
        keyMap.put("btn-"+Input.Keys.SPACE, (float)GameActions.PLAYER_JUMP);
        keyMap.put("btn-"+Input.Buttons.LEFT, (float)GameActions.PLAYER_ATTACK_LIGHT);

        controllerMap.put("axis-"+XboxOne.AXIS_LEFT_X+"-", (float)GameActions.PLAYER_LEFT);
        controllerMap.put("axis-"+XboxOne.AXIS_LEFT_X+"+", (float)GameActions.PLAYER_RIGHT);
        controllerMap.put("btn-"+XboxOne.BUTTON_A, (float)GameActions.PLAYER_JUMP);
        controllerMap.put("btn-"+XboxOne.BUTTON_B, (float)GameActions.PLAYER_DODGE);
        controllerMap.put("btn-"+XboxOne.BUTTON_X, (float)GameActions.PLAYER_ATTACK_LIGHT);
        controllerMap.put("btn-"+XboxOne.BUTTON_Y, (float)GameActions.PLAYER_ATTACK_HEAVY);
        controllerMap.put("btn-"+XboxOne.BUTTON_LB, (float)GameActions.PLAYER_BLOCK);
        controllerMap.put("btn-"+XboxOne.BUTTON_RB, (float)GameActions.PLAYER_ATTACK_RANGED);
        controllerMap.put("axis-"+XboxOne.AXIS_LEFT_TRIGGER+"+", (float)GameActions.PLAYER_ABILITY_1);
        controllerMap.put("axis-"+XboxOne.AXIS_RIGHT_TRIGGER+"+", (float)GameActions.PLAYER_ABILITY_2);
        controllerMap.put("btn-"+XboxOne.BUTTON_LB + ",btn-"+ XboxOne.BUTTON_RB, (float)GameActions.PLAYER_ABILITY_SPECIAL); //TODO
        controllerMap.put("btn-"+XboxOne.BUTTON_BACK, (float)GameActions.GAME_MENU);
        controllerMap.put("btn-"+XboxOne.BUTTON_START, (float)GameActions.ESC_MENU);
        controllerMap.put("pov-"+XboxOne.BUTTON_DPAD_LEFT.ordinal(), (float)GameActions.FAVS_LEFT);
        controllerMap.put("pov-"+XboxOne.BUTTON_DPAD_UP.ordinal(), (float)GameActions.FAVS_UP);
        controllerMap.put("pov-"+XboxOne.BUTTON_DPAD_RIGHT.ordinal(), (float)GameActions.FAVS_RIGHT);
        controllerMap.put("pov-"+XboxOne.BUTTON_DPAD_DOWN.ordinal(), (float)GameActions.FAVS_DOWN);
        controllerMap.put("axis-"+XboxOne.AXIS_RIGHT_X+"-", (float)GameActions.PAN_LEFT);
        controllerMap.put("axis-"+XboxOne.AXIS_RIGHT_X+"+", (float)GameActions.PAN_RIGHT);
        controllerMap.put("deadzone", 0.45f);

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
        //keyMap = json.fromJson(IntMap.class, file.readString());
        //controllerMap = json.fromJson(IntMap.class, Base64Coder.decodeString(cfile.readString()));
        //controllerMap = json.fromJson(IntMap.class, cfile.readString());
        keyMap = json.fromJson(HashMap.class, file.readString());
        controllerMap = json.fromJson(HashMap.class, cfile.readString());

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
    public void printMap(Map<String, Float> keyMap) {
        for (String key : keyMap.keySet()) {
            System.out.println(key + ": " + GameActions.toString(keyMap.get(key).intValue()));
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
