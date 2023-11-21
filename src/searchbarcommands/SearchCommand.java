package searchbarcommands;

import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Library;
import user.User;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SearchCommand {
    private final String myLibraryPath = CheckerConstants.TESTS_PATH + "library/library.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Library library = objectMapper.readValue(new File(myLibraryPath), Library.class);

    public SearchCommand() throws IOException {
    }

    public SearchCommand(final Library library) throws IOException {
        this.library = library;
    }

    /**
     * Performs a search based on the specified command input and delegates the search operation
     * to either the song or podcast search module, depending on the type specified in the input.
     *
     * @param commandInput   The input containing search filters and type information.
     * @param commandPromptNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     * @throws IOException   If an IO error occurs during the search operation.
     */
    public void performSearch(final CommandInput commandInput, final ObjectNode commandPromptNode,
                              final User user) throws IOException {
        user.setUser(commandInput);
        if (Objects.equals(commandInput.getType(), "song")) {
            SearchSong searchSong = new SearchSong(library);
            searchSong.performSearchSong(commandInput, commandPromptNode, user);
        } else if (Objects.equals(commandInput.getType(), "podcast")) {
            SearchPodcast searchPodcast = new SearchPodcast(library);
            searchPodcast.performSearchPodcast(commandInput, commandPromptNode, user);
        }
    }

    /**
     * Gets the ObjectMapper instance used for JSON serialization and deserialization.
     *
     * @return The ObjectMapper instance.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Gets the Library instance associated with this class.
     * Note: Ensure that you use this method safely and handle any null values appropriately.
     *
     * @return The Library instance.
     */
    public Library getLibrary() {
        return library;
    }
}
