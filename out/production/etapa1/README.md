
## Description

In this project, you will implement an application similar in functionality to Spotify, simulating various user actions through commands received in input files. As an admin, you will perceive all actions performed by users and can generate reports related to all users.

## Entities

### Audio File

There are two types:

-   Song
-   Podcast Episode

The user interacts differently based on the type of file.

### Collections of Audio Files

There are three types:

-   Library
-   Playlist
-   Podcast

**Library:** Represents all songs on the platform, accessible to all users.

**Playlist:** A collection of songs created by a user. It can be public or private and is formed from library files based on user preferences.

**Podcast:** A collection of episodes related to a specific theme. All podcasts are specified in the input file and are accessible from the beginning of the simulation.

### Search Bar

Used to search for a specific song, playlist, or podcast based on various filters.

-   For each search command, at least one filter must be specified.
-   The search is user-specific, meaning two users may get different results when searching.

### Music Player

Used to play different audio files from the library or playlists. Podcasts resume from where they left off.

### User

The platform has multiple users. At this stage, users can interact with the search bar, player, and create/play playlists. Each user has a unique username.

### Timestamp

To simulate real-time behavior, commands include a "timestamp" field, specifying when the command was executed relative to the start of the simulation.

### Commands: Search Bar

#### Search

Searches for a song, playlist, or podcast based on specified filters. Returns a list of the top 5 results or all results if fewer than 5.

#### Select

Selects one of the options obtained from the last search. The first result has index 1.

### Commands: Player

#### Load

Loads the selected song, playlist, or podcast, starting from the beginning or resuming from where it left off.

#### PlayPause

Toggles between play and pause states.

#### Repeat

Cycles through different repeat modes based on the content being played.

#### Shuffle

Shuffles the playlist when playing in shuffle mode.

#### Forward/Backward

Advances or rewinds 90 seconds when listening to a podcast.

#### Like

Registers a like for the current song.

#### Next

Skips to the next track.

#### Prev

Returns to the previous track.

#### AddRemoveInPlaylist

Adds or removes the current song from a playlist.

#### Status

Displays the current player status.

### Commands: Playlist

#### CreatePlaylist

Creates an empty playlist for a user. If a playlist with the same name already exists, an error message is displayed.

#### SwitchVisibility

Toggles a playlist between public and private.

#### FollowPlaylist

Allows a user to follow or unfollow a playlist.

#### ShowPlaylists

Displays all songs in all playlists owned by the user.

### User Statistics

#### ShowPreferredSongs

Displays a list of all songs the user has liked.

This ReadMe provides an overview of the project structure, entities, and command functionalities. Detailed information for each command is available through corresponding links in the document. Please refer to those sections for more in-depth explanations and examples.## Description

In this project, you will implement an application similar in functionality to Spotify, simulating various user actions through commands received in input files. As an admin, you will perceive all actions performed by users and can generate reports related to all users.

## Entities

### Audio File

There are two types:

-   Song
-   Podcast Episode

The user interacts differently based on the type of file.

### Collections of Audio Files

There are three types:

-   Library
-   Playlist
-   Podcast

**Library:** Represents all songs on the platform, accessible to all users.

**Playlist:** A collection of songs created by a user. It can be public or private and is formed from library files based on user preferences.

**Podcast:** A collection of episodes related to a specific theme. All podcasts are specified in the input file and are accessible from the beginning of the simulation.

### Search Bar

Used to search for a specific song, playlist, or podcast based on various filters.

-   For each search command, at least one filter must be specified.
-   The search is user-specific, meaning two users may get different results when searching.

### Music Player

Used to play different audio files from the library or playlists. Podcasts resume from where they left off.

### User

The platform has multiple users. At this stage, users can interact with the search bar, player, and create/play playlists. Each user has a unique username.

### Timestamp

To simulate real-time behavior, commands include a "timestamp" field, specifying when the command was executed relative to the start of the simulation.

### Commands: Search Bar

#### Search

Searches for a song, playlist, or podcast based on specified filters. Returns a list of the top 5 results or all results if fewer than 5.

#### Select

Selects one of the options obtained from the last search. The first result has index 1.

### Commands: Player

#### Load

Loads the selected song, playlist, or podcast, starting from the beginning or resuming from where it left off.

#### PlayPause

Toggles between play and pause states.

#### Repeat

Cycles through different repeat modes based on the content being played.

#### Shuffle

Shuffles the playlist when playing in shuffle mode.

#### Forward/Backward

Advances or rewinds 90 seconds when listening to a podcast.

#### Like

Registers a like for the current song.

#### Next

Skips to the next track.

#### Prev

Returns to the previous track.

#### AddRemoveInPlaylist

Adds or removes the current song from a playlist.

#### Status

Displays the current player status.

### Commands: Playlist

#### CreatePlaylist

Creates an empty playlist for a user. If a playlist with the same name already exists, an error message is displayed.

#### SwitchVisibility

Toggles a playlist between public and private.

#### FollowPlaylist

Allows a user to follow or unfollow a playlist.

#### ShowPlaylists

Displays all songs in all playlists owned by the user.

### User Statistics

#### ShowPreferredSongs

Displays a list of all songs the user has liked.

This ReadMe provides an overview of the project structure, entities, and command functionalities. Detailed information for each command is available through corresponding links in the document. Please refer to those sections for more in-depth explanations and examples.