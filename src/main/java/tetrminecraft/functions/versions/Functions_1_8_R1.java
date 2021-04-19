package tetrminecraft.functions.versions;

import net.minecraft.server.v1_8_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tetrminecraft.Blocks;
import tetrminecraft.Main;
import tetrminecraft.functions.versions.sendblockchangecustom.SendBlockChangeCustom_V1;

public class Functions_1_8_R1 implements Functions {

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent cTitle = ChatSerializer.a("{\"text\": \"" + title + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");
        IChatBaseComponent cSubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle iTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, cTitle);
        PacketPlayOutTitle iSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, cSubtitle);

        PacketPlayOutTitle titleLength = new PacketPlayOutTitle(5, 20, 5);
        PacketPlayOutTitle subtitleLength = new PacketPlayOutTitle(10, 30, 10);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titleLength);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iSubtitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitleLength);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        SendBlockChangeCustom_V1.sendBlockChangeCustom(player, loc, color);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        SendBlockChangeCustom_V1.sendBlockChangeCustom(player, loc, block);
    }

    @Override
    public void sendActionBarCustom(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel) {
        ItemStack[] blocks = Main.instance.customBlocks.get(player);

        World worldL = ((CraftWorld) loc.getWorld()).getHandle();
        EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldL);
        entityfallingblock.setLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0, 0);
        entityfallingblock.motX = xVel;
        entityfallingblock.motY = yVel;
        entityfallingblock.motZ = zVel;
        entityfallingblock.velocityChanged = true;

        PacketPlayOutSpawnEntity packet;
        if (Main.instance.playerIsUsingCustomBlocks.get(player)) {
            packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, blocks[color].getType().getId() + (blocks[color].getData().getData() << 12));

        } else {
            packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, Blocks.defaultBlocks[color].getType().getId() + (Blocks.defaultBlocks[color].getData().getData() << 12));
        }

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        PacketPlayOutEntityVelocity vpacket = new PacketPlayOutEntityVelocity(entityfallingblock.getId(), xVel, yVel, zVel);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(vpacket);
    }
}
