package tetrminecraft.functions.softdepend;

import java.util.Map;

import org.bukkit.entity.Player;

public interface Netherboard {
    
    void sendScoreboard(Player player, Map<Integer, String> text);
    
    void createBoard(Player player, String name);
    
    void removeBoard(Player player);
}
