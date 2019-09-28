package api;

/**
 * Represents the results of a photos request.
 */
public class PhotosResponse {

    public final int numberOfPhotosDownloaded;
    public final int numberOfPhotosSaved;
    public final boolean successful;
    public final String error;
    public final String downloadLocation;

    /**
     * Create an instance
     * @param numberOfPhotosDownloaded How many photos were downloaded
     * @param numberOfPhotosSaved How many photos were successfully saved
     * @param successful Whether the operation suceeded
     * @param error the error message, if any
     * @param downloadLocation where the files were saved
     */
    public PhotosResponse(int numberOfPhotosDownloaded, int numberOfPhotosSaved, boolean successful, String error, String downloadLocation) {
        this.numberOfPhotosDownloaded = numberOfPhotosDownloaded;
        this.numberOfPhotosSaved = numberOfPhotosSaved;
        this.successful = successful;
        this.error = error;
        this.downloadLocation = downloadLocation;
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

    /**
     * The download location for the files
     * @return the download location
     */
    public String getDownloadLocation() {
        return downloadLocation;
    }
}
