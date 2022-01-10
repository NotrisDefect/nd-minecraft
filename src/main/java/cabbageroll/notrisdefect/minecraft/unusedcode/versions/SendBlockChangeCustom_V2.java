package cabbageroll.notrisdefect.minecraft.unusedcode.versions;

/*
public class SendBlockChangeCustom_V2 {

    // used after 1.13

    public static void sendBlockChange(Player player, Location loc, int color) {
        ItemStack block;
        if (Main.gs.getData(player).isCustom()) {
            block = Main.gs.getSkin(player).get(color);
        } else {
            block = Blocks.defaultBlocks.get(color);
        }

        player.sendBlockChange(loc, block.getType().createBlockData());
    }

    public static void sendBlockChange(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getBlockData());
    }

}
*/