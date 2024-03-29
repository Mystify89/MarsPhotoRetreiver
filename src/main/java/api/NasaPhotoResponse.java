package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the response from NASA for the photos for a day.
 */
public class NasaPhotoResponse {
    private final List<Photo> photos;

    @JsonCreator
    public NasaPhotoResponse(@JsonProperty("photos") List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
