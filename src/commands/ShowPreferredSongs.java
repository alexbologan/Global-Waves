package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.Song;
import user.User;

public final class ShowPreferredSongs {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retrieves and displays the preferred songs of the user, adding the result to the specified
     * ObjectNode for use in the command prompt.
     *
     * @param user              The user for whom to show preferred songs.
     * @param commandPromptNode The ObjectNode to which the result will be added.
     */
    public void performShowPrefSongs(final User user, final ObjectNode commandPromptNode) {
        if (user.getLikedSongs() != null) {
            ArrayNode songsArray = objectMapper.createArrayNode();
            for (Song song : user.getLikedSongs()) {
                songsArray.add(song.getName());
            }
            commandPromptNode.set("result", songsArray);
        }
    }
}
