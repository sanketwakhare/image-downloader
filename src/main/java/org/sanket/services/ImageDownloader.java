package org.sanket.services;

import static java.lang.Thread.currentThread;

public class ImageDownloader implements Runnable {

    private final String photoUrl;
    private final int albumId;

    public ImageDownloader(String photoUrl, int albumId) {
        this.photoUrl = photoUrl;
        this.albumId = albumId;
    }

    @Override
    public void run() {
        FileService.downloadImage(photoUrl, albumId);
        System.out.println(currentThread().getName() + " downloaded " + photoUrl);
    }
}
