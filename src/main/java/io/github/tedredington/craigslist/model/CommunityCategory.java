package io.github.tedredington.craigslist.model;

public enum CommunityCategory implements CategoryCode {
    ALL_COMMUNITY("ccc", "all community"),
    ACTIVITIES("act", "activities"),
    ARTISTS("ats", "artists"),
    CHILDCARE("kid", "childcare"),
    CLASSES("classes", "classes"),
    EVENTS("eve", "events"),
    GENERAL("com", "general community"),
    GROUPS("grp", "groups"),
    LOCAL_NEWS("vnn", "local news & views"),
    LOST_FOUND("laf", "lost & found"),
    MISSED_CONNECTIONS("mis", "missed connections"),
    MUSICIANS("muc", "musicians"),
    PETS("pet", "pets"),
    POLITICS("pol", "politics"),
    RANTS_RAVES("rnr", "rants & raves"),
    RIDESHARE("rid", "rideshare"),
    VOLUNTEERS("vol", "volunteers");

    private final String code;
    private final String displayName;

    CommunityCategory(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override public String code() { return code; }

    @Override public String displayName() { return displayName; }
}
