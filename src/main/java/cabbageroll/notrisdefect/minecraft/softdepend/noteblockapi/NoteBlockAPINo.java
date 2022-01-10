package cabbageroll.notrisdefect.minecraft.softdepend.noteblockapi;

import cabbageroll.notrisdefect.minecraft.Room;
import org.bukkit.entity.Player;

public class NoteBlockAPINo implements NoteBlockAPI {

    @Override
    public void addPlayer(Room room, Player player) {
    }

    @Override
    public boolean isPlaying(Room room) {
        return false;
    }

    @Override
    public void loadSongs() {
    }

    @Override
    public void newRSP(Room room) {
    }

    @Override
    public void playSong(Room room, int index) {
    }

    @Override
    public void removePlayer(Room room, Player player) {
    }

    @Override
    public void setPlaying(Room room, boolean b) {
    }

    @Override
    public void startPlaying(Room room, int index) {
    }

}
