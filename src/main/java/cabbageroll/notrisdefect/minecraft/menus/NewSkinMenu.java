package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
import com.cryptomorin.xseries.XMaterial;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NewSkinMenu extends Menu {

    public static final int TORCH_LOCATION = 8;
    public static final int[] BLOCK_LOCATIONS = {10, 28, 29, 30, 31, 32, 33, 34, 12, 14, 16, 37, 38, 39, 40, 41, 42, 43};

    public NewSkinMenu(Player player) {
        super(player);
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

        boolean isCustom = Main.gs.getData(player).isCustomSkinActive();
        Skin skin;
        if (isCustom) {
            skin = Main.gs.getSkin(player);
            for (int i = 0; i < Skin.SIZE; i++) {
                ItemStack item = skin.getFancy(i);
                int index = i;
                addButton(BLOCK_LOCATIONS[i], event -> new AnvilGUI.Builder().itemLeft(item).text("Enter new XMaterial").onLeftInputClick(Main.gs::openLastMenu).onComplete((player, text) -> {
                    if (text != null && XMaterial.matchXMaterial(text).isPresent()) {
                        XMaterial xm = XMaterial.matchXMaterial(text).get();
                        Material m = xm.parseMaterial();
                        if (m == null) {
                            return AnvilGUI.Response.text("Not available");
                        } else if (m.isBlock()) {
                            skin.set(index, xm);
                            new NewSkinMenu(player);
                            return AnvilGUI.Response.close();
                        } else {
                            return AnvilGUI.Response.text("Not a block");
                        }
                    } else if (text != null && text.equalsIgnoreCase("existing")) {
                        skin.set(index, XMaterial.STICK);
                        new NewSkinMenu(player);
                        return AnvilGUI.Response.close();
                    } else {
                        return AnvilGUI.Response.text("Invalid XMaterial");
                    }
                }).plugin(Main.plugin).open(player), item);
            }
        } else {
            skin = Skin.defaultSkin;
            for (int i = 0; i < Skin.SIZE; i++) {
                addButton(BLOCK_LOCATIONS[i], event -> {
                }, skin.getFancy(i));
            }
        }

        addButton(BACK_LOCATION, event -> new HomeMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(TORCH_LOCATION, event -> {
            Main.gs.getData(player).toggleCustom();
            new NewSkinMenu(player);
        }, XMaterial.TORCH, ChatColor.WHITE + "Skin slot", (isCustom ? "custom" : "default"));

    }

}
