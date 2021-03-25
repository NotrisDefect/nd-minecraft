package tetrminecraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import tetrminecraft.Constants;
import tetrcore.LoadConfig;
import tetrminecraft.commands.Tetr;
import tetrminecraft.functions.dependencyutil.Netherboard;
import tetrminecraft.functions.dependencyutil.NetherboardNo;
import tetrminecraft.functions.dependencyutil.NetherboardYes;
import tetrminecraft.functions.dependencyutil.NoteBlockAPI;
import tetrminecraft.functions.dependencyutil.NoteBlockAPINo;
import tetrminecraft.functions.dependencyutil.NoteBlockAPIYes;
import tetrminecraft.functions.versions.Functions;
import tetrminecraft.menus.SongMenuInventoryClickEvent;

public class Main extends JavaPlugin implements Listener {

    private class UpdateChecker {

        private JavaPlugin plugin;

        public UpdateChecker(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public void getVersion(final Consumer<String> consumer) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=84269")
                        .openStream(); Scanner scanner = new Scanner(inputStream)) {
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                } catch (IOException exception) {
                    plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
                }
            });
        }
    }

    public static JavaPlugin plugin;
    public static ConsoleCommandSender console;

    public static Set<Player> interactedWithPlugin = new HashSet<Player>();

    public static Map<String, Room> roomByID = new LinkedHashMap<String, Room>();
    public static Map<Player, Room> inWhichRoomIs = new HashMap<Player, Room>();

    public static Map<Player, String> lastMenuOpened = new HashMap<Player, String>();
    public static Map<Player, Integer> joinRoomPage = new HashMap<Player, Integer>();

    public static Map<Player, Boolean> playerIsUsingCustomBlocks = new HashMap<Player, Boolean>();
    public static Map<Player, ItemStack[]> customBlocks = new HashMap<Player, ItemStack[]>();

    public static Functions functions;
    public static Netherboard netherboard;
    public static NoteBlockAPI noteBlockAPI;

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

        console = getServer().getConsoleSender();
        this.getCommand("tetr").setExecutor(Tetr.getInstance());

        // detect events
        getServer().getPluginManager().registerEvents(Listeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(Tetr.getInstance(), this);

        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") == null) {
            getLogger().severe("NoteBlockAPI not found, if you see any errors report it immediately!");
            noteBlockAPI = new NoteBlockAPINo();
        } else {
            getLogger().info("NoteBlockAPI OK.");
            noteBlockAPI = new NoteBlockAPIYes();
            NoteBlockAPIYes.loadSongs();
        }

        if (getServer().getPluginManager().getPlugin("Netherboard") == null) {
            getLogger().severe("Netherboard not found, if you see any errors report it immediately!");
            netherboard = new NetherboardNo();
        } else {
            getLogger().info("Netherboard OK.");
            netherboard = new NetherboardYes();
            Bukkit.getPluginManager().registerEvents(new SongMenuInventoryClickEvent(), this);
        }

        if (versionIsSupported()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version");
            Bukkit.getPluginManager().disablePlugin(this);
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
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (interactedWithPlugin.contains(player)) {
            lastMenuOpened.remove(player);
            if (inWhichRoomIs.get(player) != null) {
                inWhichRoomIs.get(player).removePlayer(player);
            }
            inWhichRoomIs.remove(player);
            interactedWithPlugin.remove(player);
            
            //save
            File customYml = new File(getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
            FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
            ItemStack[] blocks = customBlocks.get(player);
            for(int i=0;i<blocks.length;i++) {
                customConfig.set(Constants.NAMES[i], blocks[i]);
            }
            customConfig.set("playerIsUsingCustomBlocks", playerIsUsingCustomBlocks.get(player));
            Main.saveCustomYml(customConfig, customYml);
            
            customBlocks.remove(player);
            playerIsUsingCustomBlocks.remove(player);
        }
    }

    public static void firstInteraction(Player player) {
        interactedWithPlugin.add(player);
        lastMenuOpened.put(player, "home");

        File customYml = new File(Main.plugin.getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        ItemStack[] blocks = new ItemStack[17];
        for(int i=0;i<blocks.length;i++) {
            blocks[i] = customConfig.getItemStack(Constants.NAMES[i]);
        }
        customBlocks.put(player, blocks);
        Main.playerIsUsingCustomBlocks.put(player, customConfig.getBoolean("playerIsUsingCustomBlocks"));
    }

    private boolean versionIsSupported() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        getLogger().info("Your server is running version " + version);

        try {
            functions = (Functions) Class.forName("tetrminecraft.functions.versions.Functions_" + version.substring(1))
                    .newInstance();
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