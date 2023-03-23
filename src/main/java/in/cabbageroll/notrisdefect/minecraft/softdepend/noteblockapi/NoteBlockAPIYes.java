package in.cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi;

import in.cabbageroll.notrisdefect.minecraft.Main;
import in.cabbageroll.notrisdefect.minecraft.Room;
import in.cabbageroll.notrisdefect.minecraft.Strings;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NoteBlockAPIYes implements NoteBlockAPI {

    public static final String[] classpathSongs = {
        "flourish",
        "MUSIC_01",
        "newgrass",
        "PASSPORT",
        "SWEST_01",
        "Tetris A Theme",
        "Tetris B Theme",
        "town"
    };
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
        File file = new File(Main.PLUGIN.getDataFolder() + "/songs/");
        Song[] songArray;

        file.mkdirs();
        numberOfSongs = file.listFiles().length;

        if (numberOfSongs > 0) {
            String[] pathNames = file.list();
            songArray = new Song[numberOfSongs];

            Main.PLUGIN.getLogger().info(numberOfSongs + " song" + (numberOfSongs == 1 ? "" : "s") + " in " + Strings.pluginName + "/songs");

            for (int i = 0; i < numberOfSongs; i++) {
                String path = Main.PLUGIN.getDataFolder() + "/songs/" + pathNames[i];
                songArray[i] = NBSDecoder.parse(new File(path));
            }
        } else {
            numberOfSongs = classpathSongs.length;
            songArray = new Song[numberOfSongs];

            Main.PLUGIN.getLogger().info("0 songs in " + Strings.pluginName + "/songs, loading from jar instead");

            for (int i = 0; i < numberOfSongs; i++) {
                loadNext(songArray, i);
            }
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
        if (index == -2) {
            return;
        } else if (index == -1) {
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

        for (Player player : room.players) {
            if (rsps.get(room).getSong().getPath() == null) {
                player.sendMessage(Strings.nowPlaying(classpathSongs[random]));
            } else {
                player.sendMessage(Strings.nowPlaying(rsps.get(room).getSong().getPath().getName().replaceAll(".nbs$", "")));
            }
        }
    }

    private void loadNext(Song[] sa, int n) {
        String song = classpathSongs[n];
        InputStream is = getClass().getClassLoader().getResourceAsStream("music/" + song + ".nbs");

        if (is == null) {
            Main.PLUGIN.getLogger().warning("error loading ");
        } else {
            sa[n] = NBSDecoder.parse(is);
        }
    }

    private void setRepeatMode(Room room, RepeatMode rm) {
        rsps.get(room).setRepeatMode(rm);
    }

}
