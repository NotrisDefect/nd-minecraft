package tetrminecraft.functions.versions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Functions {

    void sendActionBarCustom(Player player, String message);

    void sendBlockChangeCustom(Player player, Location loc, Block block);

    void sendBlockChangeCustom(Player player, Location loc, int color);

    void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel);

    void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

}
