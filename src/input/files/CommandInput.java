package input.files;

import user.Playlist;

import java.util.ArrayList;

public final class CommandInput {
    private String command;
    private String username;
    private int timestamp;
    private String type;
    private Filters filters;
    private int itemNumber;
    private String playlistName;
    private int playlistId;
    private int seed;
    private ArrayList<Song> matchingSongs;
    private ArrayList<Podcast> matchingPodcasts;
    private ArrayList<Playlist> matchingPlayLists;
    private Boolean shuffle = false;
    private Boolean paused = false;
    private String repeat = "No Repeat";
    private int repeatCount;
    private int remainedTime = 0;

    public CommandInput() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(final int playlistId) {
        this.playlistId = playlistId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(final Filters filters) {
        this.filters = filters;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public ArrayList<Song> getMatchingSongs() {
        return matchingSongs;
    }

    public void setMatchingSongs(final ArrayList<Song> matchingSongs) {
        this.matchingSongs = matchingSongs;
    }

    public ArrayList<Podcast> getMatchingPodcasts() {
        return matchingPodcasts;
    }

    public void setMatchingPodcasts(final ArrayList<Podcast> matchingPodcasts) {
        this.matchingPodcasts = matchingPodcasts;
    }

    public Boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(final Boolean shuffle) {
        this.shuffle = shuffle;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(final Boolean paused) {
        this.paused = paused;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(final String repeat) {
        this.repeat = repeat;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public void setRemainedTime(final int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public ArrayList<Playlist> getMatchingPlayLists() {
        return matchingPlayLists;
    }

    public void setMatchingPlayLists(final ArrayList<Playlist> matchingPlaylists) {
        this.matchingPlayLists = matchingPlaylists;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(final int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(final int seed) {
        this.seed = seed;
    }
}
