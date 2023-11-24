package user;

import input.files.Song;

import java.util.ArrayList;

public final class Playlist {
    private String name;
    private int id;
    private ArrayList<Song> songs;
    private String owner;
    private String visibility = "public";
    private int followers;
    private int duration;
    private  ArrayList<Song> unShuffledSongs;

    public Playlist() {
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(final String visibility) {
        this.visibility = visibility;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(final int followers) {
        this.followers = followers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<Song> songs) {
        this.songs = songs;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public ArrayList<Song> getUnShuffledSongs() {
        return unShuffledSongs;
    }

    public void setUnShuffledSongs(final ArrayList<Song> unShuffledSongs) {
        this.unShuffledSongs = unShuffledSongs;
    }
}
