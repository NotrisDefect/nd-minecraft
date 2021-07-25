package cabbageroll.notrisdefect.functions.versions.sendblockchangecustom;

import cabbageroll.notrisdefect.Blocks;
import cabbageroll.notrisdefect.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SendBlockChangeCustom_V1 {

    // used from 1.8 - 1.12.2

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, int color) {
        ItemStack block;
        if (Main.gs.playerIsUsingCustomBlocks.get(player)) {
            block = Main.gs.skins.get(player).get(color);
        } else {
            block = Blocks.defaultBlocks.get(color);
        }

        player.sendBlockChange(loc, block.getType(), block.getData().getData());
    }

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getType(), block.getData());
    }

}
