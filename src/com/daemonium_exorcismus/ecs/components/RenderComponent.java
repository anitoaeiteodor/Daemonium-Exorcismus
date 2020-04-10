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

    public boolean GetVisibilityStatus() {
        return isVisible;
    }

    public void SetVisibilityStatus(boolean value) {
        isVisible = value;
    }

    public SpriteSheet GetSpriteSheet() {
        return sprite;
    }

    @Override
    public String GetName() {
        return name;
    }
}
