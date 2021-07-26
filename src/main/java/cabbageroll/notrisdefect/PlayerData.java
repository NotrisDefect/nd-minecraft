package cabbageroll.notrisdefect;

import java.io.Serializable;

public class PlayerData implements Serializable {

    private boolean transparent = false;
    private boolean custom = false;
    private Skin skin;

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public boolean isCustom() {
        return custom;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void swapCustom() {
        custom ^= true;
    }

    public void swapTransparent() {
        transparent ^= true;
    }
}
