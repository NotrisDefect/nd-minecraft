package cabbageroll.notrisdefect.minecraft.playerdata;

import cabbageroll.notrisdefect.minecraft.Table;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    public static final XMaterial EXISTING = XMaterial.STICK;
    public static final int SIZE = 18;
    private final XMaterial[] blocks;

    public Skin(XMaterial... xms) {
        if (xms.length != SIZE) {
            throw new IllegalArgumentException(xms.length + " blocks given, " + SIZE + " required");
        }
        blocks = xms;
    }

    public XMaterial get(int i) {
        return blocks[i];
    }

    public ItemStack getFancy(int i) {
        XMaterial mat = blocks[i];
        return Menu.createItem(mat == XMaterial.AIR ? XMaterial.STICK : mat, ChatColor.WHITE + Table.intToPieceName(i), mat == EXISTING ? "EXISTING" : mat.name());
    }

    public void set(int index, XMaterial is) {
        blocks[index] = is;
    }
}
