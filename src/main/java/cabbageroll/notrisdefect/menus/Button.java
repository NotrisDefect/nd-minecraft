package cabbageroll.notrisdefect.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    private final ItemStack item;

    public Button(ItemStack is) {
        item = is;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract void onClick(InventoryClickEvent event);

}
