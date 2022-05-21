package com.rnmusicfiles.Models;

import android.net.Uri;

public class Album {

    String id;
    String title;
    String[] artists;
    Uri artwork;
    Uri blurred;
    int numberOfSongs;

    public Album(String id, String title, String[] artists, Uri artwork, Uri blurred, int numberOfSongs) {
        this.id = id;
        this.title = title;
        this.artists = artists;
        this.artwork = artwork;
        this.blurred = blurred;
        this.numberOfSongs = numberOfSongs;
    }
}
