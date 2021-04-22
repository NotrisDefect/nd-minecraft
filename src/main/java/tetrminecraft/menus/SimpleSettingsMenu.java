package tetrminecraft.menus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tetrminecraft.Main;
import tetrminecraft.Table;

public class SimpleSettingsMenu extends BaseMenu {

    public final static int BACK_LOCATION = 0;
    public final static int TORCH_LOCATION = 8;

    public SimpleSettingsMenu(Player player) {
        Main.instance.lastMenuOpened.put(player, "simsettings");
        createInventory(this, 54, "Settings");
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            getInventory().setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        // clickable items

        Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);

        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION,
                createItem(XMaterial.TORCH, ChatColor.YELLOW + "This is standard settings menu",
                        ChatColor.DARK_RED + "" + ChatColor.BOLD + "Click to go to advanced menu"));

        getInventory().setItem(21, createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "X: " + table.getPosX()));
        getInventory().setItem(22, createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "Y: " + table.getPosY()));
        getInventory().setItem(23, createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "Z: " + table.getPosZ()));
        getInventory().setItem(30, createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X"));
        getInventory().setItem(31, createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y"));
        getInventory().setItem(32, createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z"));

        player.openInventory(getInventory());
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        int by = 0;
        if (event.getClick() == ClickType.LEFT) {
            by = +1;
        } else if (event.getClick() == ClickType.RIGHT) {
            by = -1;
        }

        Table table = Main.instance.inWhichRoomIs.get(player).playerTableMap.get(player);

        switch (event.getSlot()) {
        case SimpleSettingsMenu.BACK_LOCATION:
            new RoomMenu(player);
            return;
        case SimpleSettingsMenu.TORCH_LOCATION:
            new SettingsMenu(player);
            return;
        case 21:
            table.moveTableRelative(by, 0, 0);
            break;
        case 22:
            table.moveTableRelative(0, by, 0);
            break;
        case 23:
            table.moveTableRelative(0, 0, by);
            break;
        case 30:
            table.rotateTable("X");
            break;
        case 31:
            table.rotateTable("Y");
            break;
        case 32:
            table.rotateTable("Z");
            break;
        default:
            return;
        }

        new SimpleSettingsMenu(player);
    }
}
