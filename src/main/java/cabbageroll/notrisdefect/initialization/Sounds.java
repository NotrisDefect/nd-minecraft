package cabbageroll.notrisdefect.initialization;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Sound;

public class Sounds {
    public static final Sound spin;
    public static final Sound lineClearSpin;
    public static final Sound lineClear;

    static {
        spin = genSpinSound();
        lineClearSpin = genClearSpinSound();
        lineClear = genClearSound();
    }

    private static Sound genClearSound() {
        XSound[] xSounds = new XSound[]{
            XSound.ENTITY_FIREWORK_ROCKET_BLAST,
            XSound.BLOCK_NOTE_BLOCK_HARP
        };
        return parse(xSounds);
    }

    private static Sound genClearSpinSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_END_PORTAL_SPAWN,
            XSound.BLOCK_NOTE_BLOCK_PLING
        };
        return parse(xSounds);
    }

    private static Sound genSpinSound() {
        XSound[] xSounds = new XSound[]{
            XSound.BLOCK_END_PORTAL_FRAME_FILL,
            XSound.ENTITY_GENERIC_EXTINGUISH_FIRE,
            XSound.UI_BUTTON_CLICK
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
