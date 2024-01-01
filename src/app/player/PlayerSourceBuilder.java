package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating instances of the {@code PlayerSource} class with a fluent interface.
 */
public class PlayerSourceBuilder {
    private Enums.PlayerSourceType type;
    private AudioCollection audioCollection;
    private AudioFile audioFile;
    private int index;
    private int indexShuffled;
    private int remainedDuration;
    private List<Integer> indices = new ArrayList<>();

    /**
     * Sets the player source type.
     *
     * @param sourceType The player source type.
     * @return This builder instance for method chaining.
     */
    public PlayerSourceBuilder type(final Enums.PlayerSourceType sourceType) {
        this.type = sourceType;
        return this;
    }

    /**
     * Sets the audio file for the player source.
     *
     * @param file The audio file.
     * @return This builder instance for method chaining.
     */
    public PlayerSourceBuilder audioFile(final AudioFile file) {
        this.audioFile = file;
        this.audioCollection = null;
        this.index = 0;
        this.indexShuffled = 0;
        this.remainedDuration = file.getDuration();
        return this;
    }

    /**
     * Sets the audio collection for the player source.
     *
     * @param collection The audio collection.
     * @return This builder instance for method chaining.
     */
    public PlayerSourceBuilder audioCollection(final AudioCollection collection) {
        this.audioCollection = collection;
        this.audioFile = collection.getTrackByIndex(0);
        this.index = 0;
        this.indexShuffled = 0;
        this.remainedDuration = audioFile.getDuration();
        return this;
    }

    /**
     * Sets the player source based on a bookmark within an audio collection.
     *
     * @param sourceType            The player source type.
     * @param collection The audio collection.
     * @param podcastBookmark        The podcast bookmark.
     * @return This builder instance for method chaining.
     */
    public PlayerSourceBuilder bookmark(final Enums.PlayerSourceType sourceType,
                                        final AudioCollection collection,
                                        final PodcastBookmark podcastBookmark) {
        this.type = sourceType;
        this.audioCollection = collection;
        this.index = podcastBookmark.getId();
        this.remainedDuration = podcastBookmark.getTimestamp();
        this.audioFile = collection.getTrackByIndex(index);
        return this;
    }

    /**
     * Builds and returns an instance of the {@code PlayerSource} class.
     *
     * @return A new instance of {@code PlayerSource}.
     */
    public PlayerSource build() {
        return new PlayerSource(type, audioCollection, audioFile, index, indexShuffled,
                remainedDuration, indices);
    }
}

