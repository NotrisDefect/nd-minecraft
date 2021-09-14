package cabbageroll.notrisdefect.minecraft.functions.versions.sendblockchangecustom;

import cabbageroll.notrisdefect.minecraft.Blocks;
import cabbageroll.notrisdefect.minecraft.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SendBlockChangeCustom_V2 {

    // used after 1.13

    public static void sendBlockChangeCustom(Player player, Location loc, int color) {
        ItemStack block;
        if (Main.gs.getData(player).isCustom()) {
            block = Main.gs.getSkin(player).get(color);
        } else {
            block = Blocks.defaultBlocks.get(color);
        }

        player.sendBlockChange(loc, block.getType().createBlockData());
    }

    public static void sendBlockChangeCustom(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getBlockData());
    }

}
