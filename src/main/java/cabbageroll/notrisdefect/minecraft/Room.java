package cabbageroll.notrisdefect.minecraft;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public final List<Player> players = new ArrayList<>();
    public final List<Player> spectators = new ArrayList<>();
    private final String roomID;
    private final String roomName;
    private final boolean isSingleplayer;
    public List<Player> alivePlayers = new ArrayList<>();
    public int index;
    private Player host;
    private boolean isRunning;
    private boolean backfire;

    public Room(Player host, boolean isSingleplayer) {
        Main.noteBlockAPI.newRSP(this);
        roomID = Integer.toHexString(hashCode());

        this.host = host;
        addPlayer(host);
        this.isSingleplayer = isSingleplayer;
        if (isSingleplayer) {
            roomName = "Singleplayer @" + roomID;
        } else {
            roomName = "Multiplayer @" + roomID;
        }
        index = -1;
    }

    public void addPlayer(Player player) {
        players.add(player);
        Main.gs.getTable(player).joinRoom(this);
        Main.noteBlockAPI.addPlayer(this, player);
        if (this.isRunning) {
            player.sendMessage(Strings.gameInProgress);
        }
        for (Player p : players) {
            getTable(p).updateWholeTableTo(player);
        }
    }

    public void addSpectator(Player player) {
        spectators.add(player);
        player.sendMessage("Added, but this feature is work in progress");
    }

    public void forwardGarbage(int n, Player sender) {
        if (n > 0) {
            Table table;
            if (!isSingleplayer || backfire) {
                do {
                    int random = (int) (Math.random() * alivePlayers.size());
                    table = getTable(alivePlayers.get(random));
                } while (table.getPlayer() == sender && !backfire);
                table.extAddGarbage(n);
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
        Main.noteBlockAPI.removePlayer(this, player);
        getTable(player).extAbortGame();
        getTable(player).destroyTable();
        players.remove(player);
        Main.gs.getTable(player).leaveRoom();
        if (player == host) {
            if (players.size() == 0) {
                Main.gs.deleteRoom(roomID);
            } else {
                host = players.get(0);
                host.sendMessage(Strings.hostChange);
            }
        }
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public void startRoom() {
        isRunning = true;

        if (isSingleplayer || players.size() >= 2) {

            Main.noteBlockAPI.startPlaying(this, index);

            long seed = (long) (Math.random() * Long.MAX_VALUE);

            for (Player player : players) {
                Table table = getTable(player);
                table.initTable(seed);
            }
            alivePlayers = new ArrayList<>(players);
            roomLoop();
        } else {
            host.sendMessage(Strings.notEnoughPlayers);
        }

    }

    public void stopRoom() {
        isRunning = false;
    }

    public void toggleBackfire() {
        backfire ^= true;
    }

    private void afterLoopStopped() {
        Main.noteBlockAPI.setPlaying(this, false);

        for (Player player : alivePlayers) {
            getTable(player).extAbortGame();
        }
    }

    private Table getTable(Player player) {
        return Main.gs.getTable(player);
    }

    private void roomLoop() {
        new Thread(() -> {
            final double expectedTickTime = 1e9 / Table.TPS;
            long timeLast = System.nanoTime();
            long timeNow;
            double delta = 0;
            while (isRunning) {
                timeNow = System.nanoTime();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                delta += (timeNow - timeLast) / expectedTickTime;
                timeLast = timeNow;
                while (delta >= 1) {
                    tick();
                    delta--;
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    afterLoopStopped();
                }
            }.runTask(Main.plugin);
        }).start();
    }

    private void tick() {
        ArrayList<Player> stillAlivePlayers = new ArrayList<>(alivePlayers);
        for (Player player : alivePlayers) {
            Table table = getTable(player);
            if (table == null || table.isDead()) {
                stillAlivePlayers.remove(player);
            } else {
                table.extTick();
            }
        }
        alivePlayers = stillAlivePlayers;
        if (stillAlivePlayers.size() < (isSingleplayer ? 1 : 2)) {
            stopRoom();
        }
    }

}
