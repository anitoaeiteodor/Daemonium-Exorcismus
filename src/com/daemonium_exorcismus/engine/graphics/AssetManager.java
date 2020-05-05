package com.daemonium_exorcismus.engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class AssetManager {
    public static final HashMap<Assets, BufferedImage> assets = new HashMap<>();
    private static SpriteSheet resources = new SpriteSheet(LoadImage("assets/0x72_16x16DungeonTileset.v4.png"),
                            16, 16);
    private static BufferedImage map = LoadImage("assets/map.png");

    public static void InitAssets() {
        System.out.println("Initializing assets");

        assets.put(Assets.PLAYER, resources.crop(8, 14, 1, 1));
        assets.put(Assets.SMALL_ENEMY, resources.crop(0, 11, 1, 1));
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
