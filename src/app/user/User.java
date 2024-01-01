package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.ArtistPage;
import app.pages.HostPage;
import app.pages.LikedContentPage;
import app.pages.Page;
import app.pages.HomePage;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * The type User.
 */
@Getter
@Setter
public final class User extends UserAbstract implements Subscriber {
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private ArrayList<Song> songRecommendations;
    private ArrayList<Playlist> playlistRecommendations;
    private String lastRecommendationType;
    private final Player player;
    private boolean status;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private ArrayList<Page> previousPages;
    private ArrayList<Page> nextPages;
    private Page currentPage;
    private HomePage homePage;
    private LikedContentPage likedContentPage;
    private ArtistPage artistPage;
    private HostPage hostPage;
    private ArrayList<Pair<String, Integer>> topArtists;
    private ArrayList<Pair<String, Integer>> topGenres;
    private ArrayList<Pair<String, Integer>> topSongs;
    private ArrayList<Pair<String, Integer>> topSongsPremium;
    private ArrayList<Pair<String, Integer>> topSongsNonPremium;
    private ArrayList<Pair<String, Integer>> topAlbums;
    private ArrayList<Pair<String, Integer>> topEpisodes;
    private Song lastListenedSong;
    private Episode lastListenedEpisode;
    private int premium;
    private Integer adPrice;
    private boolean adBreakPlayed;
    private ArrayList<Pair<String, String>> notifications;
    private ArrayList<Merchandise> merch;
    private final Admin admin = Admin.getInstance();
    private final int limit = 5;
    private final int limit2 = 3;
    private final int listenLimit = 30;

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
        songRecommendations = new ArrayList<>();
        playlistRecommendations = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        status = true;
        adBreakPlayed = false;
        adPrice = null;

        homePage = new HomePage(this);
        currentPage = homePage;
        likedContentPage = new LikedContentPage(this);
        previousPages = new ArrayList<>();
        nextPages = new ArrayList<>();
        topArtists = new ArrayList<>();
        topGenres = new ArrayList<>();
        topSongs = new ArrayList<>();
        topAlbums = new ArrayList<>();
        topEpisodes = new ArrayList<>();
        topSongsPremium = new ArrayList<>();
        topSongsNonPremium = new ArrayList<>();
        notifications = new ArrayList<>();
        merch = new ArrayList<>();
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

            previousPages.add(currentPage);
            currentPage = selected.getPage();
            nextPages.clear();
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
        setLastListenedSong(null);
        setLastListenedEpisode(null);
        player.setAdBreakActive(false);
        adPrice = null;
        adBreakPlayed = false;
        player.setSource(null, null);

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

        if (Objects.equal(player.getType(), "song")) {
            Song song = (Song) player.getCurrentAudioFile();
            Artist artist = (Artist) admin.getAbstractUser(song.getArtist());

            if (artist == null) {
                artist = new Artist(song.getArtist(), 0, "");
                admin.addArtist(artist);
            }
            addData(artist, song);
        }

        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Load recommendations string.
     *
     * @return the string
     */
    public String loadRecommendations() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (lastRecommendationType == null) {
            return "No recommendations available.";
        }

        setLastListenedSong(null);
        setLastListenedEpisode(null);
        player.setAdBreakActive(false);
        adPrice = null;
        adBreakPlayed = false;
        player.setSource(null, null);

        if (lastRecommendationType.equals("song")) {
            player.setSource(songRecommendations.get(songRecommendations.size() - 1), "song");
            Song song = (Song) player.getCurrentAudioFile();
            Artist artist = (Artist) admin.getAbstractUser(song.getArtist());

            if (artist == null) {
                artist = new Artist(song.getArtist(), 0, "");
                admin.addArtist(artist);
            }
            addData(artist, song);
        } else {
            player.setSource(playlistRecommendations.get(playlistRecommendations.size() - 1),
                    "playlist");
        }

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Is string in array boolean.
     *
     * @param arrayList     the array list
     * @param searchString the search string
     * @return the boolean
     */
    public boolean isStringInArray(final ArrayList<Pair<String, Integer>> arrayList,
                                   final String searchString) {
        for (Pair<String, Integer> pair : arrayList) {
            if (pair.getFirst().equals(searchString)) {
                pair.setSecond(pair.getSecond() + 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the artist's page.
     *
     * @return the artist's page
     */
    public ArtistPage getArtistPage() {
        return new ArtistPage(admin.getArtist(((Song) player.getCurrentAudioFile()).getArtist()));
    }

    /**
     * Gets the host's page.
     *
     * @return the host's page
     */
    public HostPage getHostPage() {
        return new HostPage(admin.getHost(player.getCurrentAudioCollection().getOwner()));
    }

    /**
     * Add the selected page to the previous pages' history.
     */
    public void addPreviousPage(final Page page) {
        previousPages.add(page);
    }

    /**
     * Remove the last page from the previous pages' history.
     */
    public void removePreviousPage() {
        previousPages.remove(previousPages.size() - 1);
    }

    /**
     * Get the last page from the previous pages' history.
     */
    public Page popPreviousPage() {
        Page page = previousPages.get(previousPages.size() - 1);
        previousPages.remove(previousPages.size() - 1);
        return page;
    }

    /**
     * Add the selected page to the next pages' history.
     */
    public void addNextPage(final Page page) {
        nextPages.add(page);
    }

    /**
     * Clear next pages.
     */
    public void clearNextPages() {
        nextPages.clear();
    }

    /**
     * Get the last page from the next pages' history.
     */
    public Page popNextPage() {
        Page page = nextPages.get(nextPages.size() - 1);
        nextPages.remove(nextPages.size() - 1);
        return page;
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
     * Add merch string.
     *
     * @param merchandise the merchandise
     */
    public void addMerch(final Merchandise merchandise) {
        merch.add(merchandise);
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
     * Buy merch.
     *
     * @param merchName the merch name
     * @return the string
     */
    public String buyMerch(final String merchName) {
        return currentPage.buyMerch(this, merchName);
    }

    /**
     * See merch array list.
     *
     * @return the array list
     */
    public ArrayList<String> seeMerch() {
        ArrayList<String> merchNames = new ArrayList<>();
        for (Merchandise merchItem : merch) {
            merchNames.add(merchItem.getName());
        }
        return merchNames;
    }

    /**
     * Attempts to upgrade the user to a premium subscription.
     *
     * @return A message indicating the result.
     */
    public String buyPremium() {
        if (premium == 1) {
            return getUsername() + " is already a premium user.";
        }
        premium = 1;
        return getUsername() + " bought the subscription successfully.";
    }

    /**
     * Attempts to cancel the user's premium subscription.
     *
     * @return A message indicating the result.
     */
    public String cancelPremium() {
        if (premium == 0) {
            return getUsername() + " is not a premium user.";
        }

        admin.calculateRevenue(admin.getUser(getUsername()));

        topSongsPremium.clear();
        premium = 0;
        return getUsername() + " cancelled the subscription successfully.";
    }

    /**
     * Attempts to add an ad break to the user player.
     *
     * @return A message indicating the result.
     */
    public String adBreak(final int price) {
        if (player.getPaused()) {
            return getUsername() + " is not playing any music.";
        }

        if (premium == 0) {
            adPrice = price;
            player.setAdBreakActive(true);
            return "Ad inserted successfully.";
        }

        return "Ad break is only available for free users.";
    }

    /**
     * Attempts to subscribe to the current pages' artist.
     *
     * @return A message indicating the result.
     */
    public String subscribe() {
        return currentPage.processSubscription(this);
    }

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    public ArrayList<NotificationOutput> getNotifications() {
        ArrayList<NotificationOutput> notificationOutputs = new ArrayList<>();
        for (Pair<String, String> pair : notifications) {
            notificationOutputs.add(new NotificationOutput("New %s".formatted(pair.getFirst()),
                    "New %s from %s.".formatted(pair.getFirst(), pair.getSecond())));
        }
        notifications.clear();
        return notificationOutputs;
    }

    /**
     * Attempts to update the user's recommendations.
     *
     * @return A message indicating the result.
     */
    public String updateRecommendations(final String recommendationType) {
        if (player.getCurrentAudioFile() == null) {
            return "No new recommendations were found";
        }

        Song playerSong = (Song) player.getCurrentAudioFile();
        switch (recommendationType) {
            case "random_song" -> {
                if (playerSong.getDuration() - player.getSource().getRemainedDuration()
                        < listenLimit) {
                    return "No new recommendations were found";
                }
                ArrayList<Song> songs = new ArrayList<>();
                admin.getSongs().stream()
                        .filter(song -> song.getGenre().equals(playerSong.getGenre())
                                && !songs.contains(song)).forEach(songs::add);

                Random random = new Random(playerSong.getDuration()
                        - player.getSource().getRemainedDuration());

                songRecommendations.add(songs.get(random.nextInt(songs.size())));
                lastRecommendationType = "song";
            }
            case "random_playlist" -> {
                Playlist playlist = new Playlist(getUsername() + "'s recommendations",
                        getUsername());
                ArrayList<Pair<String, ArrayList<Pair<Song, Integer>>>> genre = new ArrayList<>();

                likedSongs.forEach(song ->
                    addDataToGenre(genre, song)
                );

                playlists.forEach(playlist1 ->
                    playlist1.getSongs().forEach(song ->
                        addDataToGenre(genre, song)
                    )
                );

                followedPlaylists.forEach(playlist1 ->
                    playlist1.getSongs().forEach(song ->
                        addDataToGenre(genre, song)
                    )
                );

                genre.sort(Comparator.comparingInt(pair -> sumOfIntegers(pair.getSecond())));
                genre.forEach(pair ->
                        pair.getSecond().sort(Comparator.comparingInt(Pair::getSecond)));

                if (genre.isEmpty()) {
                    return "No new recommendations were found";
                }

                for (int i = 0; i < Math.min(genre.size(), limit2); i++) {
                    genre.get(i).getSecond().stream()
                            .limit(i == 2 ? 2 : i == 1 ? limit2 : limit)
                            .filter(pair -> !playlist.getSongs().contains(pair.getFirst()))
                            .forEach(pair -> playlist.addSong(pair.getFirst()));
                }

                playlistRecommendations.add(playlist);
                lastRecommendationType = "playlist";
            }
            case "fans_playlist" -> {
                Artist artist = (Artist) admin.getAbstractUser(playerSong.getArtist());
                Playlist playlist = new Playlist(artist.getUsername() + " Fan Club recommendations",
                        getUsername());

                artist.getListeners().sort(Comparator.comparing(Pair<String, Integer>::getSecond)
                        .reversed().thenComparing(Pair::getFirst));

                int count = 0;
                for (Pair<String, Integer> pair : artist.getListeners()) {
                    User user = (User) admin.getAbstractUser(pair.getFirst());
                    int i = 0;
                    if (user != null) {
                        for (Song song : user.getLikedSongs()) {
                            if (!playlist.getSongs().contains(song)) {
                                playlist.addSong(song);
                                i++;
                            }
                            if (i == limit) {
                                break;
                            }
                        }
                    }
                    count++;
                    if (count == limit) {
                        break;
                    }
                }
                if (playlist.getSongs().isEmpty()) {
                    return "No new recommendations were found";
                }
                playlist.getSongs().sort(Comparator.comparing(Song::getLikes).reversed());
                playlistRecommendations.add(playlist);
                lastRecommendationType = "playlist";
            }
            default -> {
                return "Invalid recommendation type.";
            }
        }

        return "The recommendations for user %s have been updated successfully."
                                                        .formatted(getUsername());
    }

    private int sumOfIntegers(final List<Pair<Song, Integer>> pairList) {
        return pairList.stream()
                .mapToInt(Pair::getSecond)
                .sum();
    }

    /**
     * Add data to genre.
     *
     * @param genre the genre
     * @param song  the song
     */
    private void addDataToGenre(final ArrayList<Pair<String, ArrayList<Pair<Song, Integer>>>> genre,
                                final Song song) {
        if (genre.stream().noneMatch(pair -> pair.getFirst().equals(song.getGenre()))) {
            ArrayList<Pair<Song, Integer>> songs = new ArrayList<>();
            songs.add(new Pair<>(song, 1));
            genre.add(new Pair<>(song.getGenre(), songs));
        } else {
            genre.stream().filter(pair -> pair.getFirst().equals(song.getGenre()))
                    .forEach(pair -> {
                        if (pair.getSecond().stream().noneMatch(pair1
                                -> pair1.getFirst().equals(song))) {
                            pair.getSecond().add(new Pair<>(song, 1));
                        } else {
                            pair.getSecond().stream().filter(pair1
                                            -> pair1.getFirst().equals(song))
                                    .forEach(pair1 -> pair1.setSecond(pair1.getSecond() + 1));
                        }
                    });
        }
    }

    /**
     * Add data.
     *
     * @param artist the artist
     * @param song   the song
     */
    private void addData(final Artist artist, final Song song) {
        if (!isStringInArray(topAlbums, song.getAlbum())) {
            topAlbums.add(new Pair<>(song.getAlbum(), 1));
        }
        if (premium == 1 && !isStringInArray(topSongsPremium, song.getName())) {
            topSongsPremium.add(new Pair<>(song.getName(), 1));
        }
        if (premium == 0 && !isStringInArray(topSongsNonPremium, song.getName())) {
            topSongsNonPremium.add(new Pair<>(song.getName(), 1));
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

    /**
     * Review data for wrapped.
     */
    public void reviewData() {
        int stop = 0;
        if (Objects.equal(player.getType(), "album")
                || Objects.equal(player.getType(), "playlist")) {
            Album album = (Album) player.getCurrentAudioCollection();
            Artist artist = (Artist) admin.getAbstractUser(album.getOwner());
            if (lastListenedSong == null) {
                for (Song song : album.getSongs()) {
                    addData(artist, song);
                    if (Objects.equal(player.getCurrentAudioFile().getName(), song.getName())) {
                        stop++;
                        break;
                    }
                }
            } else {
                int found = 0;
                for (Song song : album.getSongs()) {
                    if (found == 1) {
                        addData(artist, song);
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
        } else if (Objects.equal(player.getType(), "podcast")) {
            Podcast podcast = (Podcast) player.getCurrentAudioCollection();
            Host host = (Host) admin.getAbstractUser(podcast.getOwner());
            if (host == null) {
                host = new Host(podcast.getOwner(), 0, "");
                admin.addHost(host);
            }
            if (lastListenedEpisode == null) {
                for (Episode episode : podcast.getEpisodes()) {
                    if (!isStringInArray(topEpisodes, episode.getName())) {
                        topEpisodes.add(new Pair<>(episode.getName(), 1));
                    }
                    if (!isStringInArray(host.getTopEpisodes(), episode.getName())) {
                        host.getTopEpisodes().add(new Pair<>(episode.getName(), 1));
                    }
                    if (!isStringInArray(host.getListeners(), getUsername())) {
                        host.getListeners().add(new Pair<>(getUsername(), 1));
                    }
                    if (Objects.equal(player.getCurrentAudioFile().getName(), episode.getName())) {
                        stop++;
                        break;
                    }
                }
            } else {
                int found = 0;
                for (Episode episode : podcast.getEpisodes()) {
                    if (found == 1) {
                        if (!isStringInArray(topEpisodes, episode.getName())) {
                            topEpisodes.add(new Pair<>(episode.getName(), 1));
                        }
                        if (!isStringInArray(host.getTopEpisodes(), episode.getName())) {
                            host.getTopEpisodes().add(new Pair<>(episode.getName(), 1));
                        }
                        if (!isStringInArray(host.getListeners(), getUsername())) {
                            host.getListeners().add(new Pair<>(getUsername(), 1));
                        }
                    }
                    if (Objects.equal(player.getCurrentAudioFile().getName(), episode.getName())) {
                        stop++;
                        break;
                    }
                    if (Objects.equal(episode.getName(), lastListenedEpisode.getName())) {
                        found = 1;
                    }
                }
            }
            if (stop != 0) {
                setLastListenedEpisode((Episode) player.getCurrentAudioFile());
            } else {
                setLastListenedEpisode(null);
            }
        }
    }

    /**
     * Calculate ad revenue.
     */
    public void calculateAd() {
        int numberOfListenedSongs = getTopSongsNonPremium().stream().mapToInt(Pair::getSecond)
                .sum();

        topSongsNonPremium.forEach(pair -> admin.getArtists().forEach(artist -> {
            if (artist.hasSong(pair.getFirst())) {
                artist.addSongRevenue(pair.getFirst(), (double) adPrice
                        / numberOfListenedSongs * pair.getSecond());
            }
        }));

        adPrice = null;
        adBreakPlayed = false;
        topSongsNonPremium.clear();
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

        adBreakPlayed = player.simulatePlayer(time);

        if (adBreakPlayed) {
            calculateAd();
        }

        if (player.getSource() != null) {
            reviewData();
        }

    }

    @Override
    public void update(final String notification, final String artistName) {
        notifications.add(new Pair<>(notification, artistName));
    }
}
