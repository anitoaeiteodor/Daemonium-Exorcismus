package com.daemonium_exorcismus.ecs.components;

import java.awt.image.BufferedImage;

/**
 * Component used by most entities. Only an entity that has a RenderComponent attached can be seen.
 */
public class RenderComponent extends Component {

    private boolean isVisible;
    private boolean isFlipped;
    private BufferedImage sprite;
    private int layer;
    private boolean isFlashing;

    public RenderComponent(boolean isVisible, BufferedImage sprite, boolean isFlipped, int layer) {
        this.name = ComponentNames.RENDER;
        this.isVisible = isVisible;
        this.sprite = sprite;
        this.isFlipped = isFlipped;
        this.layer = layer;
        this.isFlashing = false;
    }

    public boolean isFlashing() {
        return isFlashing;
    }

    public void setFlashing(boolean flashing) {
        isFlashing = flashing;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    @Override
    public String getName() {
        return name;
    }
}
