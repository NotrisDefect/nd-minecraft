package tetrminecraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import tetrcore.Constants;
import tetrcore.LoadConfig;
import tetrminecraft.commands.Tetr;
import tetrminecraft.functions.Functions;
import tetrminecraft.functions.Functions_1_10_R1;
import tetrminecraft.functions.Functions_1_11_R1;
import tetrminecraft.functions.Functions_1_12_R1;
import tetrminecraft.functions.Functions_1_13_R1;
import tetrminecraft.functions.Functions_1_13_R2;
import tetrminecraft.functions.Functions_1_14_R1;
import tetrminecraft.functions.Functions_1_15_R1;
import tetrminecraft.functions.Functions_1_16_R1;
import tetrminecraft.functions.Functions_1_16_R2;
import tetrminecraft.functions.Functions_1_16_R3;
import tetrminecraft.functions.Functions_1_8_R1;
import tetrminecraft.functions.Functions_1_8_R2;
import tetrminecraft.functions.Functions_1_8_R3;
import tetrminecraft.functions.Functions_1_9_R1;
import tetrminecraft.functions.Functions_1_9_R2;

public class Main extends JavaPlugin implements Listener {

    private class UpdateChecker {

        private JavaPlugin plugin;

        public UpdateChecker(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public void getVersion(final Consumer<String> consumer) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try (InputStream inputStream = new URL(
                        "https://api.spigotmc.org/legacy/update.php?resource=84269").openStream();
                        Scanner scanner = new Scanner(inputStream)) {
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                } catch (IOException exception) {
                    plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
                }
            });
        }
    }

    public static boolean noteBlockAPIIsPresent;
    public static boolean netherBoardIsPresent;

    public static JavaPlugin plugin;
    public static ConsoleCommandSender console;

    public static LinkedHashMap<String, Room> roommap = new LinkedHashMap<String, Room>();
    public static HashMap<Player, Room> inwhichroom = new HashMap<Player, Room>();

    public static HashMap<Player, String> lastui = new HashMap<Player, String>();
    public static HashMap<Player, Integer> joinroompage = new HashMap<Player, Integer>();

    public static HashMap<Player, Integer> skineditorver = new HashMap<Player, Integer>();
    public static HashMap<Player, ItemStack[]> skinmap = new HashMap<Player, ItemStack[]>();
    
    public static Playlist playlist;
    public static RadioSongPlayer rsp;

    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isDeveloper(CommandSender sender) {
        if (Constants.iKnowWhatIAmDoing && (sender.hasPermission("tetr.developer"))) {
            return true;
        }
        return false;
    }

    public static Functions functions;

    public static int numberOfSongs;
    static Song[] songArray;
    private static String version;

    @Override
    public void onEnable() {
        long timeStart = System.nanoTime();

        plugin = this;

        try {
            LoadConfig.load();
        } catch (IOException e) {
            getLogger().severe("Error loading the config from inside jar (did you /reload?)");
            e.printStackTrace();
        }
        
        Tetr fuckingHorribleCode = new Tetr();
        console = getServer().getConsoleSender();
        this.getCommand("tetr").setExecutor(fuckingHorribleCode);

        // detect events
        getServer().getPluginManager().registerEvents(new Listen(), this);
        getServer().getPluginManager().registerEvents(fuckingHorribleCode, this);
        
        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") == null) {
            getLogger().severe("NoteBlockAPI not found, if you see any errors report it immediately!");
            noteBlockAPIIsPresent = false;
        } else {
            getLogger().info("NoteBlockAPI OK.");
            noteBlockAPIIsPresent = true;
        }

        if (getServer().getPluginManager().getPlugin("Netherboard") == null) {
            getLogger().severe("Netherboard not found, if you see any errors report it immediately!");
            netherBoardIsPresent = false;
        } else {
            getLogger().info("Netherboard OK.");
            netherBoardIsPresent = true;
        }

        if (noteBlockAPIIsPresent) {
            // trash
            File f = new File(this.getDataFolder() + "/songs");
            f.mkdirs();
            numberOfSongs = f.listFiles().length;
            if (numberOfSongs > 0) {

                String[] pathNames;
                String song;

                getLogger().info(numberOfSongs + " song(s) found");

                pathNames = new String[numberOfSongs];
                songArray = new Song[numberOfSongs];
                pathNames = f.list();
                for (int i = 0; i < numberOfSongs; i++) {
                    song = this.getDataFolder() + "/songs/" + pathNames[i];
                    songArray[i] = NBSDecoder.parse(new File(song));
                }

                playlist = new Playlist(songArray);
                // tRASH end
                rsp = new RadioSongPlayer(playlist);
                rsp.setChannelMode(new MonoStereoMode());
                rsp.setVolume((byte) 50);
            } else {
                getLogger().info("No songs detected. Please add some songs!");
            }
        }

        if (checkVersion()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            lastui.put(player, "home");

            if (!Main.skineditorver.containsKey(player)) {
                Main.skineditorver.put(player, 0);
            }

            initSkin(player);
        }
        
        long timeEnd = System.nanoTime();

        long timeElapsed = timeEnd - timeStart;

        getLogger().info("Done. Time elapsed: " + timeElapsed + "ms");

        // https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
        new UpdateChecker(this).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You have older/newer version of the plugin!");
                getLogger().info("Latest version on spigot: " + version);
                getLogger().info("Your version: " + this.getDescription().getVersion());
            }
        });
    }

    private boolean checkVersion() {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        getLogger().info("Your server is running version " + version);

        if (version.equals("v1_8_R1")) {
            functions = new Functions_1_8_R1();
        } else if (version.equals("v1_8_R2")) {
            functions = new Functions_1_8_R2();
        } else if (version.equals("v1_8_R3")) {
            functions = new Functions_1_8_R3();
        } else if (version.equals("v1_9_R1")) {
            functions = new Functions_1_9_R1();
        } else if (version.equals("v1_9_R2")) {
            functions = new Functions_1_9_R2();
        } else if (version.equals("v1_10_R1")) {
            functions = new Functions_1_10_R1();
        } else if (version.equals("v1_11_R1")) {
            functions = new Functions_1_11_R1();
        } else if (version.equals("v1_12_R1")) {
            functions = new Functions_1_12_R1();
        } else if (version.equals("v1_13_R1")) {
            functions = new Functions_1_13_R1();
        } else if (version.equals("v1_13_R2")) {
            functions = new Functions_1_13_R2();
        } else if (version.equals("v1_14_R1")) {
            functions = new Functions_1_14_R1();
        } else if (version.equals("v1_15_R1")) {
            functions = new Functions_1_15_R1();
        } else if (version.equals("v1_16_R1")) {
            functions = new Functions_1_16_R1();
        } else if (version.equals("v1_16_R2")) {
            functions = new Functions_1_16_R2();
        } else if (version.equals("v1_16_R3")) {
            functions = new Functions_1_16_R3();
        }

        return functions != null;
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        lastui.put(player, "home");
        initSkin(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (lastui.containsKey(player)) {
            lastui.remove(player);
        }
        if (inwhichroom.containsKey(player)) {
            Main.inwhichroom.get(player).removePlayer(player);
        }
    }

    public void initSkin(Player player) {
        File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        ItemStack[] blocks = new ItemStack[17];
        blocks[0] = customConfig.getItemStack("blockZ");
        blocks[1] = customConfig.getItemStack("blockL");
        blocks[2] = customConfig.getItemStack("blockO");
        blocks[3] = customConfig.getItemStack("blockS");
        blocks[4] = customConfig.getItemStack("blockI");
        blocks[5] = customConfig.getItemStack("blockJ");
        blocks[6] = customConfig.getItemStack("blockT");
        blocks[7] = customConfig.getItemStack("background");
        blocks[8] = customConfig.getItemStack("garbage");
        blocks[9] = customConfig.getItemStack("ghostZ");
        blocks[10] = customConfig.getItemStack("ghostL");
        blocks[11] = customConfig.getItemStack("ghostO");
        blocks[12] = customConfig.getItemStack("ghostS");
        blocks[13] = customConfig.getItemStack("ghostI");
        blocks[14] = customConfig.getItemStack("ghostJ");
        blocks[15] = customConfig.getItemStack("ghostT");
        blocks[16] = customConfig.getItemStack("zone");
        skinmap.put(player, blocks);
        Main.skineditorver.put(player, customConfig.getInt("useSkinSlot"));
    }
}