package in.cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import in.cabbageroll.notrisdefect.minecraft.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProtocolLibNo implements ProtocolLib {

    private final int version = Main.PLUGIN.VERSION;

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
