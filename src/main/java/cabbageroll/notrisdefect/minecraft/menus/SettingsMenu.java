package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Menu {

    private static final Map<Integer, Button> buttons = new HashMap<>();

    public SettingsMenu(Player player) {
        createInventory(this, 54, "Settings");
        Inventory inventory = getInventory();

        Table table = Main.gs.getTable(player);

        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
        }
        for (int i = 45; i < 54; i++) {
            buttons.put(grid(1, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                new RoomMenu(player);
            }
        });

        buttons.put(grid(2, 1), new Button(createItem(XMaterial.RED_WOOL, ChatColor.WHITE + "Move X", "" + table.getX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(howMuch(event.getClick()), 0, 0);
            }
        });

        buttons.put(grid(2, 2), new Button(createItem(XMaterial.GREEN_WOOL, ChatColor.WHITE + "Move Y", "" + table.getY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(0, howMuch(event.getClick()), 0);
            }
        });

        buttons.put(grid(2, 3), new Button(createItem(XMaterial.BLUE_WOOL, ChatColor.WHITE + "Move Z", "" + table.getZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveTableRelative(0, 0, howMuch(event.getClick()));
            }
        });

        buttons.put(grid(2, 4), new Button(createItem(XMaterial.COMPASS, "reposition")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.reposition();
            }
        });

        buttons.put(grid(2, 5), new Button(createItem(XMaterial.FLINT_AND_STEEL, "backfire", "" + Main.gs.getRoom(player).getBackfire())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                Main.gs.getRoom(player).toggleBackfire();
            }
        });

        buttons.put(grid(2, 6), new Button(createItem(XMaterial.PACKED_ICE, ChatColor.WHITE + "Falling blocks", "" + table.enableAnimations)) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.enableAnimations ^= true;
            }
        });

        buttons.put(grid(2, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEX", "" + table.getSTAGESIZEX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.setSTAGESIZEX(table.getSTAGESIZEX() + howMuch(event.getClick()));
            }
        });

        buttons.put(grid(3, 1), new Button(createItem(XMaterial.RED_CARPET, ChatColor.WHITE + "Rotate X")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateX();
            }
        });

        buttons.put(grid(3, 2), new Button(createItem(XMaterial.GREEN_CARPET, ChatColor.WHITE + "Rotate Y")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateY();
            }
        });

        buttons.put(grid(3, 3), new Button(createItem(XMaterial.BLUE_CARPET, ChatColor.WHITE + "Rotate Z")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.rotateZ();
            }
        });

        buttons.put(grid(3, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Hold top left corner X", "" + table.getHoldTLCX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveHoldTLCXRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(3, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Hold top left corner Y", "" + table.getHoldTLCY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveHoldTLCYRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(3, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "STAGESIZEY", "" + table.getSTAGESIZEY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.setSTAGESIZEY(table.getSTAGESIZEY() + howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 1), new Button(createItem(XMaterial.RED_BANNER, "Width X multiplier", "" + table.getWMX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWX(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 2), new Button(createItem(XMaterial.GREEN_BANNER, "Width Y multiplier", "" + table.getWMY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWY(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 3), new Button(createItem(XMaterial.BLUE_BANNER, "Width Z multiplier", "" + table.getWMZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewWZ(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Next top left corner X", "" + table.getNextTLCX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveNextTLCXRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Next top left corner Y", "" + table.getNextTLCY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveNextTLCYRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(4, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTPIECESMAX", "" + table.getNEXTPIECESMAX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.setNEXTPIECESMAX(table.getNEXTPIECESMAX() + howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 1), new Button(createItem(XMaterial.RED_BANNER, "Height X multiplier", "" + table.getHMX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHX(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 2), new Button(createItem(XMaterial.GREEN_BANNER, "Height Y multiplier", "" + table.getHMY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHY(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 3), new Button(createItem(XMaterial.BLUE_BANNER, "Height Z multiplier", "" + table.getHMZ())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.skewHZ(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 4), new Button(createItem(XMaterial.RED_STAINED_GLASS, "Garbage bottom left corner X", "" + table.getGarbBLCX())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveGarbBLCXRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 5), new Button(createItem(XMaterial.GREEN_STAINED_GLASS, "Garbage bottom left corner Y", "" + table.getGarbBLCY())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.moveGarbBLCYRelative(howMuch(event.getClick()));
            }
        });

        buttons.put(grid(5, 9), new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "NEXTVERTICAL", "" + table.getNEXTVERTICAL())) {
            @Override
            public void onClick(InventoryClickEvent event) {
                table.setNEXTVERTICAL(table.getNEXTVERTICAL() + howMuch(event.getClick()));
            }
        });

        for (Map.Entry<Integer, Button> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue().getItem());
        }

        Main.gs.openMenu(player, this);
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        Button mi = buttons.get(event.getSlot());
        if (mi != null) {
            mi.onClick(event);
            //only update what is needed if slow
            //temporary hack
            if (event.getSlot() != BACK_LOCATION) {
                new SettingsMenu(player);
            }
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
