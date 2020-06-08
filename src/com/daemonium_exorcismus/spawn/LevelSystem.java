package com.daemonium_exorcismus.spawn;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.systems.SystemBase;
import com.daemonium_exorcismus.ecs.systems.SystemNames;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.spawn.Spawner;
import com.daemonium_exorcismus.spawn.Wave;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelSystem extends SystemBase {

    private HashMap<Integer, ArrayList<Wave>> levels;
    private ArrayList<Spawner> spawners = new ArrayList<>();
    public static boolean nextLevelFlag = false;
    public static boolean canLoadNextArea = false;
    private int currentLevel;
    private long waitSeconds;

    public LevelSystem(HashMap<Integer, ArrayList<Wave>> levels) {
        this.levels = levels;
        this.name = SystemNames.LEVEL;
        this.oldTime = 0;
        this.currentLevel = 0;
        spawners.add(new Spawner(Constants.SPAWN_A));
        spawners.add(new Spawner(Constants.SPAWN_B));
        spawners.add(new Spawner(Constants.SPAWN_C));
        spawners.add(new Spawner(Constants.SPAWN_D));

        loadNextLevel();
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if (newTime - oldTime < Game.timeFrame) {
            return;
        }
        waitSeconds -= newTime - oldTime;
        oldTime = newTime;

        if (finishedSpawning() && !nextLevelFlag) {
            return;
        }

        if (nextLevelFlag) {
            loadNextLevel();
        }

        System.out.println(waitSeconds);

        if (waitSeconds > 0) {
            return;
        }

        for (Spawner spr : spawners) {
            spr.update(entityList, newTime);
        }

    }

    public boolean finishedSpawning() {
        boolean result = true;
        for (Spawner spr : spawners) {
            result &= spr.isFinished();
        }
        if (result) {
            canLoadNextArea = true;
        }
        return result;
    }

    public void loadNextLevel() {
        if (levels.size() == 0) {
            return;
        }
        for (Spawner spr : spawners) {
            spr.setWave(levels.get(currentLevel).get(0));
            levels.get(currentLevel).remove(0);
        }
        nextLevelFlag = false;
        canLoadNextArea = false;
        waitSeconds = (long) (30 * Game.timeFrame);
        levels.remove(currentLevel);
        currentLevel++;
    }

}
