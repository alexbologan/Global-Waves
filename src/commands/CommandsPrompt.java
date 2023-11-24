package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.Library;
import user.User;
import input.files.CommandInput;
import java.io.IOException;
import java.util.Objects;


public final class CommandsPrompt {

    /**
     * Processes the specified command input, performs the corresponding action based on the
     * command type, and updates the provided JSON object node with the results of the command.
     *
     * @param commandInput   The input containing the command, username, timestamp and so on.
     * @param user           The user associated with the command.
     * @param commandPromptNode  The JSON object node where command results will be stored.
     * @throws IOException   If an IO error occurs during the command processing.
     */
    public void commands(final CommandInput commandInput, final User user,
                         final ObjectNode commandPromptNode, final Library library)
            throws IOException {
        commandPromptNode.put("command", commandInput.getCommand());
        commandPromptNode.put("user", commandInput.getUsername());
        commandPromptNode.put("timestamp", commandInput.getTimestamp());
        if (Objects.equals(commandInput.getCommand(), "search")) {
            Search search = new Search();
            search.performSearch(commandInput, commandPromptNode, user, library);
        } else if (Objects.equals(commandInput.getCommand(), "select")) {
            Select select = new Select();
            select.performSelect(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "load")) {
            Load load = new Load();
            load.performLoad(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "status")) {
            Status status = new Status();
            status.performStatus(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "playPause")) {
            PlayPause playPause = new PlayPause();
            playPause.performPlayPause(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "createPlaylist")) {
            CreatePlayList createPlayList = new CreatePlayList();
            createPlayList.performCreatePlayList(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "addRemoveInPlaylist")) {
            AddRemPlayList addRemPlayList = new AddRemPlayList();
            addRemPlayList.performAddRemPlayList(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "like")) {
            Like like = new Like();
            like.performLike(commandInput, user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "showPlaylists")) {
            ShowPlaylists showPlaylists = new ShowPlaylists();
            showPlaylists.performShowPlayList(user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "showPreferredSongs")) {
            ShowPreferredSongs showPreferredSongs = new ShowPreferredSongs();
            showPreferredSongs.performShowPrefSongs(user, commandPromptNode);
        } else if (Objects.equals(commandInput.getCommand(), "repeat")) {
            Repeat showPreferredSongs = new Repeat();
            showPreferredSongs.performRepeat(user, commandPromptNode, commandInput);
        } else if (Objects.equals(commandInput.getCommand(), "shuffle")) {
            Shuffle shuffle = new Shuffle();
            shuffle.performShuffle(user, commandPromptNode, commandInput);
        }
    }
}
