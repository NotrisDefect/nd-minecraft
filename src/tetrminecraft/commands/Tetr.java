package tetrminecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import tetrminecraft.Main;

public class Tetr implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }
        
        if (!sender.hasPermission("tetr.banned")) {
            if (args.length == 0 && player != null) {
                Choice.maximizeMenu(player);
            } else if (args[0].equalsIgnoreCase("controls")) {
                Choice.showControls(sender);
            } else if (args[0].equalsIgnoreCase("help")) {
                Choice.help(sender);
            } else if (args[0].equalsIgnoreCase("spectate") && Main.isDeveloper(sender) && player != null) {
                if (args.length < 2) {
                    player.sendMessage("Room id is missing!");
                } else {
                    try {
                        Main.roomMap.get(args[1]).addSpectator(player);
                    } catch (NullPointerException e) {
                        player.sendMessage("Null pointer exception! This room id most likely doesn't exist");
                    }
                }
            } else if (args[0].equalsIgnoreCase("disable") && sender.hasPermission("tetr.reload")) {
                Choice.disablePlugin();
            } else if (args[0].equalsIgnoreCase("fastjoin") && Main.isDeveloper(sender) && player != null) {
                try {
                    Main.roomMap.get(args[1]).addPlayer(player);
                    Main.lastui.put(player, "room");
                } catch (Exception e) {
                    player.sendMessage("Error");
                }

            } else if (args[0].equalsIgnoreCase("tetrachannel")) {
                if (args.length > 1) {
                   Choice.tetrioStats(sender, args[1]);
                } else {
                    sender.sendMessage("You need to specify a nickname to look up!");
                }
            } else {
                sender.sendMessage("Unknown command. /tetr help");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You cannot use this plugin");
        }
        return true;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/help tetr")) {
            e.setMessage("/tetr help");
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("help tetr")) {
            e.setCommand("tetr help");
        }
    }
    
}
