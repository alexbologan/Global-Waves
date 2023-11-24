package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.Song;
import user.Playlist;
import user.User;

public final class ShowPlaylists {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retrieves and displays the playlists of the user, adding the result to the specified
     * ObjectNode for use in the command prompt.
     *
     * @param user              The user for whom to show playlists.
     * @param commandPromptNode The ObjectNode to which the result will be added.
     */
    public void performShowPlayList(final User user, final ObjectNode commandPromptNode) {
        if (user.getUser().getMatchingPlayLists() != null) {
            for (Playlist playlist : user.getPlayLists()) {
                ArrayNode resultArray = objectMapper.createArrayNode();

                ObjectNode resultObject = objectMapper.createObjectNode();
                resultObject.put("name", playlist.getName());

                ArrayNode songsArray = objectMapper.createArrayNode();
                if (playlist.getSongs() != null) {
                    for (Song song : playlist.getSongs()) {
                        songsArray.add(song.getName());
                    }
                }
                resultObject.set("songs", songsArray);
                resultObject.put("visibility", playlist.getVisibility());
                resultObject.put("followers", playlist.getFollowers());

                resultArray.add(resultObject);

                commandPromptNode.set("result", resultArray);
            }
        }
    }
}
