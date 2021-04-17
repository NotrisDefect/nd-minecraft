package tetrminecraft.functions.versions.sendblockchangecustom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tetrminecraft.Blocks;
import tetrminecraft.Main;

public class SendBlockChangeCustom_V1 {

    // used from 1.8 - 1.12.2

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, int color) {
        ItemStack[] blocks = Main.instance.customBlocks.get(player);

        if (Main.instance.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, blocks[color].getType(), blocks[color].getData().getData());
        } else if (!Main.instance.playerIsUsingCustomBlocks.get(player)) {
            player.sendBlockChange(loc, Blocks.defaultBlocks[color].getType(),
                    Blocks.defaultBlocks[color].getData().getData());
        }
    }

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeCustom(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getType(), block.getData());
    }

}
