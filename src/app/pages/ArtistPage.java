package app.pages;

import app.audio.Collections.Album;
import app.user.Artist;
import app.user.Subscriber;
import app.user.Merchandise;
import app.user.Event;

import java.util.List;

/**
 * The type Artist page.
 */
public final class ArtistPage implements Page {
    private String name;
    private List<Album> albums;
    private List<Merchandise> merch;
    private List<Event> events;
    private List<Subscriber> subscribers;

    /**
     * Instantiates a new Artist page.
     *
     * @param artist the artist
     */
    public ArtistPage(final Artist artist) {
        albums = artist.getAlbums();
        merch = artist.getMerch();
        events = artist.getEvents();
        subscribers = artist.getSubscribers();
        name = artist.getUsername();
    }

    @Override
    public String getPageType() {
        return "artist";
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
        return "Albums:\n\t%s\n\nMerch:\n\t%s\n\nEvents:\n\t%s"
                .formatted(albums.stream().map(Album::getName).toList(),
                           merch.stream().map(merchItem -> "%s - %d:\n\t%s"
                                .formatted(merchItem.getName(),
                                           merchItem.getPrice(),
                                           merchItem.getDescription()))
                                .toList(),
                           events.stream().map(event -> "%s - %s:\n\t%s"
                                 .formatted(event.getName(),
                                            event.getDate(),
                                            event.getDescription()))
                                 .toList());
    }
}
