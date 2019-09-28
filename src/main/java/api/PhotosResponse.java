package api;

/**
 * Represents the results of a photos request.
 */
public class PhotosResponse {

    public final int numberOfPhotosDownloaded;
    public final int numberOfPhotosSaved;
    public final boolean successful;
    public final String error;

    /**
     * Create an instance
     * @param numberOfPhotosDownloaded How many photos were downloaded
     * @param numberOfPhotosSaved How many photos were successfully saved
     * @param successful Whether the operation suceeded
     * @param error the error message, if any
     */
    public PhotosResponse(int numberOfPhotosDownloaded, int numberOfPhotosSaved, boolean successful, String error) {
        this.numberOfPhotosDownloaded = numberOfPhotosDownloaded;
        this.numberOfPhotosSaved = numberOfPhotosSaved;
        this.successful = successful;
        this.error = error;
    }

    /**
     * Returns the number of photos downloaded.
     * @return the number of photos
     */
    public int getNumberOfPhotosDownloaded() {
        return numberOfPhotosDownloaded;
    }

    /**
     * Returns the number of photos saved.
     * @return the number of photos
     */
    public int getNumberOfPhotosSaved() {
        return numberOfPhotosSaved;
    }

    /**
     * Whether the operation suceeded.
     * @return true if successful
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * The error message if it failed, or "Success" if no errors
     * @return the error message
     */
    public String getError() {
        return error == null ? "Success" : error;
    }
}
