package cabbageroll.notrisdefect.functions.versions;

import cabbageroll.notrisdefect.Blocks;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_9_R1.EntityFallingBlock;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.functions.versions.sendblockchangecustom.SendBlockChangeCustom_V1;

@SuppressWarnings("ALL")
public class Functions_1_9_R1 implements Functions {

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
        ItemStack block;
        if (Main.gs.playerIsUsingCustomBlocks.get(player)) {
            block = Main.gs.skins.get(player).get(color);
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
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + "ASDF" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle iTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

}
