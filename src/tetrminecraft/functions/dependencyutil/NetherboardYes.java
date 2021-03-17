package tetrminecraft.functions.dependencyutil;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;

public class NetherboardYes implements Netherboard {

    private static Map<Player, BPlayerBoard> boards = new HashMap<Player, BPlayerBoard>();

    @Override
    public void sendScoreboard(Player player, Map<Integer, String> text) {
        for (Map.Entry<Integer, String> entry : text.entrySet()) {
            if (entry.getValue() == null) {
                boards.get(player).remove(entry.getKey());
            } else if (entry.getValue().equals("")) {
                String empty = "";
                for (int i = 0; i < entry.getKey(); i++) {
                    empty += " ";
                }
                boards.get(player).set(empty, entry.getKey());
            } else {
                boards.get(player).set(entry.getValue(), entry.getKey());
            }
        }
    }

    @Override
    public void createBoard(Player player, String name) {
        boards.put(player, fr.minuskube.netherboard.Netherboard.instance().createBoard(player, "Stats"));
    }

    @Override
    public void removeBoard(Player player) {
        boards.get(player).delete();
    }
}
