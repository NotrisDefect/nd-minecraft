package tetrminecraft.functions.dependencyutil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import tetrminecraft.Main;
import tetrminecraft.Room;

public class NoteBlockAPIYes implements NoteBlockAPI {

    private static Map<Room, RadioSongPlayer> rsps = new HashMap<Room, RadioSongPlayer>();

    public static Playlist playlist;

    private static int numberOfSongsAll;

    private static Song[] songArray;

    @Override
    public void newRSP(Room room) {
        RadioSongPlayer rsp = new RadioSongPlayer(playlist);
        rsps.put(room, rsp);
        rsp.setChannelMode(new MonoStereoMode());
        rsp.setVolume((byte) 50);
    }

    @Override
    public void addPlayer(Room room, Player player) {
        rsps.get(room).addPlayer(player);
    }

    @Override
    public void removePlayer(Room room, Player player) {
        rsps.get(room).removePlayer(player);
    }

    @Override
    public String getPlayingNow(Room room) {
        if (rsps.get(room).getSong().getPath() == null) {
            return "[TETR] Playing: Internal song (Can can)";
        } else {
            return "[TETR] Playing: " + rsps.get(room).getSong().getPath().getName().replaceAll(".nbs$", "");
        }
    }

    @Override
    public void setPlaying(Room room, boolean b) {
        rsps.get(room).setPlaying(b);
    }

    @Override
    public void playSong(Room room, int index) {
        rsps.get(room).playSong(index);
    }

    @Override
    public boolean isPlaying(Room room) {
        return rsps.get(room).isPlaying();
    }

    public static void loadSongs() {
        File file = new File(Main.instance.getDataFolder() + "/songs/");
        file.mkdirs();

        int numberOfSongsCustom = file.listFiles().length;
        int numberOfSongsClasspath = 1;
        numberOfSongsAll = numberOfSongsClasspath + numberOfSongsCustom;
        songArray = new Song[numberOfSongsAll];

        // classpath song 1
        String path = "tetrminecraft/functions/dependencyutil/internalmusic/cancan.nbs";
        songArray[0] = NBSDecoder.parse(NoteBlockAPIYes.class.getClassLoader().getResourceAsStream(path));

        String[] pathNames;
        String song;

        Main.instance.getLogger()
                .info(numberOfSongsClasspath + " song" + (numberOfSongsClasspath == 1 ? "" : "s") + " in classpath");
        Main.instance.getLogger()
                .info(numberOfSongsCustom + " song" + (numberOfSongsCustom == 1 ? "" : "s") + " in TETR/songs");

        pathNames = new String[numberOfSongsCustom];
        pathNames = file.list();
        for (int i = 0; i < numberOfSongsCustom; i++) {
            song = Main.instance.getDataFolder() + "/songs/" + pathNames[i];
            songArray[numberOfSongsClasspath + i] = NBSDecoder.parse(new File(song));
        }

        playlist = new Playlist(songArray);
    }

    @Override
    public void startPlaying(Room room, int index) {
        if (index == -1) {
            int random = (int) (Math.random() * numberOfSongsAll);
            playSong(room, random);
        } else {
            playSong(room, index);
        }
        setRepeatMode(room, RepeatMode.ONE);
        if (isPlaying(room) == false) {
            setPlaying(room, true);
        }
    }

    private void setRepeatMode(Room room, RepeatMode rm) {
        rsps.get(room).setRepeatMode(rm);
    }

}
