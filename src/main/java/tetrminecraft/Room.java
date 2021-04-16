package tetrminecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import tetrminecraft.commands.Choice;

public class Room {

    private static MapView mapView;

    private static boolean isUnique(String id) {
        Object[] keys = Main.instance.roomByID.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            if (id.equals(key)) {
                return false;
            }
        }
        return true;
    }

    private static String makeID() {
        String charSet = Constants.idCharSet;
        StringBuilder result = new StringBuilder(Constants.idLength);
        for (int i = 0; i < Constants.idLength; i++) {
            int index = (int) (charSet.length() * Math.random());
            result.append(charSet.charAt(index));
        }
        return result.toString();
    }
    
    ItemStack mapItem;
    private boolean dontRender = false;

    public List<Player> playerList = new ArrayList<Player>();
    private List<Player> spectatorList = new ArrayList<Player>();
    public Map<Player, Table> playerTableMap = new HashMap<Player, Table>();
    private String roomID;
    private String roomName;
    private Player host;
    private boolean isRunning;
    public List<Player> alivePlayers = new ArrayList<Player>();
    private boolean backfire;
    private boolean isSingleplayer;

    public int index;
    
    public Room(Player host, boolean isSingleplayer) {
        Main.instance.noteBlockAPI.newRSP(this);
        String mkID;
        do {
            mkID = makeID();
        } while (!isUnique(mkID));
        roomID = mkID;

        this.host = host;
        addPlayer(host);
        Main.instance.roomByID.put(roomID, this);
        this.isSingleplayer = isSingleplayer;
        if (isSingleplayer) {
            roomName = "Singleplayer #" + roomID;
        } else {
            roomName = "Room #" + roomID;
        }
        index = -1;
    }
    
    public void addPlayer(Player player) {
        Main.instance.inWhichRoomIs.put(player, this);
        playerList.add(player);
        Table table = new Table(player, this);
        playerTableMap.put(player, table);
        Main.instance.noteBlockAPI.addPlayer(this, player);
        tryToUpdateMenu();
    }
    
    public void addSpectator(Player player) {
        spectatorList.add(player);
        player.sendMessage("Added, but this feature is work in progress");
    }
    
    public void eliminate(Player player) {
        alivePlayers.remove(player);
        if (alivePlayers.size() < 2) {
            stopRoom();
        }
    }
    
    public void forwardGarbage(int n, Player sender) {
        if (n > 0) {
            int random = (int) (Math.random() * alivePlayers.size());
            Table table = playerTableMap.get(alivePlayers.get(random));
            Player receiver = table.getPlayer();
            if (receiver != sender || backfire) {
                table.receiveGarbage(n);
            }
        }
    }
    
    public boolean getBackfire() {
        return backfire;
    }
    
    public Player getHost() {
        return host;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public boolean getIsSingleplayer() {
        return isSingleplayer;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void removePlayer(Player player) {
        Main.instance.noteBlockAPI.removePlayer(this, player);
        playerTableMap.get(player).destroyTable();
        playerTableMap.get(player).setGameOver(true);
        eliminate(player);
        playerList.remove(player);
        playerTableMap.remove(player);
        Main.instance.inWhichRoomIs.remove(player);
        if (player == host) {
            if (playerList.size() == 0) {
                Main.instance.roomByID.remove(roomID);
            } else {
                host = playerList.get(0);
                host.sendMessage("[TETR] Since the old room host left, you became the new host.");
            }
        }
        tryToUpdateMenu();
    }

    public void removeSpectator(Player player) {
        spectatorList.remove(player);
    }

    public void startRoom() {
        if (!isSingleplayer && playerList.size() == 1) {
            host.sendMessage("[TETR] 2 players are needed");
        } else {

            Main.instance.noteBlockAPI.startPlaying(this, index);

            Random x = new Random();
            long seed = x.nextInt();
            long seed2 = x.nextInt();

            for (Player player : playerList) {
                Table table = playerTableMap.get(player);
                table.initTable(seed, seed2);
            }
            alivePlayers = new ArrayList<Player>(playerList);
            roomLoop();
        }
        tryToUpdateMenu();
    }

    public void stopRoom() {
        isRunning = false;
    }

    public void toggleBackfire() {
        backfire ^= true;
    }

    private void afterLoopStopped() {
        Main.instance.noteBlockAPI.setPlaying(this, false);

        for (Player player : alivePlayers) {
            playerTableMap.get(player).setGameOver(true);
        }
        tryToUpdateMenu();
    }

    private void roomLoop() {
        isRunning = true;
        new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        afterLoopStopped();
                    }
                }.runTask(Main.instance);
            }
        }.start();
    }

    @SuppressWarnings("unused")
    private void switchBracket(Player player) {
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
    
    private void tryToUpdateMenu() {
        for(Player player: playerList) {
            if(Main.instance.hasCustomMenuOpen.get(player) == true && Main.instance.lastMenuOpened.get(player) == "room") {
                Choice.maximizeMenu(player);
            }
        }
    }

}
