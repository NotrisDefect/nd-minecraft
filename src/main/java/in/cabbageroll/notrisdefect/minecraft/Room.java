package in.cabbageroll.notrisdefect.minecraft;

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
    public int songIndex;
    private List<Player> alivePlayers = new ArrayList<>();
    private Player owner;
    private boolean isRunning;
    private boolean backfire;

    public Room(Player owner, boolean isSingleplayer) {
        Main.NOTEBLOCKAPI.newRSP(this);
        roomID = Integer.toHexString(hashCode());

        this.owner = owner;
        addPlayer(owner);
        this.isSingleplayer = isSingleplayer;
        roomName = (isSingleplayer ? "Singleplayer @" : "Multiplayer @") + roomID;
        songIndex = -1;
    }

    public void addPlayer(Player player) {
        players.add(player);
        Main.GS.getTable(player).joinRoom(this);
        Main.NOTEBLOCKAPI.addPlayer(this, player);
        if (isRunning) {
            player.sendMessage(Strings.gameInProgress);
            for (Player p : players) {
                getTable(p).forceFullRender();
            }
        } else {
            for (Player p : players) {
                getTable(p).drawLogo();
            }
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
                table.doAddGarbage(n);
            }
        }
    }

    public boolean getBackfire() {
        return backfire;
    }

    public Player getOwner() {
        return owner;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSingleplayer() {
        return isSingleplayer;
    }

    public void removePlayer(Player player) {
        Main.NOTEBLOCKAPI.removePlayer(this, player);
        Table table = getTable(player);
        if (table.getGameState() != Table.STATE_DEAD) {
            table.doAbort();
        }
        table.destroyTable();
        players.remove(player);
        Main.GS.getTable(player).leaveRoom();
        if (player == owner) {
            if (players.size() == 0) {
                Main.GS.popRoom(this);
            } else {
                owner = players.get(0);
                owner.sendMessage(Strings.ownerChange);
            }
        }
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public void startRoom() {
        isRunning = true;
        Main.NOTEBLOCKAPI.startPlaying(this, songIndex);

        long seed = (long) (Math.random() * Long.MAX_VALUE);

        for (Player player : players) {
            Table table = getTable(player);
            table.initTable(seed);
        }
        alivePlayers = new ArrayList<>(players);
        roomLoop();
    }

    public void stopRoom() {
        isRunning = false;
    }

    public void toggleBackfire() {
        backfire ^= true;
    }

    private void afterLoopStopped() {
        Main.NOTEBLOCKAPI.setPlaying(this, false);

        for (Player player : alivePlayers) {
            getTable(player).doAbort();
            getTable(player).drawLogo();
        }
    }

    private Table getTable(Player player) {
        return Main.GS.getTable(player);
    }

    private void roomLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) {
                    tick();
                } else {
                    afterLoopStopped();
                    cancel();
                }
            }
        }.runTaskTimer(Main.PLUGIN, 80, 1);
    }

    private void tick() {
        ArrayList<Player> stillAlivePlayers = new ArrayList<>(alivePlayers);
        for (Player player : alivePlayers) {
            Table table = getTable(player);
            if (table == null || table.getGameState() == Table.STATE_DEAD) {
                stillAlivePlayers.remove(player);
            } else {
                for (Player p: players) {
                    table.render(p);
                }
                table.sendScoreboard();
                table.checkMovement();
                table.doTick();
            }
        }
        alivePlayers = stillAlivePlayers;
        if (alivePlayers.size() < (isSingleplayer ? 1 : 2)) {
            stopRoom();
        }
    }

}
