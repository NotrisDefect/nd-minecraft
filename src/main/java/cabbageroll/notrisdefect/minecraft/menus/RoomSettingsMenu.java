package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Room;
import cabbageroll.notrisdefect.minecraft.Table;
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
            addButton(grid(i + 1, 1), empty);
            addButton(grid(i + 1, 6), empty);
        }

        addButton(BACK_LOCATION, event -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(1, 2), event -> room.players.forEach(player -> {
            getTable(player).setPIECESPAWNDELAY(getTable(player).getPIECESPAWNDELAY() == 0 ? 100 : 0);
            getTable(player).setLINECLEARDELAY(getTable(player).getLINECLEARDELAY() == 0 ? 500 : 0);
        }), XMaterial.GOLD_BLOCK, ChatColor.WHITE + "Delays", "Piece spawn delay: " + getTable(player).getPIECESPAWNDELAY() + "ms", "Line clear delay: " + getTable(player).getLINECLEARDELAY() + "ms");
        addButton(grid(2, 2), event -> room.players.forEach(player -> getTable(player).setENABLENUKES(!getTable(player).isENABLENUKES())), XMaterial.TNT, ChatColor.WHITE + "Garbage type", getTable(player).isENABLENUKES() ? "Nukes" : "Holes");
        addButton(grid(3, 2), event -> room.players.forEach(player -> getTable(player).setENABLEALWAYSGARBAGE(!getTable(player).isENABLEALWAYSGARBAGE())), XMaterial.TNT, ChatColor.WHITE + "Garbage entry", getTable(player).isENABLEALWAYSGARBAGE() ? "Always" : "No lines cleared");
        addButton(grid(4, 2), event -> room.players.forEach(player -> getTable(player).setENABLEALLSPIN(!getTable(player).isENABLEALLSPIN())), XMaterial.TNT, ChatColor.WHITE + "Spin type", getTable(player).isENABLEALLSPIN() ? "Z, L, S, J, T spins" : "T spins");
    }

    private Table getTable(Player player) {
        return Main.gs.getTable(player);
    }

}
