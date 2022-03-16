package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.core.GameLogic;
import cabbageroll.notrisdefect.core.Piece;
import cabbageroll.notrisdefect.minecraft.initialization.Sounds;
import cabbageroll.notrisdefect.minecraft.menus.Menu;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Table extends GameLogic {

    public static final int GHOST_OFFSET = 10;
    private static final int BOX = 4;
    private static final int FRONTROWS = 30;
    private static final int BACKROWS = 20;
    private static final int THICKNESS = 1;
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
    private Room room;
    private Menu lastMenuOpened;
    private Location location;

    // board elements
    private int NEXTVERTICAL = 5;
    private int holdTLCX = -3 - BOX;
    private int holdTLCY = STAGESIZEY / 2;
    private int nextTLCX = STAGESIZEX + 3;
    private int nextTLCY = STAGESIZEY / 2;
    private int garbBLCX = -2;
    private int garbBLCY = STAGESIZEY - 1;

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

    // rendering
    private int[][] oldStageDisplay;
    private int[] oldGQDisplay;
    private int[][] oldNextDisplay;
    private int[][] oldHoldDisplay;
    private long linesPrinted;
    private long piecesPrinted;
    private boolean wasHeld;
    private boolean everHeld;

    public Table(Player player) {
        super();
        this.player = player;
    }

    private static char intToPieceName(int p) {
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
            default:
                return '?';
        }
    }

    public void destroyTable() {
        cleanAll();
        Main.netherboard.removeBoard(player);
    }

    public void drawLogo(int color0, int color1) {
        // temp fix
        if (STAGESIZEY != 40 || STAGESIZEX != 10) {
            drawAll(color1);
        }

        for (int i = 0; i < STAGESIZEY - BACKROWS; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                if (logo[i][j] == 1) {
                    colPrintNewRender(j, i, color1);
                } else {
                    colPrintNewForce(j, i);
                }
            }
        }

        for (int i = STAGESIZEY - BACKROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewRender(j, i, logo[i][j] == 0 ? color0 : color1);
            }
        }

        for (int i = 0; i < PLAYABLEROWS; i++) {
            colPrintNewRender(garbBLCX, garbBLCY - i, color0);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
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

    public int getNEXTPIECES() {
        return NEXTPIECES;
    }

    public void setNEXTPIECES(int NEXTPIECES) {
        cleanAll();
        this.NEXTPIECES = NEXTPIECES;
        drawLogo(PIECE_NONE, PIECE_ZONE);
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

    public int getPLAYABLEROWS() {
        return PLAYABLEROWS;
    }

    public void setPLAYABLEROWS(int PLAYABLEROWS) {
        cleanAll();
        this.PLAYABLEROWS = PLAYABLEROWS;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public Player getPlayer() {
        return player;
    }

    public Room getRoom() {
        return room;
    }

    public int getSTAGESIZEX() {
        return STAGESIZEX;
    }

    public void setSTAGESIZEX(int STAGESIZEX) {
        cleanAll();
        this.STAGESIZEX = STAGESIZEX;
        drawLogo(PIECE_NONE, PIECE_ZONE);
    }

    public int getSTAGESIZEY() {
        return STAGESIZEY;
    }

    public void setSTAGESIZEY(int STAGESIZEY) {
        cleanAll();
        this.STAGESIZEY = STAGESIZEY;
        drawLogo(PIECE_NONE, PIECE_ZONE);
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

    public void setDelays(int psd, int lcd) {
        PIECESPAWNDELAY = psd;
        LINECLEARDELAY = lcd;
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

    public void updateWholeTableTo(Player player) {

    }

    @Override
    protected void evtGameover() {
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
                        if (getStage()[i][j] != PIECE_NONE)
                            colPrintNewRender(j, i, PIECE_GARBAGE);
                    }
                }
                break;
            case CLEAR:
                for (int i = 0; i < STAGESIZEY; i++) {
                    for (int j = 0; j < STAGESIZEX; j++) {
                        if (getStage()[i][j] != PIECE_NONE) {
                            colPrintNewRender(j, i, PIECE_NONE);
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
    protected void evtLineClear(int height, int[] line) {
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
    protected void evtLockPiece(Piece piece, int linesCleared, int spinState, int combo, int backToBack) {
        StringBuilder sb = new StringBuilder();

        if (backToBack > 0) {
            sb.append("B2B ");
        }

        sb.append(intToPieceName(piece.getColor()));

        switch (spinState) {
            case SPIN_MINI:
                sb.append(" SPIN MINI");
                break;
            case SPIN_FULL:
                sb.append(" SPIN");
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

    private void cleanAll() {
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewForce(j, i);
            }
        }
        for (int i = 0; i < PLAYABLEROWS; i++) {
            colPrintNewForce(garbBLCX, garbBLCY - i);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
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
        for (int i = STAGESIZEY - BACKROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewRender(j, i, color);
            }
        }
        for (int i = 0; i < PLAYABLEROWS; i++) {
            colPrintNewRender(garbBLCX, garbBLCY - i, color);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
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
                    sendScoreboard();
                    this.cancel();
                } else {
                    render();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 1);
    }

    private boolean isThereAProblem() {
        boolean bwx = WMX != 0;
        boolean bwy = WMY != 0;
        boolean bwz = WMZ != 0;
        boolean bhx = HMX != 0;
        boolean bhy = HMY != 0;
        boolean bhz = HMZ != 0;

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
            setWidthMultiplier(0, 0, -1);
            setHeightMultiplier(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() - PLAYABLEROWS * 1.5);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - PLAYABLEROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() + STAGESIZEX / 2);
        } else if (135 <= yaw && yaw < 225) {
            setWidthMultiplier(1, 0, 0);
            setHeightMultiplier(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() - STAGESIZEX / 2);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - PLAYABLEROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() - PLAYABLEROWS * 1.5);
        } else if (225 <= yaw && yaw < 315) {
            setWidthMultiplier(0, 0, 1);
            setHeightMultiplier(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() + PLAYABLEROWS * 1.5);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - PLAYABLEROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() - STAGESIZEX / 2);
        } else if ((315 <= yaw && yaw < 360) || (0 <= yaw && yaw < 45)) {
            setWidthMultiplier(-1, 0, 0);
            setHeightMultiplier(0, -1, 0);
            this.location.setX(playerLocation.getBlockX() + STAGESIZEX / 2);
            this.location.setY(playerLocation.getBlockY() + STAGESIZEY - PLAYABLEROWS / 2);
            this.location.setZ(playerLocation.getBlockZ() + PLAYABLEROWS * 1.5);
        }
    }

    private void prepare() {
        if (isThereAProblem()) {
            player.sendMessage("there was a problem");
        }

        imi = Math.max(Math.abs(WMX), Math.abs(HMX));
        imj = Math.max(Math.abs(WMY), Math.abs(HMY));
        imk = Math.max(Math.abs(WMZ), Math.abs(HMZ));

        player.getInventory().setHeldItemSlot(8);

        cleanAll();

        oldGQDisplay = new int[PLAYABLEROWS];
        oldNextDisplay = new int[BOX * NEXTVERTICAL][BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL)];
        oldHoldDisplay = new int[BOX][BOX];
        oldStageDisplay = new int[STAGESIZEY][STAGESIZEX];
        linesPrinted = 0;
        //to print on first tick
        piecesPrinted = -1;
        wasHeld = false;
        everHeld = false;

        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                oldStageDisplay[i][j] = PIECE_NONE;
            }
        }
        for (int i = STAGESIZEY - BACKROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                colPrintNewRender(j, i, PIECE_NONE);
            }
        }

        for (int i = 0; i < PLAYABLEROWS; i++) {
            oldGQDisplay[i] = PIECE_NONE;
            colPrintNewRender(garbBLCX, garbBLCY - i, PIECE_NONE);
        }
        for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
            for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
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
        if (color == PIECE_NONE && Main.gs.getData(player).isTransparent()) {
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

        int[][] newStageDisplay = new int[STAGESIZEY][STAGESIZEX];
        // update stage
        for (int i = 0; i < STAGESIZEY; i++) {
            System.arraycopy(stage[i], 0, newStageDisplay[i], 0, STAGESIZEX);
        }

        for (Point point : getPoints(piece, rot)) {
            newStageDisplay[point.y + getLowestPossiblePosition()][point.x + cpp.x] = GHOST_OFFSET + piece - 1;
        }

        for (Point point : getPoints(piece, rot)) {
            newStageDisplay[point.y + cpp.y][point.x + cpp.x] = piece;
        }

        // print stage
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                if (newStageDisplay[i][j] != oldStageDisplay[i][j]) {
                    if (newStageDisplay[i][j] == PIECE_NONE && i >= STAGESIZEY - BACKROWS) {
                        colPrintNewRender(j, i, newStageDisplay[i][j]);
                    } else if (i >= STAGESIZEY - FRONTROWS) {
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

        int[] newGQDisplay = new int[PLAYABLEROWS];
        // update garbage meter
        int total = 0;
        int color;

        for (Object o : getGarbageQueue()) {
            Integer num = (Integer) o;
            total += num;
        }

        color = (total / PLAYABLEROWS) % 7 + 1;
        total = total % PLAYABLEROWS;

        for (int i = 0; i < total; i++) {
            newGQDisplay[i] = color;
        }

        for (int i = total; i < PLAYABLEROWS; i++) {
            newGQDisplay[i] = PIECE_NONE;
        }

        // print garbage meter
        for (int i = 0; i < PLAYABLEROWS; i++) {
            if (newGQDisplay[i] != oldGQDisplay[i]) {
                colPrintNewRender(garbBLCX, garbBLCY - i, newGQDisplay[i]);
            }
        }
        oldGQDisplay = newGQDisplay;

        if (piecesPrinted < getTotalPiecesPlaced() || (!everHeld && getHeldPiece() != null && !wasHeld && getHeld())) {
            int[][] newNextDisplay = new int[BOX * NEXTVERTICAL][BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL)];
            // update next queue
            for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
                for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
                    newNextDisplay[i][j] = PIECE_NONE;
                }
            }

            for (int i = 0; i < NEXTPIECES; i++) {
                for (Point point : getPoints(getNextPieces()[i].getColor(), 0)) {
                    newNextDisplay[point.y + i % NEXTVERTICAL * BOX][point.x + BOX * (i / NEXTVERTICAL)] = getNextPieces()[i].getColor();
                }
            }

            // print next queue
            for (int i = 0; i < BOX * NEXTVERTICAL; i++) {
                for (int j = 0; j < BOX * (int) Math.ceil(NEXTPIECES / (double) NEXTVERTICAL); j++) {
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
        text.put(-1, "Debug: " + getGameState() + getSpinState());

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


    /*
    Aelita/vision:
        passive - 2 extra next pieces
        active - can see where next garbage hole will be
    Odd/spins:
        passive - all spin
        active - stupid spin
    Ulrich/speed:
        passive - less line clear delay
        active - triplicate next piece
    William/power:
        passive - back to back chain
        active - zone mechanic
    Yumi/teleportation:
        passive - ascension kick table
        active - next 3 spins of current piece deal damage
    private enum Ability {
        VISION, SPINS, SPEED, POWER, TELEPORTATION
    }
     */

}

