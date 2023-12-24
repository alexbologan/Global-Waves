package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast / user / artist/ host
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle

    private int age;
    private String city;
    private ArrayList<EpisodeInput> episodes;
    private String name;
    private Integer price;
    private String date;
    private String description;
    private ArrayList<SongInput> songs;
    private Integer releaseYear;
    private String nextPage;
    private String recommendationType;

    public CommandInput() {
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
