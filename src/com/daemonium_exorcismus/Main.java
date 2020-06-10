package com.daemonium_exorcismus;

import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.spawn.LevelInfo;


public class Main {

    public static void main(String[] args) {
        DatabaseDriver.extractInfo();

        Game game = new Game("Daemonium Exorcismus", 1200, 800);
        game.launch();
    }
}
