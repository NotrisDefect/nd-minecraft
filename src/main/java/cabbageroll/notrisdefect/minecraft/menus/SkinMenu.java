package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Table;
import cabbageroll.notrisdefect.minecraft.playerdata.Blocks;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkinMenu extends Menu {

    public final static int TORCH_LOCATION = 8;
    public final static int[] BLOCK_LOCATIONS = {11, 28, 29, 30, 31, 32, 33, 34, 13, 15, 37, 38, 39, 40, 41, 42, 43};

    public SkinMenu(Player player) {
        super(player);
    }

    public static Skin toSkin(Inventory inv) {
        ItemStack[] blocks = new ItemStack[17];

        for (int i = 0; i < 7; i++) {
            if (inv.getItem(28 + i) != null) {
                blocks[i + 1] = inv.getItem(28 + i);
            } else {
                blocks[i + 1] = new ItemStack(XMaterial.AIR.parseMaterial());
            }
        }

        // save ghost
        for (int i = 0; i < 7; i++) {
            if (inv.getItem(37 + i) != null) {
                blocks[i + Table.GHOST_OFFSET] = inv.getItem(37 + i);
            } else {
                blocks[i + Table.GHOST_OFFSET] = new ItemStack(XMaterial.AIR.parseMaterial());
            }
        }

        if (inv.getItem(11) != null) {
            blocks[Table.PIECE_NONE] = inv.getItem(11);
        } else {
            blocks[Table.PIECE_NONE] = new ItemStack(XMaterial.AIR.parseMaterial());
        }

        if (inv.getItem(13) != null) {
            blocks[Table.PIECE_GARBAGE] = inv.getItem(13);
        } else {
            blocks[Table.PIECE_GARBAGE] = new ItemStack(XMaterial.AIR.parseMaterial());
        }

        if (inv.getItem(15) != null) {
            blocks[Table.PIECE_ZONE] = inv.getItem(15);
        } else {
            blocks[Table.PIECE_ZONE] = new ItemStack(XMaterial.AIR.parseMaterial());
        }
        return new Skin(blocks);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "Skin editor");

        for (int i = 0; i < 54; i++) {
            addButton(i, empty);
        }

        Skin skin;
        if (!Main.gs.getData(player).isCustom()) {
            skin = Blocks.defaultBlocks;
        } else {
            skin = Main.gs.getSkin(player);
        }

        for (int i = 0; i < 17; i++) {
            addButton(BLOCK_LOCATIONS[i], event -> event.setCancelled(false), skin.get(i));
            if (skin.get(i).getType() == XMaterial.AIR.parseMaterial()) {
                addButton(BLOCK_LOCATIONS[i], event -> {
                    if (event.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) {
                        if (event.getSlot() == 11 && event.getCursor().getType() == XMaterial.AIR.parseMaterial()) {
                            Main.gs.getData(player).swapTransparent();
                            player.sendMessage("Transparency turned " + (Main.gs.getData(player).isTransparent() ? "on" : "off"));
                        }
                    }
                }, skin.get(i));
            }
        }

        addButton(BACK_LOCATION, event -> {
            if (Main.gs.getData(player).isCustom()) {
                Inventory inv = event.getClickedInventory();
                Main.gs.setSkin(player, toSkin(inv));
            }
            new HomeMenu(player);
        }, XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(TORCH_LOCATION, event -> {
            if (Main.gs.getData(player).isCustom()) {
                Inventory inv = event.getClickedInventory();
                Main.gs.setSkin(player, toSkin(inv));
            }
            Main.gs.getData(player).swapCustom();
            new SkinMenu(player);
        }, XMaterial.TORCH, ChatColor.WHITE + "" + (!Main.gs.getData(player).isCustom() ? ChatColor.BOLD : "") + "Default", ChatColor.WHITE + "" + (Main.gs.getData(player).isCustom() ? ChatColor.BOLD : "") + "Custom");

    }


}
