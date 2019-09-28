package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Camera {
    private final String id;
    private final String name;
    private final String roverId;
    private final String fullName;

    @JsonCreator
    public Camera(@JsonProperty("id")String id,
                  @JsonProperty("name")String name,
                  @JsonProperty("rover_id")String roverId,
                  @JsonProperty("full_name")String fullName) {
        this.id = id;
        this.name = name;
        this.roverId = roverId;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoverId() {
        return roverId;
    }

    public String getFullName() {
        return fullName;
    }
}
