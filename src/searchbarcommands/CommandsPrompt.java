package searchbarcommands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import user.User;
import input.files.CommandInput;
import java.io.IOException;
import java.util.Objects;


public final class CommandsPrompt {

    /**
     * Processes the specified command input, performs the corresponding action based on the
     * command type, and updates the provided JSON object node with the results of the command.
     *
     * @param commandInput   The input containing the command, username, timestamp and so on.
     * @param user           The user associated with the command.
     * @param commandPromptNode  The JSON object node where command results will be stored.
     * @throws IOException   If an IO error occurs during the command processing.
     */
    public void commands(final CommandInput commandInput, final User user,
                         final ObjectNode commandPromptNode) throws IOException {
        commandPromptNode.put("command", commandInput.getCommand());
        commandPromptNode.put("user", commandInput.getUsername());
        commandPromptNode.put("timestamp", commandInput.getTimestamp());
        if (Objects.equals(commandInput.getCommand(), "search")) {
            SearchCommand searchCommand = new SearchCommand();
            searchCommand.performSearch(commandInput, commandPromptNode, user);
        } else if (Objects.equals(commandInput.getCommand(), "select")) {
            SelectCommand selectCommand = new SelectCommand();
            selectCommand.performSelect(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "load")) {
            LoadCommand loadCommand = new LoadCommand();
            loadCommand.performLoad(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "status")) {
            StatusCommand statusCommand = new StatusCommand();
            statusCommand.performStatus(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "playPause")) {
            PlayPauseCommand playPauseCommand = new PlayPauseCommand();
            playPauseCommand.performPlayPause(commandInput, user, commandPromptNode);
        }
    }
}
