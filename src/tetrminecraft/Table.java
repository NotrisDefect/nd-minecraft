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

    private static final int VISIBLEROWS = 30;
    private static final int BACKGROUNDROWS = 20;

    boolean isGettingDestroyed = false;
    private World world;
    private Player player;
    private int looptick;

    private int gx;
    private int gy;
    private int gz;
    public int mwx = 1;
    public int mhx = 0;
    public int mwy = 0;
    public int mhy = -1;
    public int mwz = 0;
    public int mhz = 0;
    public int thickness = 1;

    private int coni;
    private int conj;
    private int conk;
    public boolean enableFallingSand = true;

    private int[][] oldStageDisplay = new int[STAGESIZEY][STAGESIZEX];
    private int[] oldGQDisplay = new int[GameLogic.PLAYABLEROWS];
    private int[][] oldNextDisplay = new int[GameLogic.NEXTPIECESMAX * 4][4];
    private int[][] oldHoldDisplay = new int[4][4];

    public Table(Player p) {
        super();
        player = p;
        world = p.getWorld();
        Location location = player.getLocation();
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        int temp;
        if (45 <= yaw && yaw < 135) {
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            gx = location.getBlockX() - STAGESIZEY;
            gy = location.getBlockY() + STAGESIZEY - VISIBLEROWS / 2;
            gz = location.getBlockZ() + STAGESIZEX / 2;
        } else if (135 <= yaw && yaw < 225) {
            gx = location.getBlockX() - STAGESIZEX / 2;
            gy = location.getBlockY() + STAGESIZEY - VISIBLEROWS / 2;
            gz = location.getBlockZ() - STAGESIZEY;
        } else if (225 <= yaw && yaw < 315) {
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            gx = location.getBlockX() + STAGESIZEY;
            gy = location.getBlockY() + STAGESIZEY - VISIBLEROWS / 2;
            gz = location.getBlockZ() - STAGESIZEX / 2;
        } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            gx = location.getBlockX() + STAGESIZEX / 2;
            gy = location.getBlockY() + STAGESIZEY - VISIBLEROWS / 2;
            gz = location.getBlockZ() + STAGESIZEY;
        }
        drawAll(16);
        setGameover(true);
    }

    public void destroyTable() {
        cleanAll();
        setGameover(true);
        Main.instance.netherboard.removeBoard(player);
        isGettingDestroyed = true;
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

    public void initTable(long seed, long seed2) {

        coni = Math.max(Math.abs(mwx), Math.abs(mhx));
        conj = Math.max(Math.abs(mwy), Math.abs(mhy));
        conk = Math.max(Math.abs(mwz), Math.abs(mhz));

        player.getInventory().setHeldItemSlot(8);

        looptick = 0;

        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                oldStageDisplay[i][j] = 7;
                colPrintNewRender(j, i, 7);
            }
        }
        for (int i = 0; i < GameLogic.PLAYABLEROWS; i++) {
            oldGQDisplay[i] = 7;
            colPrintNewRender(-2, STAGESIZEY - 1 - i, 7);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                oldNextDisplay[i][j] = 7;
                colPrintNewRender(STAGESIZEX + 3 + j, STAGESIZEY / 2 + i, 7);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                oldHoldDisplay[i][j] = 7;
                colPrintNewRender(-7 + j, STAGESIZEY / 2 + i, 7);
            }
        }

        initGame();
        Main.instance.netherboard.createBoard(player, "Stats");
        gameLoop();
    }

    public void moveTable(int x, int y, int z) {
        cleanAll();
        gx = x;
        gy = y;
        gz = z;
        drawAll(16);
    }

    public void moveTableRelative(int x, int y, int z) {
        moveTable(gx + x, gy + y, gz + z);
    }

    @Override
    public void onLineClearEvent(int lineNumber, int[] line) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < STAGESIZEX; i++) {
                    turnToFallingBlock(i, lineNumber, 0.3, line[i]);
                }
            }
        }.runTask(Main.instance);
    }

    public void rotateTable(String input) {
        cleanAll();

        int temp;
        switch (input) {
        case "X":
            temp = -mwz;
            mwz = mwy;
            mwy = temp;
            temp = -mhz;
            mhz = mhy;
            mhy = temp;
            break;
        case "Y":
            temp = -mwz;
            mwz = mwx;
            mwx = temp;
            temp = -mhz;
            mhz = mhx;
            mhx = temp;
            break;
        case "Z":
            temp = -mwy;
            mwy = mwx;
            mwx = temp;
            temp = -mhy;
            mhy = mhx;
            mhx = temp;
            break;
        }

        drawAll(16);
    }

    @Override
    public void sendGarbageEvent(int n) {
        Main.instance.inWhichRoomIs.get(player).forwardGarbage(n, player);
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

    private void cleanAll() {
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewForce(j, i);
            }
        }
        for (int i = 0; i < GameLogic.PLAYABLEROWS; i++) {
            colPrintNewForce(-2, STAGESIZEY - 1 - i);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewForce(STAGESIZEX + 3 + j, STAGESIZEY / 2 + i);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewForce(-7 + j, STAGESIZEY / 2 + i);
            }
        }
    }

    private void colPrintNewForce(float x, float y) {
        int tex, tey, tez;

        for (int i = 0; i < (coni != 0 ? coni : thickness); i++) {
            tex = gx + (int) Math.floor(x * mwx) + (int) Math.floor(y * mhx) + i;
            for (int j = 0; j < (conj != 0 ? conj : thickness); j++) {
                tey = gy + (int) Math.floor(x * mwy) + (int) Math.floor(y * mhy) + j;
                for (int k = 0; k < (conk != 0 ? conk : thickness); k++) {
                    tez = gz + (int) Math.floor(x * mwz) + (int) Math.floor(y * mhz) + k;
                    Block b = world.getBlockAt(tex, tey, tez);
                    for (Player player : Main.instance.inWhichRoomIs.get(player).playerList) {
                        Main.instance.functions.sendBlockChangeCustom(player, new Location(world, tex, tey, tez), b);
                    }
                }
            }
        }
    }

    private void colPrintNewRender(float x, float y, int color) {
        int tex, tey, tez;

        for (int i = 0; i < (coni != 0 ? coni : thickness); i++) {
            tex = gx + (int) Math.floor(x * mwx) + (int) Math.floor(y * mhx) + i;
            for (int j = 0; j < (conj != 0 ? conj : thickness); j++) {
                tey = gy + (int) Math.floor(x * mwy) + (int) Math.floor(y * mhy) + j;
                for (int k = 0; k < (conk != 0 ? conk : thickness); k++) {
                    tez = gz + (int) Math.floor(x * mwz) + (int) Math.floor(y * mhz) + k;
                    printSingleBlock(tex, tey, tez, color);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }

    private void drawAll(int color) {
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewRender(j, i, color);
            }
        }
        for (int i = 0; i < GameLogic.PLAYABLEROWS; i++) {
            colPrintNewRender(-2, STAGESIZEY - 1 - i, color);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewRender(STAGESIZEX + 3 + j, STAGESIZEY / 2 + i, color);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewRender(-7 + j, STAGESIZEY / 2 + i, color);
            }
        }
    }

    private void gameLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isGettingDestroyed) {
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
        if (color == 7 && Main.instance.playerTransparentBackground.get(player)) {
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

    private void render() {
        final Point cpp = getCurrentPiecePosition();
        final int rot = getCurrentPieceRotation();
        final int piece = getCurrentPieceInt();
        final int[][] stage = getStage();

        int[][] newStageDisplay = new int[STAGESIZEY][STAGESIZEX];
        int[] newGQDisplay = new int[GameLogic.PLAYABLEROWS];
        int[][] newNextDisplay = new int[GameLogic.NEXTPIECESMAX * 4][4];
        int[][] newHoldDisplay = new int[4][4];

        // update stage
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                newStageDisplay[i][j] = stage[i][j];
            }
        }

        for (Point point : getPiece(piece, rot)) {
            newStageDisplay[point.y + getCurrentPieceLowestPossiblePosition()][point.x + cpp.x] = 9 + piece;
        }

        for (Point point : getPiece(piece, rot)) {
            newStageDisplay[point.y + cpp.y][point.x + cpp.x] = piece;
        }

        // update garbage meter
        int total = 0;
        int color;

        for (int num : getGarbageQueue()) {
            total += num;
        }

        color = (total / GameLogic.PLAYABLEROWS) % 7;
        total = total % GameLogic.PLAYABLEROWS;

        for (int i = 0; i < total; i++) {
            newGQDisplay[i] = color;
        }

        for (int i = total; i < GameLogic.PLAYABLEROWS; i++) {
            newGQDisplay[i] = 7;
        }

        // update next queue
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                newNextDisplay[i][j] = 7;
            }
        }

        for (int i = 0; i < NEXTPIECESMAX; i++) {
            for (Point point : getPiece(getNextPieces().get(i), 0)) {
                newNextDisplay[point.y + i * 4][point.x] = getNextPieces().get(i);
            }
        }

        // update hold
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newHoldDisplay[i][j] = 7;
            }
        }

        if (getHeldPiece() != -1) {
            for (Point point : getPiece(getHeldPiece(), 0)) {
                newHoldDisplay[point.y][point.x] = getHeldPiece();
            }
        }

        // print stage
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {

                if (((newStageDisplay[i][j] == 7 && i >= STAGESIZEY - BACKGROUNDROWS)
                        || (i >= STAGESIZEY - VISIBLEROWS)) && newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    colPrintNewRender(j, i, newStageDisplay[i][j]);
                }
            }
        }

        // print garbage meter
        for (int i = total; i < GameLogic.PLAYABLEROWS; i++) {
            if (newGQDisplay[i] != oldGQDisplay[i]) {
                colPrintNewRender(-2, STAGESIZEY - 1 - i, newGQDisplay[i]);
            }
        }

        // print next queue
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                if (newNextDisplay[i][j] != oldNextDisplay[i][j]) {
                    colPrintNewRender(STAGESIZEX + 3 + j, STAGESIZEY / 2 + i, newNextDisplay[i][j]);
                }
            }
        }

        // print hold
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (newHoldDisplay[i][j] != oldHoldDisplay[i][j]) {
                    colPrintNewRender(-7 + j, STAGESIZEY / 2 + i, newHoldDisplay[i][j]);
                }
            }
        }

        sendScoreboard();

        // send magic string action bar
        ActionBar.sendActionBar(player, getMagicString());

        oldStageDisplay = newStageDisplay;
        oldGQDisplay = newGQDisplay;
        oldNextDisplay = newNextDisplay;
        oldHoldDisplay = newHoldDisplay;
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
    private void turnToFallingBlock(int x, int y, double d, int color) {
        if (enableFallingSand == true) {
            int tex, tey, tez;
            ItemStack blocks[] = Blocks.defaultBlocks;
            for (int i = 0; i < (coni != 0 ? coni : thickness); i++) {
                tex = gx + x * mwx + y * mhx + i;
                for (int j = 0; j < (conj != 0 ? conj : thickness); j++) {
                    tey = gy + x * mwy + y * mhy + j;
                    for (int k = 0; k < (conk != 0 ? conk : thickness); k++) {
                        tez = gz + x * mwz + y * mhz + k;
                        FallingBlock lol = world.spawnFallingBlock(new Location(world, tex, tey, tez),
                                blocks[color].getType(), blocks[color].getData().getData());
                        lol.setVelocity(new Vector(d * (2 - Math.random() * 4) * coni,
                                d * (8 - Math.random() * 10) * conj, d * (2 - Math.random() * 4) * conk));
                        lol.setDropItem(false);
                        lol.addScoreboardTag("sand");
                    }
                }
            }
        }
    }

    private void whenPlayerDies() {
        Main.instance.inWhichRoomIs.get(player).eliminate(player);

        switch (Constants.deathAnim) {
        case EXPLOSION:
            for (int i = 0; i < STAGESIZEY; i++) {
                for (int j = 0; j < STAGESIZEX; j++) {
                    colPrintNewForce(j, i);
                }
            }

            for (int i = STAGESIZEY - VISIBLEROWS; i < STAGESIZEY; i++) {
                for (int j = 0; j < STAGESIZEX; j++) {
                    turnToFallingBlock(j, i, 1, getStage()[i][j]);
                }
            }
            break;
        case GRAYSCALE:
            for (int i = 0; i < STAGESIZEY; i++) {
                for (int j = 0; j < STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 8);
                }
            }
            break;
        case CLEAR:
            for (int i = 0; i < STAGESIZEY; i++) {
                for (int j = 0; j < STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 7);
                }
            }
        case DISAPPEAR:
            for (int i = 0; i < STAGESIZEY; i++) {
                for (int j = 0; j < STAGESIZEX; j++) {
                    if (getStage()[i][j] != 7)
                        colPrintNewRender(j, i, 7);
                }
            }

        case NONE:
            break;
        }
    }

}
