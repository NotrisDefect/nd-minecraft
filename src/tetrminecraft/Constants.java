package tetrminecraft;

import java.awt.Color;

public class Constants {
    public static int idLength = 3;
    public static String idCharSet = "ABCDEF";
    public static boolean iKnowWhatIAmDoing = true;

    public enum DeathAnimation {
        NONE, EXPLOSION, GRAYSCALE, CLEAR, DISAPPEAR
    }

    public static DeathAnimation deathAnim = DeathAnimation.GRAYSCALE;
    
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
}
