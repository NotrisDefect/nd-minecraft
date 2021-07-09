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
        ItemStack[] blocks = Main.gs.customBlocks.get(player);

        if (Main.gs.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, blocks[color].getType(), blocks[color].getData().getData());
        } else if (!Main.gs.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, Blocks.defaultBlocks[color].getType(),
                Blocks.defaultBlocks[color].getData().getData());
        }
    }

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getType(), block.getData());
    }

}
