package tetrminecraft.functions.versions;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_9_R2.EntityFallingBlock;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tetrminecraft.Blocks;
import tetrminecraft.Main;
import tetrminecraft.functions.versions.sendblockchangecustom.SendBlockChangeCustom_V1;

public class Functions_1_9_R2 implements Functions {

    @Override
    public void sendActionBarCustom(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        SendBlockChangeCustom_V1.sendBlockChangeCustom(player, loc, block);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        SendBlockChangeCustom_V1.sendBlockChangeCustom(player, loc, color);
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

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + "ASDF" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle iTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }
}
