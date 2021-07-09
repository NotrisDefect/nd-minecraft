package cabbageroll.notrisdefect.functions.versions;

import cabbageroll.notrisdefect.Blocks;
import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.functions.versions.sendblockchangecustom.SendBlockChangeCustom_V2;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_13_R2.EntityFallingBlock;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Functions_1_13_R2 implements Functions {

    @Override
    public void sendActionBarCustom(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        SendBlockChangeCustom_V2.sendBlockChangeCustom(player, loc, block);
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        SendBlockChangeCustom_V2.sendBlockChangeCustom(player, loc, color);
    }

    @Override
    public void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel) {
        ItemStack[] blocks = Main.gs.customBlocks.get(player);

        World worldL = ((CraftWorld) loc.getWorld()).getHandle();
        EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldL);
        entityfallingblock.setLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0, 0);
        entityfallingblock.motX = xVel;
        entityfallingblock.motY = yVel;
        entityfallingblock.motZ = zVel;
        entityfallingblock.velocityChanged = true;

        PacketPlayOutSpawnEntity packet;
        if (Main.gs.playerIsUsingCustomBlocks.get(player)) {
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
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
