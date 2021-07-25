package cabbageroll.notrisdefect.commands;

import cabbageroll.notrisdefect.Main;
import cabbageroll.notrisdefect.Strings;
import cabbageroll.notrisdefect.initialization.Sounds;
import com.google.gson.Gson;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Choice {

    private static final ChatColor color = ChatColor.BLUE;

    public static void bench(int people, int rooms, int duration) {

    }

    public static void disablePlugin() {
        Bukkit.getServer().getPluginManager().disablePlugin(Main.plugin);
    }

    public static void help(CommandSender sender) {
        StringBuilder sb = new StringBuilder(color + Strings.pluginName + " help");
        sb.append("\n" + color + "Aliases: notris, nd");
        //sb.append("\n" + color + "For additional help do /" + Strings.mainCommand + " " + Strings.help + " [subcommand]");

        if (sender instanceof Player) {
            sb.append("\n" + color + "/" + Strings.mainCommand + " - open game window");
        }

        if (sender.hasPermission("notrisdefect.use.experimental")) {
            sb.append("\n" + color + "/" + Strings.mainCommand + " " + Strings.restart + " - flush all current session data");
        }

        sb.append("\n" + color + "/" + Strings.mainCommand + " " + Strings.help + " - shows this help page");

        sb.append("\n" + color + "/" + Strings.mainCommand + " " + Strings.controls + " - shows guide on how to set the controls");

        sb.append("\n" + color + "/" + Strings.mainCommand + " " + Strings.tetrachannel + " <nickname> - get stats of a player from ch.tetr.io");

        sender.sendMessage(sb.toString());
    }

    public static void maximizeMenu(Player player) {
        Main.gs.openLastMenu(player);
    }

    public static void showControls(CommandSender sender) {
        TranslatableComponent[] controls = {
            new TranslatableComponent("key.hotbar.1"),
            new TranslatableComponent("key.hotbar.2"),
            new TranslatableComponent("key.hotbar.3"),
            new TranslatableComponent("key.hotbar.4"),
            new TranslatableComponent("key.hotbar.5"),
            new TranslatableComponent("key.hotbar.6"),
            new TranslatableComponent("key.hotbar.7"),
            new TranslatableComponent("key.hotbar.8"),
            new TranslatableComponent("key.sneak")
        };

        String[] descriptions = {
            "Move left: ",
            "\nMove right: ",
            "\nSoft drop: ",
            "\nHard drop: ",
            "\nRotate counterclockwise: ",
            "\nRotate clockwise: ",
            "\nRotate 180: ",
            "\nHold: ",
            "\nZone: ",
        };

        TextComponent message = new TextComponent();

        for (int i = 0; i < controls.length; i++) {
            message.addExtra(descriptions[i]);
            message.addExtra(controls[i]);
        }

        sender.spigot().sendMessage(message);

    }

    public static void showSfx(CommandSender sender) {
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

        TextComponent message = new TextComponent();

        for (int i = 0; i < descriptions.length; i++) {
            message.addExtra(descriptions[i]);
            message.addExtra(sounds[i]);
        }

        sender.spigot().sendMessage(message);
    }

    //bad code
    public static void tetrioStats(CommandSender sender, String nickname) {
        new Thread(() -> {
            URLConnection connection = null;
            try {
                connection = new URL("https://ch.tetr.io/api/users/" + nickname.toLowerCase()).openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            connection.setRequestProperty("User-Agent", Bukkit.getServer().toString());

            try {
                connection.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            int idk;
            try {
                while ((idk = reader.read()) != -1) {
                    sb.append((char) idk);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
    }
}
