package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SettingsMenu extends Menu {

    public SettingsMenu(Player player) {
        createInventory(this, 54, "Settings");
        Table table = Main.gs.getTable(player);

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), event -> new RoomMenu(player)));
        buttons.put(grid(2, 1), new Button(createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "" + table.getX()), event -> table.moveTableRelative(howMuch(event.getClick()), 0, 0)));
        buttons.put(grid(2, 2), new Button(createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "" + table.getY()), event -> table.moveTableRelative(0, howMuch(event.getClick()), 0)));
        buttons.put(grid(2, 3), new Button(createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "" + table.getZ()), event -> table.moveTableRelative(0, 0, howMuch(event.getClick()))));
        buttons.put(grid(2, 4), new Button(createItem(XMaterial.COMPASS, "reposition"), event -> table.reposition()));
        buttons.put(grid(2, 5), new Button(createItem(XMaterial.FLINT_AND_STEEL, "backfire", "" + Main.gs.getRoom(player).getBackfire()), event -> Main.gs.getRoom(player).toggleBackfire()));
        buttons.put(grid(2, 6), new Button(createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks", "" + table.enableAnimations), event -> table.enableAnimations ^= true));
        buttons.put(grid(2, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEX", "" + table.getSTAGESIZEX()), event -> table.setSTAGESIZEX(table.getSTAGESIZEX() + howMuch(event.getClick()))));
        buttons.put(grid(3, 1), new Button(createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X"), event -> table.rotateX()));
        buttons.put(grid(3, 2), new Button(createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y"), event -> table.rotateY()));
        buttons.put(grid(3, 3), new Button(createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z"), event -> table.rotateZ()));
        buttons.put(grid(3, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Hold top left corner X", "" + table.getHoldTLCX()), event -> table.moveHoldTLCXRelative(howMuch(event.getClick()))));
        buttons.put(grid(3, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Hold top left corner Y", "" + table.getHoldTLCY()), event -> table.moveHoldTLCYRelative(howMuch(event.getClick()))));
        buttons.put(grid(3, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEY", "" + table.getSTAGESIZEY()), event -> table.setSTAGESIZEY(table.getSTAGESIZEY() + howMuch(event.getClick()))));
        buttons.put(grid(4, 1), new Button(createItem(XMaterial.RED_BANNER, "Width X multiplier", "" + table.getWMX()), event -> table.skewWX(howMuch(event.getClick()))));
        buttons.put(grid(4, 2), new Button(createItem(XMaterial.GREEN_BANNER, "Width Y multiplier", "" + table.getWMY()), event -> table.skewWY(howMuch(event.getClick()))));
        buttons.put(grid(4, 3), new Button(createItem(XMaterial.BLUE_BANNER, "Width Z multiplier", "" + table.getWMZ()), event -> table.skewWZ(howMuch(event.getClick()))));
        buttons.put(grid(4, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Next top left corner X", "" + table.getNextTLCX()), event -> table.moveNextTLCXRelative(howMuch(event.getClick()))));
        buttons.put(grid(4, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Next top left corner Y", "" + table.getNextTLCY()), event -> table.moveNextTLCYRelative(howMuch(event.getClick()))));
        buttons.put(grid(4, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTPIECESMAX", "" + table.getNEXTPIECESMAX()), event -> table.setNEXTPIECESMAX(table.getNEXTPIECESMAX() + howMuch(event.getClick()))));
        buttons.put(grid(5, 1), new Button(createItem(XMaterial.RED_BANNER, "Height X multiplier", "" + table.getHMX()), event -> table.skewHX(howMuch(event.getClick()))));
        buttons.put(grid(5, 2), new Button(createItem(XMaterial.GREEN_BANNER, "Height Y multiplier", "" + table.getHMY()), event -> table.skewHY(howMuch(event.getClick()))));
        buttons.put(grid(5, 3), new Button(createItem(XMaterial.BLUE_BANNER, "Height Z multiplier", "" + table.getHMZ()), event -> table.skewHZ(howMuch(event.getClick()))));
        buttons.put(grid(5, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Garbage bottom left corner X", "" + table.getGarbBLCX()), event -> table.moveGarbBLCXRelative(howMuch(event.getClick()))));
        buttons.put(grid(5, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Garbage bottom left corner Y", "" + table.getGarbBLCY()), event -> table.moveGarbBLCYRelative(howMuch(event.getClick()))));
        buttons.put(grid(5, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTVERTICAL", "" + table.getNEXTVERTICAL()), event -> table.setNEXTVERTICAL(table.getNEXTVERTICAL() + howMuch(event.getClick()))));

        open(player);
    }


    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION) {
            new SettingsMenu((Player) event.getWhoClicked());
        }
    }
}
