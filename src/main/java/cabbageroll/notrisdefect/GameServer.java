package cabbageroll.notrisdefect;

import cabbageroll.notrisdefect.menus.HomeMenu;
import cabbageroll.notrisdefect.menus.Menu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameServer {
    /* TIPS
    A player doesn't have to be in any room to see a table (spectator mode is implemented in a different way).
    A player can only be in one room, and have one table.
    Table contains player and the room reference.
    Room contains reference to tables in the room.
    Room ticks all alive tables that are in it.
    */

    public final Map<Player, Boolean> playerIsUsingCustomBlocks = new HashMap<>();
    public final Map<Player, Skin> skins = new HashMap<>();
    public final Map<Player, Boolean> playerTransparentBackground = new HashMap<>();
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final Map<Player, Table> tables = new LinkedHashMap<>();

    public GameServer() {

    }

    public Room createMPRoom(Player player) {
        Room room = new Room(player, false);
        rooms.put(room.getRoomID(), room);
        return room;
    }

    public Room createSPRoom(Player player) {
        Room room = new Room(player, true);
        rooms.put(room.getRoomID(), room);
        return room;
    }

    public void deinitialize(Player player) {
        getTable(player).setLastMenuOpened(null);
        if (getRoom(player) != null) {
            getRoom(player).removePlayer(player);
        }
        tables.remove(player);

        Main.plugin.saveSkin(player, skins.get(player));

        skins.remove(player);
        playerIsUsingCustomBlocks.remove(player);
        playerTransparentBackground.remove(player);
    }

    public void deleteRoom(String s) {
        rooms.remove(s);
    }

    public String[] generateRoomList() {
        return rooms.keySet().toArray(new String[rooms.size()]);
    }

    public Room getRoom(String s) {
        return rooms.get(s);
    }

    public Room getRoom(Player player) {
        return tables.get(player).getRoom();
    }

    public Table getTable(Player player) {
        return tables.get(player);
    }

    public boolean hasMenuOpen(Player player) {
        InventoryView iv = player.getOpenInventory();
        Inventory upper = iv.getTopInventory();
        return upper.getHolder() instanceof Menu;
    }

    public void initialize(Player player) {
        Table table = new Table(player);
        tables.put(player, table);
        new HomeMenu(player);

        File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        skins.put(player, (Skin) customConfig.get("skin"));
        playerIsUsingCustomBlocks.put(player, customConfig.getBoolean("playerIsUsingCustomBlocks"));
        playerTransparentBackground.put(player, customConfig.getBoolean("playerTransparentBackground"));
    }

    public void openLastMenu(Player player) {
        player.openInventory(tables.get(player).getLastMenuOpened().getInventory());
    }

    public void openMenu(Player player, Menu menu) {
        tables.get(player).setLastMenuOpened(menu);
        player.openInventory(menu.getInventory());
    }

    public boolean playerIsHere(Player player) {
        return tables.containsKey(player);
    }

    public void removeRoom(String s) {
        rooms.remove(s);
    }
}
