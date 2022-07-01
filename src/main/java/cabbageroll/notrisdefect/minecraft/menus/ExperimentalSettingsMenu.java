package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ExperimentalSettingsMenu extends Menu {

    public ExperimentalSettingsMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION) {
            new ExperimentalSettingsMenu((Player) event.getWhoClicked());
        }
    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "Experimental");
        Table table = Main.gs.getTable(player);

        for (int i = 0; i < 9; i++) {
            addButton(grid(1, i + 1), empty);
            addButton(grid(6, i + 1), empty);
        }

        addButton(BACK_LOCATION, event -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(2, 9), event -> table.setSTAGESIZEX(table.getSTAGESIZEX() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEX", "" + table.getSTAGESIZEX());
        addButton(grid(3, 9), event -> table.setSTAGESIZEY(table.getSTAGESIZEY() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEY", "" + table.getSTAGESIZEY());
        addButton(grid(4, 9), event -> table.setNEXTPIECES(table.getNEXTPIECES() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTPIECESMAX", "" + table.getNEXTPIECES());

        addButton(grid(2, 5), event -> Main.gs.getRoom(player).toggleBackfire(), XMaterial.FLINT_AND_STEEL, "backfire", "" + Main.gs.getRoom(player).getBackfire());
        addButton(grid(2, 6), event -> table.enableAnimations ^= true, XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks", "" + table.enableAnimations);
        addButton(grid(2, 7), event -> table.setZONEENABLED(!table.isZONEENABLED()), XMaterial.ENCHANTED_BOOK, ChatColor.WHITE + "Enable zone", "" + table.isZONEENABLED());
        addButton(grid(2,8), event -> table.lumines(), XMaterial.GLOWSTONE_DUST, "LUMINES");
    }


}
