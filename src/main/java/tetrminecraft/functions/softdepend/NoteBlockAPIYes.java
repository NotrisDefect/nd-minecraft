package tetrminecraft.functions.softdepend;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.entity.Player;
import tetrminecraft.Main;
import tetrminecraft.Room;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NoteBlockAPIYes implements NoteBlockAPI {

    public static final String[] classpathSongs = {"cancan", "newgrass", "c01"};
    public static Playlist playlist;
    private final Map<Room, RadioSongPlayer> rsps = new HashMap<>();
    private int numberOfSongs;

    @Override
    public void addPlayer(Room room, Player player) {
        rsps.get(room).addPlayer(player);
    }

    @Override
    public boolean isPlaying(Room room) {
        return rsps.get(room).isPlaying();
    }

    @Override
    public void loadSongs() {
        File file = new File(Main.instance.getDataFolder() + "/songs/");
        file.mkdirs();
        numberOfSongs = file.listFiles().length;
        Song[] songArray;
        if (numberOfSongs > 0) {
            Main.instance.getLogger()
                .info(numberOfSongs + " song" + (numberOfSongs == 1 ? "" : "s") + " in TETR/songs");
            String[] pathNames;
            String song;
            songArray = new Song[numberOfSongs];
            pathNames = file.list();
            for (int i = 0; i < numberOfSongs; i++) {
                song = Main.instance.getDataFolder() + "/songs/" + pathNames[i];
                songArray[i] = NBSDecoder.parse(new File(song));
            }
        } else {
            Main.instance.getLogger().info("0 songs in TETR/songs");
            // classpath
            String path;
            songArray = new Song[3];
            path = "tetrminecraft/functions/softdepend/internalmusic/cancan.nbs";
            if (NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path)) == null) {
                System.out.println("Server was reloaded and an error happened");
                songArray = new Song[0];
            } else {
                songArray[0] = NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path));

                path = "tetrminecraft/functions/softdepend/internalmusic/newgrass.nbs";
                if (NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path)) == null) {
                    System.out.println("Server was reloaded and an error happened");
                    songArray = new Song[0];
                } else {
                    songArray[1] = NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path));

                    path = "tetrminecraft/functions/softdepend/internalmusic/c01.nbs";
                    if (NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path)) == null) {
                        System.out.println("Server was reloaded and an error happened");
                        songArray = new Song[0];
                    } else {
                        songArray[2] = NBSDecoder
                            .parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path));
                    }
                }
            }
            numberOfSongs = 3;
        }
        playlist = new Playlist(songArray);
    }

    @Override
    public void newRSP(Room room) {
        RadioSongPlayer rsp = new RadioSongPlayer(playlist);
        rsps.put(room, rsp);
        rsp.setChannelMode(new MonoStereoMode());
        rsp.setVolume((byte) 50);
    }

    @Override
    public void playSong(Room room, int index) {
        rsps.get(room).playSong(index);
    }

    @Override
    public void removePlayer(Room room, Player player) {
        rsps.get(room).removePlayer(player);
    }

    @Override
    public void setPlaying(Room room, boolean b) {
        rsps.get(room).setPlaying(b);
    }

    @Override
    public void startPlaying(Room room, int index) {
        int random;
        if (index == -1) {
            random = (int) (Math.random() * numberOfSongs);
            playSong(room, random);
        } else {
            random = index;
            playSong(room, index);
        }
        setRepeatMode(room, RepeatMode.ONE);
        if (!isPlaying(room)) {
            setPlaying(room, true);
        }

        for (Player player : room.playerList) {
            if (rsps.get(room).getSong().getPath() == null) {
                player.sendMessage("[TETR] Playing: " + classpathSongs[random] + " - input your own in plugins/TETR/songs");
            } else {
                player.sendMessage(
                    "[TETR] Playing: " + rsps.get(room).getSong().getPath().getName().replaceAll(".nbs$", ""));
            }
        }
    }

    private void setRepeatMode(Room room, RepeatMode rm) {
        rsps.get(room).setRepeatMode(rm);
    }

}
