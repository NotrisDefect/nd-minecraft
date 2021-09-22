package cabbageroll.notrisdefect.minecraft.menus;

import cabbageroll.notrisdefect.minecraft.Main;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MultiplayerMenu extends Menu {

    public final static int CREATEROOM_LOCATION = 21;
    public final static int LISTROOMS_LOCATION = 23;

    public MultiplayerMenu(Player player) {
        super(player);
    }

    @Override
    protected void afterInventoryClick(InventoryClickEvent event) {

    }

    @Override
    protected void prepare() {
        createInventory(this, 54, "Multiplayer");
        for (int i = 0; i < 9; i++) {
            buttons.put(grid(1, i + 1), border);
            buttons.put(grid(6, i + 1), border);
        }

        buttons.put(BACK_LOCATION, new Button(createItem(XMaterial.BEDROCK, ChatColor.WHITE + "Back"), event -> new HomeMenu(player)));
        buttons.put(CREATEROOM_LOCATION, new Button(createItem(XMaterial.COAL_ORE, ChatColor.WHITE + "Create new room"), event -> {
            Main.gs.createMPRoom(player);
            new RoomMenu(player);
        }));
        buttons.put(LISTROOMS_LOCATION, new Button(createItem(XMaterial.COAL_BLOCK, ChatColor.WHITE + "Join a room"), event -> new RoomListMenu(player)));

    }
}
