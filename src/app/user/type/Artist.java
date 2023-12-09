package app.user.type;

import app.Admin;
import app.ArtistStuff.Event;
import app.ArtistStuff.Merch;
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
    @Getter
    private final ArrayList<Event> events;
    @Getter
    private final ArrayList<Merch> merchandises;

    public Artist(final String username, final int age, final String city, final String userType) {
        super(username, age, city, userType);
        type = userType;
        this.username = username;
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchandises = new ArrayList<>();
    }

    /**
     * Checks if the username matches a specified name filter, ignoring case.
     *
     * @param nameFilter The name filter to match against the username.
     */
    public boolean matchesName(final String nameFilter) {
        return username.toLowerCase().startsWith(nameFilter.toLowerCase());
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
     * Adds a new event for the user.
     *
     * @param command The CommandInput with event details.
     */
    public String addEvent(final CommandInput command) {
        if (verifyEventName(command)) {
            return username + " has another event with the same name.";
        } else if (verifyEventDate(command)) {
            return "Event for " + username + " does not have a valid date.";
        } else {
            events.add(new Event(command.getName(), command.getDate(), command.getDescription()));
            return username + " has added new event successfully.";
        }
    }

    /**
     * Adds new merchandise for the user.
     *
     * @param command The CommandInput with merchandise details.
     */
    public String addMerch(final CommandInput command) {
        if (verifyMerchName(command)) {
            return username + " has merchandise with the same name.";
        } else if (command.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        } else {
            merchandises.add(new Merch(command.getName(), command.getDescription(),
                    command.getPrice()));
            return username + " has added new merchandise successfully.";
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
     * Verifies if the artist has another event with the same name.
     *
     * @param command The CommandInput containing event details and artist information.
     */
    public boolean verifyEventName(final CommandInput command) {
        for (Event event : events) {
            if (Objects.equals(event.getName(), command.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the artist has another merchandise with the same name.
     *
     * @param command The CommandInput containing merchandise details and artist information.
     */
    public boolean verifyMerchName(final CommandInput command) {
        for (Merch merch : merchandises) {
            if (Objects.equals(merch.getName(), command.getName())) {
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

    /**
     * Verifies if the event has a valid date.
     *
     * @param command The CommandInput containing event details and artist information.
     */
    public boolean verifyEventDate(final CommandInput command) {
        return false;
    }
}