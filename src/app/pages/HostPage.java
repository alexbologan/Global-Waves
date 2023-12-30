package app.pages;

import app.audio.Collections.Podcast;
import app.user.Announcement;
import app.user.Host;
import app.user.Subscriber;

import java.util.List;

/**
 * The type Host page.
 */
public final class HostPage implements Page {
    private String name;
    private List<Podcast> podcasts;
    private List<Announcement> announcements;
    private List<Subscriber> subscribers;

    /**
     * Instantiates a new Host page.
     *
     * @param host the host
     */
    public HostPage(final Host host) {
        podcasts = host.getPodcasts();
        announcements = host.getAnnouncements();
        subscribers = host.getSubscribers();
        name = host.getUsername();
    }

    @Override
    public String getPageType() {
        return "host";
    }

    @Override
    public String processSubscription(final Subscriber subscriber) {
        if (subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
            return subscriber.getUsername() + " unsubscribed from " + name + " successfully.";
        }
        subscribers.add(subscriber);
        return subscriber.getUsername() + " subscribed to " + name + " successfully.";
    }

    @Override
    public String printCurrentPage() {
        return "Podcasts:\n\t%s\n\nAnnouncements:\n\t%s"
               .formatted(podcasts.stream().map(podcast -> "%s:\n\t%s\n"
                          .formatted(podcast.getName(),
                                     podcast.getEpisodes().stream().map(episode -> "%s - %s"
                          .formatted(episode.getName(), episode.getDescription())).toList()))
                          .toList(),
                          announcements.stream().map(announcement -> "%s:\n\t%s\n"
                          .formatted(announcement.getName(), announcement.getDescription()))
                          .toList());
    }
}
