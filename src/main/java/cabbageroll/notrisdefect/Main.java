package cabbageroll.notrisdefect;

import cabbageroll.notrisdefect.commands.Choice;
import cabbageroll.notrisdefect.commands.Tetr;
import cabbageroll.notrisdefect.functions.softdepend.Netherboard;
import cabbageroll.notrisdefect.functions.softdepend.NetherboardNo;
import cabbageroll.notrisdefect.functions.softdepend.NetherboardYes;
import cabbageroll.notrisdefect.functions.softdepend.NoteBlockAPI;
import cabbageroll.notrisdefect.functions.softdepend.NoteBlockAPINo;
import cabbageroll.notrisdefect.functions.softdepend.NoteBlockAPIYes;
import cabbageroll.notrisdefect.functions.util.Version;
import cabbageroll.notrisdefect.functions.versions.Functions;
import cabbageroll.notrisdefect.listeners.TableListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tetrcore.LoadConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    public static GameServer gs = new GameServer();
    public static Functions functions;
    public static Netherboard netherboard;
    public static NoteBlockAPI noteBlockAPI;

    @Override
    public void onEnable() {
        long timeStart = System.nanoTime();

        if (versionIsSupported()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
            Choice.disablePlugin();
            return;
        }

        PluginCommand rootCommand = getCommand(Strings.mainCommand);
        if (rootCommand != null) {
            rootCommand.setExecutor(Tetr.getInstance());
        } else {
            Choice.disablePlugin();
            return;
        }

        plugin = this;

        try {
            LoadConfig.load();
        } catch (IOException e) {
            Main.plugin.getLogger().warning(e.getMessage());
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
        checkForUpdates();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gs.isPlayerHere(player)) {
            Main.gs.deinitialize(player);
        }
    }

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (
                InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=84269").openStream();
                Scanner scanner = new Scanner(inputStream)) {
                String currentVersion = this.getDescription().getVersion();
                String versionOnSpigot = scanner.next();
                Version current = new Version(currentVersion);
                Version onSpigot = new Version(versionOnSpigot);
                getLogger().info("You have " + current.compareToFriendly(onSpigot) + " version of the plugin!");
                if (current.compareTo(onSpigot) != 0) {
                    getLogger().info("Your version: " + currentVersion);
                    getLogger().info("Latest version on spigot: " + versionOnSpigot);
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    private boolean versionIsSupported() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        getLogger().info("Your server is running version " + version);

        try {
            functions = (Functions) Class.forName("cabbageroll.notrisdefect.functions.versions.Functions_" + version.substring(1)).newInstance();
            return true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
    }
}