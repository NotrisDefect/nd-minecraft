package tetrminecraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import tetrcore.Constants;
import tetrcore.LoadConfig;
import tetrminecraft.commands.Tetr;
import tetrminecraft.functions.Functions;

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

    public static Map<String, Room> roomMap = new LinkedHashMap<String, Room>();
    public static Map<Player, Room> inwhichroom = new HashMap<Player, Room>();

    public static Map<Player, String> lastui = new HashMap<Player, String>();
    public static Map<Player, Integer> joinroompage = new HashMap<Player, Integer>();

    public static Map<Player, Boolean> playerUsesCustom = new HashMap<Player, Boolean>();
    public static Map<Player, ItemStack[]> skinmap = new HashMap<Player, ItemStack[]>();
    
    public static Playlist playlist;

    public static Functions functions;

    public static int numberOfSongs;

    private static Song[] songArray;

    public static boolean isDeveloper(CommandSender sender) {
        if (Constants.iKnowWhatIAmDoing && (sender.hasPermission("tetr.developer"))) {
            return true;
        }
        return false;
    }
    
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            } else {
                getLogger().info("No songs detected. Please add some songs!");
            }
        }

        if (versionIsSupported()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            lastui.put(player, "home");

            if (!Main.playerUsesCustom.containsKey(player)) {
                Main.playerUsesCustom.put(player, false);
            }

            initSkin(player);
        }
        
        long timeEnd = System.nanoTime();

        long timeElapsed = timeEnd - timeStart;

        getLogger().info("Done. Time elapsed: " + timeElapsed / 1000000 + "ms");

        // https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
        new UpdateChecker(this).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You have older/newer version of the plugin!");
                getLogger().info("Latest version on spigot: " + version);
                getLogger().info("Your version: " + this.getDescription().getVersion());
            }
        });
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

    private void initSkin(Player player) {
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
        Main.playerUsesCustom.put(player, customConfig.getBoolean("useSkinSlot"));
    }

    private boolean versionIsSupported() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        getLogger().info("Your server is running version " + version);
        
        try {
            functions = (Functions) Class.forName("tetrminecraft.functions.Functions_" + version.substring(1)).newInstance();
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}