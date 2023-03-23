package in.cabbageroll.notrisdefect.minecraft;

import in.cabbageroll.notrisdefect.minecraft.menus.HomeMenu;
import in.cabbageroll.notrisdefect.minecraft.menus.Menu;
import in.cabbageroll.notrisdefect.minecraft.menus.RoomMenu;
import in.cabbageroll.notrisdefect.minecraft.playerdata.BuiltInSkins;
import in.cabbageroll.notrisdefect.minecraft.playerdata.Settings;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameServer {

    private final Map<String, Room> roomMap = new LinkedHashMap<>();
    private final Map<Player, Table> tableMap = new HashMap<>();
    private final Map<Player, Settings> offlineData = new HashMap<>();

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

        tableMap.remove(player);

        try {
            File file = new File(Main.PLUGIN.getDataFolder() + "/userdata/" + player.getUniqueId() + ".dat");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(offlineData.get(player));
            oos.close();
            fos.close();
        } catch (IOException e) {
            Main.PLUGIN.getLogger().warning(e.getMessage());
        }

        offlineData.remove(player);
    }

    public void flush() {

    }

    public Settings getData(Player player) {
        return offlineData.get(player);
    }

    public List<Room> getMultiplayerRoomList() {
        List<Room> mpRooms = new LinkedList<>();
        for (Room room : roomMap.values()) {
            if (!room.isSingleplayer()) {
                mpRooms.add(room);
            }
        }
        return mpRooms;
    }

    public Room getRoom(String s) {
        return roomMap.get(s);
    }

    public Room getRoom(Player player) {
        return tableMap.get(player).getRoom();
    }

    public HashMap<Integer, XMaterial> getSkin(Player player) {
        return offlineData.get(player).getSkin();
    }

    public Table getTable(Player player) {
        return tableMap.get(player);
    }

    public boolean hasMenuOpen(Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() instanceof Menu;
    }

    public void initialize(Player player) {
        Settings data;

        try {
            FileInputStream fis = new FileInputStream(Main.PLUGIN.getDataFolder() + "/userdata/" + player.getUniqueId() + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (Settings) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            Main.PLUGIN.getLogger().warning(e.getMessage());
            data = new Settings();
            data.setSkin(new HashMap<>(BuiltInSkins.DEFAULTSKIN));
            data.setARR(50);
            data.setDAS(300);
            data.setSDF(10);
        }

        offlineData.put(player, data);
        Table table = new Table(player);
        tableMap.put(player, table);
    }

    public boolean isPlayerUsingThePlugin(Player player) {
        return tableMap.containsKey(player);
    }

    public void openLastMenu(Player player) {
        Menu last = getMenu(player);
        if (last == null) {
            new HomeMenu(player);
            return;
        }

        if (last instanceof RoomMenu) {
            new RoomMenu(player);
        } else {
            player.openInventory(last.getInventory());
        }
    }

    public void openMenu(Player player, Menu menu) {
        tableMap.get(player).setLastMenuOpened(menu);
        player.openInventory(menu.getInventory());
    }

    public void popRoom(Room room) {
        roomMap.remove(room.getRoomID());
    }

    public boolean roomExists(Room room) {
        return roomMap.containsValue(room);
    }

    public void setSkin(Player player, HashMap<Integer, XMaterial> skin) {
        offlineData.get(player).setSkin(skin);
    }

    public Menu getMenu(Player player) {
        return tableMap.get(player).getLastMenuOpened();
    }

    private void pushRoom(Room room) {
        roomMap.put(room.getRoomID(), room);
    }
}
