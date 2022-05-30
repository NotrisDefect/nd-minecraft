package cabbageroll.notrisdefect.minecraft.playerdata;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    public static final int SIZE = 18;
    private final ItemStack[] blocks;
    private transient boolean bgTransparent;

    public Skin(ItemStack... blocks) {
        if (blocks.length != SIZE) {
            throw new IllegalArgumentException(blocks.length + " blocks given, " + SIZE + " required");
        }
        this.blocks = blocks;
    }

    public ItemStack get(int i) {
        return blocks[i];
    }
}
