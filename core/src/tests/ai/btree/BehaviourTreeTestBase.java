package tests.ai.btree;

import com.badlogic.gdx.ApplicationListener;

/**
 * Based on https://github.com/libgdx/gdx-ai/blob/master/tests/src/com/badlogic/gdx/ai/tests/btree/BehaviorTreeTestBase.java
 */
public abstract class BehaviourTreeTestBase implements ApplicationListener {

    public BehaviourTreeTestBase() {

    }

    public abstract void create();
    public abstract void render();
    public abstract void dispose();

}
