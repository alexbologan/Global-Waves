# GlobalWaves Wrapped - README

GlobalWaves Wrapped is a statistical feature of the GlobalWaves platform that provides users with a personalized summary of their musical activity, including top-listened songs, preferred genres, and other relevant statistics during the program's runtime.

## Command Invocation

The command can be invoked from any page and will display statistics from timestamp 0 to the current timestamp. The structure will be presented in the form of `${name}: ${count}`, initially sorted by count and then lexicographically by name.

### User Statistics

-   **topArtists**: Artists ranked by the number of listens.
-   **topGenres**: Genres ranked by the number of listens.
-   **topSongs**: Songs ranked by the number of listens.
-   **topAlbums**: Albums ranked by the number of listens.
-   **topEpisodes**: Podcast episodes ranked by the number of listens.

### Possible Messages

-   "No data to show for user ${username}."
-   Any deleted song, but already listened to by the user, will remain in their statistics.
-   If two songs have the same artist and name, their listens will be combined for display.

## Artist Statistics

When invoked by an artist, the following configuration will be displayed:

-   **topAlbums**: Albums ranked by the number of listens.
-   **topSongs**: Songs ranked by the number of listens.
-   **topFans**: Users ranked by the number of listens to the current artist.
-   **listeners**: The number of users who have listened to the current artist.

### Possible Messages

-   "No data to show for user ${username}."
-   Any deleted song, but already listened to by users, will remain in the artist's statistics.

## Host Statistics

When invoked by a host, the following configuration will be displayed:

-   **topEpisodes**: Podcast episodes ranked by the number of listens.
-   **listeners**: The number of users who have listened to the current host.

### Possible Messages

-   "No data to show for user ${username}."
-   Any deleted episode, but already listened to by users, will remain in the host's statistics.

## Monetization

Artists on the platform protested for not being paid, so a monetization section is introduced. For each song play, a user will pay a value calculated based on heuristics. Additionally, buying merch will transfer money to the artist's account. At the end of each run, the money each artist receives will be calculated and displayed.

### Calculations

-   **songRevenue**: The money earned from all plays on the platform.
-   **merchRevenue**: The money earned from merch sales on the platform.
-   **ranking**: The artist's ranking on the platform based on sales (indexed from 1).
-   **mostProfitableSong**: The song that generated the most revenue for the artist.

## Merch

-   **Buy merch**: A user can buy merch from an artist's page.

### Possible Messages

-   "The username ${username} doesn't exist."
-   "Cannot buy merch from this page."
-   "The merch ${name} doesn't exist."
-   "${username} has added new merch successfully."

## See my merch

-   Displays all items purchased by the user in chronological order.

### Possible Messages

-   "The username ${username} doesn't exist."

## User Types

### Premium User

-   **Buy Premium Subscription**: A user can buy a premium subscription.
-   **Cancel Premium Subscription**: A user can cancel their premium subscription.

### Possible Messages

-   "The username ${username} doesn't exist."
-   "${username} is already a premium user."
-   "${username} bought the subscription successfully."
-   "${username} is not a premium user."
-   "${username} cancelled the subscription successfully."

## Calculation of Premium User Earnings

A premium user cannot be removed from the platform. A premium subscription costs 1,000,000, and the user receives money from their premium subscription at the end of each run.

### Calculation Formula

#### val=songartistsongtotal​×price

## Free User - Ads

A free user will receive ads at predefined moments. Ads will be added to the user's queue when the "adBreak" command is received.

### Possible Messages

-   "The username ${username} doesn't exist."
-   "${username} is not playing any music."
-   "Ad inserted successfully."

### Calculation of Ads Revenue

When an ad is played, the earnings will be calculated based on the user's recent listens.

### Calculation Formula

#### val=songartistsonglast​×pricead


## Notifications

A notification system will be implemented for playlist owners and content creators (artists/hosts).

### Categories

-   **Playlist Owner**: Receives a notification when someone subscribes to their playlist.
-   **Content Creator (Artist/Host)**:
  -   Concert added (valid only for Artist).
  -   New album/podcast added.
  -   New merch added.
  -   Announcement added.

### Commands

-   **Subscribe**: Allows a user to subscribe to an artist/host.
-   **Get Notifications**: Displays all notifications for a user.

### Possible Messages

-   "The username ${username} doesn't exist."
-   "To subscribe, you need to be on the page of an artist or host."
-   "${username} (un)subscribed (to/from) ${artist/host} successfully."

## Recommendations for Users

Normal users will receive song and playlist recommendations displayed on their HomePage.

### Recommendation Types

1.  **Random Song**: Based on the current song, a random song from the same genre is recommended.
2.  **Random Playlist**: A playlist created from the user's top 3 genres, selecting top songs from each genre.
3.  **Fans Playlist**: A playlist created from the top 5 fans of the current artist.

### Commands

-   **Random Song**: Adds a random song recommendation.
-   **Random Playlist**: Adds a random playlist recommendation.
-   **Fans Playlist**: Adds a fans playlist recommendation.
-   **Load Recommendations**: Loads the last received recommendation.

### Possible Messages

-   "No new recommendations were found."
-   "The username ${username} doesn't exist."
-   "${username} is not a normal user."
-   "The recommendations for user ${username} have been updated successfully."
## Page Navigation

Normal users can navigate forward and backward between pages using the `nextPage` and `previousPage` commands.

### Possible Messages

-   "There are no pages left to go forward."
-   "There are no pages left to go back."
-   "The user ${username} has navigated successfully to the next page."
-   "The user ${username} has navigated successfully to the previous page."

This README provides an overview of the GlobalWaves Wrapped features, commands, and possible interactions.