package commands.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Episode;
import input.files.Library;
import user.User;

import java.util.ArrayList;
import java.util.Objects;

public final class Podcast {

    /**
     * Performs a podcast search based on the specified command input and filters.
     *
     * @param commandInput   The input containing search filters.
     * @param commandPromptNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     */
    public void performSearchPodcast(final CommandInput commandInput, final Library library,
                                     final ObjectNode commandPromptNode, final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<input.files.Podcast> matchingPodcasts = new ArrayList<>();
        ArrayList<input.files.Podcast> newMatchingPodcasts = new ArrayList<>();
        int filterNum = 0;
        final int maxSize = 5;
        if (commandInput.getFilters().getName() != null) {
            for (int i = 0; i < library.getPodcasts().size(); i++) {
                if (library.getPodcasts().get(i).getName().startsWith(
                        commandInput.getFilters().getName())) {
                    matchingPodcasts.add(library.getPodcasts().get(i));
                }
            }
            filterNum++;
        }

        if (commandInput.getFilters().getOwner() != null) {
            if (matchingPodcasts.isEmpty() && filterNum == 0) {
                for (int i = 0; i < library.getPodcasts().size(); i++) {
                    if (Objects.equals(library.getPodcasts().get(i).getOwner(),
                            commandInput.getFilters().getOwner())) {
                        matchingPodcasts.add(library.getPodcasts().get(i));
                    }
                }
            } else if (!matchingPodcasts.isEmpty()) {
                for (input.files.Podcast matchingPodcast :  matchingPodcasts) {
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
                int podcastDuration = 0;
                for (Episode episode : matchingPodcasts.get(i).getEpisodes()) {
                    podcastDuration += episode.getDuration();
                }
                matchingPodcasts.get(i).setDuration(podcastDuration);
            }
        } else {
            commandPromptNode.put("message", "Search returned "
                    + matchingPodcasts.size() + " results");

            for (input.files.Podcast matchingPodcast : matchingPodcasts) {
                stringMatchingPodcasts.add(matchingPodcast.getName());
                int podcastDuration = 0;
                for (Episode episode : matchingPodcast.getEpisodes()) {
                    podcastDuration += episode.getDuration();
                }
                matchingPodcast.setDuration(podcastDuration);
            }
        }
        ArrayNode matchingPodcastsNode =
                objectMapper.valueToTree(stringMatchingPodcasts);
        commandPromptNode.set("results", matchingPodcastsNode);
        user.getUser().setMatchingPodcasts(matchingPodcasts);
    }
}
