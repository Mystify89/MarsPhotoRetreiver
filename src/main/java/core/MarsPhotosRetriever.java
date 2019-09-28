package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import api.NasaPhotoResponse;
import api.Photo;

/**
 * Handles the access to NASA servers, enabling the download of both the image listings and the actual image files
 */
public class MarsPhotosRetriever {
    private static final String API_KEY =  "Rx0Se5XJa2gOHG5L2O4dBxaUKJt5prJOaofmuX4s";
    private static final String MARS_PHOTOS_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/%s/" +
            "photos?earth_date=%s&api_key=%s&page=%d";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final Logger LOG = LoggerFactory.getLogger(MarsPhotosRetriever.class);
    ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final int NASA_PAGE_SIZE = 25;

    private final RestClient restClient;

    public MarsPhotosRetriever() {
        restClient = new RestClient();
    }

    /**
     * Download the given image from its URL to the specified file
     * @param imagePath the URL to the image
     * @param destination the file location to download to
     * @throws IOException if there is an IO issue
     */
    public void downloadImage(String imagePath, String destination) throws IOException {
        LOG.info("Downloading image from: '" + imagePath + "'");

        URL resourceUrl, base, next;
        Map<String, Integer> visited = new HashMap<>();
        HttpURLConnection conn;
        String location;
        int times;
        String url = imagePath;

        while (true) {
            times = visited.compute(url, (key, count) -> count == null ? 1 : count + 1);

            if (times > 3) {
                throw new IOException("Stuck in redirect loop");
            }
            resourceUrl = new URL(url);
            conn        = (HttpURLConnection) resourceUrl.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections
            conn.setRequestProperty("User-Agent", "Mozilla/5.0...");

            switch (conn.getResponseCode())
            {
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    location = conn.getHeaderField("Location");
                    location = URLDecoder.decode(location, "UTF-8");
                    base     = new URL(url);
                    next     = new URL(base, location);  // Deal with relative URLs
                    url      = next.toExternalForm();
                    continue;
            }

            break;
        }

        try (ReadableByteChannel readChannel = Channels.newChannel(conn.getInputStream());
             FileOutputStream fileOS = new FileOutputStream(destination)) {
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel
                    .transferFrom(readChannel, 0, Long.MAX_VALUE);
        }
    }

    /**
     * Finds all of the photos taken on a given date. Checks all rovers, and automatically scrolls through the pages
     * if nessecary.
     * @param date the date to query
     * @return the list of Photos
     */
    public List<Photo> retrievePhotosForDate(LocalDate date) {
        List<Photo> allPhotos = new ArrayList<>();
        for (Rover rover: EnumSet.allOf(Rover.class)) {
            int page = 1;
            boolean needAnotherPage = true;
            while (needAnotherPage ) {
                String urlString = String.format(MARS_PHOTOS_URL, rover.apiName(), dateTimeFormatter.format(date),
                        API_KEY, page);
                LOG.info("Making request to " + urlString);

                try {
                    URI url = new URI(urlString);
                    Optional<String> result = restClient.httpGet(url, Collections.emptyMap());
                    Optional<NasaPhotoResponse> nasaPhotoResponse = result.map(json -> toPhotosResponse(json));
                    nasaPhotoResponse.ifPresent(nasaPhotos -> allPhotos.addAll(nasaPhotos.getPhotos()));

                    //This could work by querying again whenever there are any result - this would be more robust if
                    //NASA changes the page size, but would otherwise be less efficient.
                    needAnotherPage = nasaPhotoResponse.isPresent()
                            && nasaPhotoResponse.get().getPhotos().size() == NASA_PAGE_SIZE;
                    page++;
                } catch (MalformedURLException e) {
                    LOG.error("Created malformed url: " + urlString, e);
                } catch (URISyntaxException e) {
                    LOG.error("URI syntax error: " + urlString, e);
                } catch (IOException e) {
                    LOG.error("IO Exception from: " + urlString, e);
                }
            }
        }

        System.out.println(allPhotos.size());

        return allPhotos;
    }

    private NasaPhotoResponse toPhotosResponse(String json) {
        try {
            return JSON_MAPPER.readValue(json, NasaPhotoResponse.class);
        } catch (IOException e) {
            LOG.error("Error deserializing response", e);
        }
        return null;
    }
}
