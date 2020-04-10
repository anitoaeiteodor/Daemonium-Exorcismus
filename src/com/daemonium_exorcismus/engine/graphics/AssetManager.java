package com.daemonium_exorcismus.engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class AssetManager {
    public static HashMap<Assets, SpriteSheet> assets = new HashMap<>();

    public static void InitAssets() {
        System.out.println("Initializing assets");
        assets.put(Assets.COBBLE, new SpriteSheet(LoadImage("assets\\tiles\\environment\\cobblestone.png")));
        assets.put(Assets.CARPET, new SpriteSheet(LoadImage("assets\\tiles\\environment\\carpet.png")));
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
