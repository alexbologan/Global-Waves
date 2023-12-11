package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.type.Host;
import app.user.type.User;
import app.user.type.Artist;
import app.utils.Enums;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    private static List<Artist> artists = new ArrayList<>();
    private static List<Host> hosts = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;
    private static Admin instance = null;

    private Admin() {
    }

    /**
     * Retrieves the singleton instance of the Admin class.
     *
     * @return The singleton instance of the Admin class.
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Resets the list of artists, clearing all existing entries.
     */
    public static void resetArtists() {
        artists.clear();
    }

    /**
     * Resets the list of albums, clearing all existing entries.
     */
    public static void resetAlbums() {
        albums.clear();
    }

    /**
     * Resets the list of hosts, clearing all existing entries.
     */
    public static void resetHosts() {
        hosts.clear();
    }
    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(),
                    userInput.getCity(), userInput.getType()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Adds a list of songs to the existing collection.
     *
     * @param songInputList The list of SongInput objects containing song details.
     *                      Each SongInput object corresponds to a song to be added.
     */
    public static void addSongs(final List<SongInput> songInputList) {
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Finds a song based on its name from the collection of albums.
     *
     * @param name The name of the song to find.
     * @return The Song object if found, or null if not found.
     */
    public static Song findSong(final String name) {
        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                if (song.getName().equals(name)) {
                    return song;
                }
            }
        }
        return null;
    }

    /**
     * Removes a song from the collection based on its name.
     *
     * @param name The name of the song to be removed.
     */
    public static void removeSong(final String name) {
        for (Song song : songs) {
            if (song.getName().equals(name)) {
                songs.remove(song);
                break;
            }
        }
    }

    /**
     * Verifies if an album owned by a user is currently being used by any player.
     *
     * @param owner The owner of the album to check.
     * @return True if the album is in use, false otherwise.
     */
    public static boolean verifyIfAlbumIsUsed(final String owner) {
        for (User user : Admin.users) {
            if (!user.getPlayerStats().isPaused() && user.getPlayer().getSource()
                    .getAudioCollection().getOwner().equals(owner)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }


    /**
     * Adds a podcast to the collection.
     *
     * @param podcast The podcast to be added.
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Adds an album to the collection.
     *
     * @param album The album to be added.
     */
    public static void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    /**
     * Gets artists.
     *
     * @return the artists
     */
    public static List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }

    /**
     * Gets hosts.
     *
     * @return the hosts
     */
    public static List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }
    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : Admin.users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : Admin.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Deletes a user from the collection, considering the user type.
     *
     * @param user The user to be deleted.
     * @return A message indicating the success of the operation.
     */
    public static String deleteUser(final User user) {
        if (Objects.equals(user.getUserType(), "artist")) {
            return deleteArtist(user.getUsername());
        }
        //users.remove(user);
        return user.getUsername() + " was successfully deleted.";
    }

    /**
     * Deletes an artist from the collection.
     *
     * @param username The username of the artist to be deleted.
     * @return A message indicating the success of the operation.
     */
    public static String deleteArtist(final String username) {
        Artist artist = (Artist) getUser(username);
        for (User user1 : users) {
            if (!user1.getPlayerStats().isPaused()) {
                if (user1.getPlayer().getSource().getAudioCollection() != null) {
                    System.out.println(user1.getPlayer().getPaused());
                    if (user1.getPlayer().getSource().getAudioCollection().getOwner()
                            .equals(username)) {
                        return username + " can't be deleted.";
                    }
                } else {
                    Song song = (Song) user1.getPlayer().getSource().getAudioFile();
                    if (song.getArtist().equals(username)) {
                        return username + " can't be deleted.";
                    }
                }
            }
        }
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                for (User user : users) {
                    user.getLikedSongs().removeIf(song1 -> song1.getName().equals(song.getName()));
                }
                removeSong(song.getName());
            }
            albums.remove(album);
        }
        artists.remove(artist);
        users.remove(artist);
        return username + " was successfully deleted.";
    }

    /**
     * Retrieves an artist based on the provided username.
     *
     * @param username The username of the artist to be retrieved.
     */
    public static Artist getArtist(final String username) {
        for (Artist artist : Admin.artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Retrieves a host based on the provided username.
     *
     * @param username The username of the host to be retrieved.
     */
    public static Host getHost(final String username) {
        for (Host host : Admin.hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }
    /**
     * Adds a new user or artist based on the provided command input.
     *
     * @param command The CommandInput containing user details and type information.
     */
    public static String addUser(final CommandInput command) {
        String message;
        if (getUser(command.getUsername()) != null) {
            message = "The username " + command.getUsername() + " is already taken.";
        } else {
            switch (command.getType()) {
                case "artist" -> {
                    boolean added = false;
                    for (User user : users) {
                        if (user.getUserType().equals("host")) {
                            users.add(new Artist(command.getUsername(), command.getAge(),
                                    command.getCity(), command.getType()));
                            artists.add((Artist) getUser(command.getUsername()));
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        users.add(new Artist(command.getUsername(), command.getAge(),
                                command.getCity(), command.getType()));
                        artists.add((Artist) getUser(command.getUsername()));
                    }
                }
                case "user" -> {
                    boolean added = false;
                    for (User user : users) {
                        if (user.getUserType().equals("artist")) {
                            users.add(users.indexOf(user), new User(command.getUsername(),
                                    command.getAge(), command.getCity(), command.getType()));
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        users.add(new User(command.getUsername(), command.getAge(),
                                command.getCity(), command.getType()));
                    }
                }
                case "host" -> {
                    users.add(new Host(command.getUsername(), command.getAge(),
                            command.getCity(), command.getType()));
                    hosts.add((Host) getUser(command.getUsername()));
                }
                default -> {
                    message = "Invalid user type.";
                }
            }
            message = "The username " + command.getUsername() + " has been added successfully.";
        }
        return message;
    }


    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : Admin.users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Retrieves a list of usernames for users who are currently online.
     *
     * @return A list of usernames representing online users.
     */
    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : Admin.users) {
            if (user.getConnectionStatus() == Enums.ConnectionStatus.ONLINE) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Retrieves a list of usernames for all users.
     *
     * @return A list of usernames representing all users.
     */
    public static List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : Admin.users) {
            allUsers.add(user.getUsername());
        }
        return allUsers;
    }
    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
