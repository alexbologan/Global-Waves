package commands.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.Playlist;
import user.User;
import java.util.ArrayList;
import java.util.Objects;

public final class PlayList {

    /**
     * Performs a search operation on user playlists based on the provided filters.
     * Populates the matching playlists and updates the user's matching playlists accordingly.
     *
     * This method supports searching playlists by name and owner. It applies filters sequentially
     * and provides search results with a limit of 5 playlists. The results are stored in a JSON
     * node for the command prompt, and the matching playlists are stored in the user's state.
     *
     * @param commandInput      The input parameters for the command.
     * @param commandPromptNode The JSON node to store the result message and matching playlists.
     * @param user              The user for whom the playlist search is performed.
     */
    public void performSearchPlayList(final CommandInput commandInput,
                                      final ObjectNode commandPromptNode, final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Playlist> matchingPlaylists = new ArrayList<>();
        ArrayList<Playlist> newMatchingPlaylists = new ArrayList<>();
        int filterNum = 0;
        final int maxSize = 5;
        if (commandInput.getFilters().getName() != null && user.getPlayLists() != null) {
            for (int i = 0; i < user.getPlayLists().size(); i++) {
                if (user.getPlayLists().get(i).getName().startsWith(
                        commandInput.getFilters().getName())) {
                    matchingPlaylists.add(user.getPlayLists().get(i));
                }
            }
            filterNum++;
        }

        if (commandInput.getFilters().getOwner() != null && user.getPlayLists() != null) {
            if (matchingPlaylists.isEmpty() && filterNum == 0) {
                for (int i = 0; i < user.getPlayLists().size(); i++) {
                    if (Objects.equals(user.getPlayLists().get(i).getOwner(),
                            commandInput.getFilters().getOwner())) {
                        matchingPlaylists.add(user.getPlayLists().get(i));
                    }
                }
            } else if (!matchingPlaylists.isEmpty()) {
                for (Playlist matchingPlaylist : matchingPlaylists) {
                    if (Objects.equals(matchingPlaylist.getOwner(),
                            commandInput.getFilters().getOwner())) {
                        newMatchingPlaylists.add(matchingPlaylist);
                    }
                }
                matchingPlaylists = newMatchingPlaylists;
            }
        }
        ArrayList<String> stringMatchingPlayLists = new ArrayList<>();
        if (matchingPlaylists.size() > maxSize) {
            commandPromptNode.put("message", "Search returned 5 results");

            for (int i = 0; i < maxSize; ++i) {
                stringMatchingPlayLists.add(matchingPlaylists.get(i).getName());
            }
        } else {
            commandPromptNode.put("message", "Search returned "
                    + matchingPlaylists.size() + " results");

            for (Playlist matchingPlaylist : matchingPlaylists) {
                stringMatchingPlayLists.add(matchingPlaylist.getName());
            }
        }
        ArrayNode matchingPlayListsNode =
                objectMapper.valueToTree(stringMatchingPlayLists);
        commandPromptNode.set("results", matchingPlayListsNode);
        user.getUser().setMatchingPlayLists(matchingPlaylists);
    }
}
