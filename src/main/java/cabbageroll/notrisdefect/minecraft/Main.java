package cabbageroll.notrisdefect.minecraft;

import cabbageroll.notrisdefect.minecraft.commands.MainCommand;
import cabbageroll.notrisdefect.minecraft.softdepend.netherboard.Netherboard;
import cabbageroll.notrisdefect.minecraft.softdepend.netherboard.NetherboardNo;
import cabbageroll.notrisdefect.minecraft.softdepend.netherboard.NetherboardYes;
import cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPI;
import cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPINo;
import cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPIYes;
import cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLib;
import cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLibNo;
import cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLibYes;
import cabbageroll.notrisdefect.minecraft.util.Version;
import cabbageroll.notrisdefect.minecraft.listeners.Listeners;
import cabbageroll.notrisdefect.minecraft.listeners.TableListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Main extends JavaPlugin implements Listener {

    public static final GameServer gs = new GameServer();
    public static Main plugin;
    public static ProtocolLib protocollib;
    public static Netherboard netherboard;
    public static NoteBlockAPI noteBlockAPI;
    public String nmsVersion;
    public int numericalVersion;

    @Override
    public void onEnable() {
        long timeStart = System.nanoTime();

        if (versionIsSupported()) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().severe("Unsupported server version");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PluginCommand rootCommand = getCommand(Strings.mainCommand);
        if (rootCommand != null) {
            rootCommand.setExecutor(MainCommand.getInstance());
        } else {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;

        getServer().getPluginManager().registerEvents(Listeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(TableListeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(MainCommand.getInstance(), this);

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
            if (numericalVersion > 16) {
                getLogger().warning("Netherboard supports only up to 1.17.1 when this was written. Be careful.");
            }
            getLogger().info("Netherboard OK.");
            netherboard = new NetherboardYes();
        }

        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("ProtocolLib is missing.");
            protocollib = new ProtocolLibNo();
        } else {
            if (numericalVersion > 16) {
                getLogger().warning("ProtocolLib currently serves no purpose for 1.17+");
            }
            getLogger().info("ProtocolLib OK.");
            protocollib = new ProtocolLibYes();
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
        nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        numericalVersion = Integer.parseInt(nmsVersion.split("_")[1]);
        getLogger().info("Your server is running version " + nmsVersion + " (" + numericalVersion + ")");
        return numericalVersion > 7;
    }
}