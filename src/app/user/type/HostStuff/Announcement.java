package app.user.type.HostStuff;

import lombok.Getter;

@Getter
public final class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Checks if the username matches a specified name filter, ignoring case.
     *
     * @param nameFilter The name filter to match against the username.
     */
    public boolean matchesName(final String nameFilter) {
        return name.toLowerCase().startsWith(nameFilter.toLowerCase());
    }

}
