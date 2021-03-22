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
    
    private static boolean hasSongs;
    private static int numberOfSongs2;
    
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
        return "[TETR] Playing: " + rsps.get(room).getSong().getPath().getName().replaceAll(".nbs$", "");
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
        File file = new File(Main.plugin.getDataFolder() + "/songs/");
        file.mkdirs();
        int numberOfSongs = file.listFiles().length;
        if (numberOfSongs > 0) {
            hasSongs = true;
            numberOfSongs2 = numberOfSongs;
            String[] pathNames;
            String song;

            Main.plugin.getLogger().info(numberOfSongs + " song(s) found");

            pathNames = new String[numberOfSongs];
            songArray = new Song[numberOfSongs];
            pathNames = file.list();
            for (int i = 0; i < numberOfSongs; i++) {
                song = Main.plugin.getDataFolder() + "/songs/" + pathNames[i];
                songArray[i] = NBSDecoder.parse(new File(song));
            }

            playlist = new Playlist(songArray);
            // tRASH end
        } else {
            Main.plugin.getLogger().info("No songs detected. Please add some songs!");
        }
    }

    @Override
    public void startPlaying(Room room, int index) {
        if (index == -1) {
            int random = (int) (Math.random() * numberOfSongs2);
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
