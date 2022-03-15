package cabbageroll.notrisdefect.minecraft.commands;

import cabbageroll.notrisdefect.minecraft.Main;
import cabbageroll.notrisdefect.minecraft.Strings;
import cabbageroll.notrisdefect.minecraft.initialization.Sounds;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor, Listener {

    private static final MainCommand instance = new MainCommand();
    private final Map<String, SubCommand> commands = new HashMap<>();
    private final ChatColor color = ChatColor.BLUE;

    private MainCommand() {
        commands.put(Strings.help, (sender, cmd, label, args) -> {
            StringBuilder sb = new StringBuilder();

            sb.append(color).append(Strings.pluginName).append(' ').append(Strings.help);

            sb.append('\n').append(color).append("Aliases: notrisdefect, notris, nd");

            if (sender instanceof Player) {
                sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.gui + " - open game window");
            }

            if (sender.hasPermission(Strings.permManage)) {
                sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.disable).append(" - permanently disable the plugin");
                sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.flush).append(" - flush all current session data");
            }

            sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.help).append(" - shows this help page");
            sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.controls).append(" - shows guide on how to set the controls");
            sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.tetrachannel).append(" <nickname> - get stats of a player from ch.tetr.io");
            sb.append('\n').append(color).append('/').append(label).append(' ').append(Strings.sfx).append(" - show sounds");

            sender.sendMessage(sb.toString());
            return true;
        });
        commands.put(Strings.controls, (sender, cmd, label, args) -> {
            String[] stuff = {
                "Move left (once): Hotbar Slot 1",
                "\nMove left (repeat): Strafe Left",
                "\nMove right (once): Hotbar Slot 2",
                "\nMove right (repeat): Strafe Right",
                "\nInstant soft drop: Hotbar Slot 3",
                "\nSoft drop: Walk Backwards",
                "\nHard drop: Hotbar Slot 4",
                "\nRotate counterclockwise: Hotbar Slot 5",
                "\nRotate clockwise: Hotbar Slot 6",
                "\nRotate 180: Hotbar Slot 7",
                "\nHold: Hotbar Slot 8",
                "\nZone: Sneak",
            };

            StringBuilder sb = new StringBuilder();
            for (String s : stuff) {
                sb.append(s);
            }
            sender.sendMessage(sb.toString());
            return true;
        });
        commands.put(Strings.sfx, (sender, cmd, label, args) -> {
            String[] descriptions = {
                "spin: ",
                "\nclearSpin: ",
                "\nclear: ",
            };

            String[] sounds = {
                (Sounds.spin != null ? Sounds.spin.name() : "null"),
                (Sounds.lineClearSpin != null ? Sounds.lineClearSpin.name() : "null"),
                (Sounds.lineClear != null ? Sounds.lineClear.name() : "null")
            };

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < descriptions.length; i++) {
                sb.append(descriptions[i]);
                sb.append(sounds[i]);
            }

            sender.sendMessage(sb.toString());
            return true;
        });
        commands.put(Strings.gui, (sender, cmd, label, args) -> {
            if (sender instanceof Player) {
                Main.gs.openLastMenu((Player) sender);
                return true;
            }
            return false;
        });
        commands.put(Strings.disable, (sender, cmd, label, args) -> {
            if (sender.hasPermission(Strings.permManage)) {
                Bukkit.getServer().getPluginManager().disablePlugin(Main.plugin);
                return true;
            }
            return false;
        });
        commands.put(Strings.flush, (sender, cmd, label, args) -> {
            if (sender.hasPermission(Strings.permManage)) {
                Main.gs.flush();
                return true;
            }
            return false;
        });
        commands.put(Strings.tetrachannel, (sender, cmd, label, args) -> {
            String nickname = args[1];
            new Thread(() -> {
                URLConnection connection;
                try {
                    connection = new URL("https://ch.tetr.io/api/users/" + nickname.toLowerCase()).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                connection.setRequestProperty("User-Agent", Bukkit.getServer().toString());

                try {
                    connection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                int ch;
                try {
                    while ((ch = reader.read()) != -1) {
                        sb.append((char) ch);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                String jsonString = sb.toString();
                Gson gson = new Gson();
                Map<?, ?> map = gson.fromJson(jsonString, Map.class);
                Map<?, ?> data = (Map<?, ?>) map.get("data");
                Map<?, ?> user = (Map<?, ?>) data.get("user");
                Map<?, ?> league = (Map<?, ?>) user.get("league");
                sender.sendMessage("nickname: " + user.get("username"));
                sender.sendMessage("country: " + user.get("country"));
                sender.sendMessage("rank: " + league.get("rank") + ", " + league.get("rating") + "TR");
                sender.sendMessage("glicko: " + league.get("glicko") + "±" + league.get("rd"));
                sender.sendMessage(league.get("apm") + "APM " + league.get("pps") + "PPS " + league.get("vs") + "VS");
            }).start();
            return true;
        });

    }

    public static MainCommand getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        String command = args.length == 0 ? null : args[0].toLowerCase();
        boolean fail;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null && !Main.gs.isPlayerUsingThePlugin(player)) {
            Main.gs.initialize(player);
        }

        if (command == null) {
            fail = true;
        } else {
            if (commands.containsKey(command)) {
                fail = !commands.get(command).process(sender, cmd, label, args);
            } else {
                fail = true;
            }
        }

        if (fail) {
            if (player != null) {
                commands.get(Strings.gui).process(sender, cmd, label, args);
            } else {
                commands.get(Strings.help).process(sender, cmd, label, args);
            }
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

    private interface SubCommand {
        boolean process(CommandSender sender, Command cmd, String label, String[] args);
    }

}
