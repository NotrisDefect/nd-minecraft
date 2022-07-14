package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
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
        addButton(grid(3, 7), event -> {
            Main.gs.setSkin(player, Skin.ZONE_P1);
            Main.gs.getData(player).setCustom(true);
        }, XMaterial.ENCHANTED_BOOK, ChatColor.WHITE + "P1 skin");
        addButton(grid(4, 7), event -> {
            Main.gs.setSkin(player, Skin.ZONE_P2);
            Main.gs.getData(player).setCustom(true);
        }, XMaterial.ENCHANTED_BOOK, ChatColor.WHITE + "P2 skin");
        addButton(grid(2, 8), event -> table.lumines(), XMaterial.GLOWSTONE_DUST, "LUMINES");
        addButton(grid(3, 8), event -> {
            table.setPIECESPAWNDELAY(1000);
            table.setLINECLEARDELAY(5000);
        }, XMaterial.GLOWSTONE_DUST, "long delay test");

        addButton(grid(3, 6), event -> table.manualReposition(), XMaterial.GLOWSTONE_DUST, ChatColor.WHITE + "Reposition (manual)");

    }


}
