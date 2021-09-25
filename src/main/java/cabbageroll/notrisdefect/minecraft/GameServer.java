package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.menus.HomeMenu;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import cabbageroll.notrisdefect.minecraft.menus.RoomMenu;
import cabbageroll.notrisdefect.minecraft.menus.SkinMenu;
import cabbageroll.notrisdefect.minecraft.playerdata.Blocks;
import cabbageroll.notrisdefect.minecraft.playerdata.PersistentPlayerData;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameServer {
    /* TIPS
    A player doesn't have to be in any room to see a table (spectator mode is implemented in a different way).
    A player can only be in one room, and have one table.
    Table contains player and the room reference.
    Room contains reference to tables in the room.
    Room ticks all alive tables that are in it.
    */

    private final Map<String, Room> roomMap = new LinkedHashMap<>();
    private final List<Room> roomList = new LinkedList<>();
    private final Map<Player, Table> tableMap = new HashMap<>();
    private final Map<Player, PersistentPlayerData> offlineData = new HashMap<>();

    public GameServer() {

    }

    public List<Room> cloneRoomList() {
        List<Room> mpRooms = new LinkedList<>();
        for (Room room : roomList) {
            if (!room.isSingleplayer()) {
                mpRooms.add(room);
            }
        }
        return mpRooms;
    }

    public void createMPRoom(Player player) {
        pushRoom(new Room(player, false));
    }

    public void createSPRoom(Player player) {
        pushRoom(new Room(player, true));
    }

    public void deinitialize(Player player) {
        if (getRoom(player) != null) {
            getRoom(player).removePlayer(player);
        }

        if (getMenu(player).getInventory().getHolder() instanceof SkinMenu) {
            if (getData(player).isCustom()) {
                Inventory inv = getMenu(player).getInventory();
                setSkin(player, SkinMenu.toSkin(inv));
            }
        }

        tableMap.remove(player);

        try {
            File file = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getName() + "_" + player.getUniqueId() + ".dat");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BukkitObjectOutputStream oos = new BukkitObjectOutputStream(fos);
            oos.writeObject(offlineData.get(player));
            oos.close();
            fos.close();
        } catch (IOException e) {
            Main.plugin.getLogger().warning(e.getMessage());
        }

        offlineData.remove(player);

    }

    public void deleteRoom(Room room) {
        roomMap.remove(room.getRoomID());
        roomList.remove(room);
    }

    public PersistentPlayerData getData(Player player) {
        return offlineData.get(player);
    }

    public Room getRoom(String s) {
        return roomMap.get(s);
    }

    public Room getRoom(Player player) {
        return tableMap.get(player).getRoom();
    }

    public Skin getSkin(Player player) {
        return offlineData.get(player).getSkin();
    }

    public Table getTable(Player player) {
        return tableMap.get(player);
    }

    public boolean hasMenuOpen(Player player) {
        InventoryView iv = player.getOpenInventory();
        Inventory upper = iv.getTopInventory();
        return upper.getHolder() instanceof Menu;
    }

    public void initialize(Player player) {
        Table table = new Table(player);
        tableMap.put(player, table);
        PersistentPlayerData pd;

        try {
            FileInputStream fis = new FileInputStream(Main.plugin.getDataFolder() + "/userdata/" + player.getName() + "_" + player.getUniqueId() + ".dat");
            BukkitObjectInputStream ois = new BukkitObjectInputStream(fis);
            pd = (PersistentPlayerData) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            pd = new PersistentPlayerData();
            pd.setSkin(new Skin(Blocks.empty));
        }

        offlineData.put(player, pd);
    }

    public boolean isPlayerHere(Player player) {
        return tableMap.containsKey(player);
    }

    public void openLastMenu(Player player) {
        if (tableMap.get(player).getLastMenuOpened() == null) {
            new HomeMenu(player);
            return;
        }
        //DUMB
        if (tableMap.get(player).getLastMenuOpened() instanceof RoomMenu) {
            new RoomMenu(player);
        } else {
            player.openInventory(tableMap.get(player).getLastMenuOpened().getInventory());
        }
    }

    public void openMenu(Player player, Menu menu) {
        tableMap.get(player).setLastMenuOpened(menu);
        player.openInventory(menu.getInventory());
    }

    public void popRoom(String s) {
        roomList.remove(roomMap.get(s));
        roomMap.remove(s);
    }

    public boolean roomExists(Room room) {
        return roomList.contains(room);
    }

    public void setSkin(Player player, Skin skin) {
        offlineData.get(player).setSkin(skin);
    }

    private Menu getMenu(Player player) {
        return tableMap.get(player).getLastMenuOpened();
    }

    private void pushRoom(Room room) {
        roomMap.put(room.getRoomID(), room);
        roomList.add(room);
    }
}
