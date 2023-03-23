package in.cabbageroll.notrisdefect.minecraft;

import in.cabbageroll.notrisdefect.minecraft.commands.MainCommand;
import in.cabbageroll.notrisdefect.minecraft.softdepend.netherboard.Netherboard;
import in.cabbageroll.notrisdefect.minecraft.softdepend.netherboard.NetherboardNo;
import in.cabbageroll.notrisdefect.minecraft.softdepend.netherboard.NetherboardYes;
import in.cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPI;
import in.cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPINo;
import in.cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi.NoteBlockAPIYes;
import in.cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLib;
import in.cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLibNo;
import in.cabbageroll.notrisdefect.minecraft.softdepend.protocollib.ProtocolLibYes;
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

    public static GameServer GS;
    public static Main PLUGIN;
    public static ProtocolLib PROTOCOLLIB;
    public static Netherboard NETHERBOARD;
    public static NoteBlockAPI NOTEBLOCKAPI;
    public String NMSVERSION;
    public int VERSION;

    @Override
    public void onEnable() {
        long timeStart = System.nanoTime();

        if (!versionIsSupported()) {
            getLogger().severe("Unsupported server version");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PluginCommand rootCommand = getCommand(Strings.mainCommand);
        if (rootCommand == null) {
            getLogger().severe("Failed to get command");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PLUGIN = this;
        GS = new GameServer();

        rootCommand.setExecutor(MainCommand.getInstance());
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(Listeners.getInstance(), this);
        getServer().getPluginManager().registerEvents(MainCommand.getInstance(), this);

        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") == null) {
            getLogger().warning("NoteBlockAPI is missing.");
            NOTEBLOCKAPI = new NoteBlockAPINo();
        } else {
            getLogger().info("NoteBlockAPI OK.");
            NOTEBLOCKAPI = new NoteBlockAPIYes();
            NOTEBLOCKAPI.loadSongs();
        }

        if (getServer().getPluginManager().getPlugin("Netherboard") == null) {
            getLogger().warning("Netherboard is missing.");
            NETHERBOARD = new NetherboardNo();
        } else {
            if (VERSION > 16) {
                getLogger().warning("Netherboard supports only up to 1.17.1 when this was written. Be careful.");
            }
            getLogger().info("Netherboard OK.");
            NETHERBOARD = new NetherboardYes();
        }

        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().warning("ProtocolLib is missing.");
            PROTOCOLLIB = new ProtocolLibNo();
        } else {
            if (VERSION > 16) {
                getLogger().warning("ProtocolLib currently serves no purpose for 1.17+");
            }
            getLogger().info("ProtocolLib OK.");
            PROTOCOLLIB = new ProtocolLibYes();
        }

        long timeEnd = System.nanoTime();
        long timeElapsed = timeEnd - timeStart;
        getLogger().info("Done. Time elapsed: " + timeElapsed / 1000000 + "ms");

        checkForUpdates();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (GS.isPlayerUsingThePlugin(player)) {
            GS.deinitialize(player);
        }
    }

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
            try (
                InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=84269").openStream();
                Scanner scanner = new Scanner(inputStream)) {
                String currentVersion = getDescription().getVersion();
                String versionOnSpigot = scanner.next();
                SemVer current = new SemVer(currentVersion);
                SemVer onSpigot = new SemVer(versionOnSpigot);
                getLogger().info("You have " + current.compareToFriendly(onSpigot) + " version of the plugin!");
                if (current.compareTo(onSpigot) != 0) {
                    getLogger().info("Your version: " + currentVersion);
                    getLogger().info("Latest version on spigot: " + versionOnSpigot);
                }
            } catch (IOException exception) {
                PLUGIN.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    private boolean versionIsSupported() {
        NMSVERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        VERSION = Integer.parseInt(NMSVERSION.split("_")[1]);
        getLogger().info("Your server is running version " + NMSVERSION + " (" + VERSION + ")");
        return VERSION > 7;
    }
}