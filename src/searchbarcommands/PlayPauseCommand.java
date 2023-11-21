package searchbarcommands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;

import java.util.Objects;

public final class PlayPauseCommand {
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
