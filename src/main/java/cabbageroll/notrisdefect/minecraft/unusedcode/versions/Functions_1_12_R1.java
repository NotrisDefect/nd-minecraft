package cabbageroll.notrisdefect.minecraft.unusedcode.versions;

/*
@SuppressWarnings("ALL")
public class Functions_1_12_R1 implements Functions {

    @Override
    public void sendActionBarCustom(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        SendBlockChange.sendBlockChangeLegacy(player, loc, block);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        SendBlockChange.sendBlockChangeLegacy(player, loc, color);
    }

    @Override
    public void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel) {
        ItemStack block;
        if (Main.gs.getData(player).isCustom()) {
            block = Main.gs.getSkin(player).get(color);
        } else {
            block = Blocks.defaultBlocks.get(color);
        }

        World worldL = ((CraftWorld) loc.getWorld()).getHandle();
        EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldL);
        entityfallingblock.setLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0, 0);
        entityfallingblock.motX = xVel;
        entityfallingblock.motY = yVel;
        entityfallingblock.motZ = zVel;
        entityfallingblock.velocityChanged = true;

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, block.getType().getId() + (block.getData().getData() << 12));

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        PacketPlayOutEntityVelocity vpacket = new PacketPlayOutEntityVelocity(entityfallingblock.getId(), xVel, yVel, zVel);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(vpacket);
    }

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

}
*/