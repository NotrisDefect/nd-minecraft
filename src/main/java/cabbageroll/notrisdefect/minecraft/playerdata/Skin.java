package cabbageroll.notrisdefect.minecraft.playerdata;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    private final ItemStack[] blocks;
    private transient boolean bgTransparent;

    public Skin(ItemStack... blocks) {
        if (blocks.length != 17) {
            throw new IllegalArgumentException("i need 17, i got " + blocks.length);
        }
        this.blocks = blocks;
    }

    public ItemStack get(int i) {
        return blocks[i];
    }
}
