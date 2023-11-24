package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.User;

import java.util.ArrayList;
import java.util.Objects;

public final class Like {
    /**
     * Performs the like/unlike operation based on the user's command and the loaded source.
     *
     * This method checks if the user has loaded a song source using the "load" command. If a
     * song source is loaded, it allows the user to register likes or unlikes for the currently
     * matching song. If the user has not loaded a source, it provides a message indicating that
     * a source needs to be loaded before liking or unliking.
     *
     * @param user              The user for whom the like/unlike operation is performed.
     * @param commandPromptNode The JSON node to store the result message.
     */
    public void performLike(final CommandInput commandInput, final User user,
                            final ObjectNode commandPromptNode) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getMatchingSongs() == null
                    || user.getUser().getMatchingSongs().isEmpty()) {
                if (user.getUser().getMatchingPlayLists() == null
                        || user.getUser().getMatchingPlayLists().isEmpty()) {
                    commandPromptNode.put("message", "The loaded source is not a song.");
                } else {
                    Song findSong = new Song();
                    int timeDiff = commandInput.getTimestamp() - user.getUser().getTimestamp();
                    if (timeDiff > user.getUser().getRemainedTime()) {
                        for (Song song : user.getUser().getMatchingPlayLists()
                                .get(0).getSongs()) {
                            if (timeDiff < song.getDuration()) {
                                findSong = song;
                                break;
                            }
                            timeDiff -= song.getDuration();
                        }
                    }
                    if (user.getLikedSongs() == null || user.getLikedSongs().isEmpty()) {
                        ArrayList<Song> likedSongs = new ArrayList<>();
                        likedSongs.add(findSong);
                        user.setLikedSongs(likedSongs);
                        commandPromptNode.put("message", "Like registered successfully.");
                    } else {
                        if (user.getLikedSongs().contains(findSong)) {
                            user.getLikedSongs().remove(findSong);
                            commandPromptNode.put("message", "Unlike registered successfully.");
                        } else {
                            user.getLikedSongs().add(findSong);
                            commandPromptNode.put("message", "Like registered successfully.");
                        }
                    }
                }
            } else {
                if (user.getLikedSongs() == null || user.getLikedSongs().isEmpty()) {
                    ArrayList<Song> likedSongs = new ArrayList<>();
                    likedSongs.add(user.getUser().getMatchingSongs().get(0));
                    user.setLikedSongs(likedSongs);
                    commandPromptNode.put("message", "Like registered successfully.");
                } else {
                    if (user.getLikedSongs().contains(user.getUser().getMatchingSongs().get(0))) {
                        user.getLikedSongs().remove(user.getUser().getMatchingSongs().get(0));
                        commandPromptNode.put("message", "Unlike registered successfully.");
                    } else {
                        user.getLikedSongs().add(user.getUser().getMatchingSongs().get(0));
                        commandPromptNode.put("message", "Like registered successfully.");
                    }
                }
            }
        } else {
            commandPromptNode.put("message", "Please load a source before liking or unliking.");
        }
    }
}
