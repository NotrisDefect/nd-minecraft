package cabbageroll.notrisdefect.minecraft;

import java.awt.Color;

public class Constants {
    public static final DeathAnimation deathAnim = DeathAnimation.GRAYSCALE;
    public static final Color[] colors = new Color[]{
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.MAGENTA,
        Color.BLACK,
        Color.GRAY,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        Color.WHITE
    };

    public enum DeathAnimation {
        NONE, EXPLOSION, GRAYSCALE, CLEAR, DISAPPEAR
    }
}
