package cabbageroll.notrisdefect.minecraft.playerdata;

import java.io.Serializable;

public class PersistentPlayerData implements Serializable {

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

    public void swapCustom() {
        custom ^= true;
    }
}
