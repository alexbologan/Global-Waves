# Project README

## Objectives

The primary objectives of this project are to implement at least one object-oriented design pattern and refactor the code to allow the addition of new functionalities.

## Description

This project involves developing an application with functionalities similar to Spotify, simulating various actions performed by users, now including artists and hosts. These actions will be simulated using commands received in input files. The administrator's perspective on users and application elements is retained.

The commands from Stage I and the input/output format remain unchanged. All functionalities implemented in Stage I remain valid for the current stage. In this stage, you are required to extend the solution from Stage I with new functionalities.

## System of Pages

To enhance the application's structure, it will now simulate a minimal graphical interface by segmenting the application into different pages that an individual user can be on at any given time. The following types of pages will be introduced for individual users, with the possibility of adding more pages in Stage III:

1.  **HomePage:** The page where any user is located when added to the platform. Here, the user can statically view "recommendations," meaning they can see the top 5 songs they liked the most (based on the number of likes) and the top 5 playlists they follow (based on the total number of likes on all songs in that playlist).
    
2.  **LikedContentPage:** The page where any user can see all the songs and playlists they have liked or followed. These two pages are accessible from anywhere in the application at any time, except when a normal user is offline.
    
3.  **Artist Page:** Each artist will have their own page when added to the platform, allowing them to manipulate certain details about it. A normal user can reach the artist's page by searching for the artist and selecting them from the search results. The page will display the artist's albums, merchandise for sale, and events. Any changes made by the artist to albums, merchandise, or events should be reflected instantly for all users on the artist's page.
    
4.  **Host Page:** Similar to the artist page, a normal user can view podcasts and announcements on this page.
    

## Commands for the Page System

Only normal users can use commands for pages.

1.  **ChangePage:** Messages possible for this command:
    
    -   `<username> accessed <next page> successfully.`
    -   `<username> is trying to access a non-existent page.`
2.  **PrintCurrentPage:** This command will display the current page of a specific user. The format for each page is as follows:
    
    -   **HomePage:**
        
        csharpCopy code
        
        `Liked songs:
           [songname1, songname2, …]
        
        Followed playlists:
           [playlistname1, playlistname2, …]` 
        
    -   **LikedContentPage:**
        
        csharpCopy code
        
        `Liked Songs:
           [songname1 - songartist1, songname2 - songartist2, …]
        
        Followed Playlists:
           [playlistname1 - owner1, playlistname2 - owner2, …]` 
        
    -   **Artist Page:**
        
        yamlCopy code
        
        `Albums:
           [albumname1, albumname2, …]
        
        Merch:
           [merchname1 - merchprice1:
               merchdescription1, merchname2 - merchprice2:
               merchdescription2, …]
        
        Event:
           [eventname1 - eventdate1:
               eventdescription1, eventname2 - eventdate2:
               eventdescription2, …]` 
        
    -   **Host Page:**
        
        yamlCopy code
        
        `Podcasts:
           [podcastname1:
               [episodename1 - episodedescription1, episodename2 - episodedescription2, …], …]
        
        Announcements:
           [announcementname1
               announcementdescription1
            , announcementname2
               announcementdescription2
            , …]` 
        

## New Entities Compared to Stage I

### Users

Two new types of users are introduced, each having specific commands as follows:

1.  **Artist:** A user who can add albums and has their own page in this stage.
    
2.  **Host:** A user who can add podcasts and has their own page in this stage.
    

### Collections of Audio Files

1.  **Album:** An album is a collection of songs created by an artist. Normal users can search for and listen to albums through select and load functions. Songs cannot be added to the application outside of an album.

### Search Bar

New search functionalities have been introduced:

1.  **Album:**
    
    -   by name → checks if the album name starts with the specified text.
    -   by owner → checks if the artist's name who created the album starts with the specified text.
    -   by description → checks if the album description starts with the specified text.
2.  **Artist:**
    
    -   by username → checks if the artist's name starts with the specified text.
3.  **Host:**
    
    -   by username → checks if the host's name starts with the specified text.

### Music Player

Now a normal user can select an album and then load it. The album must support the same functionalities as a playlist, including repeat and shuffle.

### Recap of Timestamp

To simulate the real-time nature of the application, commands have a field called "timestamp," specifying at which second they were executed relative to the start of the test (timestamp t0). Each command is executed instantly at a moment in time. Time is common for all users and passes constantly, regardless of the received commands.

## Commands for the Search Bar

1.  **Search:**
    -   Allows users to search for an album, an artist, or a host.

## Admin Commands

1.  **AddUser:**
    
    -   Allows the admin to add new users, whether they are normal users, artists, or hosts.
2.  **DeleteUser:**
    
    -   Allows the admin to delete a user, and all its connections with other entities in the application must also be deleted or updated.
3.  **ShowAlbums:**
    
    -   Displays all albums of an artist.
4.  **ShowPodcasts:**
    
    -   Displays all podcasts of a host.

## Artist Commands

1.  **AddAlbum:**
    
    -   Allows an artist to add a new album to the application.
2.  **RemoveAlbum:**
    
    -   Allows an artist to remove one of their albums.
3.  **AddEvent:**
    
    -   Allows an artist to add an event.
4.  **RemoveEvent:**
    
    -   Allows an artist to remove an event.
5.  **AddMerch:**
    
    -   Allows an artist to add merchandise items.

## Host Commands

1.  **AddPodcast:**
    
    -   Allows a host to add a new podcast to the application.
2.  **RemovePodcast:**
    
    -   Allows a host to remove one of their podcasts.
3.  **AddAnnouncement:**
    
    -   Allows a host to add an announcement.
4.  **RemoveAnnouncement:**
    
    -   Allows a host to remove an announcement.

## Normal User Commands

1.  **SwitchConnectionStatus:**
    -   Allows normal users to switch between online and offline modes.

## General Statistics

1.  **GetTop5Albums:**
    
    -   Displays the names of the top 5 most appreciated albums based on the number of likes.
2.  **GetTop5Artists:**
    
    -   Displays the names of the top 5 most appreciated artists based on the number of likes.
3.  **GetAllUsers:**
    
    -   Displays the names of all users in the application.
4.  **GetOnlineUsers:**
    
    -   Displays the names of all normal users in the application who are online.

PS: SingleTon Pattern was used in this project for Admin.

                                                              Bologan Alexandru, 322CD, 2023