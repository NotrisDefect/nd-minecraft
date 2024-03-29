package in.cabbageroll.notrisdefect.minecraft;

public class Strings {
    public static final String controls = "controls";
    public static final String help = "help";
    public static final String joinroom = "fastjoin";
    public static final String flush = "flush";
    public static final String sfx = "sfx";
    public static final String disable = "disable";
    public static final String gui = "gui";
    public static final String materials = "xmaterial";
    public static final String tetrachannel = "tetrachannel";
    public static final String mainCommand = "notrisdefect";
    public static final String permUnsafe = "notrisdefect.use.experimental";
    public static final String permMP = "notrisdefect.use.multiplayer";
    public static final String permSkinEditor = "notrisdefect.use.skineditor";
    public static final String permSP = "notrisdefect.use.singleplayer";
    public static final String permManage = "notrisdefect.admin.manage";
    public static final String pluginName = Main.PLUGIN.getName();
    public static final String pluginName2 = "[" + pluginName + "]";
    public static final String ownerChange = pluginName2 + " You are now owner of this room.";
    public static final String gameInProgress = pluginName2 + " Game is in progress, wait for the round to finish.";
    public static final String notEnoughPlayers = pluginName2 + " 2 players are needed.";
    public static final String doesntExist = pluginName2 + " This room no longer exists.";

    public static String noPermission(String perm) {
        return pluginName2 + " Missing permission " + perm;
    }

    public static String nowPlaying(String song) {
        return pluginName2 + " Playing: " + song;
    }
}
