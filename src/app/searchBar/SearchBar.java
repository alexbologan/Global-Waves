package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.type.Artist;
import app.user.type.Host;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.filterAlbumsByDescription;
import static app.searchBar.FilterUtils.filterArtistsByName;
import static app.searchBar.FilterUtils.filterByAlbum;
import static app.searchBar.FilterUtils.filterByArtist;
import static app.searchBar.FilterUtils.filterByFollowers;
import static app.searchBar.FilterUtils.filterByGenre;
import static app.searchBar.FilterUtils.filterByLyrics;
import static app.searchBar.FilterUtils.filterByName;
import static app.searchBar.FilterUtils.filterByOwner;
import static app.searchBar.FilterUtils.filterByPlaylistVisibility;
import static app.searchBar.FilterUtils.filterByReleaseYear;
import static app.searchBar.FilterUtils.filterByTags;
import static app.searchBar.FilterUtils.filterHostsByName;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> results;
    private List<Artist> artistsResults;
    private List<Host> hostsResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;
    @Getter
    private LibraryEntry lastSelected;
    @Getter
    private Artist lastSelectedArtist;
    @Getter
    private Host lastSelectedHost;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getAlbums());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterAlbumsByDescription(entries, filters.getReleaseYear());
                }
                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     * Search artists list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<Artist> searchArtist(final Filters filters, final String type) {
        List<Artist> artists = new ArrayList<>(Admin.getArtists());
        if (filters.getName() != null) {
            artists = filterArtistsByName(artists, filters.getName());
        }

        while (artists.size() > MAX_RESULTS) {
            artists.remove(artists.size() - 1);
        }

        this.artistsResults = artists;
        this.lastSearchType = type;
        return this.artistsResults;
    }

    /**
     * Search hosts list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<Host> searchHost(final Filters filters, final String type) {
        List<Host> hosts = new ArrayList<>(Admin.getHosts());
        if (filters.getName() != null) {
            hosts = filterHostsByName(hosts, filters.getName());
        }

        while (hosts.size() > MAX_RESULTS) {
            hosts.remove(hosts.size() - 1);
        }

        this.hostsResults = hosts;
        this.lastSearchType = type;
        return this.hostsResults;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    /**
     * Select artist.
     *
     * @param itemNumber the item number
     * @return the artist
     */
    public Artist selectArtist(final Integer itemNumber) {
        if (this.artistsResults.size() < itemNumber) {
            artistsResults.clear();

            return null;
        } else {
            lastSelectedArtist =  this.artistsResults.get(itemNumber - 1);
            artistsResults.clear();

            return lastSelectedArtist;
        }
    }

    /**
     * Select host.
     *
     * @param itemNumber the item number
     * @return the host
     */
    public Host selectHost(final Integer itemNumber) {
        if (this.hostsResults.size() < itemNumber) {
            hostsResults.clear();

            return null;
        } else {
            lastSelectedHost =  this.hostsResults.get(itemNumber - 1);
            hostsResults.clear();

            return lastSelectedHost;
        }
    }
}
