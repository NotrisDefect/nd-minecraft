package cabbageroll.notrisdefect;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    private final ItemStack[] minos = new ItemStack[7];
    private final ItemStack[] ghosts = new ItemStack[7];
    private final ItemStack garbage;
    private final ItemStack background;
    private final ItemStack zone;
    private transient boolean bgTransparent;

    public Skin(ItemStack... blocks) {
        if (blocks.length != 17) {
            throw new IllegalArgumentException("17 arguments needed! " + blocks.length + " were given.");
        }
        for (int i = 0; i < 7; i++) {
            minos[i] = blocks[i];
        }
        background = blocks[7];
        garbage = blocks[8];
        for (int i = 0; i < 7; i++) {
            ghosts[i] = blocks[9 + i];
        }
        zone = blocks[16];
    }

    public ItemStack get(int i) {
        switch (i) {
            case 0:
                return minos[0];
            case 1:
                return minos[1];
            case 2:
                return minos[2];
            case 3:
                return minos[3];
            case 4:
                return minos[4];
            case 5:
                return minos[5];
            case 6:
                return minos[6];
            case 7:
                return background;
            case 8:
                return garbage;
            case 9:
                return ghosts[0];
            case 10:
                return ghosts[1];
            case 11:
                return ghosts[2];
            case 12:
                return ghosts[3];
            case 13:
                return ghosts[4];
            case 14:
                return ghosts[5];
            case 15:
                return ghosts[6];
            case 16:
                return zone;
        }
        throw new IllegalArgumentException("" + i);
    }
}
