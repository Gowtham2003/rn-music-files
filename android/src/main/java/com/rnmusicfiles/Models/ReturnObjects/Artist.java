package com.rnmusicfiles.Models.ReturnObjects;

public class Artist {

    String key;
    String title;
    int numberOfAlbums;
    int numberOfSongs;

    public Artist(String id, String key, String title, int numberOfAlbums, int numberOfSongs) {
        this.title = title;
        this.key = key;
        this.numberOfAlbums = numberOfAlbums;
        this.numberOfSongs = numberOfSongs;
    }
}
