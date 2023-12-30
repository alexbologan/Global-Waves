package app.pages;

import app.user.Subscriber;

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

    /**
     * Gets page type.
     *
     * @return the page type
     */
    String getPageType();

    /**
     * Process subscriptions.
     *
     * @return the result of the subscription process
     */
    String processSubscription(Subscriber subscriber);
}
