package commands.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.files.CommandInput;
import input.files.Library;
import user.User;

import java.util.ArrayList;
import java.util.Objects;

public final class Song {

    /**
     * Performs a song search based on the specified command input and filters.
     *
     * @param commandInput   The input containing search filters.
     * @param commandPromptNode  The JSON object node where search results will be stored.
     * @param user           The user performing the search.
     */
    public void performSearchSong(final CommandInput commandInput, final Library library,
                                  final ObjectNode commandPromptNode, final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<input.files.Song> matchingSongs = new ArrayList<>();
        ArrayList<input.files.Song> newMatchingSongs = new ArrayList<>();
        int filterNum = 0;
        final int maxSize = 5;
        if (commandInput.getFilters().getName() != null) {
            for (int i = 0; i < library.getSongs().size(); i++) {
                if (library.getSongs().get(i).getName().startsWith(
                        commandInput.getFilters().getName())) {
                    matchingSongs.add(library.getSongs().get(i));
                }
            }
            filterNum++;
        }
        if (commandInput.getFilters().getAlbum() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < library.getSongs().size(); i++) {
                    if (Objects.equals(library.getSongs().get(i).getAlbum(),
                            commandInput.getFilters().getAlbum())) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
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
                for (int i = 0; i < library.getSongs().size(); i++) {
                    if (verifyValues(commandInput.getFilters().getTags(),
                            library.getSongs().get(i).getTags())) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
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
                for (int i = 0; i < library.getSongs().size(); i++) {
                    if (library.getSongs().get(i).getLyrics().toLowerCase().contains(
                            commandInput.getFilters().getLyrics().toLowerCase())) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
                    if (matchingSong.getLyrics().toLowerCase().contains(
                            commandInput.getFilters().getLyrics())) {
                        newMatchingSongs.add(matchingSong);
                    }
                }
                matchingSongs = newMatchingSongs;
            }
        }

        if (commandInput.getFilters().getArtist() != null) {
            if (matchingSongs.isEmpty() && filterNum == 0) {
                for (int i = 0; i < library.getSongs().size(); i++) {
                    if (Objects.equals(library.getSongs().get(i).getArtist(),
                            commandInput.getFilters().getArtist())) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
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
                for (int i = 0; i < library.getSongs().size(); i++) {
                    char operator = commandInput.getFilters().getReleaseYear().charAt(0);
                    int value = Integer.parseInt(
                            commandInput.getFilters().getReleaseYear().substring(1));
                    if (compare(operator, library.getSongs().get(i).getReleaseYear(), value)) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
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
                for (int i = 0; i < library.getSongs().size(); i++) {
                    if (Objects.equals(library.getSongs().get(i).getGenre().toLowerCase(),
                            commandInput.getFilters().getGenre().toLowerCase())) {
                        matchingSongs.add(library.getSongs().get(i));
                    }
                }
            } else if (!matchingSongs.isEmpty()) {
                for (input.files.Song matchingSong : matchingSongs) {
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
            commandPromptNode.put("message", "Search returned 5 results");
            for (int i = 0; i < maxSize; ++i) {
                stringMatchingSongs.add(matchingSongs.get(i).getName());
            }
        } else {
            commandPromptNode.put("message", "Search returned "
                    + matchingSongs.size() + " results");
            for (input.files.Song matchingSong : matchingSongs) {
                stringMatchingSongs.add(matchingSong.getName());
            }
        }
        ArrayNode matchingSongsNode =
                objectMapper.valueToTree(stringMatchingSongs);
        commandPromptNode.set("results", matchingSongsNode);
        user.getUser().setMatchingSongs(matchingSongs);
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
