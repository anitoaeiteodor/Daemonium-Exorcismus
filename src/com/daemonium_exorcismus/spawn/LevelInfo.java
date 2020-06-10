package com.daemonium_exorcismus.spawn;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Helper class that parses the information from the database.
 */
public class LevelInfo {
    public static String levelInfo;

    public static HashMap<Integer, ArrayList<Wave>> parseLevelInfo() {
        HashMap<Integer, ArrayList<Wave>> levelMap = new HashMap<>();
        String[] levels = levelInfo.split("\n");

        for (String level : levels) {
            String[] tokens = level.split(" ");
            int levelNumber = Integer.parseInt(tokens[0]);
            ArrayList<Wave> waves = new ArrayList<>();

            for (int i = 1; i < tokens.length; i += 5) {
                int regularEnemies = Integer.parseInt(tokens[i]);
                int mediumEnemies = Integer.parseInt(tokens[i + 1]);
                int heavyEnemies = Integer.parseInt(tokens[i + 2]);
                int delay = Integer.parseInt(tokens[i + 3]);
                int offset = Integer.parseInt(tokens[i + 4]);

                ArrayList<EntityType> enemies = new ArrayList<>();
                while (regularEnemies != 0) {
                    enemies.add(EntityType.REGULAR_ENEMY);
                    regularEnemies--;
                }
                while (mediumEnemies != 0) {
                    enemies.add(EntityType.MEDIUM_ENEMY);
                    mediumEnemies--;
                }
                while (heavyEnemies != 0) {
                    enemies.add(EntityType.HEAVY_ENEMY);
                    heavyEnemies--;
                }

                waves.add(new Wave(enemies, delay, offset));
            }

            levelMap.put(levelNumber, waves);
        }

        return levelMap;
    }
}
