package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Blocks;
import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Skin;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkinMenu extends Menu {

    public final static int TORCH_LOCATION = 8;
    public final static int[] BLOCK_LOCATIONS = {28, 29, 30, 31, 32, 33, 34, 11, 13, 37, 38, 39, 40, 41, 42, 43, 15};

    public SkinMenu(Player player) {
        createInventory(this, 54, "Skin editor");
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        Skin skin;
        if (!Main.gs.getData(player).isCustom()) {
            skin = Blocks.defaultBlocks;
        } else {
            skin = Main.gs.getSkin(player);
        }

        // changeable blocks
        for (int i = 0; i < 17; i++) {
            getInventory().setItem(BLOCK_LOCATIONS[i], skin.get(i));
        }

        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (!Main.gs.getData(player).isCustom() ? ChatColor.BOLD : "") + "Default", ChatColor.WHITE + "" + (Main.gs.getData(player).isCustom() ? ChatColor.BOLD : "") + "Custom"));

        Main.gs.openMenu(player, this);
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
        } else if (!Main.gs.getData(player).isCustom()) {
            event.setCancelled(true);
        } else if (event.getCurrentItem().getType() == XMaterial.GLASS_PANE.parseMaterial()) {
            event.setCancelled(true);
        } else if (event.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) {
            if (slot == 11 && event.getCursor().getType() == XMaterial.AIR.parseMaterial()) {
                Main.gs.getData(player).swapTransparent();
                player.sendMessage("Transparency turned " + (Main.gs.getData(player).isTransparent() ? "on" : "off"));
                return;
            }
        } else if (event.getSlot() == BACK_LOCATION || event.getSlot() == SkinMenu.TORCH_LOCATION) {
            event.setCancelled(true);
        }

        if (event.getSlot() == SkinMenu.TORCH_LOCATION) {
            if (Main.gs.getData(player).isCustom()) {
                Inventory inv = event.getClickedInventory();
                Main.gs.setSkin(player, toSkin(inv));
            }
            Main.gs.getData(player).swapCustom();
            new SkinMenu(player);
        }

        if (event.getSlot() == BACK_LOCATION) {
            if (Main.gs.getData(player).isCustom()) {
                Inventory inv = event.getClickedInventory();
                Main.gs.setSkin(player, toSkin(inv));
            }
            new HomeMenu(player);
        }
    }

    public static Skin toSkin(Inventory inv) {
        ItemStack[] blocks = new ItemStack[17];
        for (int i = 0; i < 7; i++) {
            if (inv.getItem(28 + i) != null) {
                blocks[i] = inv.getItem(28 + i);
            } else {
                blocks[i] = new ItemStack(XMaterial.AIR.parseMaterial());
            }
        }

        // save ghost
        for (int i = 0; i < 7; i++) {
            if (inv.getItem(37 + i) != null) {
                blocks[i + 9] = inv.getItem(37 + i);
            } else {
                blocks[i + 9] = new ItemStack(XMaterial.AIR.parseMaterial());
            }
        }

        // other
        if (inv.getItem(11) != null) {
            blocks[7] = inv.getItem(11);
        } else {
            blocks[7] = new ItemStack(XMaterial.AIR.parseMaterial());
        }

        if (inv.getItem(13) != null) {
            blocks[8] = inv.getItem(13);
        } else {
            blocks[8] = new ItemStack(XMaterial.AIR.parseMaterial());
        }

        if (inv.getItem(15) != null) {
            blocks[16] = inv.getItem(15);
        } else {
            blocks[16] = new ItemStack(XMaterial.AIR.parseMaterial());
        }
        return new Skin(blocks);
    }
}
