package com.uwsoft.editor.renderer.actor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

/**
 * Created by sargis on 8/19/14.
 *
 * Edited by Andy Palmer
 */
public class SpriteAnimation {

    public static class Animation {
        public int startFrame;
        public int endFrame;
        public String name;

        public Animation(int startFrame, int endFrame, String name) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.name = name;
        }

        public Animation() {
        }

        public static String constructJsonString(Map<String, Animation> animations) {
            String str = "";
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            str = json.toJson(animations);
            return str;
        }

        public static Map<String, Animation> constructJsonObject(String animations) {
            if (animations.isEmpty()) {
                return new HashMap<>();
            }
            Json json = new Json();
            return json.fromJson(HashMap.class, animations);
        }
    }
}
