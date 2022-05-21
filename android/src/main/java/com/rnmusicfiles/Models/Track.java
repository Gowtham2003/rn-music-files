package com.rnmusicfiles.Models;

import android.net.Uri;

public class Track {

    String id;
    String title;
    String artist;
    String album;
    Long duration;
    Uri path;
    Uri artwork;
    Uri blurred;

    public Track(String id, String title, String artist, String album, Long duration, Uri path, Uri artwork, Uri blurred) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.artwork = artwork;
        this.blurred = blurred;
    }

}
