package in.cabbageroll.notrisdefect.minecraft;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Sound;

public class Sounds {
    public static final Sound spin = genSpinSound();
    public static final Sound lineClearBig = genClearSpinSound();
    public static final Sound lineClear = genClearSound();
    public static final Sound nuke = genNukeSound();
    public static final Sound pc = genPcSound();

    private static Sound genClearSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_NOTE_BLOCK_HARP
        };
        return parse(xSounds);
    }

    private static Sound genClearSpinSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_NOTE_BLOCK_PLING
        };
        return parse(xSounds);
    }

    private static Sound genSpinSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_END_PORTAL_FRAME_FILL,
            XSound.UI_BUTTON_CLICK
        };
        return parse(xSounds);
    }

    private static Sound genNukeSound() {
        XSound[] xSounds = new XSound[]{
            XSound.ENTITY_GENERIC_EXPLODE
        };
        return parse(xSounds);
    }

    private static Sound genPcSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_ANVIL_LAND
        };
        return parse(xSounds);
    }

    private static Sound parse(XSound[] xSounds) {
        for (XSound xs : xSounds) {
            Sound sound = xs.parseSound();
            if (sound != null) {
                return sound;
            }
        }
        return null;
    }

}
