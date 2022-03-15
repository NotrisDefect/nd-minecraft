package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PersonalSettingsMenu extends Menu {

    public PersonalSettingsMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() != BACK_LOCATION) {
            new PersonalSettingsMenu((Player) event.getWhoClicked());
        }
    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "My settings");
        Table table = Main.gs.getTable(player);

        for (int i = 0; i < 9; i++) {
            addButton(grid(1, i + 1), empty);
            addButton(grid(6, i + 1), empty);
        }

        addButton(BACK_LOCATION, event -> new RoomMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(grid(2, 1), event -> table.moveTableRelative(howMuch(event.getClick()), 0, 0), XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "" + table.getX());
        addButton(grid(2, 2), event -> table.moveTableRelative(0, howMuch(event.getClick()), 0), XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "" + table.getY());
        addButton(grid(2, 3), event -> table.moveTableRelative(0, 0, howMuch(event.getClick())), XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "" + table.getZ());

        addButton(grid(3, 1), event -> table.rotateX(), XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X");
        addButton(grid(3, 2), event -> table.rotateY(), XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y");
        addButton(grid(3, 3), event -> table.rotateZ(), XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z");

        addButton(grid(4, 1), event -> table.skewWX(howMuch(event.getClick())), XMaterial.RED_BANNER, "Width X multiplier", "" + table.getWMX());
        addButton(grid(4, 2), event -> table.skewWY(howMuch(event.getClick())), XMaterial.GREEN_BANNER, "Width Y multiplier", "" + table.getWMY());
        addButton(grid(4, 3), event -> table.skewWZ(howMuch(event.getClick())), XMaterial.BLUE_BANNER, "Width Z multiplier", "" + table.getWMZ());
        addButton(grid(5, 1), event -> table.skewHX(howMuch(event.getClick())), XMaterial.RED_BANNER, "Height X multiplier", "" + table.getHMX());
        addButton(grid(5, 2), event -> table.skewHY(howMuch(event.getClick())), XMaterial.GREEN_BANNER, "Height Y multiplier", "" + table.getHMY());
        addButton(grid(5, 3), event -> table.skewHZ(howMuch(event.getClick())), XMaterial.BLUE_BANNER, "Height Z multiplier", "" + table.getHMZ());

        addButton(grid(2, 4), event -> table.reposition(), XMaterial.COMPASS, "reposition");
    }
}
