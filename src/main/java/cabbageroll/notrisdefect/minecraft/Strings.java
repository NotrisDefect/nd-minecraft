package cabbageroll.notrisdefect.minecraft;

public class Strings {
    public static final String bench = "bench";
    public static final String controls = "controls";
    public static final String help = "help";
    public static final String joinroom = "fastjoin";
    public static final String restart = "flush";
    public static final String sfx = "sfx";
    public static final String tetrachannel = "tetrachannel";
    public static final String mainCommand = "notrisdefect";
    public static String pluginName = Main.plugin.getName();
    private static final String pluginName2 = "<" + pluginName + ">";
    public static String hostChange = pluginName2 + " Since the old room host left, you became the new host.";
    public static String gameInProgress = pluginName2 + " Game is in progress, wait for the round to finish.";
    public static String notEnoughPlayers = Strings.pluginName2 + " 2 players are needed";

    public static String nowPlaying(String song) {
        return Strings.pluginName2 + " Playing: " + song;
    }
}
