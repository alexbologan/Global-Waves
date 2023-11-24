package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.Playlist;
import user.User;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Objects;
import java.util.Random;

public final class Shuffle {
    /**
     * Performs the shuffle function for the user based on the given command input.
     *
     * @param user               The user for whom the shuffle operation is performed.
     * @param commandPromptNode The ObjectNode representing the command prompt.
     * @param commandInput      The input containing information about the shuffle command.
     */
    public void performShuffle(final User user, final ObjectNode commandPromptNode,
                              final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getRemainedTime() - commandInput.getTimestamp()
                    + user.getUser().getTimestamp() <= 0) {
                commandPromptNode.put("message", "Please load a source before using the shuffle "
                        + "function.");
                user.getUser().setShuffle(false);
            } else {
                if (user.getUser().getMatchingPlayLists() != null) {
                    Song findSong = new Song();
                    int timeDiff = user.getUser().getMatchingPlayLists().get(0).getDuration()
                            - user.getUser().getRemainedTime();
                    for (Song song: user.getUser().getMatchingPlayLists().get(0).getSongs()) {
                        if (timeDiff < song.getDuration()) {
                            findSong = song;
                            break;
                        }
                        timeDiff -= song.getDuration();
                    }
                    if (Objects.equals(user.getUser().getShuffle(), false)) {
                        for (Playlist playlist : user.getPlayLists()) {
                            if (Objects.equals(playlist.getName(), user.getUser()
                                    .getMatchingPlayLists().get(0).getName())) {
                                ArrayList<Song> unShuffledSongs = new ArrayList<>(user.getUser()
                                        .getMatchingPlayLists().get(0).getSongs());
                                playlist.setUnShuffledSongs(unShuffledSongs);
                            }
                        }
                        Collections.shuffle(user.getUser().getMatchingPlayLists().get(0).getSongs(),
                                new Random(commandInput.getSeed()));
                        int newRemainedTime = user.getUser().getMatchingPlayLists().get(0)
                                .getDuration() - timeDiff;
                        for (Song song : user.getUser().getMatchingPlayLists().get(0).getSongs()) {
                            if (Objects.equals(findSong, song)) {
                                user.getUser().setRemainedTime(newRemainedTime);
                            }
                            newRemainedTime -= song.getDuration();
                        }
                        user.getUser().setShuffle(true);
                        commandPromptNode.put("message", "Shuffle function activated "
                                + "successfully.");
                    } else {
                        user.getUser().getMatchingPlayLists().get(0).getSongs().clear();
                        for (Playlist playlist : user.getPlayLists()) {
                            if (Objects.equals(playlist.getName(), user.getUser()
                                    .getMatchingPlayLists().get(0).getName())) {
                                user.getUser().getMatchingPlayLists().get(0).setSongs(playlist
                                        .getUnShuffledSongs());
                            }
                        }
                        int newRemainedTime = user.getUser().getMatchingPlayLists().get(0)
                                .getDuration() - timeDiff;
                        for (Song song : user.getUser().getMatchingPlayLists().get(0).getSongs()) {
                            if (Objects.equals(findSong, song)) {
                                user.getUser().setRemainedTime(newRemainedTime);
                            }
                            newRemainedTime -= song.getDuration();
                        }
                        user.getUser().setShuffle(false);
                        commandPromptNode.put("message", "Shuffle function deactivated "
                                + "successfully.");
                    }
                } else {
                    commandPromptNode.put("message", "The loaded source is not a playlist.");
                }
            }
        } else {
            commandPromptNode.put("message", "Please load a source before using the shuffle "
                    + "function.");
            user.getUser().setShuffle(false);
        }
    }
}
