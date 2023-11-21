package searchbarcommands;

import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Library;
import input.files.Podcast;
import user.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
     * Performs a song search based on the provided filters and updates the search bar node.
     *
     * @param searchBarNode   The ObjectNode representing the search bar.
     */
    public void performSearch(final CommandInput commandInput, final ObjectNode searchBarNode,
                              final User user) throws IOException {
        if (Objects.equals(commandInput.getType(), "song")) {
            SearchSong searchSong = new SearchSong(library);
            searchSong.performSearchSong(commandInput, searchBarNode, user);
        } else if (Objects.equals(commandInput.getType(), "podcast")) {
            ArrayList<Podcast> matchingPodcasts = new ArrayList<>();
            ArrayList<Podcast> newMatchingPodcasts = new ArrayList<>();
            int filterNum = 0;
            final int maxSize = 5;
            if (commandInput.getFilters().getName() != null) {
                for (int i = 0; i < getLibrary().getPodcasts().size(); i++) {
                    if (getLibrary().getPodcasts().get(i).getName().startsWith(
                            commandInput.getFilters().getName())) {
                        matchingPodcasts.add(getLibrary().getPodcasts().get(i));
                    }
                }
                filterNum++;
            }

            if (commandInput.getFilters().getOwner() != null) {
                if (matchingPodcasts.isEmpty() && filterNum == 0) {
                    for (int i = 0; i < getLibrary().getPodcasts().size(); i++) {
                        if (Objects.equals(getLibrary().getPodcasts().get(i).getOwner(),
                                commandInput.getFilters().getOwner())) {
                            matchingPodcasts.add(getLibrary().getPodcasts().get(i));
                        }
                    }
                } else if (!matchingPodcasts.isEmpty()) {
                    for (Podcast matchingPodcast :  matchingPodcasts) {
                        if (Objects.equals(matchingPodcast.getOwner(),
                                commandInput.getFilters().getOwner())) {
                            newMatchingPodcasts.add(matchingPodcast);
                        }
                    }
                    matchingPodcasts = newMatchingPodcasts;
                }
            }
            ArrayList<String> stringMatchingPodcasts = new ArrayList<>();
            if (matchingPodcasts.size() > maxSize) {
                searchBarNode.put("message",
                        "Search returned " + maxSize + " results");
                for (int i = 0; i < maxSize; i++) {
                    stringMatchingPodcasts.add(matchingPodcasts.get(i).getName());
                }
            } else {
                searchBarNode.put("message",
                        "Search returned " + matchingPodcasts.size() + " results");
                for (Podcast matchingPodcast : matchingPodcasts) {
                    stringMatchingPodcasts.add(matchingPodcast.getName());
                }
            }
            ArrayNode matchingPodcastsNode =
                    getObjectMapper().valueToTree(stringMatchingPodcasts);
            searchBarNode.set("results", matchingPodcastsNode);
            user.getUser().setMatchingSongTitles(stringMatchingPodcasts);
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
