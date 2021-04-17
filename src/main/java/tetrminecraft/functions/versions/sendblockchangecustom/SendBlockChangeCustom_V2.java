package tetrminecraft.functions.versions.sendblockchangecustom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tetrminecraft.Blocks;
import tetrminecraft.Main;

public class SendBlockChangeCustom_V2 {

    // used after 1.13

    public static void sendBlockChangeCustom(Player player, Location loc, int color) {
        ItemStack[] blocks = Main.instance.customBlocks.get(player);

        if (Main.instance.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, blocks[color].getType().createBlockData());
        } else if (!Main.instance.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, Blocks.defaultBlocks[color].getType().createBlockData());
        }
    }

    public static void sendBlockChangeCustom(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getBlockData());
    }

}
