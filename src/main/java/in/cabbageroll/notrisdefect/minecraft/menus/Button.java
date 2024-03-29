package in.cabbageroll.notrisdefect.minecraft.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Button {

    private final ItemStack item;
    private final ButtonClick bc;

    public Button(ItemStack is, ButtonClick bc) {
        item = is;
        this.bc = bc;
    }

    public Button(ItemStack is) {
        this(is, event -> {
        });
    }

    public ItemStack getItem() {
        return item;
    }

    public void onClick(InventoryClickEvent event) {
        bc.onClick(event);
    }

    public interface ButtonClick {
        void onClick(InventoryClickEvent event);
    }

}
