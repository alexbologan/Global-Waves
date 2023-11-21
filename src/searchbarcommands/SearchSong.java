package searchbarcommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Library;
import input.files.Song;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public final class SearchSong extends SearchCommand {

    public SearchSong(final Library library) throws IOException {
        super(library);
    }

    /**
     * Performs a song search based on the specified command input and filters.
     *
     * @param commandInput   The input containing search filters.
     * @param searchBarNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     */
    public void performSearchSong(final CommandInput commandInput, final ObjectNode searchBarNode,
                                  final User user) {
        ArrayList<Song> matchingSongs = new ArrayList<>();
        ArrayList<Song> newMatchingSongs = new ArrayList<>();
        int filterNum = 0;
        final int maxSize = 5;
        if (commandInput.getFilters().getName() != null) {
            for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                if (getLibrary().getSongs().get(i).getName().startsWith(
                        commandInput.getFilters().getName())) {
                    matchingSongs.add(getLibrary().getSongs().get(i));
                }
            }
            filterNum++;
        }
        if (commandInput.getFilters().getAlbum() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    if (Objects.equals(getLibrary().getSongs().get(i).getAlbum(),
                            commandInput.getFilters().getAlbum())) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    if (Objects.equals(matchingSong.getAlbum(),
                            commandInput.getFilters().getAlbum())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getTags() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    if (verifyValues(commandInput.getFilters().getTags(),
                            getLibrary().getSongs().get(i).getTags())) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    if (verifyValues(commandInput.getFilters().getTags(),
                            matchingSong.getTags())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getLyrics() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    if (getLibrary().getSongs().get(i).getLyrics().contains(
                            commandInput.getFilters().getLyrics())) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    if (matchingSong.getLyrics().contains(
                            commandInput.getFilters().getLyrics())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getArtist() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    if (Objects.equals(getLibrary().getSongs().get(i).getArtist(),
                            commandInput.getFilters().getArtist())) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    if (Objects.equals(matchingSong.getArtist(),
                            commandInput.getFilters().getArtist())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getReleaseYear() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    char operator = commandInput.getFilters().getReleaseYear().charAt(0);
                    int value = Integer.parseInt(
                            commandInput.getFilters().getReleaseYear().substring(1));
                    if (compare(operator, getLibrary().getSongs().get(i).getReleaseYear(), value)) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    char operator = commandInput.getFilters().getReleaseYear().charAt(0);
                    int value = Integer.parseInt(
                            commandInput.getFilters().getReleaseYear().substring(1));
                    if (compare(operator, matchingSong.getReleaseYear(), value)) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getGenre() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < getLibrary().getSongs().size(); i++) {
                    if (Objects.equals(getLibrary().getSongs().get(i).getGenre().toLowerCase(),
                            commandInput.getFilters().getGenre())) {
                        matchingSongs.add(getLibrary().getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (Song matchingSong : matchingSongs) {
                    if (Objects.equals(matchingSong.getGenre(),
                            commandInput.getFilters().getGenre())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }
        ArrayList<String> stringMatchingSongs = new ArrayList<>();
        if (matchingSongs.size() > maxSize) {
            searchBarNode.put("message",
                    "Search returned " + maxSize + " results");
            for (int i = 0; i < maxSize; i++) {
                stringMatchingSongs.add(matchingSongs.get(i).getName());
            }
        } else {
            searchBarNode.put("message",
                    "Search returned " + matchingSongs.size() + " results");
            for (Song matchingSong : matchingSongs) {
                stringMatchingSongs.add(matchingSong.getName());
            }
        }
        ArrayNode matchingSongsNode =
                getObjectMapper().valueToTree(stringMatchingSongs);
        searchBarNode.set("results", matchingSongsNode);
        user.getUser().setMatchingSongTitles(stringMatchingSongs);
    }

    private static boolean verifyValues(final ArrayList<String> list1,
                                        final ArrayList<String> list2) {
        for (String value : list1) {
            if (!list2.contains(value)) {
                return false; // If any value is not found, return false
            }
        }
        return true; // All values are present
    }

    private static boolean compare(final char operator, final int number, final int value) {
        return switch (operator) {
            case '<' -> number < value;
            case '>' -> number > value;
            // Add more cases as needed
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}
