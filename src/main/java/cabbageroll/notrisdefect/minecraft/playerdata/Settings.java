package cabbageroll.notrisdefect.minecraft.playerdata;

import java.io.Serializable;

public class Settings implements Serializable {

    private boolean usesCustom = false;
    private Skin customSkin;
    private int ARR;
    private int DAS;
    private int SDF;

    public int getARR() {
        return ARR;
    }

    public void setARR(int ARR) {
        this.ARR = ARR;
    }

    public int getDAS() {
        return DAS;
    }

    public void setDAS(int DAS) {
        this.DAS = DAS;
    }

    public int getSDF() {
        return SDF;
    }

    public void setSDF(int SDF) {
        this.SDF = SDF;
    }

    public Skin getSkin() {
        return customSkin;
    }

    public void setSkin(Skin skin) {
        this.customSkin = skin;
    }

    public boolean isCustomSkinActive() {
        return usesCustom;
    }

    public void toggleCustom() {
        usesCustom ^= true;
    }
}
