package in.cabbageroll.notrisdefect.minecraft.playerdata;

import com.cryptomorin.xseries.XMaterial;

import java.util.HashMap;

public class BuiltInSkins {
    public static final HashMap<Integer, XMaterial> GLAZED_TERRACOTTA = toHashMap(
        XMaterial.BLACK_GLAZED_TERRACOTTA,
        XMaterial.RED_GLAZED_TERRACOTTA,
        XMaterial.ORANGE_GLAZED_TERRACOTTA,
        XMaterial.YELLOW_GLAZED_TERRACOTTA,
        XMaterial.LIME_GLAZED_TERRACOTTA,
        XMaterial.LIGHT_BLUE_GLAZED_TERRACOTTA,
        XMaterial.BLUE_GLAZED_TERRACOTTA,
        XMaterial.PURPLE_GLAZED_TERRACOTTA,
        XMaterial.LIGHT_GRAY_GLAZED_TERRACOTTA,
        XMaterial.WHITE_GLAZED_TERRACOTTA,
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

    public static final HashMap<Integer, XMaterial> TERRACOTTA = toHashMap(
        XMaterial.BLACK_TERRACOTTA,
        XMaterial.RED_TERRACOTTA,
        XMaterial.ORANGE_TERRACOTTA,
        XMaterial.YELLOW_TERRACOTTA,
        XMaterial.LIME_TERRACOTTA,
        XMaterial.LIGHT_BLUE_TERRACOTTA,
        XMaterial.BLUE_TERRACOTTA,
        XMaterial.PURPLE_TERRACOTTA,
        XMaterial.LIGHT_GRAY_TERRACOTTA,
        XMaterial.WHITE_TERRACOTTA,
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

    public static final HashMap<Integer, XMaterial> CONCRETE = toHashMap(
        XMaterial.BLACK_CONCRETE,
        XMaterial.RED_CONCRETE,
        XMaterial.ORANGE_CONCRETE,
        XMaterial.YELLOW_CONCRETE,
        XMaterial.LIME_CONCRETE,
        XMaterial.LIGHT_BLUE_CONCRETE,
        XMaterial.BLUE_CONCRETE,
        XMaterial.PURPLE_CONCRETE,
        XMaterial.LIGHT_GRAY_CONCRETE,
        XMaterial.WHITE_CONCRETE,
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

    public static final HashMap<Integer, XMaterial> WOOL = toHashMap(
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

    public static final HashMap<Integer, XMaterial> ZONE_PLAYER1 = toHashMap(
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

    public static final HashMap<Integer, XMaterial> ZONE_PLAYER2 = toHashMap(
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

    public static final HashMap<Integer, XMaterial> DEFAULTSKIN = WOOL;

    private static HashMap<Integer, XMaterial> toHashMap(XMaterial... mats) {
        HashMap<Integer, XMaterial> map = new HashMap<>();
        for (int i = 0; i < mats.length; i++) {
            map.put(i, mats[i]);
        }
        return map;
    }
}
