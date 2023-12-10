package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs = new ArrayList<>();
    private final Integer releaseYear;
    private final String description;

    public Album(final String name, final String owner, final Integer releaseYear,
                 final String description, final ArrayList<SongInput> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        for (SongInput song : songs) {
            this.songs.add(new Song(song.getName(), song.getDuration(), song.getAlbum(),
                    song.getTags(), song.getLyrics(), song.getGenre(), song.getReleaseYear(),
                    song.getArtist()));
        }
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }


}
