package core;

import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import api.Photo;
import api.PhotosResponse;
import server.PhotosController;

public class MarsPhotosTest {

    @Test
    public void testPhotoRetreival() {
        MarsPhotosRetriever retriever = new MarsPhotosRetriever();
        List<Photo> photos = retriever.retrievePhotosForDate(LocalDate.of(2015, 6, 3));
        Assert.assertEquals("Wrong number of photos retreived", 30, photos.size());

        photos = retriever.retrievePhotosForDate(LocalDate.of(1990, 6, 3));
        Assert.assertEquals("Wrong number of photos retreived", 0, photos.size());
    }

    @Test
    public void testEndpoint() {
        PhotosController photosController = new PhotosController();
        PhotosResponse photosResponse = photosController.route("2015-06-03");
        Assert.assertTrue("Retrieval failed", photosResponse.successful);
        Assert.assertEquals("Wrong number of photos downloaded",
                30, photosResponse.numberOfPhotosDownloaded);
        Assert.assertEquals("Wrong number of photos saved",
                30, photosResponse.numberOfPhotosSaved);

    }

}
