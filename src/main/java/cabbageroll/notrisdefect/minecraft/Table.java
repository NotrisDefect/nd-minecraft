package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.core.GameLogic;
import cabbageroll.notrisdefect.core.Piece;
import cabbageroll.notrisdefect.core.Point;
import cabbageroll.notrisdefect.minecraft.initialization.Sounds;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
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
    private static final int BOX = 4;
    private static final int FRONTROWS = 30;
    private static final int BACKROWS = 20;
    private static final int THICKNESS = 1;
    private static final int GAP = 2;
    public static DeathAnimation deathAnim = DeathAnimation.GRAYSCALE;
    private final int AWAYMOVE = (int) (getPLAYABLEROWS() * 1.5);
    private final int DOWNMOVE = getSTAGESIZEY() - (getPLAYABLEROWS() >> 1);
    private final int SIDEMOVE = (getSTAGESIZEX() >> 1);
    private final Player player;
    // stupid
    private final int[][] logo = {
        {0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
        {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 0, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
        {0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
        {1, 1, 0, 0, 1, 1, 1, 0, 0, 0},
        {1, 1, 0, 1, 1, 1, 1, 0, 0, 0},
        {1, 1, 0, 1, 1, 0, 1, 1, 0, 0},
        {1, 1, 1, 1, 1, 0, 1, 1, 0, 0},
        {0, 1, 1, 1, 0, 0, 1, 1, 0, 0},
        {0, 0, 1, 1, 0, 0, 0, 0, 0, 0}
    };
    public boolean enableAnimations = true;
    private boolean ZONEENABLED = false;
    private Room room;
    private Menu lastMenuOpened;
    private Location location;
    // board elements
    private int NEXTVERTICAL = 5;
    private int holdTLCX = -3 - BOX;
    private int holdTLCY = getSTAGESIZEY() / 2;
    private int nextTLCX = getSTAGESIZEX() + 3;
    private int nextTLCY = getSTAGESIZEY() / 2;
    private int garbBLCX = -2;
    private int garbBLCY = getSTAGESIZEY() - 1;
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
        super();
        this.player = player;
    }

    public static DeathAnimation getDeathAnim() {
        return deathAnim;
    }

    public static String intToPieceName(int p) {
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

    private static char intToPieceChar(int p) {
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

        if (leftEmptyFor < GAP) {
            doPressLeft();
        } else {
            doReleaseLeft();
            leftEmptyFor = GAP;
        }

        if (rightEmptyFor < GAP) {
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

    public void destroyTable() {
        cleanAll();
        Main.netherboard.removeBoard(player);
    }

    public void drawLogo(int color0, int color1) {
        // temp fix
        if (getSTAGESIZEY() != 40 || getSTAGESIZEX() != 10) {
            drawAll(color1);
            return;
        }

        for (int i = 0; i < getSTAGESIZEY() - BACKROWS; i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                if (logo[i][j] == 1) {
                    colPrintNewRender(j, i, color1);
                } else {
                    colPrintNewForce(j, i);
                }
            }
        }

        for (int i = getSTAGESIZEY() - BACKROWS; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewRender(j, i, logo[i][j] == 0 ? color0 : color1);
            }
        }

        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            colPrintNewRender(garbBLCX, garbBLCY - i, color0);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                colPrintNewRender(nextTLCX + j, nextTLCY + i, color0);
            }
        }
        for (int i = 0; i < BOX; i++) {
            for (int j = 0; j < BOX; j++) {
                colPrintNewRender(holdTLCX + j, holdTLCY + i, color0);
            }
        }
    }

    public int getGarbBLCX() {
        return garbBLCX;
    }

    public int getGarbBLCY() {
        return garbBLCY;
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

    public int getNEXTVERTICAL() {
        return NEXTVERTICAL;
    }

    public void setNEXTVERTICAL(int NEXTVERTICAL) {
        cleanAll();
        this.NEXTVERTICAL = NEXTVERTICAL;
        drawLogo(PIECE_NONE, PIECE_ZONE);
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
        position();
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void leaveRoom() {
        room = null;
    }

    public void moveGarbBLCXRelative(int n) {
        cleanAll();
        garbBLCX += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveGarbBLCYRelative(int n) {
        cleanAll();
        garbBLCY += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveHoldTLCXRelative(int n) {
        cleanAll();
        holdTLCX += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveHoldTLCYRelative(int n) {
        cleanAll();
        holdTLCY += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveNextTLCXRelative(int n) {
        cleanAll();
        nextTLCX += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveNextTLCYRelative(int n) {
        cleanAll();
        nextTLCY += n;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveTable(int x, int y, int z) {
        cleanAll();
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void moveTableRelative(int x, int y, int z) {
        moveTable(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
    }

    public void reposition() {
        cleanAll();
        position();
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public void rotateX() {
        cleanAll();
        setWidthMultiplier(WMX, -WMZ, WMY);
        setHeightMultiplier(HMX, -HMZ, HMY);
        drawLogo(PIECE_NONE, PIECE_Z);
    }

    public void rotateY() {
        cleanAll();
        setWidthMultiplier(WMZ, WMY, -WMX);
        setHeightMultiplier(HMZ, HMY, -HMX);
        drawLogo(PIECE_NONE, PIECE_S);
    }

    public void rotateZ() {
        cleanAll();
        setWidthMultiplier(-WMY, WMX, WMZ);
        setHeightMultiplier(-HMY, HMX, HMZ);
        drawLogo(PIECE_NONE, PIECE_J);
    }

    @Override
    public void setNEXTPIECES(int n) {
        cleanAll();
        super.setNEXTPIECES(n);
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    @Override
    public void setPLAYABLEROWS(int n) {
        cleanAll();
        super.setPLAYABLEROWS(n);
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    @Override
    public void setSTAGESIZEX(int n) {
        cleanAll();
        super.setSTAGESIZEX(n);
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    @Override
    public void setSTAGESIZEY(int n) {
        cleanAll();
        super.setSTAGESIZEY(n);
        drawLogo(PIECE_NONE, PIECE_ZONE);
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

        playSound(Sounds.lineClear, 5f, 1f);
    }

    @Override
    protected void evtLockPiece(Piece piece, int linesCleared, int spinState, int combo, int backToBack, boolean nuke) {
        StringBuilder sb = new StringBuilder();

        if (backToBack > 0) {
            sb.append("B2B ");
        }

        sb.append(intToPieceChar(piece.getColor()));

        switch (spinState) {
            case SPIN_MINI:
                sb.append("-SPIN MINI");
                break;
            case SPIN_FULL:
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
                sb.append(" NOTRIS");
                break;
        }

        if (nuke && linesCleared > 0) {
            sb.append("+");
        }

        if (combo > -1) {
            sb.append(" ").append(combo).append(" COMBO");
        }

        ActionBar.sendActionBar(player, sb.toString());
    }

    @Override
    protected void evtPerfectClear() {
        Main.protocollib.sendTitleCustom(player, "", "PERFECT CLEAR", 20, 20, 40);
    }

    @Override
    protected void evtSendGarbage(int i) {
        room.forwardGarbage(i, player);
    }

    @Override
    protected void evtSpin() {
        playSound(Sounds.spin, 5f, 1f);
    }

    public void skewHX(int n) {
        cleanAll();
        HMX += n;
        drawLogo(PIECE_NONE, PIECE_Z);
    }

    public void skewHY(int n) {
        cleanAll();
        HMY += n;
        drawLogo(PIECE_NONE, PIECE_S);
    }

    public void skewHZ(int n) {
        cleanAll();
        HMZ += n;
        drawLogo(PIECE_NONE, PIECE_J);
    }

    public void skewWX(int n) {
        cleanAll();
        WMX += n;
        drawLogo(PIECE_NONE, PIECE_Z);
    }

    public void skewWY(int n) {
        cleanAll();
        WMY += n;
        drawLogo(PIECE_NONE, PIECE_S);
    }

    public void skewWZ(int n) {
        cleanAll();
        WMZ += n;
        drawLogo(PIECE_NONE, PIECE_J);
    }

    public void switchDeathAnim() {
        deathAnim = deathAnim.next();
    }

    public void updateWholeTableTo(Player player) {

    }

    private void cleanAll() {
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            for (int j = 0; j < getSTAGESIZEX(); j++) {
                colPrintNewForce(j, i);
            }
        }
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            colPrintNewForce(garbBLCX, garbBLCY - i);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                colPrintNewForce(nextTLCX + j, nextTLCY + i);
            }
        }
        for (int i = 0; i < BOX; i++) {
            for (int j = 0; j < BOX; j++) {
                colPrintNewForce(holdTLCX + j, holdTLCY + i);
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
            colPrintNewRender(garbBLCX, garbBLCY - i, color);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                colPrintNewRender(nextTLCX + j, nextTLCY + i, color);
            }
        }
        for (int i = 0; i < BOX; i++) {
            for (int j = 0; j < BOX; j++) {
                colPrintNewRender(holdTLCX + j, holdTLCY + i, color);
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
                location.add(DOWNMOVE, 0, SIDEMOVE);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, 0, -1);
                location.add(-SIDEMOVE, 0, DOWNMOVE);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(1, 0, 0);
                location.add(-DOWNMOVE, 0, -SIDEMOVE);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, 0, 1);
                location.add(SIDEMOVE, 0, -DOWNMOVE);
            }
        } else if (-45 <= pitch && pitch < 45) {
            location.add(0, DOWNMOVE, 0);
            flipupdown = false;
            if (45 <= yaw && yaw < 135) {
                setWidthMultiplier(0, 0, -1);
                setHeightMultiplier(0, -1, 0);
                location.add(-AWAYMOVE, 0, SIDEMOVE);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, -1, 0);
                location.add(-SIDEMOVE, 0, -AWAYMOVE);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(0, -1, 0);
                location.add(AWAYMOVE, 0, -SIDEMOVE);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, -1, 0);
                location.add(SIDEMOVE, 0, AWAYMOVE);
            }
        } else if (45 <= pitch) {
            location.add(0, -AWAYMOVE, 0);
            flipupdown = true;
            if (45 <= yaw && yaw < 135) {
                setWidthMultiplier(0, 0, -1);
                setHeightMultiplier(1, 0, 0);
                location.add(-DOWNMOVE, 0, SIDEMOVE);
            } else if (135 <= yaw && yaw < 225) {
                setWidthMultiplier(1, 0, 0);
                setHeightMultiplier(0, 0, 1);
                location.add(-SIDEMOVE, 0, -DOWNMOVE);
            } else if (225 <= yaw && yaw < 315) {
                setWidthMultiplier(0, 0, 1);
                setHeightMultiplier(-1, 0, 0);
                location.add(DOWNMOVE, 0, -SIDEMOVE);
            } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
                setWidthMultiplier(-1, 0, 0);
                setHeightMultiplier(0, 0, -1);
                location.add(SIDEMOVE, 0, DOWNMOVE);
            }
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
        oldNextDisplay = new int[BOX * NEXTVERTICAL][BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL)];
        oldHoldDisplay = new int[BOX][BOX];
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
            colPrintNewRender(garbBLCX, garbBLCY - i, PIECE_NONE);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                oldNextDisplay[i][j] = PIECE_NONE;
                colPrintNewRender(nextTLCX + j, nextTLCY + i, PIECE_NONE);
            }
        }
        for (int i = 0; i < BOX; i++) {
            for (int j = 0; j < BOX; j++) {
                oldHoldDisplay[i][j] = PIECE_NONE;
                colPrintNewRender(holdTLCX + j, holdTLCY + i, PIECE_NONE);
            }
        }
    }

    private void printSingleBlockTo(Player playerTo, int x, int y, int z, int color) {
        if (Main.gs.getData(player).getSkin().get(color) == Skin.EXISTING) {
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
        final Piece pieceFull = getCurrentPiece();
        final Point cpp = new Point(pieceFull.getX(), pieceFull.getY());
        final int rot = pieceFull.getRotation();
        final int piece = getCurrentPiece().getColor();
        final int[][] stage = getStage();

        int[][] newStageDisplay = new int[getSTAGESIZEY()][getSTAGESIZEX()];
        // update stage
        for (int i = 0; i < getSTAGESIZEY(); i++) {
            System.arraycopy(stage[i], 0, newStageDisplay[i], 0, getSTAGESIZEX());
        }

        for (Point point : getPoints(piece, rot)) {
            newStageDisplay[point.y + getLowestPossiblePosition()][point.x + cpp.x] = GHOST_OFFSET + piece;
        }

        for (Point point : getPoints(piece, rot)) {
            newStageDisplay[point.y + cpp.y][point.x + cpp.x] = piece;
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
        int total = 0;
        int color;

        for (Object o : getGarbageQueue()) {
            Integer num = (Integer) o;
            total += num;
        }

        color = (total / getPLAYABLEROWS()) % 7 + 1;
        total = total % getPLAYABLEROWS();

        for (int i = 0; i < total; i++) {
            newGQDisplay[i] = color;
        }

        for (int i = total; i < getPLAYABLEROWS(); i++) {
            newGQDisplay[i] = PIECE_NONE;
        }

        // print garbage meter
        for (int i = 0; i < getPLAYABLEROWS(); i++) {
            if (newGQDisplay[i] != oldGQDisplay[i]) {
                colPrintNewRender(garbBLCX, garbBLCY - i, newGQDisplay[i]);
            }
        }
        oldGQDisplay = newGQDisplay;

        if (piecesPrinted < getTotalPiecesPlaced() || (!everHeld && getHeldPiece() != null && !wasHeld && getHeld())) {
            int[][] newNextDisplay = new int[BOX * NEXTVERTICAL][BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL)];
            // update next queue
            for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
                for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                    newNextDisplay[i][j] = PIECE_NONE;
                }
            }

            for (int i = 0; i < getNEXTPIECES(); i++) {
                for (Point point : getPoints(getNextPieces()[i].getColor(), 0)) {
                    newNextDisplay[point.y + i % NEXTVERTICAL * BOX][point.x + BOX * (i / NEXTVERTICAL)] = getNextPieces()[i].getColor();
                }
            }

            // print next queue
            for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
                for (int j = 0; j < BOX * (int) Math.ceil(getNEXTPIECES() / (double) NEXTVERTICAL); j++) {
                    if (newNextDisplay[i][j] != oldNextDisplay[i][j]) {
                        colPrintNewRender(nextTLCX + j, nextTLCY + i, newNextDisplay[i][j]);
                    }
                }
            }

            piecesPrinted = getTotalPiecesPlaced();

            oldNextDisplay = newNextDisplay;
        }

        if (wasHeld != getHeld()) {
            int[][] newHoldDisplay = new int[BOX][BOX];
            // update hold
            for (int i = 0; i < BOX; i++) {
                for (int j = 0; j < BOX; j++) {
                    newHoldDisplay[i][j] = PIECE_NONE;
                }
            }
            if (getHeldPiece() != null) {
                for (Point point : getPoints(getHeldPiece().getColor(), 0)) {
                    newHoldDisplay[point.y][point.x] = getHeldPiece().getColor();
                }
            }

            // print hold
            for (int i = 0; i < BOX; i++) {
                for (int j = 0; j < BOX; j++) {
                    if (newHoldDisplay[i][j] != oldHoldDisplay[i][j]) {
                        colPrintNewRender(holdTLCX + j, holdTLCY + i, newHoldDisplay[i][j]);
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

