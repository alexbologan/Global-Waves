package input.files;

import java.util.ArrayList;

public final class Podcast {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes;
    private int duration;
    private int remainedTime;

    public Podcast() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public void setRemainedTime(final int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }
}
