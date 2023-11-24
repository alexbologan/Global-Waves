package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.User;

import java.util.Objects;

public final class Prev {
    /**
     * Performs the previous track operation based on the loaded source type
     * (song, playlist, podcast).
     *
     * This method handles returning to the previous track in the playback,
     * considering repeat options.
     *
     * @param user              The user for whom the operation is performed.
     * @param commandPromptNode The JSON object representing the command prompt response.
     * @param commandInput      The input containing the timestamp and other relevant information.
     */
    public void performPrev(final User user, final ObjectNode commandPromptNode,
                               final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            int timePassed = user.getUser().getRemainedTime()
                    - commandInput.getTimestamp() + user.getUser().getTimestamp();
            if (user.getUser().getMatchingSongs() != null) {
                user.getUser().setTimestamp(commandInput.getTimestamp());
            } else if (user.getUser().getMatchingPlayLists() != null) {
                Song findSong = new Song();
                Song prevSong = new Song();
                int timeDiff = user.getUser().getMatchingPlayLists().get(0).getDuration()
                        - timePassed;
                for (int i = 0; i < user.getUser().getMatchingPlayLists().get(0).getSongs().size();
                     i++) {
                    if (timeDiff < user.getUser().getMatchingPlayLists().get(0).getSongs().get(i)
                            .getDuration()) {
                        findSong = user.getUser().getMatchingPlayLists().get(0).getSongs().get(i);
                        prevSong = user.getUser().getMatchingPlayLists().get(0).getSongs().get(i
                                - 1);
                        break;
                    }
                    timeDiff -= user.getUser().getMatchingPlayLists().get(0).getSongs().get(i)
                            .getDuration();
                }
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    if (timeDiff > 0) {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed + timeDiff);
                        commandPromptNode.put("message", "Returned to previous track successfully. "
                                + "The current track is " + findSong.getName() + ".");
                    } else {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed + prevSong.getDuration());
                        commandPromptNode.put("message", "Returned to previous track successfully. "
                                + "The current track is " + prevSong.getName() + ".");
                    }
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat All")) {
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            } else if (user.getUser().getMatchingPodcasts() != null) {
                user.getUser().setTimestamp(commandInput.getTimestamp());
            }
        } else {
            commandPromptNode.put("message", "Please load a source before returning to the"
                    + " previous track.");
        }
    }
}
