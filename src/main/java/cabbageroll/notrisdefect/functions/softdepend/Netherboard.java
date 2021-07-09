package cabbageroll.notrisdefect.functions.softdepend;

import org.bukkit.entity.Player;

import java.util.Map;

public interface Netherboard {

    void createBoard(Player player, String name);

    void removeBoard(Player player);

    void sendScoreboard(Player player, Map<Integer, String> text);
}
