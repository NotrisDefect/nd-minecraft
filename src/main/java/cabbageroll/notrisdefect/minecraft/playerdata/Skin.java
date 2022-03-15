package cabbageroll.notrisdefect.minecraft.playerdata;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    private final ItemStack[] blocks;
    private transient boolean bgTransparent;

    public Skin(ItemStack... blocks) {
        if (blocks.length != 17) {
            throw new IllegalArgumentException(blocks.length + " blocks given, 17 required");
        }
        this.blocks = blocks;
    }

    public ItemStack get(int i) {
        return blocks[i];
    }
}
