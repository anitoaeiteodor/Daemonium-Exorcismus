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

    private HashMap<Integer, ArrayList<Wave>> levels = new HashMap<>();
    private ArrayList<Spawner> spawners = new ArrayList<>();
    private boolean nextLevelFlag = false;
    private int currentLevel;

    public LevelSystem() {
        this.name = SystemNames.LEVEL;
        this.oldTime = 0;
        this.currentLevel = 0;
        spawners.add(new Spawner(Constants.SPAWN_A));
        spawners.add(new Spawner(Constants.SPAWN_B));
        spawners.add(new Spawner(Constants.SPAWN_C));
        spawners.add(new Spawner(Constants.SPAWN_D));
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if (finishedSpawning() && !nextLevelFlag) {
            return;
        }
        if (newTime - oldTime < Game.timeFrame) {
            return;
        }
        nextLevelFlag = false;

        for (Spawner spr : spawners) {
            spr.update(entityList, newTime);
        }
    }

    private boolean finishedSpawning() {
        boolean result = true;
        for (Spawner spr : spawners) {
            result &= spr.isFinished();
        }
        return result;
    }

    public void loadNextLevel() {
        for (Spawner spr : spawners) {
            spr.setWave(levels.get(currentLevel).get(0));
            levels.get(currentLevel).remove(0);
        }
        this.nextLevelFlag = true;
        currentLevel++;
    }

    public void addWave(int level, Wave wave) {
        if (levels.containsKey(level)) {
            ArrayList<Wave> waveList = levels.get(level);
            waveList.add(wave);
        }
        else {
            ArrayList<Wave> waveList = new ArrayList<>();
            waveList.add(wave);
            levels.put(level, waveList);
        }
    }
}
