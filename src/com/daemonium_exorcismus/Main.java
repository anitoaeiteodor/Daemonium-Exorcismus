package com.daemonium_exorcismus;

import com.daemonium_exorcismus.engine.core.Game;

public class Main {

    public static void main(String[] args) {
        Game game = new Game("Daemonium Exorcismus", 1200, 800);
        game.startGame();
    }
}
