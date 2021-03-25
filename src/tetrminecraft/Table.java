package tetrminecraft;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;

import tetrcore.GameLogic;

public class Table extends GameLogic {

    public static boolean transparent = false;
    private static final int VISIBLEROWS = 30;

    private static final int BACKGROUNDROWS = 20;

    boolean destroying = false;
    private World world;
    private Player player;
    private int looptick;

    private int gx = 100;
    private int gy = 50;
    private int gz = 0;
    public int m1x = 1;
    public int m1y = 0;
    public int m2x = 0;
    public int m2y = -1;
    public int m3x = 0;
    public int m3y = 0;
    public int thickness = 1;

    // intermediate variables
    private int coni;
    private int conj;

    private int conk;
    public boolean ULTRAGRAPHICS = true;

    double maxvelocity = 0;

    long startTime;

    boolean moving = false;

    String direction;

    boolean singlemove;

    Table(Player p) {
        super();
        player = p;
        world = p.getWorld();
        Location location = player.getLocation();
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        if (45 <= yaw && yaw < 135) {
            rotateTable("Y");
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX() - GameLogic.STAGESIZEY,
                    location.getBlockY() + GameLogic.STAGESIZEY - VISIBLEROWS / 2,
                    location.getBlockZ() + GameLogic.STAGESIZEX / 2);
        } else if (135 <= yaw && yaw < 225) {
            moveTable(location.getBlockX() - GameLogic.STAGESIZEX / 2,
                    location.getBlockY() + GameLogic.STAGESIZEY - VISIBLEROWS / 2,
                    location.getBlockZ() - GameLogic.STAGESIZEY);
        } else if (225 <= yaw && yaw < 315) {
            rotateTable("Y");
            moveTable(location.getBlockX() + GameLogic.STAGESIZEY,
                    location.getBlockY() + GameLogic.STAGESIZEY - VISIBLEROWS / 2,
                    location.getBlockZ() - GameLogic.STAGESIZEX / 2);
        } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
            rotateTable("Y");
            rotateTable("Y");
            moveTable(location.getBlockX() + GameLogic.STAGESIZEX / 2,
                    location.getBlockY() + GameLogic.STAGESIZEY - VISIBLEROWS / 2,
                    location.getBlockZ() + GameLogic.STAGESIZEY);
        }
        setGameover(true);
    }

    public void destroyTable() {
        boolean ot = transparent;
        transparent = true;
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        transparent = ot;
        setGameover(true);
        Main.instance.netherboard.removeBoard(player);
        destroying = true;
    }

    public int getGx() {
        return gx;
    }

    public int getGy() {
        return gy;
    }

    public int getGz() {
        return gz;
    }

    public int getLooptick() {
        return looptick;
    }

    public Player getPlayer() {
        return player;
    }

    public void moveTable(int x, int y, int z) {
        boolean ot = transparent;
        transparent = true;
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                colPrintNewRender(j, i, 7);
            }
        }
        gx = x;
        gy = y;
        gz = z;
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }

    public void moveTableRelative(int x, int y, int z) {
        moveTable(gx + x, gy + y, gz + z);
    }

    public void initTable(long seed, long seed2) {

        coni = Math.max(Math.abs(m1x), Math.abs(m1y));
        conj = Math.max(Math.abs(m2x), Math.abs(m2y));
        conk = Math.max(Math.abs(m3x), Math.abs(m3y));

        player.getInventory().setHeldItemSlot(8);

        looptick = 0;

        initGame();
        initScoreboard();
        gameLoop();
    }

    public void rotateTable(String input) {
        boolean ot = transparent;
        transparent = true;
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                colPrintNewRender(j, i, 7);
            }
        }

        int temp;
        switch (input) {
        case "X":
            temp = -m3x;
            m3x = m2x;
            m2x = temp;
            temp = -m3y;
            m3y = m2y;
            m2y = temp;
            break;
        case "Y":
            temp = -m3x;
            m3x = m1x;
            m1x = temp;
            temp = -m3y;
            m3y = m1y;
            m1y = temp;
            break;
        case "Z":
            temp = -m2x;
            m2x = m1x;
            m1x = temp;
            temp = -m2y;
            m2y = m1y;
            m1y = temp;
            break;
        }

        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                colPrintNewRender(j, i, 16);
            }
        }
        transparent = ot;
    }

    public void setGameOver(boolean value) {
        setGameover(value);
    }

    @SuppressWarnings("deprecation")
    public boolean userInput(String input) {
        if (!getGameover()) {
            switch (input) {
            case "y":
                if (rotatePiece(-1)) {
                    boolean v = checkTSpin();
                    if (v) {
                        playSound(XSound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
                        settSpin(v);
                    }
                }
                break;
            case "x":
                if (rotatePiece(+1)) {
                    boolean v = checkTSpin();
                    if (v) {
                        playSound(XSound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
                        settSpin(v);
                    }
                }
                break;
            case "c":
                if (!holdPiece()) {
                    playSound(XSound.ENTITY_SPLASH_POTION_BREAK, 1f, 1f);
                }
                break;
            case "left":
                movePieceRelative(-1, 0);
                break;
            case "right":
                movePieceRelative(+1, 0);
                break;

            case "up":
                if (rotatePiece(+2)) {
                    boolean v = checkTSpin();
                    if (v) {
                        playSound(XSound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
                        settSpin(v);
                    }
                }
                break;
            case "down":
                movePieceRelative(0, +1);
                break;

            case "space":
                hardDropPiece();
                break;
            case "l":
                setGameOver(true);
                break;
            case "instant":
                sonicDrop();
                break;
            case "shift":
                startZone();
            default:
                System.out.println("wee woo wee woo");
            }
        }
        return false;
    }

    private void colPrintNewRender(float x, float y, int color) {
        int tex, tey, tez;
        for (int i = 0; i < (coni != 0 ? coni : thickness); i++) {
            tex = gx + (int) Math.floor(x * m1x) + (int) Math.floor(y * m1y) + i;
            for (int j = 0; j < (conj != 0 ? conj : thickness); j++) {
                tey = gy + (int) Math.floor(x * m2x) + (int) Math.floor(y * m2y) + j;
                for (int k = 0; k < (conk != 0 ? conk : thickness); k++) {
                    tez = gz + (int) Math.floor(x * m3x) + (int) Math.floor(y * m3y) + k;
                    printSingleBlock(tex, tey, tez, color);
                }
            }
        }

    }

    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }

    private void gameLoop() {
        // thread unsafe code
        new BukkitRunnable() {
            @Override
            public void run() {
                if (destroying) {
                    this.cancel();
                } else if (getGameover()) {
                    this.cancel();
                    whenPlayerDies();
                } else {
                    render();
                    looptick++;
                }
            }
        }.runTaskTimer(Main.instance, 0, 1);
    }

    // rendering functions

    private void initScoreboard() {
        Main.instance.netherboard.createBoard(player, "Stats");
    }

    private void playSound(XSound xSound, float volume, float pitch) {
        Sound sound = xSound.parseSound();
        if (volume < 1) {
            player.playSound(player.getEyeLocation(), sound, volume, pitch);
        } else {
            for (int i = 0; i < volume; i++) {
                player.playSound(player.getEyeLocation(), sound, 1f, pitch);
            }
        }
    }

    private void printSingleBlock(int x, int y, int z, int color) {
        if (color == 7 && transparent) {
            Block b = world.getBlockAt(x, y, z);
            for (Player player : Main.instance.inWhichRoomIs.get(player).playerList) {
                Main.instance.functions.sendBlockChangeCustom(player, new Location(world, x, y, z), b);
            }
            return;
        }

        for (Player player : Main.instance.inWhichRoomIs.get(player).playerList) {
            Main.instance.functions.sendBlockChangeCustom(player, new Location(world, x, y, z), color);
        }
    }

    private void printStaticPieceNewRender(int x, int y, int block) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewRender(j + x, i + y, 7);
            }
        }

        if (block != -1) {
            for (Point point : getPiece(block, 0)) {
                colPrintNewRender(x + point.x, y + point.y, block);
            }
        }
    }

    int[][] oldStageDisplay = new int[GameLogic.STAGESIZEY][GameLogic.STAGESIZEX];

    private void render() {
        Point cpp = getCurrentPiecePosition();
        int rot = getCurrentPieceRotation();
        int piece = getCurrentPieceInt();
        int[][] stage = getStage();
        int[][] newStageDisplay = new int[40][10];

        // update stage
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                newStageDisplay[i][j] = stage[i][j];
            }
        }

        // print next queue
        for (int i = 0; i < GameLogic.NEXTPIECESMAX; i++) {
            printStaticPieceNewRender(GameLogic.STAGESIZEX + 3, GameLogic.STAGESIZEY / 2 + i * 4,
                    getNextPieces().get(i));
        }

        // print held piece
        printStaticPieceNewRender(-7, GameLogic.STAGESIZEY / 2, getHeldPiece());

        // update ghost
        for (Point point : getPiece(piece, rot)) {
            newStageDisplay[point.y + getCurrentPieceLowestPossiblePosition()][point.x + cpp.x] = 9 + piece;
        }

        // update current piece
        for (Point point : getPiece(piece, rot)) {
            newStageDisplay[point.y + cpp.y][point.x + cpp.x] = piece;
        }

        // print garbage meter
        int total = 0;
        for (int num : getGarbageQueue()) {
            total += num;
        }

        for (int i = 0; i < GameLogic.PLAYABLEROWS; i++) {
            colPrintNewRender(-2, GameLogic.STAGESIZEY - 1 - i, 7);
        }

        for (int i = 0; i < total; i++) {
            colPrintNewRender(-2, GameLogic.STAGESIZEY - 1 - i % GameLogic.PLAYABLEROWS,
                    (i / (GameLogic.STAGESIZEY / 2)) % 7);
        }

        // print stage+piece+ghost
        for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
            for (int j = 0; j < GameLogic.STAGESIZEX; j++) {

                if (((newStageDisplay[i][j] == 7 && i >= GameLogic.STAGESIZEY - BACKGROUNDROWS)
                        || (i >= GameLogic.STAGESIZEY - VISIBLEROWS))
                        && newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    colPrintNewRender(j, i, newStageDisplay[i][j]);
                }
            }
        }

        // send scoreboard

        sendScoreboard();

        // send magic string action bar
        ActionBar.sendActionBar(player, getMagicString());

        oldStageDisplay = newStageDisplay;
    }

    private void sendScoreboard() {
        Map<Integer, String> text = new HashMap<Integer, String>();

        if (getCombo() > 0) {
            text.put(7, "Combo: " + getCombo());
        } else {
            text.put(7, null);
        }

        text.put(6, "Garbage received: " + getTotalGarbageReceived());
        text.put(5, "Lines: " + getTotalLinesCleared());
        text.put(4, "Pieces: " + getTotalPiecesPlaced());
        text.put(3, "Score: " + getScore());

        if (getBackToBack() > 0) {
            text.put(2, "Back to back: " + getBackToBack());
        } else {
            text.put(2, null);
        }

        text.put(1, "Time: " + looptick);
        text.put(0, "getcounter: " + getCounter());

        Main.instance.netherboard.sendScoreboard(player, text);
    }

    @SuppressWarnings("deprecation")
    private void turnToFallingBlock(int x, int y, double d) {
        if (ULTRAGRAPHICS == true) {
            int tex, tey, tez;
            ItemStack blocks[] = Blocks.defaultBlocks;
            int color = getStage()[y][x];
            for (int i = 0; i < (coni != 0 ? coni : thickness); i++) {
                tex = gx + x * m1x + y * m1y + i;
                for (int j = 0; j < (conj != 0 ? conj : thickness); j++) {
                    tey = gy + x * m2x + y * m2y + j;
                    for (int k = 0; k < (conk != 0 ? conk : thickness); k++) {
                        tez = gz + x * m3x + y * m3y + k;
                        FallingBlock lol = world.spawnFallingBlock(new Location(world, tex, tey, tez),
                                blocks[color].getType(), blocks[color].getData().getData());
                        lol.setVelocity(new Vector(d * (2 - Math.random() * 4), d * (5 - Math.random() * 10),
                                d * (2 - Math.random() * 4)));
                        lol.setDropItem(false);
                        lol.addScoreboardTag("sand");
                    }
                }
            }
        }
    }

    private void whenPlayerDies() {
        switch (Constants.deathAnim) {
        case EXPLOSION:
            boolean ot = transparent;
            transparent = true;
            for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
                for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                    colPrintNewRender(j, i, 7);
                }
            }
            transparent = ot;

            for (int i = GameLogic.STAGESIZEY - VISIBLEROWS; i < GameLogic.STAGESIZEY; i++) {
                for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                    turnToFallingBlock(j, i, 1);
                }
            }
            break;
        case GRAYSCALE:
            for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
                for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 8);
                }
            }
            break;
        case CLEAR:
            for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
                for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 7);
                }
            }
        case DISAPPEAR:
            for (int i = 0; i < GameLogic.STAGESIZEY; i++) {
                for (int j = 0; j < GameLogic.STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 7);
                }
            }

        case NONE:
            break;
        }
    }

    @Override
    public void sendGarbageEvent(int n) {
        Main.instance.inWhichRoomIs.get(player).forwardGarbage(n, player);
    }

}
