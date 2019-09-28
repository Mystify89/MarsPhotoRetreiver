package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.NasaPhotoResponse;
import api.Photo;

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

    public void downloadImage(String imagePath, String destination) throws IOException {
        System.out.println(imagePath);
        try (InputStream in = new URL(imagePath).openStream()) {
            Files.copy(in, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        }
    }

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
