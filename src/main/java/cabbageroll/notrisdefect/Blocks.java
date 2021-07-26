package cabbageroll.notrisdefect;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

public class Blocks {
    public static final Skin defaultBlocks = new Skin(
        XMaterial.RED_WOOL.parseItem(),
        XMaterial.ORANGE_WOOL.parseItem(),
        XMaterial.YELLOW_WOOL.parseItem(),
        XMaterial.LIME_WOOL.parseItem(),
        XMaterial.LIGHT_BLUE_WOOL.parseItem(),
        XMaterial.BLUE_WOOL.parseItem(),
        XMaterial.PURPLE_WOOL.parseItem(),
        XMaterial.BLACK_WOOL.parseItem(),
        XMaterial.LIGHT_GRAY_WOOL.parseItem(),
        XMaterial.RED_STAINED_GLASS.parseItem(),
        XMaterial.ORANGE_STAINED_GLASS.parseItem(),
        XMaterial.YELLOW_STAINED_GLASS.parseItem(),
        XMaterial.LIME_STAINED_GLASS.parseItem(),
        XMaterial.LIGHT_BLUE_STAINED_GLASS.parseItem(),
        XMaterial.BLUE_STAINED_GLASS.parseItem(),
        XMaterial.PURPLE_STAINED_GLASS.parseItem(),
        XMaterial.WHITE_WOOL.parseItem()
    );

    public static final ItemStack[] empty = new ItemStack[]{
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem(),
        XMaterial.AIR.parseItem()
    };
}
