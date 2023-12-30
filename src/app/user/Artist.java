package app.user;

import java.util.ArrayList;
import java.util.List;

import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Files.Song;
import app.pages.ArtistPage;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Artist.
 */
@Getter
public final class Artist extends ContentCreator {
    private ArrayList<Album> albums;
    private ArrayList<Merchandise> merch;
    private ArrayList<Event> events;
    @Setter
    private ArrayList<Pair<String, Integer>> topSongs;
    @Setter
    private ArrayList<Pair<String, Integer>> topAlbums;
    @Setter
    private ArrayList<Pair<String, Integer>> topFans;
    private ArrayList<Pair<String, Integer>> listeners;
    private double merchRevenue;
    private ArrayList<Pair<String, Double>> songRevenue;
    private ArrayList<Subscriber> subscribers;

    /**
     * Instantiates a new Artist.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merch = new ArrayList<>();
        events = new ArrayList<>();
        topSongs = new ArrayList<>();
        topAlbums = new ArrayList<>();
        topFans = new ArrayList<>();
        listeners = new ArrayList<>();
        songRevenue = new ArrayList<>();
        subscribers = new ArrayList<>();
        merchRevenue = 0;

        super.setPage(new ArtistPage(this));
    }

    public void setNotifications(final String notification) {

    }

    /**
     * Gets event.
     *
     * @param eventName the event name
     * @return the event
     */
    public Event getEvent(final String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                return event;
            }
        }

        return null;
    }

    /**
     * Gets album.
     *
     * @param albumName the album name
     * @return the album
     */
    public Album getAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }

        return null;
    }

    /**
     * Gets all songs.
     *
     * @return the all songs
     */
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        albums.forEach(album -> songs.addAll(album.getSongs()));

        return songs;
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutput = new ArrayList<>();
        for (Album album : albums) {
            albumOutput.add(new AlbumOutput(album));
        }

        return albumOutput;
    }

    /**
     * Check if the artist has a song
     *
     * @param songName the song name
     * @return true if the artist has the song
     */
    public boolean hasSong(final String songName) {
        for (Pair<String, Integer> pair : topSongs) {
            if (pair.getFirst().equals(songName)) {
                return true;
            }
        }
        return false;
    }

    public void addSongRevenue(final String songName, final double revenue) {
        boolean found = false;

        for (Pair<String, Double> pair : songRevenue) {
            if (pair.getFirst().equals(songName)) {
                pair.setSecond(pair.getSecond() + revenue);
                found = true;
                break;
            }
        }

        if (!found) {
            songRevenue.add(new Pair<>(songName, revenue));
        }
    }

    /**
     * Notifies all subscribers about a specific message from the artist.
     *
     * @param message The message to be sent to subscribers.
     */
    public void notifySubscribers(final String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(message, getUsername());
        }
    }

    /**
     * Get the revenue from the songs
     *
     * @return the revenue
     */
    public double getSongsRevenue() {
        double revenue = 0;
        for (Pair<String, Double> pair : songRevenue) {
            revenue += pair.getSecond();
        }
        return revenue;
    }
    /**
     * Get user type
     *
     * @return user type string
     */
    public String userType() {
        return "artist";
    }
}
