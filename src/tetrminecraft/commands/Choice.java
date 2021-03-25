package tetrminecraft.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import tetrminecraft.Main;
import tetrminecraft.menus.HomeMenu;
import tetrminecraft.menus.JoinRoomMenu;
import tetrminecraft.menus.MultiplayerMenu;
import tetrminecraft.menus.RoomMenu;
import tetrminecraft.menus.SettingsMenu;
import tetrminecraft.menus.SimpleSettingsMenu;
import tetrminecraft.menus.SkinMenu;
import tetrminecraft.menus.SongMenu;

/*
 * can also be forced
 */
public class Choice {

    public static void maximizeMenu(Player player) {
        switch (Main.lastMenuOpened.get(player)) {
        case "home":
            new HomeMenu(player);
            break;
        case "multiplayer":
            new MultiplayerMenu(player);
            break;
        case "joinroom":
            new JoinRoomMenu(player, Main.joinRoomPage.get(player));
            break;
        case "room":
            new RoomMenu(player);
            break;
        case "skin":
            new SkinMenu(player);
            break;
        case "settings":
            new SettingsMenu(player);
            break;
        case "simsettings":
            new SimpleSettingsMenu(player);
            break;
        case "song":
            new SongMenu(player);
            break;
        }
    }

    public static void showControls(CommandSender sender) {
        TranslatableComponent[] controls = { new TranslatableComponent("key.hotbar.1"),
                new TranslatableComponent("key.hotbar.2"), new TranslatableComponent("key.hotbar.3"),
                new TranslatableComponent("key.hotbar.4"), new TranslatableComponent("key.hotbar.5"),
                new TranslatableComponent("key.hotbar.6"), new TranslatableComponent("key.hotbar.7"),
                new TranslatableComponent("key.hotbar.8"), new TranslatableComponent("key.sneak") };

        String[] descriptions = { new String("Move left: "), new String("\nMove right: "), new String("\nSoft drop: "),
                new String("\nHard drop: "), new String("\nRotate counterclockwise: "),
                new String("\nRotate clockwise: "), new String("\nRotate 180: "), new String("\nHold: "),
                new String("\nZone: "),

        };

        // control.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new
        // ComponentBuilder("asd").create()));

        TextComponent message = new TextComponent();

        for (int i = 0; i < controls.length; i++) {
            message.addExtra(descriptions[i]);
            message.addExtra(controls[i]);
        }

        sender.spigot().sendMessage(message);

    }

    public static void tetrioStats(CommandSender sender, String nickname) {
        new Thread() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL("https://ch.tetr.io/api/users/" + nickname.toLowerCase()).openConnection();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                connection.setRequestProperty("User-Agent", "TETR, executed by " + sender.getName());

                try {
                    connection.connect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
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
            }
        }.start();
    }
    
    public static void help(CommandSender sender) {
        TextComponent message = new TextComponent();
        message.addExtra(ChatColor.GOLD + "" + ChatColor.BOLD + "Help");
        if (sender.hasPermission("tetr.reload")) {
            message.addExtra("\n" + ChatColor.GOLD + "" + ChatColor.BOLD
                    + "/tetr disable - disable the plugin (you cannot re-enable it)");
        }

        if (sender instanceof Player) {
            message.addExtra("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "/tetr - open game window");
        }

        message.addExtra("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "/tetr help - shows this help page");
        message.addExtra("\n" + ChatColor.GOLD + "" + ChatColor.BOLD
                + "/tetr controls - shows guide on how to set the controls");
        message.addExtra("\n" + ChatColor.GOLD + "" + ChatColor.BOLD
                + "/tetr tetrachannel <nickname> - get stats of a player from ch.tetr.io");
        sender.spigot().sendMessage(message);
    }
    
    public static void disablePlugin() {
        Bukkit.getServer().getPluginManager().disablePlugin(Main.plugin);
    }
}
