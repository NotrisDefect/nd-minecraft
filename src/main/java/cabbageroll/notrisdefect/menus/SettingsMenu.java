package cabbageroll.notrisdefect.menus;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Menu;
import cabbageroll.notrisdefect.Table;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends BaseMenu {

    private static final Map<Integer, MenuItem> buttons = new HashMap<>();

    public SettingsMenu(Player player) {
        Main.gs.setLastMenuOpened(player, Menu.ROOMSETTINGSPLUS);
        createInventory(this, 54, "Settings");
        Inventory inventory = getInventory();
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, border);
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, border);
        }

        Table table = Main.gs.getTable(player);

        buttons.put(BACK_LOCATION, new MenuItem(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                new RoomMenu(player);
            }
        });

        buttons.put(grid(2, 1), new MenuItem(createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "" + table.getX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(howMuch(event.getClick()), 0, 0);
            }
        });

        buttons.put(grid(2, 2), new MenuItem(createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "" + table.getY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(0, howMuch(event.getClick()), 0);
            }
        });

        buttons.put(grid(2, 3), new MenuItem(createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "" + table.getZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(0, 0, howMuch(event.getClick()));
            }
        });

        buttons.put(grid(2, 4), new MenuItem(createItem(XMaterial.COMPASS, "reposition")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.reposition();
            }
        });

        buttons.put(grid(2, 5), new MenuItem(createItem(XMaterial.FLINT_AND_STEEL, "backfire", "" + Main.gs.getRoom(player).getBackfire())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                Main.gs.getRoom(player).toggleBackfire();
            }
        });

        buttons.put(grid(2, 6), new MenuItem(createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks: ", "" + table.enableAnimations)) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.enableAnimations ^= true;
            }
        });

        buttons.put(grid(3, 1), new MenuItem(createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X", "")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateX();
            }
        });

        buttons.put(grid(3, 2), new MenuItem(createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y", "")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateY();
            }
        });

        buttons.put(grid(3, 3), new MenuItem(createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z", "")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateZ();
            }
        });

        buttons.put(grid(4, 1), new MenuItem(createItem(XMaterial.RED_BANNER, "Width X multiplier: " + table.getWidthMultiplier().getX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWX(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 2), new MenuItem(createItem(XMaterial.GREEN_BANNER, "Width Y multiplier: " + table.getWidthMultiplier().getY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWY(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 3), new MenuItem(createItem(XMaterial.BLUE_BANNER, "Width Z multiplier: " + table.getWidthMultiplier().getZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWZ(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 1), new MenuItem(createItem(XMaterial.RED_BANNER, "Height X multiplier: " + table.getHeightMultiplier().getX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHX(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 2), new MenuItem(createItem(XMaterial.GREEN_BANNER, "Height Y multiplier: " + table.getHeightMultiplier().getY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHY(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 3), new MenuItem(createItem(XMaterial.BLUE_BANNER, "Height Z multiplier: " + table.getHeightMultiplier().getZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHZ(howMuch(event.getClick()));
            }
        });

        for (Map.Entry<Integer, MenuItem> eim : buttons.entrySet()) {
            inventory.setItem(eim.getKey(), eim.getValue().getItem());
        }

        player.openInventory(inventory);
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        MenuItem mi = buttons.get(event.getSlot());
        if (mi != null) {
            mi.onClick(event);
            //only update what is needed if slow
            //temporary hack
            if (event.getSlot() != BACK_LOCATION)
                new SettingsMenu(player);
        }
    }

    private static int howMuch(ClickType ct) {
        switch (ct) {
            case LEFT:
                return 1;
            case RIGHT:
                return -1;
            case SHIFT_LEFT:
                return 10;
            case SHIFT_RIGHT:
                return -10;
        }
        return 0;
    }
}
