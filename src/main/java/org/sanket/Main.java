package org.sanket;

import org.sanket.rest.RestClient;
import org.sanket.rest.models.Photo;
import org.sanket.services.FileService;
import org.sanket.services.ImageDownloader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        RestClient restClient = new RestClient();
        Call<List<Photo>> callbackGetPhotos = restClient.getJsonPlaceholderAPI().getPhotos();

        callbackGetPhotos.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                List<Photo> photos = response.body();
                if (!Objects.isNull(photos)) {
                    // build albums map
                    Map<Integer, List<Photo>> map = FileService.buildAlbumsMap(photos, 10, 2);

                    // download images with multiple threads
                    List<Thread> imageDownloaderThreads = new ArrayList<>();
                    for (Map.Entry<Integer, List<Photo>> entry : map.entrySet()) {
                        int albumId = entry.getKey();
                        List<Photo> photoList = entry.getValue();
                        for (Photo photo : photoList) {
                            String photoUrl = photo.getUrl();
                            // create image downloader threads
                            ImageDownloader imageDownloader = new ImageDownloader(photoUrl, albumId);
                            Thread thread = new Thread(imageDownloader);
                            thread.start();
                            imageDownloaderThreads.add(thread);
                        }
                    }
                    for (Thread imageDownloaderThread : imageDownloaderThreads) {
                        try {
                            imageDownloaderThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("all photos finished downloading");
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                System.out.println("error downloading images " + t.getMessage());
            }
        });
    }

}