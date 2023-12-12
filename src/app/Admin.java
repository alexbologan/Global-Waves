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
@Getter
public final class Admin {
    private List<User> users = new ArrayList<>();
    private final List<Artist> artists = new ArrayList<>();
    private final List<Host> hosts = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private final List<Album> albums = new ArrayList<>();
    private int timestamp = 0;
    private final int limit = 5;
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
     * Sets the timestamp to the provided value.
     *
     * @param timestamp The new value for the timestamp.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Resets the list of artists, clearing all existing entries.
     */
    public void resetArtists() {
        artists.clear();
    }

    /**
     * Resets the list of albums, clearing all existing entries.
     */
    public void resetAlbums() {
        albums.clear();
    }

    /**
     * Resets the list of hosts, clearing all existing entries.
     */
    public void resetHosts() {
        hosts.clear();
    }

    /**
     * Removes a playlist from the collection.
     *
     * @param name The name of the playlist to be removed.
     */
    public void removePlaylist(final String name) {
        for (User user : users) {
            user.getFollowedPlaylists().removeIf(playlist -> playlist.getName().equals(name));
        }
        getPlaylists().removeIf(playlist -> playlist.getName().equals(name));
    }

    /**
     * Removes a podcast from the collection.
     */
    public void removePodcast(final String name) {
        podcasts.removeIf(podcast -> podcast.getName().equals(name));
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
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
    public void setSongs(final List<SongInput> songInputList) {
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
     * @param songList The list of SongInput objects containing song details.
     *                      Each SongInput object corresponds to a song to be added.
     */
    public void addSongs(final List<Song> songList) {
        songs.addAll(songList);
    }

    /**
     * Finds a song based on its name from the collection of albums.
     *
     * @param name The name of the song to find.
     * @return The Song object if found, or null if not found.
     */
    public Song findSong(final String name) {
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
    public void removeSong(final String name) {
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
    public boolean verifyIfAudioCollectionIsUsed(final String owner, final String type) {
        for (User user : users) {
            if (!user.getPlayerStats().isPaused() && Objects.equals(type, "album")) {
                Song song = (Song) user.getPlayer().getSource().getAudioFile();
                if (song.getArtist().equals(owner)) {
                    return true;
                }
            }

            if (!user.getPlayerStats().isPaused() && user.getPlayer().getSource()
                    .getAudioCollection().getOwner().equals(owner)) {
                return true;
            }

            for (Playlist playlist : user.getPlaylists()) {
                for (Song song : playlist.getSongs()) {
                    if (song.getArtist().equals(owner)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
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
    public void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Adds an album to the collection.
     *
     * @param album The album to be added.
     */
    public void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    /**
     * Gets artists.
     *
     * @return the artists
     */
    public List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }

    /**
     * Gets hosts.
     *
     * @return the hosts
     */
    public List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }
    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
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
    public User getUser(final String username) {
        for (User user : users) {
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
    public String deleteUser(final User user) {
        if (Objects.equals(user.getUserType(), "artist")) {
            return deleteArtist(user.getUsername());
        } else if (Objects.equals(user.getUserType(), "host")) {
            return deleteHost(user.getUsername());
        } else {
            return deleteSimpleUser(user.getUsername());

        }
    }

    /**
     * Deletes an artist from the collection.
     *
     * @param username The username of the artist to be deleted.
     * @return A message indicating the success of the operation.
     */
    public String deleteArtist(final String username) {
        if (checkIfUserCanBeDeleted(username)) {
            return username + " can't be deleted.";
        }

        Artist artist = (Artist) getUser(username);
        for (User user : users) {
            for (Album album : artist.getAlbums()) {
                for (Song song : album.getSongs()) {
                    user.getLikedSongs().removeIf(song1 -> song1.getName().equals(song.getName()));
                    removeSong(song.getName());
                }
                albums.remove(album);
            }
        }
        artists.remove(artist);
        users.remove(artist);
        return username + " was successfully deleted.";
    }

    /**
     * Deletes a simple user from the collection.
     *
     * @param username The username of the user to be deleted.
     * @return A message indicating the success of the operation.
     */
    public String deleteSimpleUser(final String username) {
        if (checkIfUserCanBeDeleted(username)) {
            return username + " can't be deleted.";
        }

        User user = getUser(username);
        for (User user1 : users) {
            for (Playlist playlist : user.getPlaylists()) {
                for (Playlist playlist1 : user1.getFollowedPlaylists()) {
                    if (playlist1.getName().equals(playlist.getName())) {
                        playlist1.decreaseFollowers();
                    }
                }
                user1.getFollowedPlaylists().removeIf(playlist1 -> playlist1.getName()
                        .equals(playlist.getName()));
            }
        }
        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlist.decreaseFollowers();
        }
        users.remove(user);
        return username + " was successfully deleted.";
    }

    /**
     * Deletes a host from the collection.
     *
     * @param username The username of the host to be deleted.
     * @return A message indicating the success of the operation.
     */
    public String deleteHost(final String username) {
        if (checkIfUserCanBeDeleted(username)) {
            return username + " can't be deleted.";
        }

        Host host = (Host) getUser(username);
        for (Podcast podcast : host.getPodcasts()) {
            removePodcast(podcast.getName());
        }
        hosts.remove(host);
        users.remove(host);
        return username + " was successfully deleted.";
    }

    /**
     * Checks if a user can be deleted from the collection.
     *
     * @param username The username of the user to be checked.
     * @return True if the user can't be deleted, false otherwise.
     */
    public boolean checkIfUserCanBeDeleted(final String username) {
        for (User user : users) {
            if (!user.getPlayerStats().isPaused()) {
                if (user.getPlayer().getSource().getAudioCollection() != null
                        && user.getPlayer().getSource().getAudioCollection().getOwner()
                        .equals(username)) {
                    return true;
                } else {
                    if (user.getPlayer().getSource().getAudioFile() instanceof Song song) {
                        if (song.getArtist().equals(username)) {
                            return true;
                        }
                    }
                }
            }
            if (Objects.equals(user.getCurrentOwnerPage(), username)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Retrieves an artist based on the provided username.
     *
     * @param username The username of the artist to be retrieved.
     */
    public Artist getArtist(final String username) {
        for (Artist artist : artists) {
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
    public Host getHost(final String username) {
        for (Host host : hosts) {
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
    public String addUser(final CommandInput command) {
        String message;
        if (getUser(command.getUsername()) != null) {
            message = "The username " + command.getUsername() + " is already taken.";
        } else {
            switch (command.getType()) {
                case "artist" -> {
                    boolean added = false;
                    for (User user : users) {
                        if (user.getUserType().equals("host")) {
                            users.add(users.indexOf(user), new Artist(command.getUsername(),
                                    command.getAge(), command.getCity(), command.getType()));
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
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= limit) {
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
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= limit) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(albums);
        countAlbumLikes();
        sortedAlbums.sort(Comparator.comparingInt(Album::getLikes).reversed()
                .thenComparing(Album::getName));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= limit) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets top 5 artists.
     *
     * @return the top 5 artists
     */
    public List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(artists);
        for (Artist artist : sortedArtists) {
            int likes = 0;
            for (Album album : artist.getAlbums()) {
                likes += album.getLikes();
            }
            artist.setLikes(likes);
        }
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed()
                .thenComparing(Artist::getUsername));
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= limit) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }

    /**
     * Retrieves a list of usernames for users who are currently online.
     *
     * @return A list of usernames representing online users.
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getConnectionStatus() == Enums.ConnectionStatus.ONLINE
                    && Objects.equals(user.getUserType(), "user")) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Counts the number of likes for each album.
     */
    public void countAlbumLikes() {
        for (Album album : albums) {
            int likes = 0;
            for (Song song : album.getSongs()) {
                likes += song.getLikes();
            }
            album.setLikes(likes);
        }
    }
    /**
     * Retrieves a list of usernames for all users.
     *
     * @return A list of usernames representing all users.
     */
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : users) {
            allUsers.add(user.getUsername());
        }
        return allUsers;
    }
    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
