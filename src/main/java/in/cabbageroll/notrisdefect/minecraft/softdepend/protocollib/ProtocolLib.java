package in.cabbageroll.notrisdefect.minecraft.softdepend.protocollib;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtocolLib {

    void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel);

    void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

}
