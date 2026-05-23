package io.github.tedredington.craigslist.model;

public enum JobsCategory implements CategoryCode {
  ALL_JOBS("jjj", "all jobs"),
  ACCOUNTING_FINANCE("acc", "accounting & finance"),
  ADMIN_OFFICE("ofc", "admin & office"),
  ARCHITECT_ENGINEERING("egr", "architect / engineer / cad"),
  ART_MEDIA_DESIGN("med", "art / media / design"),
  BIOTECH_SCIENCE("sci", "biotech & science"),
  BUSINESS_MGMT("bus", "business / mgmt"),
  CUSTOMER_SERVICE("csr", "customer service"),
  EDUCATION("edu", "education / teaching"),
  ETC_MISC("etc", "et cetera jobs"),
  FOOD_BEVERAGE_HOSPITALITY("fbh", "food / beverage / hospitality"),
  GENERAL_LABOR("lab", "general labor"),
  GOVERNMENT("gov", "government"),
  HEALTHCARE("hea", "healthcare"),
  HUMAN_RESOURCE("hum", "human resource"),
  LEGAL_PARALEGAL("lgl", "legal / paralegal"),
  MANUFACTURING("mnu", "manufacturing"),
  MARKETING_PR_AD("mar", "marketing / pr / ad"),
  NONPROFIT("npo", "nonprofit"),
  REAL_ESTATE("rej", "real estate"),
  RETAIL_WHOLESALE("ret", "retail / wholesale"),
  SALES("sls", "sales"),
  SALON_SPA_FITNESS("spa", "salon / spa / fitness"),
  SECURITY("sec", "security"),
  SKILLED_TRADE_CRAFT("trd", "skilled trade / craft"),
  SOFTWARE_QA_DBA("sof", "software / qa / dba / etc"),
  SYSTEMS_NETWORK("sad", "systems / networking"),
  TECHNICAL_SUPPORT("tch", "technical support"),
  TRANSPORTATION("trp", "transportation"),
  TV_FILM_VIDEO("tfr", "tv / film / video / radio"),
  WEB_INFO_DESIGN("web", "web / info design"),
  WRITING_EDITING("wri", "writing / editing");

  private final String code;
  private final String displayName;

  JobsCategory(String code, String displayName) {
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
