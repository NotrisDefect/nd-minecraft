package tetrminecraft.functions.dependencyutil;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import tetrminecraft.Main;
import tetrminecraft.Room;

public class NoteBlockAPIYes implements NoteBlockAPI {
    
    private static Map<Room, RadioSongPlayer> rsps = new HashMap<Room, RadioSongPlayer>();

    @Override
    public void newRSP(Room room) {
        RadioSongPlayer rsp = new RadioSongPlayer(Main.playlist);
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

    @Override
    public void setRepeatMode(Room room, RepeatMode x) {
        rsps.get(room).setRepeatMode(x);
    }
    
}
