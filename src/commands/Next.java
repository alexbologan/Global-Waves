package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Episode;
import input.files.Song;
import user.User;

import java.util.Objects;

public final class Next {
    /**
     * Performs the next track operation based on the loaded source type (song, playlist, podcast).
     *
     * This method handles skipping to the next track in the playback, considering repeat options.
     *
     * @param user              The user for whom the operation is performed.
     * @param commandPromptNode The JSON object representing the command prompt response.
     * @param commandInput      The input containing the timestamp and other relevant information.
     */
    public void performNext(final User user, final ObjectNode commandPromptNode,
                               final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            int timePassed = user.getUser().getRemainedTime()
                    - commandInput.getTimestamp() + user.getUser().getTimestamp();
            if (user.getUser().getMatchingSongs() != null) {
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    user.getUser().setMatchingSongs(null);
                    commandPromptNode.put("message", "Please load a source before skipping "
                            + "to the next track.");
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Once")) {
                    if (timePassed > 0) {
                        user.getUser().setRepeatCount(1);
                        user.getUser().setRepeat("No Repeat");
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(user.getUser().getMatchingSongs().get(0)
                                .getDuration());
                        commandPromptNode.put("message", "Skipped to next track successfully. "
                                + "The current track is " + user.getUser().getMatchingSongs().get(0)
                                .getName() + ".");
                    } else {
                        commandPromptNode.put("message", "Please load a source before skipping "
                                + "to the next track.");
                    }
                } else {
                    commandPromptNode.put("message", "Skipped to next track successfully. "
                            + "The current track is " + user.getUser().getMatchingSongs().get(0)
                            .getName() + ".");
                }
            } else if (user.getUser().getMatchingPlayLists() != null) {
                Song findSong = new Song();
                Song nextSong = new Song();
                int timeDiff = user.getUser().getMatchingPlayLists().get(0).getDuration()
                        - timePassed;
                for (int i = 0; i < user.getUser().getMatchingPlayLists().get(0).getSongs().size();
                     i++) {
                    if (timeDiff < user.getUser().getMatchingPlayLists().get(0).getSongs().get(i)
                            .getDuration()) {
                        findSong = user.getUser().getMatchingPlayLists().get(0).getSongs().get(i);
                        nextSong = user.getUser().getMatchingPlayLists().get(0).getSongs().get(i
                                + 1);
                        break;
                    }
                    timeDiff -= user.getUser().getMatchingPlayLists().get(0).getSongs().get(i)
                            .getDuration();
                }
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    if (timePassed - findSong.getDuration() + timeDiff > 0) {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed - findSong.getDuration()
                                + timeDiff);
                        commandPromptNode.put("message", "Skipped to next track successfully. "
                                + "The current track is " + nextSong.getName() + ".");
                    }
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat All")) {
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            } else if (user.getUser().getMatchingPodcasts() != null) {
                Episode findEpisode = new Episode();
                Episode nextEpisode = new Episode();
                int timeDiff = user.getUser().getMatchingPodcasts().get(0).getDuration()
                        - timePassed;
                for (int i = 0; i < user.getUser().getMatchingPodcasts().get(0).getEpisodes()
                        .size(); i++) {
                    if (timeDiff < user.getUser().getMatchingPodcasts().get(0).getEpisodes().get(i)
                            .getDuration()) {
                        findEpisode = user.getUser().getMatchingPodcasts().get(0).getEpisodes()
                                .get(i);
                        nextEpisode = user.getUser().getMatchingPodcasts().get(0).getEpisodes()
                                .get(i + 1);
                        break;
                    }
                    timeDiff -= user.getUser().getMatchingPodcasts().get(0).getEpisodes().get(i)
                            .getDuration();
                }
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    if (timePassed - findEpisode.getDuration() + timeDiff > 0) {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed - findEpisode.getDuration()
                                + timeDiff);
                        commandPromptNode.put("message", "Skipped to next track successfully. "
                                + "The current track is " + nextEpisode.getName() + ".");
                    }
                }
            }
        } else {
            commandPromptNode.put("message", "Please load a source before skipping to the next "
                    + "track.");
        }
    }
}
