package tests.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import tests.ai.btree.NPC;



public class WalkTask extends LeafTask<NPC> {

    @Override
    public void run(NPC npc) {
        npc.wander();
        success();
    }

    @Override
    protected Task<NPC> copyTo(Task<NPC> task) {
        return task;
    }
}
