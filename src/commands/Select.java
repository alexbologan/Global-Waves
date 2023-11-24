package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Podcast;
import input.files.Song;
import user.Playlist;
import user.User;

import java.util.ArrayList;


public final class Select {
    /**
     * Performs the "select" command based on the specified command input and updates the provided
     * JSON object node with the result of the selection.
     *
     * @param commandInput   The input containing the command, item number, and so on.
     * @param user           The user associated with the command.
     * @param commandPromptNode  The JSON object node where the selection result will be stored.
     */
    public void performSelect(final CommandInput commandInput, final User user,
                              final ObjectNode commandPromptNode) {
        if (user.getUser().getMatchingSongs() == null) {
            if (user.getUser().getMatchingPlayLists() == null) {
                if (user.getUser().getMatchingPodcasts() == null) {
                    commandPromptNode.put("message", "Fu");
                } else if (commandInput.getItemNumber() > user.getUser().getMatchingPodcasts()
                        .size()) {
                    commandPromptNode.put("message", "The selected ID is too high.");
                } else {
                    commandPromptNode.put("message", "Successfully selected "
                            + user.getUser().getMatchingPodcasts()
                            .get(commandInput.getItemNumber() - 1).getName() + ".");
                    ArrayList<Podcast> podcasts = new ArrayList<>();
                    podcasts.add(user.getUser().getMatchingPodcasts().get(
                            commandInput.getItemNumber() - 1));
                    commandInput.setMatchingPodcasts(podcasts);

                }
            } else if (commandInput.getItemNumber() > user.getUser().getMatchingPlayLists()
                                                                                    .size()) {
                commandPromptNode.put("message", "The selected ID is too high.");
            } else {
                commandPromptNode.put("message", "Successfully selected "
                        + user.getUser().getMatchingPlayLists()
                        .get(commandInput.getItemNumber() - 1).getName() + ".");
                ArrayList<Playlist> playlists = new ArrayList<>();
                playlists.add(user.getUser().getMatchingPlayLists().get(
                        commandInput.getItemNumber() - 1));

                commandInput.setMatchingPlayLists(playlists);
            }
        } else if (commandInput.getItemNumber() > user.getUser().getMatchingSongs().size()) {
            commandPromptNode.put("message", "The selected ID is too high.");
        } else {
            commandPromptNode.put("message", "Successfully selected "
                    + user.getUser().getMatchingSongs().get(commandInput.getItemNumber() - 1)
                    .getName() + ".");
            ArrayList<Song> selectedSong = new ArrayList<>();
            selectedSong.add(user.getUser().getMatchingSongs().get(
                    commandInput.getItemNumber() - 1));
            commandInput.setMatchingSongs(selectedSong);
        }
        user.setUser(commandInput);
    }
}
