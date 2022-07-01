package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.core.GameLogic;
import cabbageroll.notrisdefect.core.Point;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import cabbageroll.notrisdefect.minecraft.playerdata.Settings;
import cabbageroll.notrisdefect.minecraft.playerdata.Skin;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Table extends GameLogic {

    public static final int GHOST_OFFSET = 10;
    private static int FRONTROWS = 30;
    private static int BACKROWS = 20;
    private static final int THICKNESS = 1;
    public static DeathAnimation deathAnim = DeathAnimation.GRAYSCALE;
    private int AWAYMOVE = (int) (getPLAYABLEROWS() * 1.5);
    private int DOWNMOVE = getSTAGESIZEY() - (getPLAYABLEROWS() >> 1);
    private int SIDEMOVE = (getSTAGESIZEX() >> 1);
    private final Player player;
    // stupid
    private final char[][] logo = {
        {'_', 'Z', 'L', 'O', 'S', 'I', 'J', 'T', '_', '_'},
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
    public boolean enableAnimations = true;
    public boolean readyForClick;
    private int GAP = 2;
    private boolean ZONEENABLED = false;
    private final boolean HOLDENABLED = true;
    private Room room;
    private Menu lastMenuOpened;
    // board elements
    private Location location;
    private Point[] nextLocation;
    private Point holdLocation;
    private Point garbageBarLocation;
    private Point scoreBarLocation;
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
    private int[] oldGQDisplay;
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
        super(Main.gs.getData(player).getARR(), Main.gs.getData(player).getDAS(), Main.gs.getData(player).getSDF());
        this.player = player;
    }

    public static DeathAnimation getDeathAnim() {
        return deathAnim;
    }

    public static String pieceIntToString(int p) {
        switch (p) {
            case PIECE_Z:
                return "Z piece";
            case PIECE_L:
                return "L piece";
            case PIECE_O:
                return "O piece";
            case PIECE_S:
                return "S piece";
            case PIECE_I:
                return "I piece";
            case PIECE_J:
                return "J piece";
            case PIECE_T:
                return "T piece";
            case PIECE_NONE:
                return "Background";
            case PIECE_GARBAGE:
                return "Garbage";
            case PIECE_ZONE:
                return "Zone";
            case PIECE_NUKE:
                return "Nuke";
            case PIECE_Z + GHOST_OFFSET:
                return "Z ghost";
            case PIECE_L + GHOST_OFFSET:
                return "L ghost";
            case PIECE_O + GHOST_OFFSET:
                return "O ghost";
            case PIECE_S + GHOST_OFFSET:
                return "S ghost";
            case PIECE_I + GHOST_OFFSET:
                return "I ghost";
            case PIECE_J + GHOST_OFFSET:
                return "J ghost";
            case PIECE_T + GHOST_OFFSET:
                return "T ghost";
            default:
                return "Unknown";
        }
    }

    @Override
    public void lumines() {
        super.lumines();
        FRONTROWS = 10;
        BACKROWS = 8;
        AWAYMOVE = (int) (getPLAYABLEROWS() * 1.5);
        DOWNMOVE = getSTAGESIZEY() - (getPLAYABLEROWS() >> 1);
        SIDEMOVE = (getSTAGESIZEX() >> 1);
        automaticReposition();
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
                return PIECE_Z;
            case 'L':
                return PIECE_L;
            case 'O':
                return PIECE_O;
            case 'S':
                return PIECE_S;
            case 'I':
                return PIECE_I;
            case 'J':
                return PIECE_J;
            case 'T':
                return PIECE_T;
            case '#':
                return PIECE_GARBAGE;
            case 'N':
                return PIECE_ZONE;
            case 'U':
                return PIECE_NUKE;
            default:
                return PIECE_NONE;
        }
    }

    private static char pieceIntToChar(int p) {
        switch (p) {
            case PIECE_Z:
                return 'Z';
            case PIECE_L:
                return 'L';
            case PIECE_O:
                return 'O';
            case PIECE_S:
                return 'S';
            case PIECE_I:
                return 'I';
            case PIECE_J:
                return 'J';
            case PIECE_T:
                return 'T';
            case PIECE_NONE:
                return '_';
            case PIECE_GARBAGE:
                return '#';
            case PIECE_ZONE:
                return 'N';
            case PIECE_NUKE:
                return 'U';
            default:
                return '?';
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
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(from.setDirection(to.getDirection()));
                }
            }.runTask(Main.plugin);
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

    public void confirmPosition() {
        readyForClick = false;
    }

    public void destroyTable() {
        cleanAll();
        Main.netherboard.removeBoard(player);
    }

    public void drawLogo() {
        // temp fix
        if (getSTAGESIZEY() != 40 || getSTAGESIZEX() != 10 || getNEXTPIECES() != 5) {
            drawAll(PIECE_NONE);
            return;
        }

        int[][] logo2 = decode(logo);

        for (int i = 0; i < getSTAGESIZEY() - BACKROWS; i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                if (logo2[i][j] == PIECE_NONE) {
                    colPrintNewForce(j, i);
                } else {
                    colPrintNewRender(j, i, logo2[i][j]);
                }
            }
        }

        for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewRender(j, i, logo2[i][j]);
            }
        }

        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            colPrintNewRender(garbageBarLocation.x, garbageBarLocation.y + i, PIECE_NONE);
        }

        //bandaid
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    colPrintNewRender(nextLocation[i].x + k, nextLocation[i].y + j, PIECE_NONE);
                }
            }
        }


        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                colPrintNewRender(holdLocation.x + j, holdLocation.y + i, PIECE_NONE);
            }
        }
    }

    public int getGAP() {
        return GAP;
    }

    public void setGAP(int GAP) {
        if (GAP > 0) {
            this.GAP = GAP;
        } else this.GAP = 1;
    }

    public int getGarbBLCX() {
        return garbageBarLocation.x;
    }

    public int getGarbBLCY() {
        return garbageBarLocation.y;
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

    public int getHoldTLCX() {
        return holdLocation.x;
    }

    public int getHoldTLCY() {
        return holdLocation.y;
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
        doStart(seed);
        Main.netherboard.createBoard(player, "Stats");
        gameLoop();
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
        drawLogo();
    }

    public void leaveRoom() {
        room = null;
    }

    //todo
    public void manualReposition() {
        cleanAll();
        readyForClick = true;
        new BukkitRunnable() {
            int operation = 0;
            int i = 0;
            Point temp;

            @Override
            public void run() {
                if (readyForClick) {
                    switch (operation) {
                        case 0:
                            player.sendMessage("test0");
                            centerTable();
                            ActionBar.sendActionBar(player, "Click to confirm table location");
                            break;
                        case 1:
                            player.sendMessage("test1");
                            ActionBar.sendActionBar(player, "Click to confirm next " + i + " location");
                            break;
                        case 2:
                            player.sendMessage("test2");
                            ActionBar.sendActionBar(player, "Click to confirm hold location");
                            break;
                        case 3:
                            player.sendMessage("test3");
                            ActionBar.sendActionBar(player, "Click to confirm garbage bar location");
                            break;
                        case 4:
                            player.sendMessage("test4");
                            ActionBar.sendActionBar(player, "Click to confirm score bar location");
                            break;
                    }
                } else {
                    switch (operation) {
                        case 0:
                            operation++;
                            readyForClick = true;
                            break;
                        case 1:
                            nextLocation[i] = temp;
                            i++;
                            if(i==getNEXTPIECES()) {
                                operation++;
                                readyForClick = true;
                            }
                            break;
                        case 2:
                            operation++;
                            readyForClick = true;
                            break;
                        case 3:
                            operation++;
                            readyForClick = true;
                            break;
                        case 4:
                            this.cancel();
                            ActionBar.sendActionBar(player, "Ready!");
                            drawLogo();
                            break;
                    }
                }
            }
        }.runTaskTimer(Main.plugin, 0, 10);

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

    public void rotateX() {
        cleanAll();
        setWidthMultiplier(WMX, -WMZ, WMY);
        setHeightMultiplier(HMX, -HMZ, HMY);
        drawLogo();
    }

    public void rotateY() {
        cleanAll();
        setWidthMultiplier(WMZ, WMY, -WMX);
        setHeightMultiplier(HMZ, HMY, -HMX);
        drawLogo();
    }

    public void rotateZ() {
        cleanAll();
        setWidthMultiplier(-WMY, WMX, WMZ);
        setHeightMultiplier(-HMY, HMX, HMZ);
        drawLogo();
    }

    @Override
    public void setARR(int n) {
        super.setARR(n);
        Main.gs.getData(player).setARR(n);
    }

    @Override
    public void setDAS(int n) {
        super.setDAS(n);
        Main.gs.getData(player).setDAS(n);
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
        Main.gs.getData(player).setSDF(n);
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
    protected void evtGameover() {
        switch (deathAnim) {
            case EXPLOSION:
                for (int i = 0; i < getSTAGESIZEY(); i++) {
                    for (int j = 0; j < getSTAGESIZEX(); j++) {
                        colPrintNewForce(j, i);
                    }
                }

                for (int i = getSTAGESIZEY() - FRONTROWS; i < getSTAGESIZEY(); i++) {
                    for (int j = 0; j < getSTAGESIZEX(); j++) {
                        turnToFallingBlock(j, i, 1, getStage()[i][j]);
                    }
                }
                break;
            case GRAYSCALE:
                for (int i = 0; i < getSTAGESIZEY(); i++) {
                    for (int j = 0; j < getSTAGESIZEX(); j++) {
                        if (getStage()[i][j] != PIECE_NONE)
                            colPrintNewRender(j, i, PIECE_GARBAGE);
                    }
                }
                break;
            case CLEAR:
                for (int i = 0; i < getSTAGESIZEY(); i++) {
                    for (int j = 0; j < getSTAGESIZEX(); j++) {
                        if (getStage()[i][j] != PIECE_NONE) {
                            colPrintNewRender(j, i, PIECE_NONE);
                        }
                    }
                }
            case DISAPPEAR:
                for (int i = 0; i < getSTAGESIZEY(); i++) {
                    for (int j = 0; j < getSTAGESIZEX(); j++) {
                        colPrintNewForce(j, i);
                    }
                }

            case NONE:
                break;
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
        }.runTask(Main.plugin);
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
        Main.protocollib.sendTitleCustom(player, "", "PERFECT CLEAR", 20, 20, 40);
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
        deathAnim = deathAnim.next();
    }

    public void updateWholeTableTo(Player player) {

    }

    private void automaticElements() {
        nextLocation = new Point[5];
        nextLocation[0] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS());
        nextLocation[1] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS() + getPieceTable().mostPiecePoints());
        nextLocation[2] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS() + 2 * getPieceTable().mostPiecePoints());
        nextLocation[3] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS() + 3 * getPieceTable().mostPiecePoints());
        nextLocation[4] = new Point(getSTAGESIZEX() + 3, getPLAYABLEROWS() + 4 * getPieceTable().mostPiecePoints());

        holdLocation = new Point(-3 - getPieceTable().mostPiecePoints(), getPLAYABLEROWS());
        garbageBarLocation = new Point(-2, getPLAYABLEROWS());
        scoreBarLocation = new Point(getSTAGESIZEX() + 1, getPLAYABLEROWS());
    }

    private void centerTable() {
        Location playerLocation = player.getLocation();
        location = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;
        float pitch = player.getLocation().getPitch();
        if (pitch < -45) {
            location.add(0, AWAYMOVE, 0);
            flipupdown = false;
            if (45 <= yaw && yaw < 135) {
                setWidthMultiplier(0, 0, -1);
                setHeightMultiplier(-1, 0, 0);
                location.add(DOWNMOVE - (90 + pitch) / 2, 0, SIDEMOVE);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, 0, -1);
                location.add(-SIDEMOVE, 0, DOWNMOVE - (90 + pitch) / 2);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(1, 0, 0);
                location.add(-DOWNMOVE + (90 + pitch) / 2, 0, -SIDEMOVE);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, 0, 1);
                location.add(SIDEMOVE, 0, -DOWNMOVE + (90 + pitch) / 2);
            }
        } else if (-45 <= pitch && pitch < 45) {
            location.add(0, DOWNMOVE - pitch / 2, 0);
            flipupdown = false;
            if (45 <= yaw && yaw < 135) {
                setWidthMultiplier(0, 0, -1);
                setHeightMultiplier(0, -1, 0);
                location.add(-AWAYMOVE, 0, SIDEMOVE + (90 - yaw) / 2);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, -1, 0);
                location.add(-SIDEMOVE - (180 - yaw) / 2, 0, -AWAYMOVE);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(0, -1, 0);
                location.add(AWAYMOVE, 0, -SIDEMOVE - (270 - yaw) / 2);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, -1, 0);
                location.add(SIDEMOVE + (yaw > 45 ? 360 - yaw : -yaw) / 2, 0, AWAYMOVE);
            }
        } else if (45 <= pitch) {
            location.add(0, -AWAYMOVE, 0);
            flipupdown = true;
            if (45 <= yaw && yaw < 135) {
                setWidthMultiplier(0, 0, -1);
                setHeightMultiplier(1, 0, 0);
                location.add(-DOWNMOVE - (90 - pitch) / 2, 0, SIDEMOVE);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, 0, 1);
                location.add(-SIDEMOVE, 0, -DOWNMOVE - (90 - pitch) / 2);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(-1, 0, 0);
                location.add(DOWNMOVE + (90 - pitch) / 2, 0, -SIDEMOVE);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, 0, -1);
                location.add(SIDEMOVE, 0, DOWNMOVE + (90 - pitch) / 2);
            }
        }
    }

    private void cleanAll() {
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewForce(j, i);
            }
        }
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            colPrintNewForce(garbageBarLocation.x, garbageBarLocation.y + i);
        }
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    colPrintNewForce(nextLocation[i].x + k, nextLocation[i].y + j);
                }
            }
        }
        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                colPrintNewForce(holdLocation.x + j, holdLocation.y + i);
            }
        }
    }

    private void colPrintNewForce(float x, float y) {
        int blockX, blockY, blockZ;

        for (int i = 0; i < (imi != 0 ? imi : THICKNESS); i++) {
            blockX = location.getBlockX() + (int) Math.floor(x * WMX) + (int) Math.floor(y * HMX) + i;
            for (int j = 0; j < (imj != 0 ? imj : THICKNESS); j++) {
                blockY = location.getBlockY() + (int) Math.floor(x * WMY) + (int) Math.floor(y * HMY) + j;
                for (int k = 0; k < (imk != 0 ? imk : THICKNESS); k++) {
                    blockZ = location.getBlockZ() + (int) Math.floor(x * WMZ) + (int) Math.floor(y * HMZ) + k;
                    Block b = location.getWorld().getBlockAt(blockX, blockY, blockZ);
                    for (Player player : room.players) {
                        Main.protocollib.sendBlockChangeCustom(player,
                            new Location(location.getWorld(), blockX, blockY, blockZ), b);
                    }
                }
            }
        }
    }

    private void colPrintNewRender(float x, float y, int color) {
        int blockX, blockY, blockZ;

        for (int i = 0; i < (imi != 0 ? imi : THICKNESS); i++) {
            blockX = location.getBlockX() + (int) Math.floor(x * WMX) + (int) Math.floor(y * HMX) + i;
            for (int j = 0; j < (imj != 0 ? imj : THICKNESS); j++) {
                blockY = location.getBlockY() + (int) Math.floor(x * WMY) + (int) Math.floor(y * HMY) + j;
                for (int k = 0; k < (imk != 0 ? imk : THICKNESS); k++) {
                    blockZ = location.getBlockZ() + (int) Math.floor(x * WMZ) + (int) Math.floor(y * HMZ) + k;
                    printSingleBlockToAll(blockX, blockY, blockZ, color);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void debug(String s) {
        System.out.println(s);
    }

    private void drawAll(int color) {
        for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewRender(j, i, color);
            }
        }
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            colPrintNewRender(garbageBarLocation.x, garbageBarLocation.y + i, color);
        }
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    colPrintNewRender(nextLocation[i].x + k, nextLocation[i].y + j, color);
                }
            }
        }
        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                colPrintNewRender(holdLocation.x + j, holdLocation.y + i, color);
            }
        }
    }

    private void gameLoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getGameState() == STATE_DEAD) {
                    if (player.isOnline()) {
                        sendScoreboard();
                    }
                    this.cancel();
                } else {
                    render();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
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
            player.sendMessage("Invalid table multipliers");
        }

        imi = Math.max(Math.abs(WMX), Math.abs(HMX));
        imj = Math.max(Math.abs(WMY), Math.abs(HMY));
        imk = Math.max(Math.abs(WMZ), Math.abs(HMZ));

        player.getInventory().setHeldItemSlot(8);

        cleanAll();

        oldGQDisplay = new int[getPLAYABLEROWS()];
        oldNextDisplay = new int[getPieceTable().mostPiecePoints() * getNEXTPIECES()][getPieceTable().mostPiecePoints()];
        oldHoldDisplay = new int[getPieceTable().mostPiecePoints()][getPieceTable().mostPiecePoints()];
        oldStageDisplay = new int[getSTAGESIZEY()][getSTAGESIZEX()];
        linesPrinted = 0;
        //to print on first tick
        piecesPrinted = -1;
        wasHeld = false;
        everHeld = false;

        from = player.getLocation();
        leftEmptyFor = GAP;
        rightEmptyFor = GAP;
        downEmptyFor = GAP;

        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                oldStageDisplay[i][j] = PIECE_NONE;
            }
        }
        for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewRender(j, i, PIECE_NONE);
            }
        }

        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            oldGQDisplay[i] = PIECE_NONE;
            colPrintNewRender(garbageBarLocation.x, garbageBarLocation.y + i, PIECE_NONE);
        }
        for (int i = 0; i < getNEXTPIECES(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                    oldNextDisplay[i * getPieceTable().mostPiecePoints() + j][k] = PIECE_NONE;
                    colPrintNewRender(nextLocation[i].x + k, nextLocation[i].y + j, PIECE_NONE);
                }
            }
        }
        for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
            for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                oldHoldDisplay[i][j] = PIECE_NONE;
                colPrintNewRender(holdLocation.x + j, holdLocation.y + i, PIECE_NONE);
            }
        }
    }

    private void printSingleBlockTo(Player playerTo, int x, int y, int z, int color) {
        Settings set = Main.gs.getData(player);
        if (set.isCustomSkinActive() && set.getSkin().get(color) == Skin.EXISTING) {
            Block b = location.getWorld().getBlockAt(x, y, z);
            Main.protocollib.sendBlockChangeCustom(playerTo, new Location(location.getWorld(), x, y, z), b);
        } else {
            Main.protocollib.sendBlockChangeCustom(playerTo, new Location(location.getWorld(), x, y, z), color);
        }
    }

    private void printSingleBlockToAll(int x, int y, int z, int color) {
        for (Player player : room.players) {
            printSingleBlockTo(player, x, y, z, color);
        }
    }

    private void render() {
        final UsablePiece piece = getCurrentPiece();
        final int[][] stage = getStage();

        int[][] newStageDisplay = new int[getSTAGESIZEY()][getSTAGESIZEX()];
        // update stage
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            System.arraycopy(stage[i], 0, newStageDisplay[i], 0, getSTAGESIZEX());
        }

        Point[] points = piece.getPoints();

        for (int i = 0; i < points.length; i++) {
            newStageDisplay[points[i].y + getLowestPossiblePosition()][points[i].x + piece.getX()] = GHOST_OFFSET + piece.getColors()[i];
        }

        for (int i = 0; i < points.length; i++) {
            newStageDisplay[points[i].y + piece.getY()][points[i].x + piece.getX()] = piece.getColors()[i];
        }

        // print stage
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                if (newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    if (newStageDisplay[i][j] == PIECE_NONE && i >= getSTAGESIZEY() - BACKROWS) {
                        colPrintNewRender(j, i, newStageDisplay[i][j]);
                    } else if (i >= getSTAGESIZEY() - FRONTROWS) {
                        if (newStageDisplay[i][j] != PIECE_NONE) {
                            colPrintNewRender(j, i, newStageDisplay[i][j]);
                        } else {
                            colPrintNewForce(j, i);
                        }
                    }
                }
            }
        }
        oldStageDisplay = newStageDisplay;

        int[] newGQDisplay = new int[getPLAYABLEROWS()];
        // update garbage meter
        int currentGarbage = 0;

        for (Object o : getGarbageQueue()) {
            Integer num = (Integer) o;
            currentGarbage += num;
        }

        int color = (currentGarbage / getPLAYABLEROWS()) % 7 + 1;
        currentGarbage = currentGarbage % getPLAYABLEROWS();

        for (int i = getPLAYABLEROWS() - 1; i >= getPLAYABLEROWS() - currentGarbage; i--) {
            newGQDisplay[i] = color;
        }

        for (int i = getPLAYABLEROWS() - currentGarbage - 1; i >= 0; i--) {
            newGQDisplay[i] = color - 1;
        }

        // print garbage meter
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            if (newGQDisplay[i] != oldGQDisplay[i]) {
                colPrintNewRender(garbageBarLocation.x, garbageBarLocation.y + i, newGQDisplay[i]);
            }
        }
        oldGQDisplay = newGQDisplay;

        if (piecesPrinted < getTotalPiecesPlaced() || (!everHeld && getHeldPiece() != null && !wasHeld && getHeld())) {
            int[][] newNextDisplay = new int[getPieceTable().mostPiecePoints() * getNEXTPIECES()][getPieceTable().mostPiecePoints()];
            // update next queue
            for (int i = 0; i < getNEXTPIECES(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                        newNextDisplay[i * getPieceTable().mostPiecePoints() + j][k] = PIECE_NONE;
                    }
                }
            }

            for (int i = 0; i < getNEXTPIECES(); i++) {
                    points = getNextPieces()[i].getPoints(0);

                    for (int j = 0; j < points.length; j++) {
                    newNextDisplay[i * getPieceTable().mostPiecePoints() + points[j].y][points[j].x] = getNextPieces()[i].getColors()[j];
                }
            }

            // print next queue
            for (int i = 0; i < getNEXTPIECES(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    for (int k = 0; k < getPieceTable().mostPiecePoints(); k++) {
                        colPrintNewRender(nextLocation[i].x + k, nextLocation[i].y + j, newNextDisplay[i * getPieceTable().mostPiecePoints() + j][k]);
                    }
                }
            }

            piecesPrinted = getTotalPiecesPlaced();

            oldNextDisplay = newNextDisplay;
        }

        if (wasHeld != getHeld()) {
            int[][] newHoldDisplay = new int[getPieceTable().mostPiecePoints()][getPieceTable().mostPiecePoints()];
            // update hold
            for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    newHoldDisplay[i][j] = PIECE_NONE;
                }
            }
            if (getHeldPiece() != null) {
                points = getHeldPiece().getPoints(0);

                for (int i = 0; i < points.length; i++) {
                    newHoldDisplay[points[i].y][points[i].x] = getHeldPiece().getColors()[i];
                }
            }

            // print hold
            for (int i = 0; i < getPieceTable().mostPiecePoints(); i++) {
                for (int j = 0; j < getPieceTable().mostPiecePoints(); j++) {
                    if (newHoldDisplay[i][j] != oldHoldDisplay[i][j]) {
                        colPrintNewRender(holdLocation.x + j, holdLocation.y + i, newHoldDisplay[i][j]);
                    }
                }
            }
            wasHeld = getHeld();
            everHeld = true;
            oldHoldDisplay = newHoldDisplay;
        }

        sendScoreboard();
    }

    private void sendScoreboard() {
        Map<Integer, String> text = new HashMap<>();

        text.put(6, "Garbage sent: " + getTotalGarbageSent());
        text.put(5, "Garbage received: " + getTotalGarbageReceived());
        text.put(4, "Lines: " + getTotalLinesCleared());
        text.put(3, "Pieces: " + getTotalPiecesPlaced());
        text.put(2, "Score: " + getTotalScore());
        text.put(1, "Tick: " + getTicksPassed());
        text.put(0, "Counter: " + getCounter() + "/" + getCounterEnd());
        text.put(-1, "Debug: " + getGameState() + getSpinState() + leftEmptyFor + rightEmptyFor + downEmptyFor + movLeft + movRight + movSoft + movCW);

        Main.netherboard.sendScoreboard(player, text);
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
            int tex, tey, tez;
            double xv = d * (2 - Math.random() * 4) * imi;
            double yv = d * (8 - Math.random() * 10) * imj;
            double zv = d * (2 - Math.random() * 4) * imk;
            for (int i = 0; i < (imi != 0 ? imi : THICKNESS); i++) {
                tex = location.getBlockX() + x * WMX + y * HMX + i;
                for (int j = 0; j < (imj != 0 ? imj : THICKNESS); j++) {
                    tey = location.getBlockY() + x * WMY + y * HMY + j;
                    for (int k = 0; k < (imk != 0 ? imk : THICKNESS); k++) {
                        tez = location.getBlockZ() + x * WMZ + y * HMZ + k;
                        Main.protocollib.sendFallingBlockCustom(player, new Location(location.getWorld(), tex - WMZ * 2, tey, tez + WMX * 2), color, xv, yv, zv);
                    }
                }
            }
        }
    }

    public enum DeathAnimation {
        NONE, EXPLOSION, GRAYSCALE, CLEAR, DISAPPEAR;
        private static final DeathAnimation[] vals = values();

        public DeathAnimation next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

}

