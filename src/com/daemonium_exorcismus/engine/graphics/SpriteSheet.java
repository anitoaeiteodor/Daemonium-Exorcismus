package com.daemonium_exorcismus.engine.graphics;

import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.image.BufferedImage;

/**
 * This class stores a sprite sheet with fixed width and height and allows fast cropping.
 */
public class SpriteSheet
{
    private BufferedImage spriteSheet;
    private Vec2D size;

    public SpriteSheet(BufferedImage buffImg, int spriteSizeX, int spriteSizeY)
    {
        spriteSheet = buffImg;
        size = new Vec2D(spriteSizeX, spriteSizeY);
    }

    /**
     * Cropping method. Returns a buffered image representing the sprite at position x, y
     * with dimension width * spriteSizeX, height * spriteSizeY.
     * @param x coordinate
     * @param y coordinate
     * @param width width of sprite
     * @param height height of sprite
     * @return BufferedImage representing the sprite at position x, y
     */
    public BufferedImage crop(int x, int y, int width, int height)
    {
        int tileWidth = (int)size.getPosX();
        int tileHeight = (int)size.getPosY();
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight,
                            width * tileWidth, height * tileHeight);
    }

}
