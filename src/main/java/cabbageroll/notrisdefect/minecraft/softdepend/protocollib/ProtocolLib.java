package cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ProtocolLib {

    static ItemStack colorToBlock(Player player, int color) {
        if (Main.gs.getData(player).isCustomSkinActive()) {
            return Main.gs.getSkin(player).get(color).parseItem();
        } else {
            return Skin.DEFAULTSKIN.get(color).parseItem();
        }
    }

    static void sendBlockChange(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getBlockData());
    }

    // 1.13+

    static void sendBlockChange(Player player, Location loc, int color) {
        ItemStack block = colorToBlock(player, color);

        player.sendBlockChange(loc, block.getType().createBlockData());
    }

    // 1.8 - 1.12.2

    @SuppressWarnings("deprecation")
    static void sendBlockChangeLegacy(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getType(), block.getData());
    }

    @SuppressWarnings("deprecation")
    static void sendBlockChangeLegacy(Player player, Location loc, int color) {
        ItemStack block = colorToBlock(player, color);

        player.sendBlockChange(loc, block.getType(), block.getData().getData());
    }

    void sendActionBarCustom(Player player, String message);

    void sendBlockChangeCustom(Player player, Location loc, Block block);

    void sendBlockChangeCustom(Player player, Location loc, int color);

    void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel);

    void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

}
