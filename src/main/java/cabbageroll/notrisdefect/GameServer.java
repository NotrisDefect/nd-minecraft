package cabbageroll.notrisdefect;

import cabbageroll.notrisdefect.menus.BaseMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

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
    public final Map<Player, ItemStack[]> customBlocks = new HashMap<>();
    public final Map<Player, Boolean> playerTransparentBackground = new HashMap<>();
    public final Map<Player, ItemStack[]> skinMenuBuffer = new HashMap<>();
    public final Map<Player, Boolean> useSkinMenuBuffer = new HashMap<>();

    private final Map<String, Room> globalRooms = new LinkedHashMap<>();
    private final Map<Player, Table> globalTables = new LinkedHashMap<>();

    public GameServer() {

    }

    public void deleteRoom(String s) {
        globalRooms.remove(s);
    }

    public void deinitialize(Player player) {
        getTable(player).setLastMenuOpened(null);
        if (getRoom(player) != null) {
            getRoom(player).removePlayer(player);
        }
        globalTables.remove(player);

        Main.plugin.saveSkin(player, customBlocks.get(player));

        customBlocks.remove(player);
        playerIsUsingCustomBlocks.remove(player);
        playerTransparentBackground.remove(player);
    }

    public void initialize(Player player) {
        Table table = new Table(player);
        globalTables.put(player, table);
        table.setLastMenuOpened(Menu.HOME);

        File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        ItemStack[] blocks = new ItemStack[17];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = customConfig.getItemStack(Constants.NAMES[i]);
        }
        customBlocks.put(player, blocks);
        playerIsUsingCustomBlocks.put(player, customConfig.getBoolean("playerIsUsingCustomBlocks"));
        playerTransparentBackground.put(player, customConfig.getBoolean("playerTransparentBackground"));
    }

    public Menu getLastMenuOpened(Player player) {
        return globalTables.get(player).getLastMenuOpened();
    }

    public Room getRoom(String s) {
        return globalRooms.get(s);
    }

    public Room getRoom(Player player) {
        return globalTables.get(player).getRoom();
    }

    public Table getTable(Player player) {
        return globalTables.get(player);
    }

    public boolean hasMenuOpen(Player player) {
        InventoryView iv = player.getOpenInventory();
        Inventory upper = iv.getTopInventory();
        return upper.getHolder() instanceof BaseMenu;
    }

    public void setLastMenuOpened(Player player, Menu menu) {
        globalTables.get(player).setLastMenuOpened(menu);
    }

    public boolean playerIsHere(Player player) {
        return globalTables.containsKey(player);
    }

    public Room createMPRoom(Player player) {
        Room room = new Room(player, false);
        globalRooms.put(room.getRoomID(), room);
        return room;
    }

    public Room createSPRoom(Player player) {
        Room room = new Room(player,true);
        globalRooms.put(room.getRoomID(), room);
        return room;
    }

    public String[] generateRoomList() {
        return globalRooms.keySet().toArray(new String[globalRooms.size()]);
    }

    public void removeRoom(String s) {
        globalRooms.remove(s);
    }
}
