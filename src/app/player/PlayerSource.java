package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The type Player source.
 */

public class PlayerSource {
    @Getter
    private Enums.PlayerSourceType type;
    @Getter
    @Setter
    private AudioCollection audioCollection;
    /**
     * -- SETTER --
     *  Sets audio file.
     *
     * @param audioFile the audio file
     */
    @Setter
    @Getter
    private AudioFile audioFile;
    @Getter
    private int index;
    private int indexShuffled;
    @Getter
    @Setter
    private int remainedDuration;
    private List<Integer> indices = new ArrayList<>();

    /**
     * Instantiates a new Player source.
     *
     * @param type      the type
     * @param audioFile the audio file
     */
    public PlayerSource(final Enums.PlayerSourceType type,
                         final AudioCollection audioCollection,
                         final AudioFile audioFile,
                         final int index,
                         final int indexShuffled,
                         final int remainedDuration,
                         final List<Integer> indices) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.audioFile = audioFile;
        this.index = index;
        this.indexShuffled = indexShuffled;
        this.remainedDuration = remainedDuration;
        this.indices = indices;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return remainedDuration;
    }

    /**
     * Sets next audio file.
     *
     * @param repeatMode the repeat mode
     * @param shuffle    the shuffle
     * @return the next audio file
     */
    public boolean setNextAudioFile(final Enums.RepeatMode repeatMode,
                                    final boolean shuffle) {
        boolean isPaused = false;

        if (type == Enums.PlayerSourceType.LIBRARY) {
            if (repeatMode != Enums.RepeatMode.NO_REPEAT) {
                remainedDuration = audioFile.getDuration();
            } else {
                remainedDuration = 0;
                isPaused = true;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE
                || repeatMode == Enums.RepeatMode.REPEAT_CURRENT_SONG
                || repeatMode == Enums.RepeatMode.REPEAT_INFINITE) {
                remainedDuration = audioFile.getDuration();
            } else if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
                if (shuffle) {
                    if (indexShuffled == indices.size() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        indexShuffled++;

                        index = indices.get(indexShuffled);
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                } else {
                    if (index == audioCollection.getNumberOfTracks() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        index++;
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                }
            } else if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                if (shuffle) {
                    indexShuffled = (indexShuffled + 1) % indices.size();
                    index = indices.get(indexShuffled);
                } else {
                    index = (index + 1) % audioCollection.getNumberOfTracks();
                }
                updateAudioFile();
                remainedDuration = audioFile.getDuration();
            }
        }

        return isPaused;
    }

    /**
     * Sets prev audio file.
     *
     * @param shuffle the shuffle
     */
    public void setPrevAudioFile(final boolean shuffle) {
        if (type != Enums.PlayerSourceType.LIBRARY) {
            if (remainedDuration == audioFile.getDuration()) {
                if (shuffle) {
                    if (indexShuffled > 0) {
                        indexShuffled--;
                    }
                    index = indices.get(indexShuffled);
                } else {
                    if (index > 0) {
                        index--;
                    }
                }
                updateAudioFile();
            }
        }
        remainedDuration = audioFile.getDuration();
    }

    /**
     * Generate shuffle order.
     *
     * @param seed the seed
     */
    public void generateShuffleOrder(final Integer seed) {
        indices.clear();
        Random random = new Random(seed);
        for (int i = 0; i < audioCollection.getNumberOfTracks(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, random);
    }

    /**
     * Update shuffle index.
     */
    public void updateShuffleIndex() {
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) == index) {
                indexShuffled = i;
                break;
            }
        }
    }

    /**
     * Skip.
     *
     * @param duration the duration
     */
    public void skip(final int duration) {
        remainedDuration += duration;
        if (remainedDuration > audioFile.getDuration()) {
            remainedDuration = 0;
            index++;
            updateAudioFile();
        } else if (remainedDuration < 0) {
            remainedDuration = 0;
        }
    }

    private void updateAudioFile() {
        setAudioFile(audioCollection.getTrackByIndex(index));
    }
}
