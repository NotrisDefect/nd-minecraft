package tetrminecraft.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

public class BaseMenu implements InventoryHolder {

    private Inventory inventory = null;

    public static ItemStack createItem(final XMaterial material, final String name, final String... lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta;
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    public void createInventory(InventoryHolder holder, int size, String name) {
        inventory = Bukkit.createInventory(holder, size, name);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
