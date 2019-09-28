package core;

public enum Rover {
    Curiosity,
    Opportunity,
    Spirit;

    public String apiName() {
        return name().toLowerCase();
    }
}
