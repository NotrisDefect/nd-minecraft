package cabbageroll.notrisdefect;

import java.awt.Color;

public class Constants {
    public static final int idLength = 3;
    public static final String idCharSet = "ABCDEF";
    public static final boolean iKnowWhatIAmDoing = true;

    public static final String[] NAMES = {
        "blockZ", "blockL", "blockO", "blockS", "blockI", "blockJ", "blockT",
        "background", "garbage",
        "ghostZ", "ghostL", "ghostO", "ghostS", "ghostI", "ghostJ", "ghostT",
        "zone"
    };

    public static final DeathAnimation deathAnim = DeathAnimation.GRAYSCALE;

    public static Color intToColor(int number) {
        switch (number) {
            case 0:
                return Color.RED;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.BLUE;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.BLACK;
            case 8:
                return Color.GRAY;
            case 16:
                return Color.WHITE;
        }
        return null;
    }

    public enum DeathAnimation {
        NONE, EXPLOSION, GRAYSCALE, CLEAR, DISAPPEAR
    }
}
