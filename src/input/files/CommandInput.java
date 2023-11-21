package input.files;

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
    private ArrayList<String> matchingSongTitles;

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

    public ArrayList<String> getMatchingSongTitles() {
        return matchingSongTitles;
    }

    public void setMatchingSongTitles(final ArrayList<String> matchingSongTitles) {
        this.matchingSongTitles = matchingSongTitles;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(final int seed) {
        this.seed = seed;
    }
}
