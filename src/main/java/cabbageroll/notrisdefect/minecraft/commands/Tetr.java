package cabbageroll.notrisdefect.minecraft.commands;

import cabbageroll.notrisdefect.minecraft.GameServer;
import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Strings;
import cabbageroll.notrisdefect.minecraft.menus.RoomMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class Tetr implements CommandExecutor, Listener {

    private static final Tetr instance = new Tetr();

    public static Tetr getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        String command = args.length == 0 ? null : args[0].toLowerCase();
        boolean fail = false;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null && !Main.gs.isPlayerHere(player)) {
            Main.gs.initialize(player);
            return true;
        }

        if (true) {
            if (command == null) {
                if (player != null) {
                    Choice.maximizeMenu(player);
                } else {
                    fail = true;
                }
            } else switch (command) {
                case Strings.bench:
                    if (player.hasPermission(Strings.permUnsafe)) {
                        int people = Integer.parseInt(args[1]);
                        int rooms = Integer.parseInt(args[2]);
                        int duration = Integer.parseInt(args[3]);
                        Choice.bench(people, rooms, duration);
                    } else {
                        fail = true;
                    }
                    break;
                case Strings.controls:
                    Choice.showControls(sender);
                    break;
                case Strings.help:
                    Choice.help(sender);
                    break;
                case Strings.joinroom:
                    if (sender.hasPermission(Strings.permUnsafe)) {
                        Main.gs.getRoom(args[1]).addPlayer(player);
                        new RoomMenu(player);
                    } else {
                        fail = true;
                    }
                    break;
                case Strings.restart:
                    if (sender.hasPermission("notrisdefect.use.experimental")) {
                        Main.gs = new GameServer();
                    } else {
                        fail = true;
                    }
                    break;
                case Strings.sfx:
                    Choice.showSfx(sender);
                    break;
                case Strings.tetrachannel:
                    if (args.length > 1) {
                        Choice.tetrioStats(sender, args[1]);
                    } else {
                        sender.sendMessage("You need to specify a nickname to look up!");
                    }
                    break;
                default:
                    fail = true;
                    break;
            }
        } else {
            sender.sendMessage("how");
        }

        if (fail) {
            Choice.help(sender);
        }

        return true;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/help " + Strings.pluginName)) {
            e.setMessage("/" + Strings.pluginName + " " + Strings.help);
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("help " + Strings.pluginName)) {
            e.setCommand(Strings.pluginName + " " + Strings.help);
        }
    }

}
