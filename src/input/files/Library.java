package input.files;

import java.util.ArrayList;

public final class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<UserIn> users;

    public Library() {
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(final ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public ArrayList<UserIn> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<UserIn> users) {
        this.users = users;
    }
}
