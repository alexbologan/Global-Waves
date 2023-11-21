package searchbarcommands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;

import java.util.Objects;

public final class LoadCommand {
    public void performLoad(final CommandInput commandInput, final User user,
                            final ObjectNode commandPromptNode) {
        if (Objects.equals(user.getUser().getCommand(), "select")) {
            commandPromptNode.put("message", "Playback loaded successfully.");
        } else {
            commandPromptNode.put("message", "Please select a source before attempting to load.");
        }
        user.getUser().setCommand(commandInput.getCommand());
        user.getUser().setTimestamp(commandInput.getTimestamp());
        if (user.getUser().getMatchingSong() != null
                && !user.getUser().getMatchingSong().isEmpty()) {
            user.getUser().setRemainedTime(user.getUser().getMatchingSong().get(0).getDuration());
        }
    }
}
