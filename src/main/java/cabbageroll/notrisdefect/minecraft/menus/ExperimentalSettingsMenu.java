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
        createInventory(this, 54, "CLOCK?mclock");
        Table table = Main.gs.getTable(player);

        for (int i = 0; i < 9; i++) {
            addButton(grid(1, i + 1), empty);
            addButton(grid(6, i + 1), empty);
        }

        addButton(BACK_LOCATION, event -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(2, 5), event -> Main.gs.getRoom(player).toggleBackfire(), XMaterial.FLINT_AND_STEEL, "backfire", "" + Main.gs.getRoom(player).getBackfire());
        addButton(grid(2, 6), event -> table.enableAnimations ^= true, XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks", "" + table.enableAnimations);
        addButton(grid(2, 9), event -> table.setSTAGESIZEX(table.getSTAGESIZEX() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEX", "" + table.getSTAGESIZEX());

        addButton(grid(3, 4), event -> table.moveHoldTLCXRelative(howMuch(event.getClick())), XMaterial.RED_STAINED_GLASS, "Hold top left corner X", "" + table.getHoldTLCX());
        addButton(grid(3, 5), event -> table.moveHoldTLCYRelative(howMuch(event.getClick())), XMaterial.GREEN_STAINED_GLASS, "Hold top left corner Y", "" + table.getHoldTLCY());
        addButton(grid(3, 9), event -> table.setSTAGESIZEY(table.getSTAGESIZEY() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEY", "" + table.getSTAGESIZEY());

        addButton(grid(4, 4), event -> table.moveNextTLCXRelative(howMuch(event.getClick())), XMaterial.RED_STAINED_GLASS, "Next top left corner X", "" + table.getNextTLCX());
        addButton(grid(4, 5), event -> table.moveNextTLCYRelative(howMuch(event.getClick())), XMaterial.GREEN_STAINED_GLASS, "Next top left corner Y", "" + table.getNextTLCY());
        addButton(grid(4, 8), event -> table.setDelays(0, 0), XMaterial.GOLD_BLOCK, ChatColor.WHITE + "Disable delays");
        addButton(grid(4, 9), event -> table.setNEXTPIECES(table.getNEXTPIECES() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTPIECESMAX", "" + table.getNEXTPIECES());

        addButton(grid(5, 4), event -> table.moveGarbBLCXRelative(howMuch(event.getClick())), XMaterial.RED_STAINED_GLASS, "Garbage bottom left corner X", "" + table.getGarbBLCX());
        addButton(grid(5, 5), event -> table.moveGarbBLCYRelative(howMuch(event.getClick())), XMaterial.GREEN_STAINED_GLASS, "Garbage bottom left corner Y", "" + table.getGarbBLCY());
        addButton(grid(5, 8), event -> table.setDelays(100, 500), XMaterial.GOLD_BLOCK, ChatColor.WHITE + "Enable delays");
        addButton(grid(5, 9), event -> table.setNEXTVERTICAL(table.getNEXTVERTICAL() + howMuch(event.getClick())), XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTVERTICAL", "" + table.getNEXTVERTICAL());

    }
}
