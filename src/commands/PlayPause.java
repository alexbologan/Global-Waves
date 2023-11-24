package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;

import java.util.Objects;

public final class PlayPause {
    /**
     * Performs the play or pause operation on the current playback based on the user's command.
     *
     * This method checks the current playback state to determine whether to pause or resume
     * the playback. If the playback is currently not paused, it calculates the remaining time
     * and updates the playback state to paused. If the playback is already paused, it resumes
     * the playback. The method also updates the timestamp to reflect the time of the play/pause
     * operation.
     *
     * @param commandInput      The input parameters for the command, including the timestamp.
     * @param user              The user for whom the play/pause operation is performed.
     * @param commandPromptNode The JSON node to store the result message.
     */
    public void performPlayPause(final CommandInput commandInput, final User user,
                                 final ObjectNode commandPromptNode) {

        if (Objects.equals(user.getUser().getPaused(), false)) {
            user.getUser().setRemainedTime(user.getUser().getRemainedTime()
                    - commandInput.getTimestamp() + user.getUser().getTimestamp());
            user.getUser().setPaused(true);
            commandPromptNode.put("message", "Playback paused successfully.");
        } else {
            user.getUser().setPaused(false);
            commandPromptNode.put("message", "Playback resumed successfully.");
        }
        user.getUser().setTimestamp(commandInput.getTimestamp());
    }
}
