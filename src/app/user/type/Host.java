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

    public String addAnnouncement(final Announcement announcement) {
        if (verifyAnnouncementName(announcement)) {
            return username + " has already added an announcement with this name.";
        } else {
            announcements.add(announcement);
            return username + " has successfully added new announcement.";
        }
    }

    public String removeAnnouncement(final String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                announcements.remove(announcement);
                return username + " has successfully deleted the announcement.";
            }
        }
        return username + " has no announcement with the given name.";
    }

    public String removePodcast(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                podcasts.remove(podcast);
                return username + " has successfully deleted the podcast.";
            }
        }
        return username + " has no podcast with the given name.";
    }

    public List<Podcast> showPodcasts() {
        return podcasts;
    }
    public boolean verifyPodcastName(final Podcast podcast) {
        for (Podcast p : podcasts) {
            if (p.getName().equals(podcast.getName())) {
                return true;
            }
        }
        return false;
    }

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

    public boolean verifyAnnouncementName(final Announcement announcement) {
        for (Announcement a : announcements) {
            if (a.getName().equals(announcement.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesName(final String nameFilter) {
        return username.toLowerCase().startsWith(nameFilter.toLowerCase());
    }

}
