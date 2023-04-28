package org.sanket.services;

import okhttp3.ResponseBody;
import org.sanket.http.HttpClient;
import org.sanket.rest.models.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService {

    public static String ALBUMS_ROOT_DIRECTORY_PATH = "albums/";
    public static String DOT_PNG = ".png";

    public static void downloadImage(String photoUrl, int albumId) {
        String photoName = getPhotoNameFromUrl(photoUrl);

        // call api to get the image
        HttpClient httpClient = new HttpClient();
        ResponseBody responseBody = httpClient.get(photoUrl);

        String destinationDirectory = ALBUMS_ROOT_DIRECTORY_PATH + albumId;
        String destinationFileName = photoName + DOT_PNG;

        try {
            // create file before writing to it
            File directory = new File(destinationDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(destinationDirectory, destinationFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            // write byte stream data to file
            InputStream inputStream = responseBody.byteStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            int bytesRead;
            while ((bytesRead = inputStream.read()) != -1) {
                outputStream.write(bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPhotoNameFromUrl(String photoUrl) {
        String[] urlParts = photoUrl.split("/");
        return urlParts[urlParts.length - 1];
    }

    public static Map<Integer, List<Photo>> buildAlbumsMap(List<Photo> photos, int maxAlbums, int maxPhotosPerAlbum) {
        Map<Integer, List<Photo>> map = new HashMap<>();
        photos.forEach(photo -> {
            int albumId = photo.getAlbumId();
            if (!map.containsKey(albumId)) {
                if (map.size() < maxAlbums) {
                    List<Photo> list = new ArrayList<>();
                    list.add(photo);
                    map.put(albumId, list);
                }
            } else {
                List<Photo> list = map.get(albumId);
                if (list.size() < maxPhotosPerAlbum) {
                    list.add(photo);
                    map.put(albumId, list);
                }
            }
        });
        return map;
    }
}
