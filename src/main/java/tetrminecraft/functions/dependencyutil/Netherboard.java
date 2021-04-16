package tetrminecraft.functions.dependencyutil;

import java.util.Map;

import org.bukkit.entity.Player;

public interface Netherboard {
    
    public void sendScoreboard(Player player, Map<Integer, String> text);
    
    public void createBoard(Player player, String name);
    
    public void removeBoard(Player player);
}
