package cabbageroll.notrisdefect;

import cabbageroll.notrisdefect.functions.util.Point3Dint;
import cabbageroll.notrisdefect.initialization.Sounds;
import cabbageroll.notrisdefect.menus.Menu;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tetrcore.GameLogic;
import tetrcore.Piece;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Table extends GameLogic {
    private static final int FRONTROWS = 30;
    private static final int BACKROWS = 20;
    private final Player player;
    private final int thickness = 1;
    public boolean enableAnimations = true;
    private int holdTLCX = -7;
    private int holdTLCY = STAGESIZEY / 2;
    private int nextTLCX = STAGESIZEX + 3;
    private int nextTLCY = STAGESIZEY / 2;
    private Room room;
    private Menu lastMenuOpened;
    private Point3Dint widthMultiplier;
    private Point3Dint heightMultiplier;
    private Location location;
    private int imi;
    private int imj;
    private int imk;
    private int[][] oldStageDisplay = new int[STAGESIZEY][STAGESIZEX];
    private int[] oldGQDisplay = new int[PLAYABLEROWS];
    private int[][] oldNextDisplay = new int[NEXTPIECESMAX * 4][4];
    private int[][] oldHoldDisplay = new int[4][4];

    public Table(Player player) {
        super();
        this.player = player;
    }

    public void destroyTable() {
        cleanAll();
        extAbortGame();
        Main.netherboard.removeBoard(player);
    }

    @Override
    public void evtGameover() {
        room.eliminate(player);

        switch (Constants.deathAnim) {
            case EXPLOSION:
                for (int i = 0; i < STAGESIZEY; i++) {
                    for (int j = 0; j < STAGESIZEX; j++) {
                        colPrintNewForce(j, i);
                    }
                }

                for (int i = STAGESIZEY - FRONTROWS; i < STAGESIZEY; i++) {
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
                        if (getStage()[i][j] != 7) {
                            colPrintNewRender(j, i, 7);
                        }
                    }
                }
            case DISAPPEAR:
                for (int i = 0; i < STAGESIZEY; i++) {
                    for (int j = 0; j < STAGESIZEX; j++) {
                        colPrintNewForce(j, i);
                    }
                }

            case NONE:
                break;
        }
    }

    @Override
    public void evtLineClear(int height, int[] line) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < STAGESIZEX; i++) {
                    turnToFallingBlock(i, height, 0.3, line[i]);
                }
            }
        }.runTask(Main.plugin);

        playSound(Sounds.lineClear, 5f, 1f);
    }

    @Override
    public void evtPerfectClear() {
        Main.functions.sendTitleCustom(player, "", "PERFECT CLEAR", 20, 20, 40);
    }

    @Override
    public void evtSendGarbage(int i) {
        room.forwardGarbage(i, player);
    }

    @Override
    public void evtSpin() {
        playSound(Sounds.spin, 5f, 1f);
    }

    public Point3Dint getHeightMultiplier() {
        return heightMultiplier;
    }

    public int getHoldTLCX() {
        return holdTLCX;
    }

    public int getHoldTLCY() {
        return holdTLCY;
    }

    public Menu getLastMenuOpened() {
        return lastMenuOpened;
    }

    public void setLastMenuOpened(Menu lastMenuOpened) {
        this.lastMenuOpened = lastMenuOpened;
    }

    public int getNextTLCX() {
        return nextTLCX;
    }

    public int getNextTLCY() {
        return nextTLCY;
    }

    public Player getPlayer() {
        return player;
    }

    public Room getRoom() {
        return room;
    }

    public Point3Dint getWidthMultiplier() {
        return widthMultiplier;
    }

    public int getX() {
        return location.getBlockX();
    }

    public int getY() {
        return location.getBlockY();
    }

    public int getZ() {
        return location.getBlockZ();
    }

    public void initTable(long seed) {
        prepare();
        extStartGame(seed);
        Main.netherboard.createBoard(player, "Stats");
        gameLoop();
    }

    public void joinRoom(Room r) {
        room = r;
        position();
        drawAll(16);
    }

    public void leaveRoom() {
        room = null;
    }

    public void moveHoldTLCXRelative(int n) {
        cleanAll();
        holdTLCX += n;
        drawAll(16);
    }

    public void moveHoldTLCYRelative(int n) {
        cleanAll();
        holdTLCY += n;
        drawAll(16);
    }

    public void moveNextTLCXRelative(int n) {
        cleanAll();
        nextTLCX += n;
        drawAll(16);
    }

    public void moveNextTLCYRelative(int n) {
        cleanAll();
        nextTLCY += n;
        drawAll(16);
    }

    public void moveTable(int x, int y, int z) {
        cleanAll();
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        drawAll(16);
    }

    public void moveTableRelative(int x, int y, int z) {
        moveTable(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
    }

    public void reposition() {
        cleanAll();
        position();
        drawAll(16);
    }

    public void rotateX() {
        cleanAll();
        widthMultiplier = new Point3Dint(widthMultiplier.getX(), -widthMultiplier.getZ(), widthMultiplier.getY());
        heightMultiplier = new Point3Dint(heightMultiplier.getX(), -heightMultiplier.getZ(), heightMultiplier.getY());
        drawAll(16);
    }

    public void rotateY() {
        cleanAll();
        widthMultiplier = new Point3Dint(widthMultiplier.getZ(), widthMultiplier.getY(), -widthMultiplier.getX());
        heightMultiplier = new Point3Dint(heightMultiplier.getZ(), heightMultiplier.getY(), -heightMultiplier.getX());
        drawAll(16);
    }

    public void rotateZ() {
        cleanAll();
        widthMultiplier = new Point3Dint(-widthMultiplier.getY(), widthMultiplier.getX(), widthMultiplier.getZ());
        heightMultiplier = new Point3Dint(-heightMultiplier.getY(), heightMultiplier.getX(), heightMultiplier.getZ());
        drawAll(16);
    }

    public void skewHX(int n) {
        cleanAll();
        heightMultiplier.addX(n);
        drawAll(16);
    }

    public void skewHY(int n) {
        cleanAll();
        heightMultiplier.addY(n);
        drawAll(16);
    }

    public void skewHZ(int n) {
        cleanAll();
        heightMultiplier.addZ(n);
        drawAll(16);
    }

    public void skewWX(int n) {
        cleanAll();
        widthMultiplier.addX(n);
        drawAll(16);
    }

    public void skewWY(int n) {
        cleanAll();
        widthMultiplier.addY(n);
        drawAll(16);
    }

    public void skewWZ(int n) {
        cleanAll();
        widthMultiplier.addZ(n);
        drawAll(16);
    }

    public void updateWholeTableTo(Player player) {

    }

    private void cleanAll() {
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewForce(j, i);
            }
        }
        for (int i = 0; i < PLAYABLEROWS; i++) {
            colPrintNewForce(-2, STAGESIZEY - 1 - i);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewForce(nextTLCX + j, nextTLCY + i);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewForce(holdTLCX + j, holdTLCY + i);
            }
        }
    }

    private void colPrintNewForce(float x, float y) {
        int blockX, blockY, blockZ;

        for (int i = 0; i < (imi != 0 ? imi : thickness); i++) {
            blockX = location.getBlockX() + (int) Math.floor(x * widthMultiplier.getX()) + (int) Math.floor(y * heightMultiplier.getX()) + i;
            for (int j = 0; j < (imj != 0 ? imj : thickness); j++) {
                blockY = location.getBlockY() + (int) Math.floor(x * widthMultiplier.getY()) + (int) Math.floor(y * heightMultiplier.getY()) + j;
                for (int k = 0; k < (imk != 0 ? imk : thickness); k++) {
                    blockZ = location.getBlockZ() + (int) Math.floor(x * widthMultiplier.getZ()) + (int) Math.floor(y * heightMultiplier.getZ()) + k;
                    Block b = location.getWorld().getBlockAt(blockX, blockY, blockZ);
                    for (Player player : room.players) {
                        Main.functions.sendBlockChangeCustom(player,
                            new Location(location.getWorld(), blockX, blockY, blockZ), b);
                    }
                }
            }
        }
    }

    private void colPrintNewRender(float x, float y, int color) {
        int blockX, blockY, blockZ;

        for (int i = 0; i < (imi != 0 ? imi : thickness); i++) {
            blockX = location.getBlockX() + (int) Math.floor(x * widthMultiplier.getX()) + (int) Math.floor(y * heightMultiplier.getX()) + i;
            for (int j = 0; j < (imj != 0 ? imj : thickness); j++) {
                blockY = location.getBlockY() + (int) Math.floor(x * widthMultiplier.getY()) + (int) Math.floor(y * heightMultiplier.getY()) + j;
                for (int k = 0; k < (imk != 0 ? imk : thickness); k++) {
                    blockZ = location.getBlockZ() + (int) Math.floor(x * widthMultiplier.getZ()) + (int) Math.floor(y * heightMultiplier.getZ()) + k;
                    printSingleBlockToAll(blockX, blockY, blockZ, color);
                }
            }
        }
    }

    private void colPrintNewRender2(float x, float y, int color) {
        int blockX, blockY, blockZ;
        int varI = imi != 0 ? imi : thickness;
        int varJ = imj != 0 ? imj : thickness;
        int varK = imk != 0 ? imk : thickness;
        for (int i = 0; i < varI * varJ * varK; i++) {
            blockX = location.getBlockX() + (int) Math.floor(x * widthMultiplier.getX()) + (int) Math.floor(y * heightMultiplier.getX()) + i % varI * varJ * varK;

            blockY = location.getBlockY() + (int) Math.floor(x * widthMultiplier.getY()) + (int) Math.floor(y * heightMultiplier.getY()) + i % varJ * varK;

            blockZ = location.getBlockZ() + (int) Math.floor(x * widthMultiplier.getZ()) + (int) Math.floor(y * heightMultiplier.getZ()) + i % varK;

            printSingleBlockToAll(blockX, blockY, blockZ, color);
        }
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }

    private void drawAll(int color) {
        for (int i = STAGESIZEY - BACKROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewRender(j, i, color);
            }
        }
        for (int i = 0; i < PLAYABLEROWS; i++) {
            colPrintNewRender(-2, STAGESIZEY - 1 - i, color);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewRender(nextTLCX + j, nextTLCY + i, color);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                colPrintNewRender(holdTLCX + j, holdTLCY + i, color);
            }
        }
    }

    private void gameLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getGameover()) {
                    this.cancel();
                } else {
                    render();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }

    private boolean isThereAProblem() {
        boolean bwx = widthMultiplier.isNonZeroX();
        boolean bwy = widthMultiplier.isNonZeroY();
        boolean bwz = widthMultiplier.isNonZeroZ();
        boolean bhx = heightMultiplier.isNonZeroX();
        boolean bhy = heightMultiplier.isNonZeroY();
        boolean bhz = heightMultiplier.isNonZeroZ();

        return (!bwx && !bwy && !bwz) || (!bhx && !bhy && !bhz);
    }

    private void playSound(Sound sound, float volume, float pitch) {
        if (volume < 1) {
            player.playSound(player.getEyeLocation(), sound, volume, pitch);
        } else {
            for (int i = 0; i < volume; i++) {
                player.playSound(player.getEyeLocation(), sound, 1f, pitch);
            }
        }
    }

    private void position() {
        Location playerLocation = player.getLocation();
        this.location = new Location(playerLocation.getWorld(), 0, 0, 0);
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        if (45 <= yaw && yaw < 135) {
            widthMultiplier = new Point3Dint(0, 0, -1);
            heightMultiplier = new Point3Dint(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() - STAGESIZEY);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - BACKROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() + STAGESIZEX / 2);
        } else if (135 <= yaw && yaw < 225) {
            widthMultiplier = new Point3Dint(1, 0, 0);
            heightMultiplier = new Point3Dint(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() - STAGESIZEX / 2);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - BACKROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() - STAGESIZEY);
        } else if (225 <= yaw && yaw < 315) {
            widthMultiplier = new Point3Dint(0, 0, 1);
            heightMultiplier = new Point3Dint(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() + STAGESIZEY);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - BACKROWS / 2);
            this.location.setX(playerLocation.getBlockZ() - STAGESIZEX / 2);
        } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
            widthMultiplier = new Point3Dint(-1, 0, 0);
            heightMultiplier = new Point3Dint(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() + STAGESIZEX / 2);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - BACKROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() + STAGESIZEY);
        }
    }

    private void prepare() {
        if (isThereAProblem()) {
            player.sendMessage("there was a problem");
        }

        imi = Math.max(widthMultiplier.getAbsoluteX(), heightMultiplier.getAbsoluteX());
        imj = Math.max(widthMultiplier.getAbsoluteY(), heightMultiplier.getAbsoluteY());
        imk = Math.max(widthMultiplier.getAbsoluteZ(), heightMultiplier.getAbsoluteZ());

        player.getInventory().setHeldItemSlot(8);

        for (int i = STAGESIZEY - BACKROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                oldStageDisplay[i][j] = 7;
                colPrintNewRender(j, i, 7);
            }
        }
        for (int i = 0; i < PLAYABLEROWS; i++) {
            oldGQDisplay[i] = 7;
            colPrintNewRender(-2, STAGESIZEY - 1 - i, 7);
        }
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                oldNextDisplay[i][j] = 7;
                colPrintNewRender(nextTLCX + j, nextTLCY + i, 7);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                oldHoldDisplay[i][j] = 7;
                colPrintNewRender(holdTLCX + j, holdTLCY + i, 7);
            }
        }
    }

    private void printSingleBlockTo(Player playerTo, int x, int y, int z, int color) {
        if (color == 7 && Main.gs.playerTransparentBackground.get(player)) {
            Block b = location.getWorld().getBlockAt(x, y, z);
            Main.functions.sendBlockChangeCustom(playerTo, new Location(location.getWorld(), x, y, z), b);
        } else {
            Main.functions.sendBlockChangeCustom(playerTo, new Location(location.getWorld(), x, y, z), color);
        }
    }

    private void printSingleBlockToAll(int x, int y, int z, int color) {
        for (Player player : room.players) {
            printSingleBlockTo(player, x, y, z, color);
        }
    }

    private void render() {
        final Piece pieceFull = getCurrentPiece();
        final Point cpp = new Point(pieceFull.getX(), pieceFull.getY());
        final int rot = pieceFull.getRotation();
        final int piece = getCurrentPiece().getPieceNumber();
        final int[][] stage = getStage();

        int[][] newStageDisplay = new int[STAGESIZEY][STAGESIZEX];
        int[] newGQDisplay = new int[PLAYABLEROWS];
        int[][] newNextDisplay = new int[NEXTPIECESMAX * 4][4];
        int[][] newHoldDisplay = new int[4][4];

        // update stage
        for (int i = 0; i < STAGESIZEY; i++) {
            if (STAGESIZEX >= 0) System.arraycopy(stage[i], 0, newStageDisplay[i], 0, STAGESIZEX);
        }

        for (Point point : genPiece(piece, rot)) {
            newStageDisplay[point.y + getCurrentPieceLowestPossiblePosition()][point.x + cpp.x] = 9 + piece;
        }

        for (Point point : genPiece(piece, rot)) {
            newStageDisplay[point.y + cpp.y][point.x + cpp.x] = piece;
        }

        // update garbage meter
        int total = 0;
        int color;

        for (Object o : getGarbageQueue()) {
            Integer num = (Integer) o;
            total += num;
        }

        color = (total / PLAYABLEROWS) % 7;
        total = total % PLAYABLEROWS;

        for (int i = 0; i < total; i++) {
            newGQDisplay[i] = color;
        }

        for (int i = total; i < PLAYABLEROWS; i++) {
            newGQDisplay[i] = 7;
        }

        // update next queue
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                newNextDisplay[i][j] = 7;
            }
        }

        for (int i = 0; i < NEXTPIECESMAX; i++) {
            for (Point point : genPiece(getNextPieces()[i].getPieceNumber(), 0)) {
                newNextDisplay[point.y + i * 4][point.x] = getNextPieces()[i].getPieceNumber();
            }
        }

        // update hold
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newHoldDisplay[i][j] = 7;
            }
        }

        if (getHeldPiece() != -1) {
            for (Point point : genPiece(getHeldPiece(), 0)) {
                newHoldDisplay[point.y][point.x] = getHeldPiece();
            }
        }

        // print stage
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                if (newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    if (newStageDisplay[i][j] == 7 && i >= STAGESIZEY - BACKROWS) {
                        colPrintNewRender(j, i, newStageDisplay[i][j]);
                    } else if (i >= STAGESIZEY - FRONTROWS) {
                        if (newStageDisplay[i][j] != 7) {
                            colPrintNewRender(j, i, newStageDisplay[i][j]);
                        } else {
                            colPrintNewForce(j, i);
                        }
                    }
                }
            }
        }

        // print garbage meter
        for (int i = 0; i < PLAYABLEROWS; i++) {
            if (newGQDisplay[i] != oldGQDisplay[i]) {
                colPrintNewRender(-2, STAGESIZEY - 1 - i, newGQDisplay[i]);
            }
        }

        // print next queue
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                if (newNextDisplay[i][j] != oldNextDisplay[i][j]) {
                    colPrintNewRender(nextTLCX + j, nextTLCY + i, newNextDisplay[i][j]);
                }
            }
        }

        // print hold
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (newHoldDisplay[i][j] != oldHoldDisplay[i][j]) {
                    colPrintNewRender(holdTLCX + j, holdTLCY + i, newHoldDisplay[i][j]);
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
        Map<Integer, String> text = new HashMap<>();

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

        text.put(1, "Time: " + getTick());
        text.put(0, "getcounter: " + getCounter());

        Main.netherboard.sendScoreboard(player, text);
    }

    private void turnToFallingBlock(int x, int y, double d, int color) {
        if (enableAnimations) {
            int tex, tey, tez;
            double xv = d * (2 - Math.random() * 4) * imi;
            double yv = d * (8 - Math.random() * 10) * imj;
            double zv = d * (2 - Math.random() * 4) * imk;
            for (int i = 0; i < (imi != 0 ? imi : thickness); i++) {
                tex = location.getBlockX() + x * widthMultiplier.getX() + y * heightMultiplier.getX() + i;
                for (int j = 0; j < (imj != 0 ? imj : thickness); j++) {
                    tey = location.getBlockY() + x * widthMultiplier.getY() + y * heightMultiplier.getY() + j;
                    for (int k = 0; k < (imk != 0 ? imk : thickness); k++) {
                        tez = location.getBlockZ() + x * widthMultiplier.getZ() + y * heightMultiplier.getZ() + k;
                        Main.functions.sendFallingBlockCustom(player, new Location(location.getWorld(), tex - widthMultiplier.getZ(), tey, tez + widthMultiplier.getX()), color, xv, yv, zv);
                    }
                }
            }
        }
    }

}

