package com.daemonium_exorcismus.engine.graphics;

import com.daemonium_exorcismus.engine.utils.Vec2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class SpriteSheet
{
    private BufferedImage       spriteSheet;              /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private final Vec2D size = new Vec2D(32, 32);

    public SpriteSheet(BufferedImage buffImg)
    {
        /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
    }


    public BufferedImage crop(int x, int y)
    {
        int tileWidth = (int)size.getPosX();
        int tileHeight = (int)size.getPosY();
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }

}
