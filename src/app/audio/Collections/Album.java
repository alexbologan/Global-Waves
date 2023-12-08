package app.audio.Collections;

import app.audio.Files.AudioFile;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<SongInput> songs;
    private final Integer releaseYear;
    private final String description;

    public Album(final String name, final String owner, final Integer releaseYear,
                 final String description, final ArrayList<SongInput> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return 0;
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return null;
    }


}
