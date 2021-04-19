package tetrminecraft.functions.versions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Functions {

    void sendTitleCustom(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    
    void sendBlockChangeCustom(Player player, Location loc, int color);
    
    void sendBlockChangeCustom(Player player, Location loc, Block block);
    
    void sendActionBarCustom(Player player, String message);

    void sendFallingBlockCustom(Player player, Location loc, int color, double xVel, double yVel, double zVel);

}
