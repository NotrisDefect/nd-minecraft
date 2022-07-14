package cabbageroll.notrisdefect.minecraft.playerdata;

import cabbageroll.notrisdefect.minecraft.Table;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Skin implements Serializable {
    public static final Skin DEFAULTSKIN = new Skin(
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
        XMaterial.PURPLE_STAINED_GLASS,
        XMaterial.REDSTONE_BLOCK
    );

    public static final Skin ZONE_P1 = new Skin(
        XMaterial.COAL_BLOCK,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.GLOWSTONE,
        XMaterial.SEA_LANTERN,
        XMaterial.QUARTZ_BLOCK,
        XMaterial.TNT,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.YELLOW_STAINED_GLASS,
        XMaterial.SEA_LANTERN
    );

    public static final Skin ZONE_P2 = new Skin(
        XMaterial.COAL_BLOCK,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.SEA_LANTERN,
        XMaterial.GLOWSTONE,
        XMaterial.QUARTZ_BLOCK,
        XMaterial.TNT,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.LIGHT_BLUE_STAINED_GLASS,
        XMaterial.GLOWSTONE
    );

    public static final XMaterial[] EMPTY = new XMaterial[]{
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
        XMaterial.PURPLE_STAINED_GLASS,
        XMaterial.REDSTONE_BLOCK
    };

    public static final XMaterial EXISTING = XMaterial.STICK;
    public static final int SIZE = 19;
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
        return Menu.createItem(mat == XMaterial.AIR ? XMaterial.STICK : mat, ChatColor.WHITE + Table.pieceIntToString(i), mat == EXISTING ? "EXISTING" : mat.name());
    }

    public void set(int index, XMaterial is) {
        blocks[index] = is;
    }
}
