package tests.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import tests.ai.btree.NPC;

import java.util.Random;


public class WorkTask extends LeafTask<NPC> {

    private Random random = new Random();
    
    @Override
    public void run(NPC npc) {
        while (random.nextFloat() < 0.15f) npc.work();
        success();
    }

    @Override
    protected Task<NPC> copyTo(Task<NPC> task) {
        return task;
    }
}
