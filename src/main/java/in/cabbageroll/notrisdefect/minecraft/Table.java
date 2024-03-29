package in.cabbageroll.notrisdefect.minecraft;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import in.cabbageroll.notrisdefect.core.GameLogic;
import in.cabbageroll.notrisdefect.core.Point;
import in.cabbageroll.notrisdefect.minecraft.menus.Menu;
import in.cabbageroll.notrisdefect.minecraft.menus.RoomMenu;
import in.cabbageroll.notrisdefect.minecraft.menus.SkinMenu;
import in.cabbageroll.notrisdefect.minecraft.playerdata.BuiltInSkins;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Table extends GameLogic {

    public static final int GHOST_RED = 11;
    public static final int GHOST_ORANGE = 12;
    public static final int GHOST_YELLOW = 13;
    public static final int GHOST_GREEN = 14;
    public static final int GHOST_LIGHTBLUE = 15;
    public static final int GHOST_BLUE = 16;
    public static final int GHOST_PURPLE = 17;
    public static final int INCOMING_GARBAGE = 18;

    private static final int THICKNESS = 1;
    private DeathAnimation deathAnimation = DeathAnimation.GRAYSCALE;
    private static int FRONTROWS = 30;
    private static int BACKROWS = 20;
    private final Player player;
    // bandaid
    private final char[][] logo = {
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_'},
        {'_', '_', '_', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', '_', 'N', 'N', 'N', 'N', '_', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', '_', '_', '_', '_', '_', '_', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', '_', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', '_', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', '_', '_', '_', '_'},
        {'_', '_', 'N', 'N', 'N', 'N', '_', '_', '_', '_'},
        {'_', '_', '_', '_', '_', 'N', '_', '_', '_', '_'},
        {'_', '_', '_', '_', '_', 'N', '_', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_'},
        {'_', '_', '_', '_', '_', 'N', 'N', '_', '_', '_'},
        {'_', '_', '_', '_', '_', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'_', '_', '_', '_', 'N', 'N', 'N', 'N', '_', '_'},
        {'_', '_', '_', '_', '_', '_', 'N', 'N', '_', '_'},
        {'_', '_', '_', '_', '_', '_', 'N', 'N', '_', '_'},
        {'_', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', 'N'},
        {'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', 'N'},
        {'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', '_', 'N'},
        {'_', 'N', '_', '_', '_', '_', '_', '_', '_', '_'},
        {'_', '_', '_', '_', 'N', 'N', '_', '_', '_', '_'},
        {'N', 'N', '_', '_', 'N', 'N', 'N', '_', '_', '_'},
        {'N', 'N', '_', 'N', 'N', 'N', 'N', '_', '_', '_'},
        {'N', 'N', '_', 'N', 'N', '_', 'N', 'N', '_', '_'},
        {'N', 'N', 'N', 'N', 'N', '_', 'N', 'N', '_', '_'},
        {'_', 'N', 'N', 'N', '_', '_', 'N', 'N', '_', '_'},
        {'_', '_', 'N', 'N', '_', '_', '_', '_', '_', '_'}
    };
    private final boolean HOLDENABLED = true;
    public boolean enableAnimations = true;
    public boolean readyForClick;
    private int GAP = 2;
    private boolean ZONEENABLED = false;
    private Room room;
    private Menu lastMenuOpened;
    // board elements
    private Location location;
    private Point[] nextLocation;
    private Point holdLocation;
    private Point garbageBarLocation;
    private Point zoneBarLocation;
    // multipliers
    private int WMX;
    private int WMY;
    private int WMZ;
    private int HMX;
    private int HMY;
    private int HMZ;
    // helper variables
    private int imi;
    private int imj;
    private int imk;
    private boolean flipupdown;
    // rendering
    private int[][] oldStageDisplay;
    private int[] oldGarbageDisplay;
    private int[] oldZoneDisplay;
    private int[][] oldNextDisplay;
    private int[][] oldHoldDisplay;
    private long linesPrinted;
    private long piecesPrinted;
    private boolean wasHeld;
    private boolean everHeld;
    // movement
    private Location from;
    private int leftEmptyFor;
    private int rightEmptyFor;
    private int downEmptyFor;
    private int movLeft = 0;
    private int movRight = 0;
    private int movSoft = 0;
    private int movCW = 0;

    public Table(Player player) {
        super(Main.GS.getData(player).getARR(), Main.GS.getData(player).getDAS(), Main.GS.getData(player).getSDF());
        this.player = player;
    }

    public DeathAnimation getDeathAnimation() {
        return deathAnimation;
    }

    public static String pieceIntToString(int p) {
        switch (p) {
            case BLOCK_RED:
                return "Z piece";
            case BLOCK_ORANGE:
                return "L piece";
            case BLOCK_YELLOW:
                return "O piece";
            case BLOCK_GREEN:
                return "S piece";
            case BLOCK_LIGHTBLUE:
                return "I piece";
            case BLOCK_BLUE:
                return "J piece";
            case BLOCK_PURPLE:
                return "T piece";
            case BLOCK_NONE:
                return "Background";
            case BLOCK_GRAY:
                return "Garbage";
            case BLOCK_WHITE:
                return "Zone";
            case BLOCK_NUKE:
                return "Nuke";
            case GHOST_RED:
                return "Z ghost";
            case GHOST_ORANGE:
                return "L ghost";
            case GHOST_YELLOW:
                return "O ghost";
            case GHOST_GREEN:
                return "S ghost";
            case GHOST_LIGHTBLUE:
                return "I ghost";
            case GHOST_BLUE:
                return "J ghost";
            case GHOST_PURPLE:
                return "T ghost";
            default:
                return "Unknown";
        }
    }

    private static int[][] decode(char[][] matrix) {
        int[][] decoded = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            int[] row = new int[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                row[j] = pieceCharToInt(matrix[i][j]);
            }
            decoded[i] = row;
        }
        return decoded;
    }

    private static int pieceCharToInt(char c) {
        switch (Character.toUpperCase(c)) {
            case 'Z':
                return BLOCK_RED;
            case 'L':
                return BLOCK_ORANGE;
            case 'O':
                return BLOCK_YELLOW;
            case 'S':
                return BLOCK_GREEN;
            case 'I':
                return BLOCK_LIGHTBLUE;
            case 'J':
                return BLOCK_BLUE;
            case 'T':
                return BLOCK_PURPLE;
            case '#':
                return BLOCK_GRAY;
            case 'N':
                return BLOCK_WHITE;
            case 'U':
                return BLOCK_NUKE;
            default:
                return BLOCK_NONE;
        }
    }

    private static char pieceIntToChar(int p) {
        switch (p) {
            case BLOCK_RED:
                return 'Z';
            case BLOCK_ORANGE:
                return 'L';
            case BLOCK_YELLOW:
                return 'O';
            case BLOCK_GREEN:
                return 'S';
            case BLOCK_LIGHTBLUE:
                return 'I';
            case BLOCK_BLUE:
                return 'J';
            case BLOCK_PURPLE:
                return 'T';
            case BLOCK_NONE:
                return '_';
            case BLOCK_GRAY:
                return '#';
            case BLOCK_WHITE:
                return 'N';
            case BLOCK_NUKE:
                return 'U';
            default:
                return '?';
        }
    }

    @SuppressWarnings("deprecation")
    private static void sendBlock(Player player, Location loc) {
        if (Main.PLUGIN.VERSION < 13) {
            player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
        } else {
            player.sendBlockChange(loc, loc.getBlock().getBlockData());
        }
    }

    @SuppressWarnings("deprecation")
    private static void sendBlock(Player player, Location loc, int color) {
        XMaterial xm = (Main.GS.getData(player).isCustom() ? Main.GS.getSkin(player) : BuiltInSkins.DEFAULTSKIN).get(color);

        if (xm == SkinMenu.EXISTING_MATERIAL) {
            sendBlock(player, loc);
        } else {
            if (Main.PLUGIN.VERSION < 13) {
                player.sendBlockChange(loc, xm.parseItem().getType(), xm.parseItem().getData().getData());
            } else {
                player.sendBlockChange(loc, xm.parseItem().getType().createBlockData());
            }
        }
    }

    public void automaticReposition() {
        cleanAll();
        centerTable();
        automaticElements();
        drawLogo();
    }

    public void checkMovement() {
        Location to = player.getLocation();

        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        if (dx != 0 || dz != 0) {
            player.teleport(from.setDirection(to.getDirection()));
        }

        movLeft = 0;
        movRight = 0;
        movSoft = 0;
        movCW = 0;

        if (Math.abs(dx) * 3 > Math.abs(dz)) {
            if (WMX * dx < 0) {
                movLeft++;
            } else if (WMX * dx > 0) {
                movRight++;
            }

            if (HMX * dx < 0) {
                movSoft++;
            } else if (HMX * dx > 0) {
                movCW++;
            } else {
                if (WMZ * dx < 0) {
                    movSoft++;
                } else if (WMZ * dx > 0) {
                    movCW++;
                }
            }
        }

        if (Math.abs(dz) * 3 > Math.abs(dx)) {
            if (WMZ * dz < 0) {
                movLeft++;
            } else if (WMZ * dz > 0) {
                movRight++;
            }

            if (HMZ * dz < 0) {
                movSoft++;
            } else if (HMZ * dz > 0) {
                movCW++;
            } else {
                if (WMX * dz < 0) {
                    movCW++;
                } else if (WMX * dz > 0) {
                    movSoft++;
                }
            }
        }

        if (flipupdown) {
            int temp = movSoft;
            movSoft = movCW;
            movCW = temp;
        }

        if (movLeft == 0) {
            leftEmptyFor++;
        } else {
            leftEmptyFor = 0;
        }

        if (movRight == 0) {
            rightEmptyFor++;
        } else {
            rightEmptyFor = 0;
        }

        if (movSoft == 0) {
            downEmptyFor++;
        } else {
            downEmptyFor = 0;
        }

        if (movCW > 0) {
            doRotateCW();
        }

        if (leftEmptyFor < GAP && rightEmptyFor != 0) {
            doPressLeft();
        } else {
            doReleaseLeft();
            leftEmptyFor = GAP;
        }

        if (rightEmptyFor < GAP && leftEmptyFor != 0) {
            doPressRight();
        } else {
            doReleaseRight();
            rightEmptyFor = GAP;
        }

        if (downEmptyFor < GAP) {
            doPressDown();
        } else {
            doReleaseDown();
            downEmptyFor = GAP;
        }
    }

    public void cleanAll() {
        for (Player player : room.players) {
            cleanAll(player);
        }
    }

    public void cleanAll(Player player) {
        cleanStage(player);
        cleanGarbage(player);
        cleanZone(player);
        cleanNext(player);
        cleanHold(player);
    }

    public void cleanGarbage(Player player) {
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            printBlock(garbageBarLocation.x, garbageBarLocation.y + i, player);

        }
    }

    public void cleanHold(Player player) {
        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                printBlock(holdLocation.x + j, holdLocation.y + i, player);
            }
        }

    }

    public void cleanNext(Player player) {
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    printBlock(nextLocation[i].x + k, nextLocation[i].y + j, player);
                }

            }
        }
    }

    public void cleanStage(Player player) {
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                printBlock(j, i, player);
            }
        }

    }

    public void cleanZone(Player player) {
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            printBlock(zoneBarLocation.x, zoneBarLocation.y + i, player);

        }
    }

    public void doLeftClick() {
        readyForClick = false;
    }

    public void doRightClick() {

    }

    public void destroyTable() {
        cleanAll();
        Main.NETHERBOARD.removeBoard(player);
    }

    public void drawAll(int color) {
        for (Player player : room.players) {
            drawStage(color, player);
            drawGarbage(color, player);
            drawZone(color, player);
            drawNext(color, player);
            drawHold(color, player);
        }
    }

    public void drawGarbage(int color, Player player) {
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            printBlock(garbageBarLocation.x, garbageBarLocation.y + i, color, player);

        }
    }

    public void drawHold(int color, Player player) {

        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                printBlock(holdLocation.x + j, holdLocation.y + i, color, player);
            }

        }
    }

    public void drawLogo() {
        if (getSTAGESIZEY() != 40 || getSTAGESIZEX() != 10) {
            drawAll(BLOCK_NONE);
            return;
        }

        for (Player player : room.players) {

            int[][] logo2 = decode(logo);

            for (int i = 0; i < getSTAGESIZEY() - BACKROWS; i++) {
                for (int j = 0; j < getSTAGESIZEX(); j++) {
                    if (logo2[i][j] == BLOCK_NONE) {
                        printBlock(j, i, player);
                    } else {
                        printBlock(j, i, logo2[i][j], player);
                    }
                }
            }

            for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
                for (int j = 0; j < getSTAGESIZEX(); j++) {
                    printBlock(j, i, logo2[i][j], player);
                }
            }

            drawGarbage(BLOCK_NONE, player);
            drawZone(BLOCK_NONE, player);
            drawNext(BLOCK_NONE, player);
            drawHold(BLOCK_NONE, player);
        }
    }

    public void drawNext(int color, Player player) {
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    printBlock(nextLocation[i].x + k, nextLocation[i].y + j, color, player);
                }

            }
        }
    }

    public void drawStage(int color, Player player) {
        for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                printBlock(j, i, color, player);

            }
        }
    }

    public void drawZone(int color, Player player) {
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            printBlock(zoneBarLocation.x, zoneBarLocation.y + i, color, player);
        }

    }

    // bandaid
    public void forceFullRender() {
        for (int[] row : oldStageDisplay) {
            Arrays.fill(row, -1);
        }
        Arrays.fill(oldGarbageDisplay, -1);
        Arrays.fill(oldZoneDisplay, -1);
        for (int[] row : oldNextDisplay) {
            Arrays.fill(row, -1);
        }
        for (int[] row : oldHoldDisplay) {
            Arrays.fill(row, -1);
        }
    }

    public int getGAP() {
        return GAP;
    }

    public void setGAP(int GAP) {
        this.GAP = Math.max(GAP, 1);
    }

    public int getHMX() {
        return HMX;
    }

    public int getHMY() {
        return HMY;
    }

    public int getHMZ() {
        return HMZ;
    }

    public Menu getLastMenuOpened() {
        return lastMenuOpened;
    }

    public void setLastMenuOpened(Menu lastMenuOpened) {
        this.lastMenuOpened = lastMenuOpened;
    }

    public Player getPlayer() {
        return player;
    }

    public Room getRoom() {
        return room;
    }

    public int getWMX() {
        return WMX;
    }

    public int getWMY() {
        return WMY;
    }

    public int getWMZ() {
        return WMZ;
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
        Main.NETHERBOARD.createBoard(player, "Stats");

        player.closeInventory();

        new BukkitRunnable() {
            @Override
            public void run() {
                Main.PROTOCOLLIB.sendTitleCustom(player, "3", "", 4, 16, 0);
            }
        }.runTaskLater(Main.PLUGIN, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.PROTOCOLLIB.sendTitleCustom(player, "2", "", 4, 16, 0);
            }
        }.runTaskLater(Main.PLUGIN, 40);
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.PROTOCOLLIB.sendTitleCustom(player, "1", "", 4, 16, 0);
            }
        }.runTaskLater(Main.PLUGIN, 60);

        new BukkitRunnable() {
            @Override
            public void run() {
                doStart(seed);
            }
        }.runTaskLater(Main.PLUGIN, 80);
    }

    public boolean isZONEENABLED() {
        return ZONEENABLED;
    }

    public void setZONEENABLED(boolean ZONEENABLED) {
        this.ZONEENABLED = ZONEENABLED;
    }

    public void joinRoom(Room r) {
        room = r;
        centerTable();
        automaticElements();
        manualReposition();
    }

    public void leaveRoom() {
        room = null;
    }

    public void manualReposition() {
        cleanAll();
        player.closeInventory();
        readyForClick = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (readyForClick) {
                    cleanAll();
                    centerTable();
                    drawLogo();
                    ActionBar.sendActionBar(player, "Left click to confirm table location");
                } else {
                    ActionBar.sendActionBar(player, "Ready!");
                    new RoomMenu(player);
                    cancel();
                }
            }
        }.runTaskTimer(Main.PLUGIN, 0, 5);
    }

    public void moveTable(int x, int y, int z) {
        cleanAll();
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        drawLogo();
    }

    public void moveTableRelative(int x, int y, int z) {
        moveTable(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
    }

    private void rotateFastX() {
        setWidthMultiplier(WMX, WMZ, -WMY);
        setHeightMultiplier(HMX, HMZ, -HMY);
    }

    private void rotateFastY() {
        setWidthMultiplier(-WMZ, WMY, WMX);
        setHeightMultiplier(-HMZ, HMY, HMX);
    }

    private void rotateFastZ() {
        setWidthMultiplier(WMY, -WMX, WMZ);
        setHeightMultiplier(HMY, -HMX, HMZ);
    }

    public void rotateX() {
        cleanAll();
        rotateFastX();
        drawLogo();
    }

    public void rotateY() {
        cleanAll();
        rotateFastY();
        drawLogo();
    }

    public void rotateZ() {
        cleanAll();
        rotateFastZ();
        drawLogo();
    }

    @Override
    public void setARR(int n) {
        super.setARR(n);
        Main.GS.getData(player).setARR(n);
    }

    @Override
    public void setDAS(int n) {
        super.setDAS(n);
        Main.GS.getData(player).setDAS(n);
    }

    @Override
    public void setNEXTPIECES(int n) {
        cleanAll();
        super.setNEXTPIECES(n);
        drawLogo();
    }

    @Override
    public void setPLAYABLEROWS(int n) {
        cleanAll();
        super.setPLAYABLEROWS(n);
        drawLogo();
    }

    @Override
    public void setSDF(int n) {
        super.setSDF(n);
        Main.GS.getData(player).setSDF(n);
    }

    @Override
    public void setSTAGESIZEX(int n) {
        cleanAll();
        super.setSTAGESIZEX(n);
        drawLogo();
    }

    @Override
    public void setSTAGESIZEY(int n) {
        cleanAll();
        super.setSTAGESIZEY(n);
        drawLogo();
    }

    @Override
    public void lumines() {
        super.lumines();
        FRONTROWS = 10;
        BACKROWS = 8;
        automaticReposition();
    }

    @Override
    protected void evtGameover() {
        for (Player player : room.players) {
            switch (deathAnimation) {
                case EXPLOSION:
                    cleanStage(player);

                    for (int i = getSTAGESIZEY() - FRONTROWS; i < getSTAGESIZEY(); i++) {
                        for (int j = 0; j < getSTAGESIZEX(); j++) {
                            turnToFallingBlock(j, i, 1, getStage()[i][j]);
                        }
                    }
                    break;
                case GRAYSCALE:
                    for (int i = 0; i < getSTAGESIZEY(); i++) {
                        for (int j = 0; j < getSTAGESIZEX(); j++) {
                            if (getStage()[i][j] != BLOCK_NONE)
                                printBlock(j, i, BLOCK_GRAY, player);
                        }
                    }
                    break;
                case CLEAR:
                    drawAll(BLOCK_NONE);
                    break;
                case DISAPPEAR:
                    cleanAll();
                    break;
                case NONE:
                    break;
            }
        }
    }

    @Override
    protected void evtLineClear(int height, int[] line) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < getSTAGESIZEX(); i++) {
                    turnToFallingBlock(i, height, 0.3, line[i]);
                }
            }
        }.runTask(Main.PLUGIN);
    }

    @Override
    protected void evtLockPiece(UsablePiece piece, int linesCleared, int spinState, int combo, int backToBack, boolean nuke) {
        StringBuilder sb = new StringBuilder();

        Sound sound = Sounds.lineClear;

        if (backToBack > 0 && linesCleared > 0) {
            sb.append("B2B ");
        }

        sb.append(pieceIntToChar(piece.getColors()[0]));

        switch (spinState) {
            case SPIN_MINI:
                sound = Sounds.lineClearBig;
                sb.append("-SPIN MINI");
                break;
            case SPIN_FULL:
                sound = Sounds.lineClearBig;
                sb.append("-SPIN");
                break;
        }

        switch (linesCleared) {
            case 1:
                sb.append(" SINGLE");
                break;
            case 2:
                sb.append(" DOUBLE");
                break;
            case 3:
                sb.append(" TRIPLE");
                break;
            case 4:
                sound = Sounds.lineClearBig;
                sb.append(" NOTRIS");
                break;
        }

        playSound(sound, linesCleared, 0.5f + combo * 0.1f);

        if (nuke) {
            if (linesCleared > 0) {
                sb.append("+");
            }
            playSound(Sounds.nuke, 1f, 0.5f);
        }

        if (combo > 0) {
            sb.append(" ").append(combo).append(" COMBO");
        }

        ActionBar.sendActionBar(player, sb.toString());
    }

    @Override
    protected void evtPerfectClear() {
        Main.PROTOCOLLIB.sendTitleCustom(player, "", "PERFECT CLEAR", 20, 20, 40);
        playSound(Sounds.pc, 1f, 0.5f);
    }

    @Override
    protected void evtSendGarbage(int i) {
        room.forwardGarbage(i, player);
    }

    @Override
    protected void evtSpin() {
        playSound(Sounds.spin, 1f, 1f);
    }

    public void skewHX(int n) {
        cleanAll();
        HMX += n;
        drawLogo();
    }

    public void skewHY(int n) {
        cleanAll();
        HMY += n;
        drawLogo();
    }

    public void skewHZ(int n) {
        cleanAll();
        HMZ += n;
        drawLogo();
    }

    public void skewWX(int n) {
        cleanAll();
        WMX += n;
        drawLogo();
    }

    public void skewWY(int n) {
        cleanAll();
        WMY += n;
        drawLogo();
    }

    public void skewWZ(int n) {
        cleanAll();
        WMZ += n;
        drawLogo();
    }

    public void switchDeathAnim() {
        deathAnimation = deathAnimation.next();
    }

    public void updateWholeTableTo(Player player) {

    }

    private void automaticElements() {
        nextLocation = new Point[getNEXTPIECES()];

        for (int i = 0; i < nextLocation.length; i++) {
            nextLocation[i] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS() + i * getPieceTable().mostPiecePoints());
        }

        holdLocation = new Point(-3 - getPieceTable().mostPiecePoints(), getPLAYABLEROWS());
        garbageBarLocation = new Point(-2, getPLAYABLEROWS());
        zoneBarLocation = new Point(getSTAGESIZEX() + 1, getPLAYABLEROWS());
    }

    private void centerTable() {
        int dist = 30;
        Location playerLocation = player.getLocation();
        location = new Location(playerLocation.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
        float yaw = (player.getLocation().getYaw() % 360 + 360) % 360;
        float pitch = player.getLocation().getPitch();
        double phi = Math.toRadians(270 - yaw);
        double theta = Math.toRadians(pitch + 90);
        location.add(dist * Math.sin(theta) * Math.cos(phi), dist * Math.cos(theta), -dist * Math.sin(theta) * Math.sin(phi));

        setWidthMultiplier(-1, 0, 0);
        setHeightMultiplier(0, -1, 0);

        if (pitch < -45) {
            flipupdown = false;
            rotateFastX();
        } else if (-45 <= pitch && pitch < 45) {
            flipupdown = false;
        } else if (45 <= pitch) {
            flipupdown = true;
            rotateFastX();
            rotateFastX();
            rotateFastX();
        }

        if (45 <= yaw && yaw < 135) {
            rotateFastY();
        } else if (135 <= yaw && yaw < 225) {
            rotateFastY();
            rotateFastY();
        } else if (225 <= yaw && yaw < 315) {
            rotateFastY();
            rotateFastY();
            rotateFastY();
        }

        location = toActualCoords(-getSTAGESIZEX() / 2, getPLAYABLEROWS() / 2 - getSTAGESIZEY()).iterator().next();
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }

    private boolean isTableValid() {
        boolean bwx = WMX != 0;
        boolean bwy = WMY != 0;
        boolean bwz = WMZ != 0;
        boolean bhx = HMX != 0;
        boolean bhy = HMY != 0;
        boolean bhz = HMZ != 0;

        return (bwx || bwy || bwz) && (bhx || bhy || bhz) && (bwx != bhx || bwy != bhy || bwz != bhz);
    }

    private void playSound(Sound sound, float volume, float pitch) {
        if (volume == 0) {
            return;
        }

        if (volume < 1) {
            player.playSound(player.getEyeLocation(), sound, volume, pitch);
            return;
        }

        for (int i = 0; i < volume; i++) {
            player.playSound(player.getEyeLocation(), sound, 1f, pitch);
        }
    }

    private void prepare() {
        if (!isTableValid()) {
            player.sendMessage("Invalid table multipliers!");
        }

        imi = Math.max(Math.abs(WMX), Math.abs(HMX));
        imj = Math.max(Math.abs(WMY), Math.abs(HMY));
        imk = Math.max(Math.abs(WMZ), Math.abs(HMZ));

        player.getInventory().setHeldItemSlot(8);

        cleanAll();

        oldGarbageDisplay = new int[getPLAYABLEROWS()];
        oldZoneDisplay = new int[getPLAYABLEROWS()];
        oldNextDisplay = new int[getPieceTable().mostPiecePoints() * getNEXTPIECES()][getPieceTable().mostPiecePoints()];
        oldHoldDisplay = new int[getPieceTable().mostPiecePoints()][getPieceTable().mostPiecePoints()];
        oldStageDisplay = new int[getSTAGESIZEY()][getSTAGESIZEX()];
        linesPrinted = 0;
        // to print on first tick
        piecesPrinted = -1;
        wasHeld = false;
        everHeld = false;

        from = player.getLocation();
        leftEmptyFor = GAP;
        rightEmptyFor = GAP;
        downEmptyFor = GAP;

        drawAll(BLOCK_NONE);
    }

    private void printBlock(int x, int y, int color, Player player) {
        for (Location loc : toActualCoords(x, y)) {
            sendBlock(player, loc, color);
        }
    }

    private void printBlock(int x, int y, Player player) {
        for (Location loc : toActualCoords(x, y)) {
            sendBlock(player, loc);
        }
    }

    private Set<Location> toActualCoords(int x, int y) {
        int blockX, blockY, blockZ;
        Set<Location> locations = new HashSet<>();

        for (int i = 0; i < (imi != 0 ? imi : THICKNESS); i++) {
            blockX = location.getBlockX() + x * WMX + y * HMX + i;
            for (int j = 0; j < (imj != 0 ? imj : THICKNESS); j++) {
                blockY = location.getBlockY() + x * WMY + y * HMY + j;
                for (int k = 0; k < (imk != 0 ? imk : THICKNESS); k++) {
                    blockZ = location.getBlockZ() + x * WMZ + y * HMZ + k;
                    locations.add(new Location(location.getWorld(), blockX, blockY, blockZ));
                }
            }
        }
        return locations;
    }

    public void render(Player player) {
        renderStage(player);
        renderGarbage(player);
        if (ZONEENABLED) {
            renderZone(player);
        }
        renderNext(player);
        renderHold(player);
    }

    private void renderGarbage(Player player) {
        int[] newGarbageDisplay = new int[getPLAYABLEROWS()];
        int totalGarbage = 0;

        for (Object o : getGarbageQueue()) {
            totalGarbage += (Integer) o;
        }

        for (int i = getPLAYABLEROWS() - 1; i >= Math.max(getPLAYABLEROWS() - totalGarbage, 0); i--) {
            newGarbageDisplay[i] = INCOMING_GARBAGE;
        }

        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            if (newGarbageDisplay[i] != oldGarbageDisplay[i]) {
                printBlock(garbageBarLocation.x, garbageBarLocation.y + i, newGarbageDisplay[i], player);
            }
        }

        oldGarbageDisplay = newGarbageDisplay;
    }

    private void renderHold(Player player) {
        if (wasHeld != getHeld()) {
            int[][] newHoldDisplay = new int[getPieceTable().mostPiecePoints()][getPieceTable().mostPiecePoints()];

            if (getHeldPiece() != null) {
                Point[] points = getHeldPiece().getPoints(0);

                for (int i = 0; i < points.length; i++) {
                    newHoldDisplay[points[i].y][points[i].x] = getHeldPiece().getColors()[i];
                }
            }

            for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    if (newHoldDisplay[i][j] != oldHoldDisplay[i][j]) {
                        printBlock(holdLocation.x + j, holdLocation.y + i, newHoldDisplay[i][j], player);
                    }
                }
            }
            wasHeld = getHeld();
            everHeld = true;
            oldHoldDisplay = newHoldDisplay;
        }
    }

    private void renderNext(Player player) {
        if (piecesPrinted < getTotalPiecesPlaced() || (!everHeld && getHeldPiece() != null && !wasHeld && getHeld())) {
            int[][] newNextDisplay = new int[getPieceTable().mostPiecePoints() * getNEXTPIECES()][getPieceTable().mostPiecePoints()];

            for (int i = 0; i < getNEXTPIECES(); i++) {
                Point[] points = getNextPieces()[i].getPoints(0);

                for (int j = 0; j < points.length; j++) {
                    newNextDisplay[i * getPieceTable().mostPiecePoints() + points[j].y][points[j].x] = getNextPieces()[i].getColors()[j];
                }
            }

            for (int i = 0; i < getNEXTPIECES(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                        printBlock(nextLocation[i].x + k, nextLocation[i].y + j, newNextDisplay[i * getPieceTable().mostPiecePoints() + j][k], player);
                    }
                }
            }

            piecesPrinted = getTotalPiecesPlaced();

            oldNextDisplay = newNextDisplay;
        }
    }

    private void renderStage(Player player) {
        UsablePiece piece = getCurrentPiece();
        int[][] stage = getStage();
        int[][] newStageDisplay = new int[getSTAGESIZEY()][getSTAGESIZEX()];

        for (int i = 0; i < getSTAGESIZEY(); i++) {
            System.arraycopy(stage[i], 0, newStageDisplay[i], 0, getSTAGESIZEX());
        }

        Point[] points = piece.getPoints();

        if (getGameState() < STATE_DELAY) {
            for (int i = 0; i < points.length; i++) {
                newStageDisplay[points[i].y + getLowestPossiblePosition()][points[i].x + piece.getX()] = 10 + piece.getColors()[i];
            }

            for (int i = 0; i < points.length; i++) {
                newStageDisplay[points[i].y + piece.getY()][points[i].x + piece.getX()] = piece.getColors()[i];
            }
        }

        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                if (newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    if (newStageDisplay[i][j] == BLOCK_NONE && i >= getSTAGESIZEY() - BACKROWS) {
                        printBlock(j, i, newStageDisplay[i][j], player);
                    } else if (i >= getSTAGESIZEY() - FRONTROWS) {
                        if (newStageDisplay[i][j] != BLOCK_NONE) {
                            printBlock(j, i, newStageDisplay[i][j], player);
                        } else {
                            printBlock(j, i, player);
                        }
                    }
                }
            }
        }
        oldStageDisplay = newStageDisplay;
    }

    private void renderZone(Player player) {
        int[] newZoneDisplay = new int[getPLAYABLEROWS()];

        for (int i = getPLAYABLEROWS() - 1; i >= getPLAYABLEROWS() - getZoneCharge() * getPLAYABLEROWS() / 4; i--) {
            newZoneDisplay[i] = BLOCK_WHITE;
        }

        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            if (newZoneDisplay[i] != oldZoneDisplay[i]) {
                printBlock(zoneBarLocation.x, zoneBarLocation.y + i, newZoneDisplay[i], player);
            }
        }

        oldZoneDisplay = newZoneDisplay;
    }

    public void sendScoreboard() {
        Map<Integer, String> text = new HashMap<>();

        text.put(6, "Garbage sent: " + getTotalGarbageSent());
        text.put(5, "Garbage received: " + getTotalGarbageReceived());
        text.put(4, "Lines: " + getTotalLinesCleared());
        text.put(3, "Pieces: " + getTotalPiecesPlaced());
        text.put(2, "Score: " + getTotalScore());
        text.put(1, "Tick: " + getTicksPassed());
        text.put(0, "Counter: " + getCounter() + "/" + getCounterEnd());
        text.put(-1, "Debug: " + getGameState() + " " + leftEmptyFor + rightEmptyFor + downEmptyFor + " " + movLeft + movRight + movSoft + movCW);

        Main.NETHERBOARD.sendScoreboard(player, text);
    }

    private void setHeightMultiplier(int x, int y, int z) {
        HMX = x;
        HMY = y;
        HMZ = z;
    }

    private void setWidthMultiplier(int x, int y, int z) {
        WMX = x;
        WMY = y;
        WMZ = z;
    }

    private void turnToFallingBlock(int x, int y, double d, int color) {
        if (enableAnimations) {
            double xv = d * (2 - Math.random() * 4) * imi;
            double yv = d * (8 - Math.random() * 10) * imj;
            double zv = d * (2 - Math.random() * 4) * imk;
            for (Location loc : toActualCoords(x, y)) {
                Main.PROTOCOLLIB.sendFallingBlockCustom(player, loc.add(-WMZ * 2, 0, WMX * 2), color, xv, yv, zv);
            }
        }
    }

    public enum DeathAnimation {
        NONE, EXPLOSION, GRAYSCALE, CLEAR, DISAPPEAR;
        private static final DeathAnimation[] vals = values();

        public DeathAnimation next() {
            return vals[(ordinal() + 1) % vals.length];
        }
    }

}

