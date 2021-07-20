package cabbageroll.notrisdefect.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MenuItem {

    private final ItemStack item;

    public MenuItem(ItemStack is) {
        item = is;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract void onClick(InventoryClickEvent event);

}
