package cabbageroll.notrisdefect.functions.softdepend;

import cabbageroll.notrisdefect.Room;
import org.bukkit.entity.Player;

public interface NoteBlockAPI {
    void addPlayer(Room room, Player player);

    boolean isPlaying(Room room);

    void loadSongs();

    void newRSP(Room room);

    void playSong(Room room, int index);

    void removePlayer(Room room, Player player);

    void setPlaying(Room room, boolean b);

    void startPlaying(Room room, int index);
}
