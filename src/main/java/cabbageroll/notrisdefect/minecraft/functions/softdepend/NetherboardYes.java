package cabbageroll.notrisdefect.minecraft.functions.softdepend;

import org.bukkit.entity.Player;

import java.util.Map;

public class NetherboardYes implements Netherboard {

    @Override
    public void createBoard(Player player, String name) {
        fr.minuskube.netherboard.Netherboard.instance().createBoard(player, "Stats");
    }

    @Override
    public void removeBoard(Player player) {
        if (fr.minuskube.netherboard.Netherboard.instance().hasBoard(player)) {
            fr.minuskube.netherboard.Netherboard.instance().getBoard(player).delete();
        }
    }

    @Override
    public void sendScoreboard(Player player, Map<Integer, String> text) {
        for (Map.Entry<Integer, String> entry : text.entrySet()) {
            if (entry.getValue() == null) {
                fr.minuskube.netherboard.Netherboard.instance().getBoard(player).remove(entry.getKey());
            } else if (entry.getValue().equals("")) {
                StringBuilder empty = new StringBuilder();
                for (int i = 0; i < entry.getKey(); i++) {
                    empty.append(" ");
                }
                fr.minuskube.netherboard.Netherboard.instance().getBoard(player).set(empty.toString(), entry.getKey());
            } else {
                fr.minuskube.netherboard.Netherboard.instance().getBoard(player).set(entry.getValue(), entry.getKey());
            }
        }
    }
}
