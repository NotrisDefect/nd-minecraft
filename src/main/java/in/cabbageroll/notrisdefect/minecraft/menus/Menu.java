package in.cabbageroll.notrisdefect.minecraft.menus;

import in.cabbageroll.notrisdefect.minecraft.Main;
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

    public static final int BACK_LOCATION = grid(1, 1);
    public static final int PREV_PAGE_LOCATION = grid(4, 6);
    public static final int NEXT_PAGE_LOCATION = grid(6, 6);
    public static final int CONTENT_BEGINNING = grid(1, 2);
    public static final int PAGE_SIZE = 36;

    protected final Player player;
    protected final Button empty = new Button(createItem(XMaterial.GLASS_PANE, "" + ChatColor.RESET));
    private final Map<Integer, Button> buttons = new HashMap<>();
    private Inventory inventory = null;

    public Menu(Player player) {
        this.player = player;
        prepare();
        open();
    }

    public static ItemStack createItem(XMaterial material, String name, String... lore) {
        ItemStack item = material.parseItem();
        ItemMeta meta;
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static int grid(int x, int y) {
        return (y - 1) * 9 + x - 1;
    }

    protected static int checkClickType(ClickType ct, int a, int b) {
        switch (ct) {
            case LEFT:
                return a;
            case RIGHT:
                return -a;
            case SHIFT_LEFT:
                return b;
            case SHIFT_RIGHT:
                return -b;
        }
        return 0;
    }

    protected static int checkClickType(ClickType ct) {
        return checkClickType(ct, 1);
    }

    protected static int checkClickType(ClickType ct, int a) {
        return checkClickType(ct, a, a);
    }

    public void createInventory(InventoryHolder holder, int size, String name) {
        inventory = Bukkit.createInventory(holder, size, name);
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

    protected void addBorder() {
        for (int i = 1; i <= 9; i++) {
            addButton(grid(i, 1), empty);
            addButton(grid(i, 6), empty);
        }
    }

    protected void addButton(int location, Button.ButtonClick bc, XMaterial material, String name, String... lore) {
        addButton(location, new Button(createItem(material, name, lore), bc));
    }

    protected void addButton(int location, Button button) {
        buttons.put(location, button);
    }

    protected void addButton(int location, Button.ButtonClick bc, ItemStack is) {
        addButton(location, new Button(is, bc));
    }

    protected abstract void afterInventoryClick(InventoryClickEvent event);

    protected void clearContent() {
        for (int i = CONTENT_BEGINNING; i < CONTENT_BEGINNING + PAGE_SIZE; i++) {
            buttons.remove(i);
        }
    }

    protected void open() {
        placeAll();
        Main.GS.openMenu(player, this);
    }

    protected void placeAll() {
        inventory.clear();
        for (Map.Entry<Integer, Button> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue().getItem());
        }
    }

    protected abstract void prepare();

}
