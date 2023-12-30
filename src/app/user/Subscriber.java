package app.user;

public interface Subscriber {
    String getUsername();
    void update(String notification, String artistName);
}
