package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Song;
import user.Playlist;
import user.User;

import java.util.ArrayList;
import java.util.Objects;

public final class CreatePlayList {
    /**
     * Creates a new playlist based on the provided {@code commandInput} and adds it to the user's
     * list of playlists. If the user has no playlists, a new list is created.
     *
     * @param commandInput      The input containing information about the playlist to be created.
     * @param user              The user for whom the playlist is being created.
     * @param commandPromptNode The ObjectNode to which the result message will be added.
     */
    public void performCreatePlayList(final CommandInput commandInput, final User user,
                                      final ObjectNode commandPromptNode) {
        if (user.getPlayLists() == null || user.getPlayLists().isEmpty()) {
            ArrayList<Playlist> playlists = new ArrayList<>();
            Playlist playlist = new Playlist();
            ArrayList<Song> songs = new ArrayList<>();
            playlist.setName(commandInput.getPlaylistName());
            playlist.setId(1);
            playlist.setSongs(songs);
            playlist.setOwner(commandInput.getUsername());
            playlists.add(playlist);
            user.setPlayLists(playlists);
            commandPromptNode.put("message", "Playlist created successfully.");
        } else {
            boolean playListExists = false;
            for (Playlist playlist : user.getPlayLists()) {
                if (Objects.equals(playlist.getName(), commandInput.getPlaylistName())) {
                    playListExists = true;
                    break;
                }
            }
            if (playListExists) {
                commandPromptNode.put("message", "A playlist with the same name already exists.");
            } else {
                Playlist playlist = new Playlist();
                ArrayList<Song> songs = new ArrayList<>();
                playlist.setName(commandInput.getPlaylistName());
                playlist.setId(user.getPlayLists().size() + 1);
                playlist.setSongs(songs);
                playlist.setOwner(commandInput.getUsername());
                user.getPlayLists().add(playlist);
                commandPromptNode.put("message", "Playlist created successfully.");
            }
        }
    }
}
