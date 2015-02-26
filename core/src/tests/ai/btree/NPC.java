package tests.ai.btree;


import com.djammr.westernknights.WKGame;

public class NPC {

    private String name;


    public NPC(String name) {
        this.name = name;
    }

    public void wander() {
        WKGame.logger.logInfo(name + " walks around");
    }

    public void work() {
        WKGame.logger.logInfo(name + " starts working");
    }
}
