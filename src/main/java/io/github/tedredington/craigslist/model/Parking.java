package io.github.tedredington.craigslist.model;

public enum Parking {
    CARPORT("1"),
    ATTACHED_GARAGE("2"),
    DETACHED_GARAGE("3"),
    OFF_STREET_PARKING("4"),
    STREET_PARKING("5"),
    VALET_PARKING("6"),
    NO_PARKING("7");

    private final String urlValue;

    Parking(String urlValue) {
        this.urlValue = urlValue;
    }

    public String urlValue() {
        return urlValue;
    }
}
