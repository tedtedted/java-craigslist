package io.github.tedtedted.craigslist.model;

public enum ServicesCategory implements CategoryCode {
  ALL_SERVICES("bbb", "all services"),
  AUTOMOTIVE("aos", "automotive services"),
  BEAUTY("bts", "beauty services"),
  CELL_PHONE("cms", "cell phone / mobile services"),
  COMPUTER("cps", "computer services"),
  CREATIVE("crs", "creative services"),
  CYCLE("cys", "cycle services"),
  EVENT("evs", "event services"),
  FARM_GARDEN("fgs", "farm & garden services"),
  FINANCIAL("fns", "financial services"),
  HOUSEHOLD("hss", "household services"),
  LABOR_MOVE("lbs", "labor / moving services"),
  LEGAL("lgs", "legal services"),
  LESSONS("lss", "lessons & tutoring"),
  MARINE("mas", "marine services"),
  PET("pas", "pet services"),
  REAL_ESTATE("rts", "real estate services"),
  SKILLED_TRADE("sks", "skilled trade services"),
  SMALL_BIZ_AD("biz", "small biz ads"),
  THERAPEUTIC("hws", "health/wellness services"),
  TRAVEL_VACATION("trv", "travel/vacation services"),
  WRITING_EDITING("wet", "writing / editing / translation");

  private final String code;
  private final String displayName;

  ServicesCategory(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String displayName() {
    return displayName;
  }
}
