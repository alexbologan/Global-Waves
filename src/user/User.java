package user;

import input.files.CommandInput;
import input.files.Song;

import java.util.ArrayList;
import java.util.Objects;

public final class User {
    private CommandInput user;
    private ArrayList<Playlist> playLists;
    private ArrayList<Song> likedSongs;

    public User() {
    }

    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(final ArrayList<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public CommandInput getUser() {
        return user;
    }

    public void setUser(final CommandInput user) {
        this.user = user;
    }

    public ArrayList<Playlist> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(final ArrayList<Playlist> playlists) {
        this.playLists = playlists;
    }

    /**
     * Searches for a user in the provided list of users based on the given search criteria.
     * If the user is not found, it adds the user to the list.
     *
     * @param users      The list of users to search.
     * @param user1      The user to be searched or added.
     * @param commandInput  The search criteria for finding the user.
     */
    public void searchUser(final ArrayList<User> users, final User user1,
                           final CommandInput commandInput) {
        if (users.isEmpty()) {
            user1.setUser(commandInput);
            users.add(user1);
        } else {
            boolean userExists = false;
            for (User userSearch : users) {
                if (userSearch.getUser() != null && Objects.equals(
                        userSearch.getUser().getUsername(), commandInput.getUsername())) {
                    userExists = true;
                    setUser(userSearch.getUser());
                    break;
                }
            }
            if (!userExists) {
                user1.setUser(commandInput);
                users.add(user1);
            }
        }
    }
}
