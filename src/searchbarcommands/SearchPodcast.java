package searchbarcommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Library;
import input.files.Podcast;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public final class SearchPodcast extends SearchCommand {

    public SearchPodcast(final Library library) throws IOException {
        super(library);
    }

    /**
     * Performs a podcast search based on the specified command input and filters.
     *
     * @param commandInput   The input containing search filters.
     * @param commandPromptNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     */
    public void performSearchPodcast(final CommandInput commandInput,
                                     final ObjectNode commandPromptNode, final User user) {
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
            commandPromptNode.put("message", "Search returned 5 results");

            for (int i = 0; i < maxSize; ++i) {
                stringMatchingPodcasts.add(matchingPodcasts.get(i).getName());
            }
        } else {
            commandPromptNode.put("message", "Search returned "
                    + matchingPodcasts.size() + " results");

            for (Podcast matchingPodcast : matchingPodcasts) {
                stringMatchingPodcasts.add(matchingPodcast.getName());
            }
        }
        ArrayNode matchingPodcastsNode =
                getObjectMapper().valueToTree(stringMatchingPodcasts);
        commandPromptNode.set("results", matchingPodcastsNode);
        user.getUser().setMatchingPodcast(matchingPodcasts);
    }
}
