package cabbageroll.notrisdefect.menus;

import cabbageroll.notrisdefect.Blocks;
import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Skin;
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
        createInventory(this, 54, "cabbageroll.notrisdefect.Skin editor");
        ItemStack border = XMaterial.GLASS_PANE.parseItem();
        // fill the border with glass
        for (int i = 0; i < 54; i++) {
            getInventory().setItem(i, border);
        }

        Skin skin;
        if (!Main.gs.playerIsUsingCustomBlocks.get(player)) {
            skin = Blocks.defaultBlocks;
        } else {
            skin = Main.gs.skins.get(player);
        }

        // changeable blocks
        for (int i = 0; i < 17; i++) {
            getInventory().setItem(BLOCK_LOCATIONS[i], skin.get(i));
        }

        getInventory().setItem(BACK_LOCATION, createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"));
        getInventory().setItem(TORCH_LOCATION, createItem(XMaterial.TORCH, ChatColor.WHITE + "" + (!Main.gs.playerIsUsingCustomBlocks.get(player) ? ChatColor.BOLD : "") + "Default", ChatColor.WHITE + "" + (Main.gs.playerIsUsingCustomBlocks.get(player) ? ChatColor.BOLD : "") + "Custom"));

        Main.gs.openMenu(player, this);
    }

    public static void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
        } else if (event.getCurrentItem().getType() == XMaterial.GLASS_PANE.parseMaterial()) {
            event.setCancelled(true);
        } else if (event.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) {
            if (slot == 11 && event.getCursor().getType() == XMaterial.AIR.parseMaterial()) {
                Main.gs.playerTransparentBackground.put(player, !Main.gs.playerTransparentBackground.get(player));
                player.sendMessage("Transparency turned " + (Main.gs.playerTransparentBackground.get(player) ? "on" : "off"));
                return;
            }
        } else if (event.getSlot() == SkinMenu.BACK_LOCATION || event.getSlot() == SkinMenu.TORCH_LOCATION) {
            event.setCancelled(true);
        }

        if (event.getSlot() == SkinMenu.TORCH_LOCATION) {
            Main.gs.playerIsUsingCustomBlocks.put(player, !Main.gs.playerIsUsingCustomBlocks.get(player));
            new SkinMenu(player);
        }

        if (event.getSlot() == SkinMenu.BACK_LOCATION) {
            if (Main.gs.playerIsUsingCustomBlocks.get(player)) {
                ItemStack[] blocks = new ItemStack[17];
                Inventory inv = event.getClickedInventory();
                // save blocks
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
                Main.gs.skins.put(player, new Skin(blocks));
            }
            new HomeMenu(player);
        }
    }
}
