package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.pages.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
@Getter
@Setter
public final class User extends UserAbstract {
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private boolean status;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private Page currentPage;
    private HomePage homePage;
    private LikedContentPage likedContentPage;
    private ArrayList<Pair<String, Integer>> topArtists;
    private ArrayList<Pair<String, Integer>> topGenres;
    private ArrayList<Pair<String, Integer>> topSongs;
    private ArrayList<Pair<String, Integer>> topAlbums;
    private ArrayList<Pair<String, Integer>> topPodcasts;
    private Song lastListenedSong;
    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        status = true;

        homePage = new HomePage(this);
        currentPage = homePage;
        likedContentPage = new LikedContentPage(this);
        topArtists = new ArrayList<>();
        topGenres = new ArrayList<>();
        topSongs = new ArrayList<>();
        topAlbums = new ArrayList<>();
        topPodcasts = new ArrayList<>();
    }

    @Override
    public String userType() {
        return "user";
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        setLastListenedSong(null);
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();

        if (type.equals("artist") || type.equals("host")) {
            List<ContentCreator> contentCreatorsEntries =
            searchBar.searchContentCreator(filters, type);

            for (ContentCreator contentCreator : contentCreatorsEntries) {
                results.add(contentCreator.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist")
            || searchBar.getLastSearchType().equals("host")) {
            ContentCreator selected = searchBar.selectContentCreator(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            currentPage = selected.getPage();
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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());

        Admin admin = Admin.getInstance();
        if (Objects.equal(player.getType(), "song")) {
            Song song = (Song) player.getCurrentAudioFile();
            if (!isStringInArray(topSongs, song.getName())) {
                topSongs.add(new Pair<>(song.getName(), 1));
            }
            if (!isStringInArray(topArtists, song.getArtist())) {
                topArtists.add(new Pair<>(song.getArtist(), 1));
            }
            if (!isStringInArray(topGenres, song.getGenre())) {
                topGenres.add(new Pair<>(song.getGenre(), 1));
            }
            if (!isStringInArray(topAlbums, song.getAlbum())) {
                topAlbums.add(new Pair<>(song.getAlbum(), 1));
            }

            Artist artist = (Artist)admin.getAbstractUser(song.getArtist());
            if (artist == null) {
                artist = new Artist(song.getArtist(), 0, "");
                admin.addArtist(artist);
            }
            if (!isStringInArray(artist.getTopSongs(), song.getName())) {
                artist.getTopSongs().add(new Pair<>(song.getName(), 1));
            }
            if (!isStringInArray(artist.getTopAlbums(), song.getAlbum())) {
                artist.getTopAlbums().add(new Pair<>(song.getAlbum(), 1));
            }
            if (!isStringInArray(artist.getListeners(), getUsername())) {
                artist.getListeners().add(new Pair<>(getUsername(), 1));
            }
        } else if (Objects.equal(player.getType(), "podcast")) {
            Podcast podcast = (Podcast) player.getCurrentAudioCollection();
            if (!isStringInArray(topPodcasts, podcast.getName())) {
                topPodcasts.add(new Pair<>(podcast.getName(), 1));
            }
        }

        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    public boolean isStringInArray(ArrayList<Pair<String, Integer>> arrayList, String searchString) {
        for (Pair<String, Integer> pair : arrayList) {
            if (pair.getFirst().equals(searchString)) {
                pair.setSecond(pair.getSecond() + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist")
            && !player.getType().equals("album")) {
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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
            && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

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
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(getUsername())) {
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
     * Switch status.
     */
    public void switchStatus() {
        status = !status;
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (!status) {
            return;
        }

        player.simulatePlayer(time);

        if (player.getSource() != null) {
            if (Objects.equal(player.getType(), "album")) {
                Album album = (Album) player.getCurrentAudioCollection();
                Admin admin = Admin.getInstance();
                Artist artist = (Artist) admin.getAbstractUser(album.getOwner());
                int stop = 0;
                if (lastListenedSong == null) {
                    for (Song song : album.getSongs()) {
                        if (!isStringInArray(topAlbums, song.getAlbum())) {
                            topAlbums.add(new Pair<>(song.getAlbum(), 1));
                        }
                        if (!isStringInArray(topArtists, song.getArtist())) {
                            topArtists.add(new Pair<>(song.getArtist(), 1));
                        }
                        if (!isStringInArray(topSongs, song.getName())) {
                            topSongs.add(new Pair<>(song.getName(), 1));
                        }
                        if (!isStringInArray(topGenres, song.getGenre())) {
                            topGenres.add(new Pair<>(song.getGenre(), 1));
                        }
                        if (!isStringInArray(artist.getTopSongs(), song.getName())) {
                            artist.getTopSongs().add(new Pair<>(song.getName(), 1));
                        }
                        if (!isStringInArray(artist.getTopAlbums(), song.getAlbum())) {
                            artist.getTopAlbums().add(new Pair<>(song.getAlbum(), 1));
                        }
                        if (!isStringInArray(artist.getListeners(), getUsername())) {
                            artist.getListeners().add(new Pair<>(getUsername(), 1));
                        }
                        if (Objects.equal(player.getCurrentAudioFile().getName(), song.getName())) {
                            stop++;
                            break;
                        }
                    }
                } else {
                    int found = 0;
                    for (Song song : album.getSongs()) {
                        if (found == 1) {
                            if (!isStringInArray(topAlbums, song.getAlbum())) {
                                topAlbums.add(new Pair<>(song.getAlbum(), 1));
                            }
                            if (!isStringInArray(topArtists, song.getArtist())) {
                                topArtists.add(new Pair<>(song.getArtist(), 1));
                            }
                            if (!isStringInArray(topSongs, song.getName())) {
                                topSongs.add(new Pair<>(song.getName(), 1));
                            }
                            if (!isStringInArray(topGenres, song.getGenre())) {
                                topGenres.add(new Pair<>(song.getGenre(), 1));
                            }
                            if (!isStringInArray(artist.getTopSongs(), song.getName())) {
                                artist.getTopSongs().add(new Pair<>(song.getName(), 1));
                            }
                            if (!isStringInArray(artist.getTopAlbums(), song.getAlbum())) {
                                artist.getTopAlbums().add(new Pair<>(song.getAlbum(), 1));
                            }
                            if (!isStringInArray(artist.getListeners(), getUsername())) {
                                artist.getListeners().add(new Pair<>(getUsername(), 1));
                            }
                        }
                        if (Objects.equal(player.getCurrentAudioFile().getName(), song.getName())) {
                            stop++;
                            break;
                        }
                        if (Objects.equal(song.getName(), lastListenedSong.getName())) {
                            found = 1;
                        }
                    }
                }
                if (stop != 0) {
                    setLastListenedSong((Song) player.getCurrentAudioFile());
                } else {
                    setLastListenedSong(null);
                }
            }
        }
    }
}
