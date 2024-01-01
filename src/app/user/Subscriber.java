package app.user;

public interface Subscriber {
    /**
     * Gets username.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Update the subscriber with a notification.
     *
     * @param notification the notification
     * @param artistName   the artist name
     */
    void update(String notification, String artistName);
}
