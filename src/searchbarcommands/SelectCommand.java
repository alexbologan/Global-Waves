package searchbarcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.User;


public final class SelectCommand {
    public void performSelect(final CommandInput commandInput, final ObjectMapper objectMapper,
                              final User user,
                              final ObjectNode searchBarNode) {
        if (user.getUser().getMatchingSongTitles() == null) {
            searchBarNode.put("message", "Fu");
        } else if (commandInput.getItemNumber() > user.getUser().getMatchingSongTitles().size()) {
            searchBarNode.put("message", "The selected ID is too high.");
        } else {
            searchBarNode.put("message", "Successfully selected "
                    + user.getUser().getMatchingSongTitles().get(commandInput.getItemNumber() - 1)
                    + ".");
        }
    }
}
