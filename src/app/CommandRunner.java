package app;

import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.type.Host;
import app.user.type.HostStuff.Announcement;
import app.user.type.User;
import app.user.type.Artist;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CommandRunner {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Performs a search based on the given command input.
     *
     * @param commandInput The input containing search parameters.
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        filters.setDescription(commandInput.getDescription());
        String type = commandInput.getType();

        if (user == null) {
            return null;
        }
        ArrayList<String> results = new ArrayList<>();
        String message;
        if (user.getConnectionStatus() == Enums.ConnectionStatus.OFFLINE) {
            message = user.getUsername() + " is offline.";
        } else {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", OBJECT_MAPPER.valueToTree(results));
        return objectNode;

    }

    /**
     * Selects an item based on the given command input.
     *
     * @param commandInput The input containing selection parameters.
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            return null;
        }
        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Loads an item based on the given command input.
     *
     * @param commandInput The input containing load parameters.
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.load();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Performs a play/pause operation based on the given command input.
     *
     * @param commandInput The input containing play/pause parameters.
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Performs a repeat operation based on the given command input.
     *
     * @param commandInput The input containing repeat parameters.
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.repeat();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Performs a shuffle operation based on the given command input.
     *
     * @param commandInput The input containing shuffle parameters.
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Performs a forward operation based on the given command input.
     *
     * @param commandInput The input containing forward parameters.
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.forward();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Performs a backward operation based on the given command input.
     *
     * @param commandInput The input containing backward parameters.
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Performs a like operation based on the given command input.
     *
     * @param commandInput The input containing like parameters.
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        String message;
        if (user.getConnectionStatus() == Enums.ConnectionStatus.OFFLINE) {
            message = user.getUsername() + " is offline.";
        } else {
            message = user.like();
        }
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Skips to the next item in the playlist based on the given command input.
     *
     * @param commandInput The input containing next parameters.
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Returns to the previous item in the playlist based on the given command input.
     *
     * @param commandInput The input containing previous parameters.
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Creates a new playlist based on the given command input.
     *
     * @param commandInput The input containing create playlist parameters.
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds or removes playlist based on the given command input.
     *
     * @param commandInput The input containing add/remove in playlist parameters.
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switches the visibility of a playlist based on the given command input.
     *
     * @param commandInput The input containing switch visibility parameters.
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Displays the user's playlists based on the given command input.
     *
     * @param commandInput The input containing show playlists parameters.
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follows playlists based on the given command input.
     *
     * @param commandInput The input containing follow parameters.
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }
        String message = user.follow();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Displays the current status and statistics of the user's player.
     *
     * @param commandInput The input containing status parameters.
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            return null;
        }

        PlayerStats stats = user.getPlayerStats();
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", OBJECT_MAPPER.valueToTree(stats));
        return objectNode;
    }

    /**
     * Displays the user's liked songs based on the given command input.
     *
     * @param commandInput The input containing show liked songs parameters.
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(songs));

        return objectNode;
    }

    /**
     * Retrieves the preferred genre of the user.
     *
     * @param commandInput The input containing parameters for retrieving the preferred genre.
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Retrieves the top 5 songs in the application.
     *
     * @param commandInput The input containing parameters for retrieving top songs.
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(songs));

        return objectNode;
    }

    /**
     * Retrieves the top 5 playlists in the application.
     *
     * @param commandInput The input containing parameters for retrieving top playlists.
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Switches the connection status (online/offline) of the user.
     *
     * @param commandInput The input containing parameters for switching the connection status.
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.switchConnectionStatus();
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves a list of online users in the application.
     *
     * @param commandInput The input containing parameters for retrieving online users.
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = Admin.getOnlineUsers();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(onlineUsers));

        return objectNode;
    }

    /**
     * Adds a user to the application.
     *
     * @param commandInput The input containing parameters for adding a user.
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = Admin.addUser(commandInput);

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds an album for the artist based on the provided command input.
     *
     * @param commandInput The CommandInput containing album details and artist information.
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        Artist artist = Admin.getArtist(commandInput.getUsername());
        String message;
        if (artist == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = artist.addAlbum(commandInput);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves and formats the list of albums for the artist.
     *
     * @param commandInput The CommandInput containing user information.
     * @return An ObjectNode containing information about the operation result.
     *         The result includes the command, user, timestamp, and a formatted list of albums.
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Artist artist = Admin.getArtist(commandInput.getUsername());
        assert artist != null;
        List<Album> albums = artist.getAlbums();

        ArrayNode resultAlbums = OBJECT_MAPPER.createArrayNode();

        for (Album album : albums) {
            ObjectNode albumObjectNode = OBJECT_MAPPER.createObjectNode();
            albumObjectNode.put("name", album.getName());

            ArrayNode songsArrayNode = OBJECT_MAPPER.createArrayNode();
            for (Song song: album.getSongs()) {
                songsArrayNode.add(song.getName());
            }

            albumObjectNode.putArray("songs").addAll(songsArrayNode);

            resultAlbums.add(albumObjectNode);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(resultAlbums));

        return objectNode;
    }

    /**
     * Retrieves and formats the current page information for the user.
     *
     * @param commandInput The CommandInput containing user information.
     * @return An ObjectNode containing information about the operation result.
     *         The result includes the user, command, timestamp, and a message
     *         with the current page information.
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message;
        if (user.getConnectionStatus() == Enums.ConnectionStatus.OFFLINE) {
            message = user.getUsername() + " is offline.";
        } else {
            message = user.printCurrentPage();
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds a new event for the artist user.
     *
     * @param commandInput The CommandInput with event details.
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!Objects.equals(user.getUserType(), "artist")) {
            message = user.getUsername() + " is not an artist.";
        } else {
            Artist artist = Admin.getArtist(commandInput.getUsername());
            message = artist.addEvent(commandInput);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    /**
     * Adds new merchandise for the artist user.
     *
     * @param commandInput The CommandInput with merchandise details.
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!Objects.equals(user.getUserType(), "artist")) {
            message = user.getUsername() + " is not an artist.";
        } else {
            Artist artist = Admin.getArtist(commandInput.getUsername());
            message = artist.addMerch(commandInput);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves a list of all users in the application.
     *
     * @param commandInput The input containing parameters for retrieving all users.
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> allUsers = Admin.getAllUsers();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(allUsers));

        return objectNode;
    }

    /**
     * Deletes a user based on the provided command input.
     *
     * @param commandInput The command input containing the username.
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = Admin.deleteUser(user);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds a podcast to the host's collection based on the provided command input.
     *
     * @param commandInput The command input containing podcast information.
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!Objects.equals(user.getUserType(), "host")) {
            message = user.getUsername() + " is not a host.";
        } else {
            Host host = Admin.getHost(commandInput.getUsername());
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            message = host.addPodcast(new Podcast(commandInput.getName(),
                    commandInput.getUsername(), episodes));
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds an announcement to the host's collection based on the provided command input.
     *
     * @param commandInput The command input containing announcement information.
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!Objects.equals(user.getUserType(), "host")) {
            message = user.getUsername() + " is not a host.";
        } else {
            Host host = Admin.getHost(commandInput.getUsername());
            message = host.addAnnouncement(new Announcement(commandInput.getName(),
                    commandInput.getDescription()));
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes an announcement from the host's collection based on the provided command input.
     *
     * @param commandInput The command input containing the announcement name.
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!Objects.equals(user.getUserType(), "host")) {
            message = user.getUsername() + " is not a host.";
        } else {
            Host host = Admin.getHost(commandInput.getUsername());
            message = host.removeAnnouncement(commandInput.getName());
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves and formats the podcasts of a host based on the provided command input.
     *
     * @param commandInput The command input containing the username.
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {

        Host host = Admin.getHost(commandInput.getUsername());
        List<Podcast> podcasts = host.showPodcasts();
        ArrayNode jsonArray = OBJECT_MAPPER.createArrayNode();

        for (Podcast podcast : podcasts) {
            ObjectNode podcastObject = OBJECT_MAPPER.createObjectNode();
            podcastObject.put("name", podcast.getName());

            ArrayNode episodesArray = OBJECT_MAPPER.createArrayNode();
            for (Episode episode : podcast.getEpisodes()) {
                episodesArray.add(episode.getName());
            }

            podcastObject.putArray("episodes").addAll(episodesArray);

            jsonArray.add(podcastObject);
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(jsonArray));

        return objectNode;
    }
}
