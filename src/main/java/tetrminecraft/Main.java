package tetrminecraft;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tetrcore.LoadConfig;
import tetrminecraft.commands.Tetr;
import tetrminecraft.functions.softdepend.*;
import tetrminecraft.functions.versions.Functions;
import tetrminecraft.listeners.TableListeners;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class Main extends JavaPlugin implements Listener {

    public static Main instance;
    public final Set<Player> interactedWithPlugin = new HashSet<>();
    public final Map<String, Room> roomByID = new LinkedHashMap<>();
    public final Map<Player, Room> inWhichRoomIs = new HashMap<>();
    public final Map<Player, String> lastMenuOpened = new HashMap<>();
    public final Map<Player, Integer> joinRoomPage = new HashMap<>();
    public final Map<Player, Boolean> playerIsUsingCustomBlocks = new HashMap<>();
    public final Map<Player, ItemStack[]> customBlocks = new HashMap<>();
    public final Map<Player, ItemStack[]> skinMenuBuffer = new HashMap<>();
    public final Map<Player, Boolean> useSkinMenuBuffer = new HashMap<>();
    public final Map<Player, Boolean> playerTransparentBackground = new HashMap<>();
    public final Map<Player, Boolean> hasCustomMenuOpen = new HashMap<>();
    public Functions functions;
    public Netherboard netherboard;
    public NoteBlockAPI noteBlockAPI;

    public static boolean isDeveloperModeEnabled(CommandSender sender) {
        return Constants.iKnowWhatIAmDoing && (sender.hasPermission("tetr.developer"));
    }

    public void firstInteraction(Player player) {
        interactedWithPlugin.add(player);
        lastMenuOpened.put(player, "home");
        Main.instance.hasCustomMenuOpen.put(player, false);

        File customYml = new File(getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        ItemStack[] blocks = new ItemStack[17];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = customConfig.getItemStack(Constants.NAMES[i]);
        }
        customBlocks.put(player, blocks);
        playerIsUsingCustomBlocks.put(player, customConfig.getBoolean("playerIsUsingCustomBlocks"));
        playerTransparentBackground.put(player, customConfig.getBoolean("playerTransparentBackground"));
    }

    @Override
    public void onDisable() {
        for (Map.Entry<Player, ItemStack[]> entry : skinMenuBuffer.entrySet()) {
            saveSkin(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void onEnable() {
        long timeStart = System.nanoTime();

        if (versionIsSupported()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        PluginCommand commandTetr = getCommand("tetr");
        if (commandTetr != null) {
            commandTetr.setExecutor(Tetr.getInstance());
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        try {
            LoadConfig.load();
        } catch (Exception e) {
            getLogger().severe("Error loading the config from inside jar (did you /reload?)");
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(Listeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(TableListeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(Tetr.getInstance(), this);

        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") == null) {
            getLogger().severe("NoteBlockAPI is missing.");
            noteBlockAPI = new NoteBlockAPINo();
        } else {
            getLogger().info("NoteBlockAPI OK.");
            noteBlockAPI = new NoteBlockAPIYes();
            noteBlockAPI.loadSongs();
        }

        if (getServer().getPluginManager().getPlugin("Netherboard") == null) {
            getLogger().severe("Netherboard is missing.");
            netherboard = new NetherboardNo();
        } else {
            getLogger().info("Netherboard OK.");
            netherboard = new NetherboardYes();
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

            saveSkin(player, customBlocks.get(player));

            customBlocks.remove(player);
            playerIsUsingCustomBlocks.remove(player);
            playerTransparentBackground.remove(player);

            hasCustomMenuOpen.remove(player);
        }
    }

    private void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSkin(Player player, ItemStack[] blocks) {
        File customYml = new File(getDataFolder() + "/userdata/" + player.getUniqueId() + ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        for (int i = 0; i < blocks.length; i++) {
            customConfig.set(Constants.NAMES[i], blocks[i]);
        }
        customConfig.set("playerIsUsingCustomBlocks", playerIsUsingCustomBlocks.get(player));
        customConfig.set("playerTransparentBackground", playerTransparentBackground.get(player));
        saveCustomYml(customConfig, customYml);
    }

    private boolean versionIsSupported() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        getLogger().info("Your server is running version " + version);

        try {
            functions = (Functions) Class.forName("tetrminecraft.functions.versions.Functions_" + version.substring(1)).newInstance();
            return true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
    }

    private static class UpdateChecker {

        private final JavaPlugin plugin;

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
}