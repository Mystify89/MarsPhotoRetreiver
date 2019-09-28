package core;

/**
 * The rovers used by NASA
 */
public enum Rover {
    Curiosity,
    Opportunity,
    Spirit;

    public String apiName() {
        return name().toLowerCase();
    }
}
