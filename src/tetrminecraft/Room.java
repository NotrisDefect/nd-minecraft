package tetrminecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;

public class Room {

    private static String makeID() {
        String charSet = Constants.idCharSet;
        StringBuilder result = new StringBuilder(Constants.idLength);
        for (int i = 0; i < Constants.idLength; i++) {
            int index = (int) (charSet.length() * Math.random());
            result.append(charSet.charAt(index));
        }
        return result.toString();
    }

    private static boolean isUnique(String id) {
        Object[] keys = Main.roomMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            if (id.equals(key)) {
                return false;
            }
        }
        return true;
    }

    ItemStack mapItem;
    private static MapView mapView;
    private boolean dontRender = false;

    public List<Player> playerList = new ArrayList<Player>();
    public List<Player> spectatorList = new ArrayList<Player>();
    public Map<Player, Table> playerTableMap = new HashMap<Player, Table>();
    public String roomID;
    public String roomName;
    public Player host;
    public boolean isRunning;
    public int playersAlive;
    public boolean backfire = false;
    public boolean isSingleplayer;

    public int index;

    public Room(Player host, boolean isSingleplayer) {
        Main.noteblockapi.newRSP(this);
        
        String mkID;
        do {
            mkID = makeID();
        } while (!isUnique(mkID));
        roomID = mkID;

        this.host = host;
        addPlayer(host);
        Main.roomMap.put(roomID, this);
        this.isSingleplayer = isSingleplayer;
        if (isSingleplayer) {
            roomName = "Singleplayer #" + roomID;
        } else {
            roomName = "Room #" + roomID;
        }
        index = -1;
    }

    public void stopRoom() {
        Main.noteblockapi.setPlaying(this, false);

        for (Player player : playerList) {
            playerTableMap.get(player).setGameOver(true);
        }

        isRunning = false;
    }

    public void startRoom() {
        if (index == -1) {
            int random = (int) (Math.random() * Main.numberOfSongs);
            Main.noteblockapi.playSong(this, random);
        } else {
            Main.noteblockapi.playSong(this, index);
        }
        Main.noteblockapi.setRepeatMode(this, RepeatMode.ONE);
        if (Main.noteblockapi.isPlaying(this) == false) {
            Main.noteblockapi.setPlaying(this, true);
        }

        Random x = new Random();
        long seed = x.nextInt();
        long seed2 = x.nextInt();

        for (Player player : playerList) {
            Table table = playerTableMap.get(player);
            table.initTable(seed, seed2);
            table.getPlayer().sendMessage(Main.noteblockapi.getPlayingNow(this));
        }

        playersAlive = playerList.size();
        isRunning = true;

    }

    public void addPlayer(Player player) {
        Main.inwhichroom.put(player, this);
        playerList.add(player);
        Table table = new Table(player);
        playerTableMap.put(player, table);
        Main.noteblockapi.addPlayer(this, player);
    }

    public void addSpectator(Player player) {
        spectatorList.add(player);
        player.sendMessage("Added, but this feature is work in progress");
    }

    public void removeSpectator(Player player) {
        spectatorList.remove(player);
    }

    public void removePlayer(Player player) {
        Main.noteblockapi.removePlayer(this, player);
        playerTableMap.get(player).destroyTable();
        playerTableMap.get(player).setGameOver(true);
        playersAlive--;
        if (playersAlive < 2) {
            stopRoom();
        }
        playerList.remove(player);
        playerTableMap.remove(player);
        Main.inwhichroom.remove(player);
        if (player == host) {
            if (playerList.size() == 0) {
                Main.roomMap.remove(roomID);
            } else {
                host = playerList.get(0);
                host.sendMessage("[TETR] Since the old room host left, you became the new host.");
            }
        }
    }

    public void forwardGarbage(int n, Player sender) {
        if (n > 0) {
            int random = (int) (Math.random() * playerList.size());
            Table table = playerTableMap.get(playerList.get(random));
            Player receiver = table.getPlayer();
            if (receiver != sender || (receiver == sender && backfire)) {
                if (!table.getGameover()) {
                    table.receiveGarbage(n);
                } else if (isRunning) {
                    forwardGarbage(n, sender);
                }
            } else if (!isSingleplayer) {
                forwardGarbage(n, sender);
            }
        }
    }

    public void switchBracket(Player player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            playerTableMap.get(player).destroyTable();
            playerTableMap.remove(player);
            spectatorList.add(player);
        } else {
            spectatorList.remove(player);
            playerList.add(player);
        }
    }

}
