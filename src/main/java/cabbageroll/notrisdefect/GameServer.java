package cabbageroll.notrisdefect;

import cabbageroll.notrisdefect.menus.HomeMenu;
import cabbageroll.notrisdefect.menus.Menu;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final Map<Player, Table> tables = new LinkedHashMap<>();
    private final Map<Player, PlayerData> data = new HashMap<>();

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

        try {
            File file = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".dat");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BukkitObjectOutputStream oos = new BukkitObjectOutputStream(fos);
            oos.writeObject(data.get(player));
            oos.close();
            fos.close();
        } catch (IOException e) {
            Main.plugin.getLogger().warning(e.getMessage());
        }

        data.remove(player);

    }

    public void deleteRoom(String s) {
        rooms.remove(s);
    }

    public String[] generateRoomList() {
        return rooms.keySet().toArray(new String[rooms.size()]);
    }

    public PlayerData getData(Player player) {
        return data.get(player);
    }

    public Room getRoom(String s) {
        return rooms.get(s);
    }

    public Room getRoom(Player player) {
        return tables.get(player).getRoom();
    }

    public Skin getSkin(Player player) {
        return data.get(player).getSkin();
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
        PlayerData pd;

        try {
            FileInputStream fis = new FileInputStream(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".dat");
            BukkitObjectInputStream ois = new BukkitObjectInputStream(fis);
            pd = (PlayerData) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            Main.plugin.getLogger().warning(e.getMessage());
            pd = new PlayerData();
            pd.setSkin(new Skin(
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem(),
                XMaterial.AIR.parseItem()
            ));
        }

        data.put(player, pd);
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

    public void setSkin(Player player, Skin skin) {
        data.get(player).setSkin(skin);
    }
}
