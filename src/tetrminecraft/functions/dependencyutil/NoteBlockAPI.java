package tetrminecraft.functions.dependencyutil;

import org.bukkit.entity.Player;


import tetrminecraft.Room;

public interface NoteBlockAPI {
    public void newRSP(Room room);
    
    public void addPlayer(Room room, Player player);
    
    public void removePlayer(Room room, Player player);
    
    public void setPlaying(Room room, boolean b);
    
    public void playSong(Room room, int index);
    
    public boolean isPlaying(Room room);
    
    public void startPlaying(Room room, int index);
    
    public void loadSongs();
}
