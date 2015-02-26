package tests.ai.btree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.Reader;

/**
 * Based on https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/btree/tests/ParseAndRunTest.java
 */
public class BehaviourTreeTest extends BehaviourTreeTestBase {

    private BehaviorTree<NPC> npcBehaviorTree;
    private float elapsedTime;


    @Override
    public void create() {
        elapsedTime = 0;

        Reader reader = null;
        try {
            reader = Gdx.files.internal("test/btree/npc.tree").reader();
            BehaviorTreeParser<NPC> parser = new BehaviorTreeParser<NPC>(BehaviorTreeParser.DEBUG_NONE);
            npcBehaviorTree = parser.parse(reader, new NPC("Blacksmith"));
        }
        finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getRawDeltaTime();

        if (elapsedTime > 0.8f) {
            npcBehaviorTree.step();
            elapsedTime = 0;
        }
    }

    @Override
    public void resize(int width, int height) {

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
