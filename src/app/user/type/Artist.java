package app.user.type;

import app.Admin;
import app.audio.Collections.Album;
import app.user.User;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Artist extends User {
    private final String username;
    private final String type;
    @Getter
    private final ArrayList<Album> albums;
    public Artist(final String username, final int age, final String city, final String userType) {
        super(username, age, city, userType);
        albums = new ArrayList<>();
        type = userType;
        this.username = username;
    }

    /**
     * Adds a new album for the artist based on the provided command input.
     *
     * @param command The CommandInput containing album details and artist information.
     */
    public String addAlbum(final CommandInput command) {
        Album album = new Album(command.getName(), command.getUsername(),
                command.getReleaseYear(), command.getDescription(), command.getSongs());
        if (!Objects.equals(type, "artist")) {
            return username + " is not an artist.";
        } else if (verifyAlbumName(command)) {
            return username + " has another album with the same name.";
        } else if (verifySongs(album)) {
            return username + " has the same song at least twice in this album.";
        } else {
            albums.add(album);
            Admin.addSongs(command.getSongs());
            return username + " has added new album successfully.";
        }
    }

    /**
     * Verifies if the artist has another album with the same name.
     *
     * @param command The CommandInput containing album details and artist information.
     */
    public boolean verifyAlbumName(final CommandInput command) {
        for (Album album : albums) {
            if (Objects.equals(album.getName(), command.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the album contains the same song at least twice.
     *
     * @param album The Album object containing song details.
     */
    public boolean verifySongs(final Album album) {
        Map<String, Integer> elementCount = new HashMap<>();
        for (SongInput song : album.getSongs()) {
            elementCount.put(song.getName(), elementCount.getOrDefault(song.getName(), 0) + 1);

            if (elementCount.get(song.getName()) > 1) {
                return true;
            }
        }
        return false;
    }
}
