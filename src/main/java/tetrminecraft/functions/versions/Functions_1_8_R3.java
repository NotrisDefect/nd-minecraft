package tetrminecraft.functions.versions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import tetrminecraft.functions.versions.sendblockchangecustom.SendBlockChangeCustom_V1;

public class Functions_1_8_R3 implements Functions{

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        IChatBaseComponent cTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent cSubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        
        PacketPlayOutTitle iTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, cTitle);
        PacketPlayOutTitle iSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, cSubtitle);
        
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(iSubtitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
        
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
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}