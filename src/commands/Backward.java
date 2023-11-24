package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Episode;
import user.User;

import java.util.Objects;

public final class Backward {
    /**
     * Performs the backward operation for the loaded podcast.
     *
     * This method handles rewinding the podcast playback based on the specified backward time.
     *
     * @param user              The user for whom the operation is performed.
     * @param commandPromptNode The JSON object representing the command prompt response.
     * @param commandInput      The input containing the timestamp and other relevant information.
     */
    public void performBackward(final User user, final ObjectNode commandPromptNode,
                               final CommandInput commandInput) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getMatchingPodcasts() != null) {
                final int backwardTime = 90;
                int timePassed = user.getUser().getRemainedTime()
                        - commandInput.getTimestamp() + user.getUser().getTimestamp();
                Episode findEpisode = new Episode();
                int timeDiff = user.getUser().getMatchingPodcasts().get(0).getDuration()
                        - timePassed;
                for (int i = 0; i < user.getUser().getMatchingPodcasts().get(0).getEpisodes()
                        .size(); i++) {
                    if (timeDiff < user.getUser().getMatchingPodcasts().get(0).getEpisodes().get(i)
                            .getDuration()) {
                        findEpisode = user.getUser().getMatchingPodcasts().get(0).getEpisodes()
                                .get(i);
                        break;
                    }
                    timeDiff -= user.getUser().getMatchingPodcasts().get(0).getEpisodes().get(i)
                            .getDuration();
                }
                if (Objects.equals(user.getUser().getRepeat(), "No Repeat")) {
                    if (timeDiff - backwardTime > 0) {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed + backwardTime);
                        commandPromptNode.put("message", "Rewound successfully.");
                    } else {
                        user.getUser().setTimestamp(commandInput.getTimestamp());
                        user.getUser().setRemainedTime(timePassed + timeDiff);
                        commandPromptNode.put("message", "Rewound successfully.");
                    }
                } else if (Objects.equals(user.getUser().getRepeat(), "Repeat Once")) {
                    user.getUser().setTimestamp(commandInput.getTimestamp());
                }
            } else {
                commandPromptNode.put("message", "The loaded source is not a podcast.");
            }
        } else {
            commandPromptNode.put("message", "Please select a source before rewinding.");
        }
    }
}
