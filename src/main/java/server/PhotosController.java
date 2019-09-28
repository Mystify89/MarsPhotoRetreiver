package server;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.Photo;
import api.PhotosResponse;
import core.MarsPhotosRetriever;

@RestController
@RequestMapping("/api/photos")
public class PhotosController {

    private static final Logger LOG = LoggerFactory.getLogger(PhotosController.class);

    private final MarsPhotosRetriever retriever = new MarsPhotosRetriever();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public PhotosResponse route(@RequestParam("requestDate") String requestDate) {
        try {
            LocalDate date = dateTimeFormatter.parse(requestDate, LocalDate::from);
            List<Photo> photoList = retriever.retrievePhotosForDate(date);
            String destination = "MarsPhotos/" + requestDate + "/";
            File outputDirectory = new File(destination);
            outputDirectory.mkdirs();
            int photoCount = 0;
            for (Photo photo : photoList) {
                try {
                    retriever.downloadImage(photo.getImgSrc(),
                            destination + photoCount + getSuffix(photo.getImgSrc()));
                } catch (IOException e) {
                    LOG.error("Could not download " + photo.getImgSrc(), e);
                }
                photoCount++;
            }
            return new PhotosResponse(photoList.size(), photoCount, true, null);
        } catch (Exception e) {
            LOG.error("Error: ", e);
            return new PhotosResponse(0, 0, false, e.getMessage());
        }
    }

    private String getSuffix(String imgSrc) {
        String suffix = imgSrc.substring(imgSrc.length() - 4);
        if (suffix.startsWith(".")) {
            return suffix;
        }
        throw new IllegalArgumentException("Suffix '" + suffix  + " is not valid");
    }
}

