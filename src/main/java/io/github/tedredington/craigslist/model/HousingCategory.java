package io.github.tedredington.craigslist.model;

public enum HousingCategory implements CategoryCode {
    ALL_HOUSING("hhh", "all housing"),
    APARTMENTS("apa", "apartments / housing for rent"),
    HOUSING_SWAP("swp", "housing swap"),
    OFFICE_COMMERCIAL("off", "office & commercial"),
    PARKING_STORAGE("prk", "parking & storage"),
    REAL_ESTATE("rea", "real estate - all"),
    REAL_ESTATE_BY_OWNER("reb", "real estate - by owner"),
    REAL_ESTATE_BY_BROKER("reo", "real estate - by broker"),
    REAL_ESTATE_WANTED("rew", "real estate wanted"),
    ROOMS_SHARED("roo", "rooms & shares"),
    ROOMS_WANTED("sha", "rooms wanted"),
    SUBLETS_TEMPORARY("sub", "sublets & temporary"),
    VACATION_RENTALS("vac", "vacation rentals"),
    HOUSING_WANTED("hsw", "housing wanted");

    private final String code;
    private final String displayName;

    HousingCategory(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override public String code() { return code; }

    @Override public String displayName() { return displayName; }
}
