package com.daemonium_exorcismus.engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class that is responsible for loading and providing assets.
 */
public class AssetManager {
    public static final HashMap<Assets, BufferedImage> assets = new HashMap<>();
    private static SpriteSheet resources = new SpriteSheet(LoadImage("assets/0x72_16x16DungeonTileset.v4.png"),
                            16, 16);
    private static BufferedImage map = LoadImage("assets/map.png");
    private static SpriteSheet menuAsset = new SpriteSheet(LoadImage("assets/menu.png"),
            1200, 800);
    private static BufferedImage tutorialScreen = LoadImage("assets/tutorial.png");
    private static SpriteSheet pauseScreen = new SpriteSheet(LoadImage("assets/pause.png"),
            500, 600);
    private static SpriteSheet gameOverScreen = new SpriteSheet(LoadImage("assets/gameover.png"),
            1200, 800);

    public static void InitAssets() {
        System.out.println("Initializing assets");

        assets.put(Assets.PLAYER, resources.crop(8, 14, 1, 1));
        assets.put(Assets.REGULAR_ENEMY, resources.crop(0, 11, 1, 1));
        assets.put(Assets.SKULL, resources.crop(1, 3, 1, 1));
        assets.put(Assets.CRATE, resources.crop(5, 6,1, 2));
        assets.put(Assets.COLUMN, resources.crop(13, 11, 1, 2));
        assets.put(Assets.PLAYER_PROJ, resources.crop(5, 8, 1, 1));
        assets.put(Assets.ENEMY_PROJ, resources.crop(7, 8, 1, 1));
        assets.put(Assets.MEDIUM_ENEMY, resources.crop(2, 11, 1, 1));
        assets.put(Assets.BIG_ENEMY, resources.crop(4, 11, 1, 1));
        assets.put(Assets.MENU_PLAY, menuAsset.crop(0, 0, 1, 1));
        assets.put(Assets.MENU_LOAD, menuAsset.crop(1, 0, 1, 1));
        assets.put(Assets.MENU_TUT, menuAsset.crop(0, 1, 1, 1));
        assets.put(Assets.MENU_EXIT, menuAsset.crop(1, 1, 1, 1));
        assets.put(Assets.TUT_SCREEN, tutorialScreen);
        assets.put(Assets.PAUSE_SCREEN_RES, pauseScreen.crop(0, 0, 1, 1));
        assets.put(Assets.PAUSE_SCREEN_EXIT, pauseScreen.crop(1, 0 , 1, 1));
        assets.put(Assets.GAME_OVER_BACK, gameOverScreen.crop(0, 0, 1, 1));
        assets.put(Assets.GAME_OVER_EXIT, gameOverScreen.crop(0, 1, 1, 1));
        assets.put(Assets.MAP, map);
    }

    private static BufferedImage LoadImage(String path)
    {
        try
        {
            BufferedImage img = ImageIO.read(new FileInputStream(path));
            System.out.println("Successfully opened image " + path);
            return img;
        }
        catch(IOException e)
        {
            System.out.println("[ERROR]: LoadImage failed on " + path);
            e.printStackTrace();
        }
        return null;
    }
}
