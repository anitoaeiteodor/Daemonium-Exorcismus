package com.daemonium_exorcismus.ecs.components;

import com.daemonium_exorcismus.engine.graphics.SpriteSheet;

public class RenderComponent extends Component {

    private boolean isVisible;
    private SpriteSheet sprite;

    public RenderComponent(boolean isVisible, SpriteSheet sprite) {
        this.name = ComponentNames.RENDER;
        this.isVisible = isVisible;
        this.sprite = sprite;
    }

    public boolean getVisibilityStatus() {
        return isVisible;
    }

    public void getVisibilityStatus(boolean value) {
        isVisible = value;
    }

    public SpriteSheet getSpriteSheet() {
        return sprite;
    }

    @Override
    public String getName() {
        return name;
    }
}
