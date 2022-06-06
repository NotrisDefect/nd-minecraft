package cabbageroll.notrisdefect.minecraft.playerdata;

import cabbageroll.notrisdefect.minecraft.Table;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    public static final Skin defaultSkin = new Skin(
        XMaterial.BLACK_WOOL,
        XMaterial.RED_WOOL,
        XMaterial.ORANGE_WOOL,
        XMaterial.YELLOW_WOOL,
        XMaterial.LIME_WOOL,
        XMaterial.LIGHT_BLUE_WOOL,
        XMaterial.BLUE_WOOL,
        XMaterial.PURPLE_WOOL,
        XMaterial.LIGHT_GRAY_WOOL,
        XMaterial.WHITE_WOOL,
        XMaterial.TNT,
        XMaterial.RED_STAINED_GLASS,
        XMaterial.ORANGE_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.LIME_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.BLUE_STAINED_GLASS,
        XMaterial.PURPLE_STAINED_GLASS
    );

    public static final XMaterial[] empty = new XMaterial[]{
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR,
        XMaterial.AIR
    };

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
