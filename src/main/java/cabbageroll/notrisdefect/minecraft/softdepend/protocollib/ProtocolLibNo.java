package cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import cabbageroll.notrisdefect.minecraft.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ProtocolLibNo implements ProtocolLib {

    private final int version = Main.plugin.numericalVersion;

    @Override
    public void sendActionBarCustom(Player player, String message) {
        if (version > 11) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, Block block) {
        if (version < 13) {
            ProtocolLib.sendBlockChangeLegacy(player, loc, block);
        } else {
            ProtocolLib.sendBlockChange(player, loc, block);
        }
    }

    @Override
    public void sendBlockChangeCustom(Player player, Location loc, int color) {
        if (version < 13) {
            ProtocolLib.sendBlockChangeLegacy(player, loc, color);
        } else {
            ProtocolLib.sendBlockChange(player, loc, color);
        }
    }

    @Override
    public void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel) {

    }

    @Override
    public void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (version > 11) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

}