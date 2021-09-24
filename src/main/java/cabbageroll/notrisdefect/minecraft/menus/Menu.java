package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHolder {

    public final static int BACK_LOCATION = 0;
    protected final Map<Integer, Button> buttons = new HashMap<>();
    protected final Player player;
    protected Button border = new Button(createItem(XMaterial.GLASS_PANE, "" + ChatColor.RESET));
    private Inventory inventory = null;

    public Menu(Player player) {
        this.player = player;
        prepare();
        open();
    }

    public static int grid(int column, int row) {
        return (column - 1) * 9 + row - 1;
    }

    protected static int howMuch(ClickType ct) {
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

    public void createInventory(InventoryHolder holder, int size, String name) {
        inventory = Bukkit.createInventory(holder, size, name);
    }

    public ItemStack createItem(final XMaterial material, final String name, final String... lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta;
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @NotNull
    Inventory getInventory() {
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Button button = buttons.get(event.getSlot());
        if (button != null) {
            button.onClick(event);
            afterInventoryClick(event);
        }
    }

    protected abstract void afterInventoryClick(InventoryClickEvent event);

    protected void open() {
        placeAll();
        Main.gs.openMenu(player, this);
    }

    protected void placeAll() {
        inventory.clear();
        for (Map.Entry<Integer, Button> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue().getItem());
        }
    }

    protected abstract void prepare();

}
