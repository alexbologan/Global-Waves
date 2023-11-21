package searchbarcommands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.User;

import java.util.ArrayList;


public final class SelectCommand {
    /**
     * Performs the "select" command based on the specified command input and updates the provided
     * JSON object node with the result of the selection.
     *
     * @param commandInput   The input containing the command, item number, and so on.
     * @param user           The user associated with the command.
     * @param commandPromptNode  The JSON object node where the selection result will be stored.
     */
    public void performSelect(final CommandInput commandInput, final User user,
                              final ObjectNode commandPromptNode) {
        if (user.getUser().getMatchingSong() == null) {
            commandPromptNode.put("message", "Fu");
        } else if (commandInput.getItemNumber() > user.getUser().getMatchingSong().size()) {
            commandPromptNode.put("message", "The selected ID is too high.");
        } else {
            commandPromptNode.put("message", "Successfully selected "
                    + user.getUser().getMatchingSong().get(commandInput.getItemNumber() - 1)
                    .getName() + ".");
            ArrayList<Song> selectedSong = new ArrayList<>();
            selectedSong.add(user.getUser().getMatchingSong().get(
                    commandInput.getItemNumber() - 1));
            commandInput.setMatchingSong(selectedSong);
        }
        user.setUser(commandInput);
    }
}
