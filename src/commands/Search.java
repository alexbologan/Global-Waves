package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.search.PlayList;
import commands.search.Podcast;
import commands.search.Song;
import input.files.CommandInput;
import input.files.Library;
import user.User;

import java.util.Objects;

public final class Search {

    /**
     * Performs a search based on the specified command input and delegates the search operation
     * to either the song or podcast search module, depending on the type specified in the input.
     *
     * @param commandInput   The input containing search filters and type information.
     * @param commandPromptNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     */
    public void performSearch(final CommandInput commandInput, final ObjectNode commandPromptNode,
                              final User user, final Library library) {
        if (Objects.equals(user.getUser().getCommand(), "load")
            && user.getUser().getMatchingPodcasts() != null) {
            for (input.files.Podcast podcast : library.getPodcasts()) {
                if (Objects.equals(podcast.getName(), user.getUser().getMatchingPodcasts().get(0)
                        .getName())) {
                    podcast.setRemainedTime(user.getUser()
                            .getRemainedTime() - commandInput.getTimestamp()
                            + user.getUser().getTimestamp());
                    break;
                }
            }
        }
        user.setUser(commandInput);
        if (Objects.equals(commandInput.getType(), "song")) {
            Song song = new Song();
            song.performSearchSong(commandInput, library, commandPromptNode, user);
        } else if (Objects.equals(commandInput.getType(), "podcast")) {
            Podcast podcast = new Podcast();
            podcast.performSearchPodcast(commandInput, library, commandPromptNode, user);
        } else if (Objects.equals(commandInput.getType(), "playlist")) {
            PlayList podcast = new PlayList();
            podcast.performSearchPlayList(commandInput, commandPromptNode, user);
        }
    }
}
