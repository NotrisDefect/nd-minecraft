package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RoomSettingsMenu extends Menu {

    public RoomSettingsMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION) {
            new RoomSettingsMenu((Player) event.getWhoClicked());
        }
    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "Room settings");
        Room room = Main.gs.getRoom(player);

        for (int i = 0; i < 9; i++) {
            addButton(grid(1, i + 1), empty);
            addButton(grid(6, i + 1), empty);
        }

        addButton(BACK_LOCATION, event -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(2, 1), event -> room.players.forEach(player -> Main.gs.getTable(player).setDelays(100, 500)), XMaterial.GOLD_BLOCK, ChatColor.WHITE + "Enable delays");
        addButton(grid(2, 2), event -> room.players.forEach(player -> Main.gs.getTable(player).setDelays(0, 0)), XMaterial.GOLD_BLOCK, ChatColor.WHITE + "Disable delays");
    }

}
