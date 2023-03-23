package in.cabbageroll.notrisdefect.minecraft.menus;

import in.cabbageroll.notrisdefect.minecraft.Main;
import in.cabbageroll.notrisdefect.minecraft.Table;
import in.cabbageroll.notrisdefect.minecraft.playerdata.BuiltInSkins;
import com.cryptomorin.xseries.XMaterial;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SkinMenu extends Menu {

    public static final XMaterial EXISTING_MATERIAL = XMaterial.STICK;
    public static final String EXISTING_STRING = "EXISTING";
    public static final int TORCH_LOCATION = grid(9, 1);
    public static final int[] BLOCK_LOCATIONS = {
        grid(2, 2),
        grid(2, 4),
        grid(3, 4),
        grid(4, 4),
        grid(5, 4),
        grid(6, 4),
        grid(7, 4),
        grid(8, 4),
        grid(4, 2),
        grid(6, 2),
        grid(8, 2),
        grid(2, 5),
        grid(3, 5),
        grid(4, 5),
        grid(5, 5),
        grid(6, 5),
        grid(7, 5),
        grid(8, 5),
    };

    public SkinMenu(Player player) {
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

        boolean isCustom = Main.GS.getData(player).isCustom();
        HashMap<Integer, XMaterial> skin;
        if (isCustom) {
            skin = Main.GS.getSkin(player);
            for (int i = 0; i < 18; i++) {
                ItemStack item = getFancy(skin, i);
                int index = i;
                addButton(BLOCK_LOCATIONS[i], event -> new AnvilGUI.Builder().itemLeft(item).text("Enter new XMaterial").onLeftInputClick(Main.GS::openLastMenu).onComplete((player, text) -> {
                    if (text != null && XMaterial.matchXMaterial(text).isPresent()) {
                        XMaterial xm = XMaterial.matchXMaterial(text).get();
                        Material m = xm.parseMaterial();
                        if (m == null) {
                            return AnvilGUI.Response.text("Not available");
                        } else if (m.isBlock()) {
                            skin.put(index, xm);
                            new SkinMenu(player);
                            return AnvilGUI.Response.close();
                        } else {
                            return AnvilGUI.Response.text("Not a block");
                        }
                    } else if (EXISTING_STRING.equalsIgnoreCase(text)) {
                        skin.put(index, EXISTING_MATERIAL);
                        new SkinMenu(player);
                        return AnvilGUI.Response.close();
                    } else {
                        return AnvilGUI.Response.text("Invalid XMaterial");
                    }
                }).plugin(Main.PLUGIN).open(player), item);
            }
        } else {
            skin = BuiltInSkins.DEFAULTSKIN;
            for (int i = 0; i < 18; i++) {
                addButton(BLOCK_LOCATIONS[i], event -> {
                }, getFancy(skin, i));
            }
        }

        addButton(BACK_LOCATION, event -> new HomeMenu(player), XMaterial.BEDROCK, ChatColor.WHITE + "Back");

        addButton(TORCH_LOCATION, event -> {
            Main.GS.getData(player).toggleCustom();
            new SkinMenu(player);
        }, XMaterial.TORCH, ChatColor.WHITE + "Skin slot", (isCustom ? "custom" : "default"));

    }

    private ItemStack getFancy(HashMap<Integer, XMaterial> skin, int i) {
        XMaterial mat = skin.get(i);
        return Menu.createItem(mat == XMaterial.AIR ? XMaterial.STICK : mat, ChatColor.WHITE + Table.pieceIntToString(i), mat == EXISTING_MATERIAL ? EXISTING_STRING : mat.name());
    }

}
