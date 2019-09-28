package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo {
    private final String id;
    private final int sol;
    private final Camera camera;
    private final String imgSrc;
    private final String earthDate;
    private final RoverStruct rover;

    @JsonCreator
    public Photo(@JsonProperty("id") String id,
                 @JsonProperty("sol") int sol,
                 @JsonProperty("camera") Camera camera,
                 @JsonProperty("img_src") String imgSrc,
                 @JsonProperty("earth_date") String earthDate,
                 @JsonProperty("rover") RoverStruct rover) {
        this.id = id;
        this.sol = sol;
        this.camera = camera;
        this.imgSrc = imgSrc;
        this.earthDate = earthDate;
        this.rover = rover;
    }


    public String getId() {
        return id;
    }

    public int getSol() {
        return sol;
    }

    public Camera getCamera() {
        return camera;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public RoverStruct getRover() {
        return rover;
    }
}
