package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.User;

import java.util.Objects;

public final class Repeat {
    /**
     * Performs the repeat operation for the user based on the current state of the repeat mode.
     * If the user is in "load" mode and has matching songs, podcasts, or playlists, the repeat mode
     * is changed accordingly. The possible repeat modes are "No Repeat," "Repeat Once,"
     * "Repeat Infinite," "Repeat All," and "Repeat Current Song."
     *
     * @param user              The user for whom the repeat mode is being changed.
     * @param commandPromptNode The ObjectNode to which the result message will be added.
     */
    public void performRepeat(final User user, final ObjectNode commandPromptNode,
                              final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getMatchingSongs() != null
                    || user.getUser().getMatchingPodcasts() != null) {
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    user.getUser().setRepeat("Repeat Once");
                    commandPromptNode.put("message", "Repeat mode changed to repeat once.");
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Once")) {
                    user.getUser().setRepeat("Repeat Infinite");
                    commandPromptNode.put("message", "Repeat mode changed to repeat infinite.");
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Infinite")) {
                    user.getUser().setRepeat("No Repeat");
                    commandPromptNode.put("message", "Repeat mode changed to no repeat.");
                    if (user.getUser().getRemainedTime()
                            - commandInput.getTimestamp() + user.getUser().getTimestamp() <= 0) {
                        if (user.getUser().getMatchingSongs() != null) {
                            int multiplier = 1;
                            while (user.getUser().getMatchingSongs().get(0).getDuration()
                                    * multiplier < Math.abs(user.getUser().getRemainedTime()
                                    - commandInput.getTimestamp()
                                    + user.getUser().getTimestamp())) {
                                multiplier++;
                            }
                            user.getUser().setRemainedTime(user.getUser().getMatchingSongs().get(0)
                                    .getDuration() * multiplier
                                    - Math.abs(user.getUser().getRemainedTime()
                                    - commandInput.getTimestamp() + user.getUser().getTimestamp()));
                        } else if (user.getUser().getMatchingPodcasts() != null) {
                            user.getUser().setRemainedTime(user.getUser().getMatchingPodcasts()
                                    .get(0).getDuration() - Math.abs(user.getUser()
                                    .getRemainedTime() - commandInput.getTimestamp()
                                    + user.getUser().getTimestamp()));
                        }
                    } else {
                        user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                                - commandInput.getTimestamp() + user.getUser().getTimestamp());
                    }
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            } else if (user.getUser().getMatchingPlayLists() != null) {
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    user.getUser().setRepeat("Repeat All");
                    commandPromptNode.put("message", "Repeat mode changed to repeat all.");
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat All")) {
                    user.getUser().setRepeat("Repeat Current Song");
                    commandPromptNode.put("message", "Repeat mode changed to repeat current song.");
                    if (user.getUser().getRemainedTime()
                            - commandInput.getTimestamp() + user.getUser().getTimestamp() <= 0) {
                        user.getUser().setRemainedTime(user.getUser().getMatchingPlayLists().get(0)
                                .getDuration() - Math.abs(user.getUser().getRemainedTime()
                                - commandInput.getTimestamp() + user.getUser().getTimestamp()));
                    } else {
                        user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                                - commandInput.getTimestamp() + user.getUser().getTimestamp());
                    }
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Current Song")) {
                    user.getUser().setRepeat("No Repeat");
                    commandPromptNode.put("message", "Repeat mode changed to no repeat.");
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
                    int multiplier = findSong.getDuration() - timeDiff;
                    while (multiplier < commandInput.getTimestamp()
                            - user.getUser().getTimestamp()) {
                        multiplier += findSong.getDuration();
                    }
                    user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                            - commandInput.getTimestamp() + user.getUser().getTimestamp()
                            + multiplier - findSong.getDuration() + timeDiff);
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            }
        } else {
            commandPromptNode.put("message", "Please load a source before setting the "
                    + "repeat status.");
        }
    }
}
