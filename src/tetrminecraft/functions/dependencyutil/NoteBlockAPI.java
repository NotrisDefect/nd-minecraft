package tetrminecraft.functions.dependencyutil;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;

import tetrminecraft.Room;

public interface NoteBlockAPI {
    public void newRSP(Room room);
    
    public void addPlayer(Room room, Player player);
    
    public void removePlayer(Room room, Player player);
    
    public String getPlayingNow(Room room);
    
    public void setPlaying(Room room, boolean b);
    
    public void playSong(Room room, int index);
    
    public boolean isPlaying(Room room);
    
    public void setRepeatMode(Room room, RepeatMode x);
}
