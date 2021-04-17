package tetrminecraft.functions.dependencyutil;

import org.bukkit.entity.Player;


import tetrminecraft.Room;

public interface NoteBlockAPI {
    void newRSP(Room room);
    
    void addPlayer(Room room, Player player);
    
    void removePlayer(Room room, Player player);
    
    void setPlaying(Room room, boolean b);
    
    void playSong(Room room, int index);
    
    boolean isPlaying(Room room);
    
    void startPlaying(Room room, int index);
    
    void loadSongs();
}
