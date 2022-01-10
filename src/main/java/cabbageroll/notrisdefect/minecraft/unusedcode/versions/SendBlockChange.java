package cabbageroll.notrisdefect.minecraft.unusedcode.versions;

/*
public class SendBlockChange {

    // 1.13+

    public static void sendBlockChange(Player player, Location loc, int color) {
        ItemStack block = colorToBlock(player, color);

        player.sendBlockChange(loc, block.getType().createBlockData());
    }

    public static void sendBlockChange(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getBlockData());
    }

    // 1.8 - 1.12.2

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeLegacy(Player player, Location loc, int color) {
        ItemStack block = colorToBlock(player, color);

        player.sendBlockChange(loc, block.getType(), block.getData().getData());
    }

    @SuppressWarnings("deprecation")
    public static void sendBlockChangeLegacy(Player player, Location loc, Block block) {
        player.sendBlockChange(loc, block.getType(), block.getData());
    }

    private static ItemStack colorToBlock(Player player, int color) {
        if (Main.gs.getData(player).isCustom()) {
            return Main.gs.getSkin(player).get(color);
        } else {
            return Blocks.defaultBlocks.get(color);
        }
    }

}
*/