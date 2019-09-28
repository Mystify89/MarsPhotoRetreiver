package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RoverStruct {
    private final String id;
    private final String name;
    private final String landingDate;
    private final String launchDate;
    private final String status;
    private final int maxSol;
    private final String maxDate;
    private final int totalPhotos;
    private final List<Camera> cameras;

    @JsonCreator
    public RoverStruct(@JsonProperty("id") String id,
                       @JsonProperty("name") String name,
                       @JsonProperty("landing_date") String landingDate,
                       @JsonProperty("launch_date") String launchDate,
                       @JsonProperty("status") String status,
                       @JsonProperty("max_sol") int maxSol,
                       @JsonProperty("max_date") String maxDate,
                       @JsonProperty("total_photos") int totalPhotos,
                       @JsonProperty("cameras") List<Camera> cameras) {
        this.id = id;
        this.name = name;
        this.landingDate = landingDate;
        this.launchDate = launchDate;
        this.status = status;
        this.maxSol = maxSol;
        this.maxDate = maxDate;
        this.totalPhotos = totalPhotos;
        this.cameras = cameras;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLandingDate() {
        return landingDate;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public String getStatus() {
        return status;
    }

    public int getMaxSol() {
        return maxSol;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public List<Camera> getCameras() {
        return cameras;
    }
}
