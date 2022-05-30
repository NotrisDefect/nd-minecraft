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

    public static final int TORCH_LOCATION = 8;
    public static final int[] BLOCK_LOCATIONS = {10, 28, 29, 30, 31, 32, 33, 34, 12, 14, 16, 37, 38, 39, 40, 41, 42, 43};

    public SkinMenu(Player player) {
        super(player);
    }

    public static Skin toSkin(Inventory inv) {
        ItemStack[] blocks = new ItemStack[Skin.SIZE];

        for (int i = 0; i < Skin.SIZE; i++) {
            if (inv.getItem(BLOCK_LOCATIONS[i]) != null) {
                blocks[i] = inv.getItem(BLOCK_LOCATIONS[i]);
            } else {
                blocks[i] = new ItemStack(XMaterial.AIR.parseMaterial());
            }
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

        for (int i = 0; i < Skin.SIZE; i++) {
            addButton(BLOCK_LOCATIONS[i], event -> event.setCancelled(!Main.gs.getData(player).isCustom()), skin.get(i));
            if (skin.get(i).getType() == XMaterial.AIR.parseMaterial()) {
                addButton(BLOCK_LOCATIONS[i], event -> {
                    event.setCancelled(!Main.gs.getData(player).isCustom());
                    if (event.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) {
                        if (event.getSlot() == BLOCK_LOCATIONS[Table.PIECE_NONE] && event.getCursor().getType() == XMaterial.AIR.parseMaterial()) {
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
