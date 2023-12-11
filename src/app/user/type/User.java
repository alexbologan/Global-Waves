package app.user.type;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.Episode;
import app.user.type.ArtistStuff.Event;
import app.user.type.ArtistStuff.Merch;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.type.HostStuff.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type User.
 */
@Getter
public class User {
    private final String username;
    private final int age;
    private final String city;
    private final String userType;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Song> likedSongs;
    private final ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private Enums.ConnectionStatus connectionStatus;
    private String currentPage;
    private String currentOwnerPage;
    private String lastSearchedType;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city,
                final String userType) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.userType = Objects.requireNonNullElse(userType, "user");
        if (this.userType.equals("artist")) {
            connectionStatus = Enums.ConnectionStatus.OFFLINE;
        } else {
            connectionStatus = Enums.ConnectionStatus.ONLINE;
        }
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        currentPage = "Home";
        currentOwnerPage = "";
        lastSearchedType = null;
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();

        if (Objects.equals(type, "artist")) {
            List<Artist> libraryEntries = searchBar.searchArtist(filters, type);
            for (Artist libraryEntry : libraryEntries) {
                results.add(libraryEntry.getUsername());
            }
            lastSearchedType = "artist";
        } else if (Objects.equals(type, "host")) {
            List<Host> libraryEntries = searchBar.searchHost(filters, type);
            for (Host libraryEntry : libraryEntries) {
                results.add(libraryEntry.getUsername());
            }
            lastSearchedType = "host";
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);
            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
            lastSearchedType = type;
        }

        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (Objects.equals(lastSearchedType, "artist")) {
            Artist selected = searchBar.selectArtist(itemNumber);
            if (selected == null) {
                return "The selected ID is too high.";
            }
            currentPage = "Artist";
            currentOwnerPage = selected.getUsername();
            return "Successfully selected %s's page.".formatted(selected.getUsername());
        } else if (Objects.equals(lastSearchedType, "host")) {
            Host selected = searchBar.selectHost(itemNumber);
            if (selected == null) {
                return "The selected ID is too high.";
            }
            currentPage = "Host";
            currentOwnerPage = selected.getUsername();
            return "Successfully selected %s's page.".formatted(selected.getUsername());
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);
            if (selected == null) {
                return "The selected ID is too high.";
            }
            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus;

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }


        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        Admin admin = Admin.getInstance();
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            if (admin.findSong(song.getName()) != null) {
                admin.findSong(song.getName()).dislike();
            }
            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        if (admin.findSong(song.getName()) != null) {
            admin.findSong(song.getName()).like();
        }
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switches the connection status of the user between online and offline.
     *
     * @return A message indicating the success of the status change.
     */
    public String switchConnectionStatus() {
        if (connectionStatus == Enums.ConnectionStatus.ONLINE) {
            connectionStatus = Enums.ConnectionStatus.OFFLINE;
        } else {
            connectionStatus = Enums.ConnectionStatus.ONLINE;
        }
        return username + " has changed status successfully.";
    }

    /**
     * Returns a formatted string representing the content of the current user's page.
     * The format includes liked songs and followed playlists.
     *
     * @return A formatted string containing liked songs and followed playlists.
     */
    public String printCurrentPage() {
        switch (currentPage) {
            case "Home" -> {
                StringBuilder message = new StringBuilder("Liked songs:\n\t[");

                for (Song song : likedSongs) {
                    message.append(song.getName()).append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }

                message.append("]\n\nFollowed playlists:\n\t[");

                for (Playlist playlist : followedPlaylists) {
                    message.append(playlist.getName()).append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }
                return message + "]";
            }
            case "Artist" -> {
                Artist artist = searchBar.getLastSelectedArtist();
                StringBuilder message = new StringBuilder("Albums:\n\t[");
                for (Album album : artist.getAlbums()) {
                    message.append(album.getName()).append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }

                message.append("]\n\nMerch:\n\t[");
                for (Merch merch : artist.getMerchandises()) {
                    message.append(merch.getName()).append(" - ").append(merch.getPrice())
                            .append(":\n\t").append(merch.getDescription()).append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }

                message.append("]\n\nEvents:\n\t[");
                for (Event event : artist.getEvents()) {
                    message.append(event.getName()).append(" - ").append(event.getDate())
                            .append(":\n\t").append(event.getDescription()).append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }
                return message + "]";
            }
            case "Host" -> {
                Host host = searchBar.getLastSelectedHost();
                StringBuilder message = new StringBuilder("Podcasts:\n\t[");
                for (Podcast podcast : host.getPodcasts()) {
                    message.append(podcast.getName()).append(":\n\t[");
                    for (Episode episode : podcast.getEpisodes()) {
                        message.append(episode.getName()).append((" - ")).
                                append(episode.getDescription()).append(", ");
                    }
                    if (message.charAt(message.length() - 1) == ' ') {
                        message = new StringBuilder(message.substring(0, message.length() - 2));
                    }
                    message.append("]\n, ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }

                message.append("]\n\nAnnouncements:\n\t[");
                for (Announcement announcement : host.getAnnouncements()) {
                    message.append(announcement.getName()).append(":\n\t")
                            .append(announcement.getDescription()).append("\n, ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }
                return message + "]";
            }
            case "LikedContent" -> {
                StringBuilder message = new StringBuilder("Liked songs:\n\t[");

                for (Song song : likedSongs) {
                    message.append(song.getName()).append(" - ").append(song.getArtist())
                            .append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }

                message.append("]\n\nFollowed playlists:\n\t[");

                for (Playlist playlist : followedPlaylists) {
                    message.append(playlist.getName()).append(" - ").append(playlist.getOwner())
                            .append(", ");
                }

                if (message.charAt(message.length() - 1) == ' ') {
                    message = new StringBuilder(message.substring(0, message.length() - 2));
                }
                return message + "]";
            }
            default -> {
                return "Unknown";
            }
        }
    }

    /**
     * Change page.
     *
     * @return the next page.
     */
    public String changePage(final String nextPage) {
        if (Objects.equals(nextPage, "Home") || Objects.equals(nextPage, "LikedContent")) {
            currentPage = nextPage;
            return username + " accessed " + nextPage + " successfully.";
        }
        return username + " is trying to access a non-existent page.";
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }
}
