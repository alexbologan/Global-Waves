package searchbarcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import user.User;
import input.files.CommandInput;
import java.io.IOException;
import java.util.Objects;


public final class SearchBarCommands {

    public void commands(final CommandInput commandInput, final ObjectMapper objectMapper,
                         final User user, final ObjectNode searchBarNode) throws IOException {
        searchBarNode.put("command", commandInput.getCommand());
        searchBarNode.put("user", commandInput.getUsername());
        searchBarNode.put("timestamp", commandInput.getTimestamp());
        if (Objects.equals(commandInput.getCommand(), "search")) {
            SearchCommand searchCommand = new SearchCommand();
            searchCommand.performSearch(commandInput, searchBarNode, user);
        } else if (Objects.equals(commandInput.getCommand(), "select")) {
            SelectCommand  selectCommand = new SelectCommand();
            selectCommand.performSelect(commandInput, objectMapper, user, searchBarNode);
        }
    }
}
