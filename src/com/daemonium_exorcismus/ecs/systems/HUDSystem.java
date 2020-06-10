package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.HealthComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.core.RenderManager;
import com.daemonium_exorcismus.engine.graphics.Assets;
import com.daemonium_exorcismus.engine.input.KeyboardManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;
import com.daemonium_exorcismus.menu.GameState;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * System that keeps track of player health and time.
 * It also handles the pause mechanic logic.
 */
public class HUDSystem extends SystemBase {

    public static Vec2D HealthPosition = new Vec2D(50, 25);
    public static int HealthValue;
    public static Vec2D ScorePosition = new Vec2D(1000, 20);
    public static double ScoreValue;

    public HUDSystem(long oldTime) {
        super(oldTime);
        this.name = SystemNames.HUD;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if (newTime - oldTime < Game.timeFrame) {
            return;
        }

        ScoreValue += (newTime - oldTime) / 1000000000.;
        oldTime = newTime;

        Entity player = null;
        for (Entity ent : entityList.values()) {
            if (ent.getType() == EntityType.PLAYER) {
                player = ent;
            }
        }

        assert player != null;

        HealthComponent hc = (HealthComponent) player.getComponent(ComponentNames.HEALTH);
        HealthValue = hc.getHealth();

        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_ESCAPE)) {
            Game.gameState = GameState.GS_PAUSE;
            int option = 0;
            Assets[] options = {Assets.PAUSE_SCREEN_RES, Assets.PAUSE_SCREEN_EXIT};
            GameState[] gameStates = {GameState.GS_PLAY, GameState.GS_QUIT};

            while (!KeyboardManager.keysPressed.contains(KeyEvent.VK_ENTER)) {

                if (KeyboardManager.keysPressed.contains(KeyEvent.VK_W)) {
                    KeyboardManager.keysPressed.remove(KeyEvent.VK_W);
                    option--;
                    if (option < 0) {
                        option = options.length - 1;
                    }
                }
                if (KeyboardManager.keysPressed.contains(KeyEvent.VK_S)) {
                    KeyboardManager.keysPressed.remove(KeyEvent.VK_S);
                    option++;
                    if (option == options.length) {
                        option = 0;
                    }
                }

                RenderManager.GetInstance().drawPauseMenu(options[option]);
            }

            switch (gameStates[option]) {
                case GS_QUIT:
                    Game.stopGameSignal = true;
                    Game.gameState = GameState.GS_MENU;
                    System.out.println("Quitting game...");
                    break;
                case GS_PLAY:
                    Game.gameState = GameState.GS_PLAY;
                    break;
            }
        }
    }
}
