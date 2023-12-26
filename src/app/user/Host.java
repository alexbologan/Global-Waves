package app.user;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.pages.HostPage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * The type Host.
 */
@Getter
@Setter
public final class Host extends ContentCreator {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    private ArrayList<Pair<String, Integer>> topEpisodes;
    private Integer listeners;

    /**
     * Instantiates a new Host.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        topEpisodes = new ArrayList<>();
        listeners = 0;

        super.setPage(new HostPage(this));
    }

    /**
     * Gets podcast.
     *
     * @param podcastName the podcast name
     * @return the podcast
     */
    public Podcast getPodcast(final String podcastName) {
        for (Podcast podcast: podcasts) {
            if (podcast.getName().equals(podcastName)) {
                return podcast;
            }
        }

        return null;
    }

    /**
     * Gets announcement.
     *
     * @param announcementName the announcement name
     * @return the announcement
     */
    public Announcement getAnnouncement(final String announcementName) {
        for (Announcement announcement: announcements) {
            if (announcement.getName().equals(announcementName)) {
                return announcement;
            }
        }

        return null;
    }

    /**
     * Add listeners.
     */
    public void addListeners() {
        this.listeners++;
    }

    @Override
    public String userType() {
        return "host";
    }
}
