package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;

import java.util.Objects;

public final class Load {
    /**
     * Performs the loading of a playback based on the user's command and selected source.
     *
     * This method checks if the user has previously selected a source using the "select" command.
     * If a source is selected, it indicates that the playback has been loaded successfully.
     * If no source is selected, it prompts the user to select a source before attempting to load.
     * The method also updates the user's command, timestamp, and remaining time based on the
     * selected source, if available.
     *
     * @param commandInput      The input parameters for the command.
     * @param user              The user for whom the playback is loaded.
     * @param commandPromptNode The JSON node to store the result message.
     */
    public void performLoad(final CommandInput commandInput, final User user,
                            final ObjectNode commandPromptNode) {
        if (Objects.equals(user.getUser().getCommand(), "select")) {
            commandPromptNode.put("message", "Playback loaded successfully.");
            user.getUser().setCommand(commandInput.getCommand());
            user.getUser().setTimestamp(commandInput.getTimestamp());
            if (user.getUser().getMatchingSongs() != null
                    && !user.getUser().getMatchingSongs().isEmpty()) {
                user.getUser().setRemainedTime(user.getUser().getMatchingSongs()
                        .get(0).getDuration());
            } else if (user.getUser().getMatchingPlayLists() != null
                    && !user.getUser().getMatchingPlayLists().isEmpty()) {
                user.getUser().setRemainedTime(user.getUser()
                        .getMatchingPlayLists().get(0).getDuration());
            } else if (user.getUser().getMatchingPodcasts() != null
                    && !user.getUser().getMatchingPodcasts().isEmpty()) {
                if (user.getUser().getMatchingPodcasts().get(0).getRemainedTime() != 0) {
                    user.getUser().setRemainedTime(user.getUser().getMatchingPodcasts().get(0)
                            .getRemainedTime());
                } else {
                    user.getUser().setRemainedTime(user.getUser().getMatchingPodcasts().get(0)
                            .getDuration());
                }
            }
        } else {
            commandPromptNode.put("message", "Please select a source before attempting to load.");
        }

    }
}
