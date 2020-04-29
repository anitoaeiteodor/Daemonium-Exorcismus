package com.daemonium_exorcismus.ecs.components;

import com.daemonium_exorcismus.engine.graphics.SpriteSheet;

public class RenderComponent extends Component {

    private boolean isVisible;
    private boolean isFlipped;
    private SpriteSheet sprite;

    public RenderComponent(boolean isVisible, SpriteSheet sprite, boolean isFlipped) {
        this.name = ComponentNames.RENDER;
        this.isVisible = isVisible;
        this.sprite = sprite;
        this.isFlipped = isFlipped;
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

    public boolean getVisibilityStatus() {
        return isVisible;
    }

    public SpriteSheet getSpriteSheet() {
        return sprite;
    }

    @Override
    public String getName() {
        return name;
    }
}
