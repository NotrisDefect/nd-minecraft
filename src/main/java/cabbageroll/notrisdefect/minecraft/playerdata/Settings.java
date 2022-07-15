package cabbageroll.notrisdefect.minecraft.playerdata;

import com.cryptomorin.xseries.XMaterial;

import java.io.Serializable;
import java.util.HashMap;

public class Settings implements Serializable {

    private static final long serialVersionUID = 132378826677569901L;

    private boolean custom = false;
    private HashMap<Integer, XMaterial> skin;
    private int ARR;
    private int DAS;
    private int SDF;

    public XMaterial get(int i) {
        return skin.get(i);
    }

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

    public HashMap<Integer, XMaterial> getSkin() {
        return skin;
    }

    public void setSkin(HashMap<Integer, XMaterial> skin) {
        this.skin = skin;
    }

    public boolean isCustomSkinActive() {
        return custom;
    }

    public void setCustom(boolean value) {
        custom = value;
    }

    public void toggleCustom() {
        custom ^= true;
    }
}
