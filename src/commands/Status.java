package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Episode;
import input.files.Song;
import user.User;

import java.util.Objects;

public final class Status {
    /**
     * Performs the retrieval of user status information based on the given command input.
     *
     * This method calculates the remaining time of the currently playing song, updates the
     * user's status information, and constructs a JSON node containing the relevant statistics
     * such as the song name, remaining time, repeat status, shuffle status, and pause status.
     *
     * @param commandInput      The input parameters for the command.
     * @param user              The user for whom the status information is retrieved.
     * @param commandPromptNode The JSON node to store the user's status information.
     */
    public void performStatus(final CommandInput commandInput, final User user,
                            final ObjectNode commandPromptNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode statsNode = objectMapper.createObjectNode();
        int timePassed = user.getUser().getRemainedTime()
                - commandInput.getTimestamp() + user.getUser().getTimestamp();
        if (Objects.equals(user.getUser().getPaused(), false)) {
            if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                user.getUser().setRemainedTime(timePassed);
            } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Once")) {
                if (user.getUser().getRepeatCount() == 0) {
                    if (timePassed <= 0) {
                        user.getUser().setRepeatCount(1);
                        user.getUser().setRepeat("No Repeat");
                        if (user.getUser().getMatchingSongs() != null) {
                            user.getUser().setRemainedTime(user.getUser().getMatchingSongs().get(0)
                                    .getDuration() - Math.abs(timePassed));
                        } else if (user.getUser().getMatchingPodcasts() != null) {
                            user.getUser().setRemainedTime(user.getUser().getMatchingPodcasts()
                                    .get(0).getDuration() - Math.abs(timePassed));
                        }
                    } else {
                        user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                                - commandInput.getTimestamp() + user.getUser().getTimestamp());
                    }
                } else {
                    user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                            - commandInput.getTimestamp() + user.getUser().getTimestamp());
                }
            } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Infinite")) {
                if (timePassed <= 0) {
                    if (user.getUser().getMatchingSongs() != null) {
                        int multiplier = 1;
                        while (user.getUser().getMatchingSongs().get(0).getDuration() * multiplier
                                < Math.abs(timePassed)) {
                            multiplier++;
                        }
                        user.getUser().setRemainedTime(user.getUser().getMatchingSongs().get(0)
                                .getDuration() * multiplier
                                - Math.abs(timePassed));
                    } else if (user.getUser().getMatchingPodcasts() != null) {
                        user.getUser().setRemainedTime(user.getUser().getMatchingPodcasts().get(0)
                                .getDuration() - Math.abs(timePassed));
                    }
                } else {
                    user.getUser().setRemainedTime(timePassed);
                }
            } else if (Objects.equals(user.getUser().getRepeat(), "Repeat All")) {
                if (timePassed <= 0) {
                    user.getUser().setRemainedTime(user.getUser().getMatchingPlayLists().get(0)
                            .getDuration() - Math.abs(timePassed));
                } else {
                    user.getUser().setRemainedTime(timePassed);
                }
            } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Current Song")) {
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
                while (multiplier < commandInput.getTimestamp() - user.getUser().getTimestamp()) {
                    multiplier += findSong.getDuration();
                }
                user.getUser().setRemainedTime(timePassed + multiplier - findSong.getDuration()
                        + timeDiff);
            }
        }
        if (user.getUser().getRemainedTime() > 0) {
            if (user.getUser().getMatchingSongs() != null) {
                statsNode.put("name", user.getUser().getMatchingSongs().get(0).getName());
                statsNode.put("remainedTime", user.getUser().getRemainedTime());
                statsNode.put("repeat", user.getUser().getRepeat());
                statsNode.put("shuffle", user.getUser().getShuffle());
                statsNode.put("paused", user.getUser().getPaused());
            } else if (user.getUser().getMatchingPlayLists() != null) {
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
                statsNode.put("name", findSong.getName());
                statsNode.put("remainedTime", findSong.getDuration() - timeDiff);
                statsNode.put("repeat", user.getUser().getRepeat());
                statsNode.put("shuffle", user.getUser().getShuffle());
                statsNode.put("paused", user.getUser().getPaused());
            } else if (user.getUser().getMatchingPodcasts() != null) {
                Episode findEpisode = new Episode();
                int timeDiff = user.getUser().getMatchingPodcasts().get(0).getDuration()
                        - user.getUser().getRemainedTime();
                for (Episode episode: user.getUser().getMatchingPodcasts()
                        .get(0).getEpisodes()) {
                    if (timeDiff < episode.getDuration()) {
                        findEpisode = episode;
                        break;
                    }
                    timeDiff -= episode.getDuration();
                }
                statsNode.put("name", findEpisode.getName());
                statsNode.put("remainedTime", findEpisode.getDuration()
                        - timeDiff);
                statsNode.put("repeat", user.getUser().getRepeat());
                statsNode.put("shuffle", user.getUser().getShuffle());
                statsNode.put("paused", user.getUser().getPaused());
            }
        } else {
            statsNode.put("name", "");
            statsNode.put("remainedTime", 0);
            statsNode.put("repeat", user.getUser().getRepeat());
            statsNode.put("shuffle", user.getUser().getShuffle());
            user.getUser().setPaused(true);
            statsNode.put("paused", user.getUser().getPaused());
        }
        commandPromptNode.set("stats", statsNode);
        user.getUser().setTimestamp(commandInput.getTimestamp());
    }
}
