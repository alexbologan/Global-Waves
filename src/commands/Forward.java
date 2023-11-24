package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Episode;
import user.User;

import java.util.Objects;

public final class Forward {
    /**
     * Performs the forward operation for the loaded podcast.
     *
     * This method checks if a podcast is loaded, calculates the time difference, and
     * skips forward to the next episode based on the specified time.
     *
     * @param user The user for whom the forward operation is performed.
     * @param commandPromptNode The ObjectNode for command prompt messages.
     * @param commandInput The input containing timestamp and other command details.
     */
    public void performForward(final User user, final ObjectNode commandPromptNode,
                               final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getMatchingPodcasts() != null) {
                final int forwardTime = 90;
                int timePassed = user.getUser().getRemainedTime()
                        - commandInput.getTimestamp() + user.getUser().getTimestamp();
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
                    if (findEpisode.getDuration() - timeDiff - forwardTime > 0) {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed - forwardTime);
                        commandPromptNode.put("message", "Skipped forward successfully.");
                    }
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Once")) {
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            } else {
                commandPromptNode.put("message", "The loaded source is not a podcast.");
            }
        } else {
            commandPromptNode.put("message", "Please load a source before attempting to forward.");
        }
    }
}
