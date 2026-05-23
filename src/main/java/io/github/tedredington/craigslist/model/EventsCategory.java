package io.github.tedredington.craigslist.model;

public enum EventsCategory implements CategoryCode {
    ALL_EVENTS("eee", "all events");

    private final String code;
    private final String displayName;

    EventsCategory(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override public String code() { return code; }

    @Override public String displayName() { return displayName; }
}
