package app.user.type;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.type.HostStuff.Announcement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Host extends User {
    private final String username;
    @Getter
    private final ArrayList<Podcast> podcasts;
    @Getter
    private final ArrayList<Announcement> announcements;

    public Host(final String username, final int age, final String city,
                final String userType) {
        super(username, age, city, userType);
        this.username = username;
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Adds a new podcast for the host based on the provided command input.
     *
     * @param podcast Containing podcast information.
     */
    public String addPodcast(final Podcast podcast) {
        if (verifyPodcastName(podcast)) {
            return username + " has another podcast with the same name.";
        } else if (verifyEpisodes(podcast)) {
            return username + " has the same episode in this podcast.";
        } else {
            podcasts.add(podcast);
            Admin.addPodcast(podcast);
            return username + " has added new podcast successfully.";
        }
    }

    /**
     * Adds a new announcement for the host based on the provided command input.
     *
     * @param announcement Containing announcement information.
     */
    public String addAnnouncement(final Announcement announcement) {
        if (verifyAnnouncementName(announcement)) {
            return username + " has already added an announcement with this name.";
        } else {
            announcements.add(announcement);
            return username + " has successfully added new announcement.";
        }
    }

    /**
     * Removes an announcement from the host's list of announcements.
     *
     * @param name The name of the announcement to be removed.
     */
    public String removeAnnouncement(final String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                announcements.remove(announcement);
                return username + " has successfully deleted the announcement.";
            }
        }
        return username + " has no announcement with the given name.";
    }

    /**
     * Removes a podcast from the host's list of podcasts.
     *
     * @param name The name of the podcast to be removed.
     */
    public String removePodcast(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                podcasts.remove(podcast);
                return username + " has successfully deleted the podcast.";
            }
        }
        return username + " has no podcast with the given name.";
    }

    /**
     * Shows the podcasts of the host.
     */
    public List<Podcast> showPodcasts() {
        return podcasts;
    }

    /**
     * Verifies if the artist has another podcast with the same name.
     *
     * @param podcast Containing podcast information.
     */
    public boolean verifyPodcastName(final Podcast podcast) {
        for (Podcast p : podcasts) {
            if (p.getName().equals(podcast.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the host has another episode with the same name in the current podcast.
     *
     * @param podcast The CommandInput containing episode details and host information.
     */
    public boolean verifyEpisodes(final Podcast podcast) {
        Map<String, Integer> elementCount = new HashMap<>();
        for (Episode episode : podcast.getEpisodes()) {
            elementCount.put(episode.getName(),
                    elementCount.getOrDefault(episode.getName(), 0) + 1);
            if (elementCount.get(episode.getName()) > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the host has another announcement with the same name.
     *
     * @param announcement The CommandInput containing event details and host information.
     */
    public boolean verifyAnnouncementName(final Announcement announcement) {
        for (Announcement a : announcements) {
            if (a.getName().equals(announcement.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Matches name.
     *
     * @param nameFilter the name filter
     * @return the boolean
     */
    public boolean matchesName(final String nameFilter) {
        return username.toLowerCase().startsWith(nameFilter.toLowerCase());
    }

}
