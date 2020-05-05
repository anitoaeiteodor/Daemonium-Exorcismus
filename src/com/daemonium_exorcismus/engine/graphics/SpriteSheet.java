package com.daemonium_exorcismus.engine.graphics;

import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.image.BufferedImage;

public class SpriteSheet
{
    private BufferedImage spriteSheet;
    private Vec2D size;

    public SpriteSheet(BufferedImage buffImg, int spriteSizeX, int spriteSizeY)
    {
        spriteSheet = buffImg;
        size = new Vec2D(spriteSizeX, spriteSizeY);
    }

    public BufferedImage crop(int x, int y, int width, int height)
    {
        int tileWidth = (int)size.getPosX();
        int tileHeight = (int)size.getPosY();
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight,
                            width * tileWidth, height * tileHeight);
    }

}
