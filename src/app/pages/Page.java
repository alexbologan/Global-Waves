package app.pages;

import app.user.Subscriber;
import app.user.User;

/**
 * The interface Page.
 */
public interface Page {
    /**
     * Print current page string.
     *
     * @return the current page string
     */
    String printCurrentPage();

    String buyMerch(User user, String merchName);

    /**
     * Process subscriptions.
     *
     * @return the result of the subscription process
     */
    String processSubscription(Subscriber subscriber);
}
