package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import user.Playlist;
import user.User;

import java.util.Objects;

public final class AddRemPlayList {
    /**
     * Performs the addition or removal of a song to/from a playlist based on the user's command.
     *
     * This method checks if the user has loaded a song source using the "load" command. If a
     * song source is loaded, it then checks if the specified playlist exists. If the playlist
     * exists, it adds or removes the currently matching song from the playlist accordingly.
     *
     * @param commandInput      The input parameters for the command, including the playlist ID.
     * @param user              The user for whom the playlist modification is performed.
     * @param commandPromptNode The JSON node to store the result message.
     */
    public void performAddRemPlayList(final CommandInput commandInput, final User user,
                                      final ObjectNode commandPromptNode) {
        if (Objects.equals(user.getUser().getCommand(), "load")) {
            if (user.getUser().getMatchingSongs() == null
                    || user.getUser().getMatchingSongs().isEmpty()) {
                commandPromptNode.put("message", "The loaded source is not a song.");
            } else {
                boolean playListExists = false;
                Playlist thePlaylist = new Playlist();
                if (user.getPlayLists() != null) {
                    for (Playlist playlist : user.getPlayLists()) {
                        if (Objects.equals(playlist.getId(), commandInput.getPlaylistId())) {
                            playListExists = true;
                            thePlaylist = playlist;
                            break;
                        }
                    }
                }
                if (playListExists) {
                    if (thePlaylist.getSongs().contains(user.getUser().getMatchingSongs().get(0))) {
                        thePlaylist.getSongs().remove(user.getUser().getMatchingSongs().get(0));
                        thePlaylist.setDuration(thePlaylist.getDuration()
                                - user.getUser().getMatchingSongs().get(0).getDuration());
                        commandPromptNode.put("message", "Successfully removed from playlist.");

                    } else {
                        thePlaylist.getSongs().add(user.getUser().getMatchingSongs().get(0));
                        thePlaylist.setDuration(thePlaylist.getDuration()
                                + user.getUser().getMatchingSongs().get(0).getDuration());
                        commandPromptNode.put("message", "Successfully added to playlist.");
                    }
                } else {
                    commandPromptNode.put("message", "The specified playlist does not exist.");
                }
            }
        } else {
            commandPromptNode.put("message", "Please load a source before adding to or removing"
                                                            + " from the playlist.");
        }
    }
}
