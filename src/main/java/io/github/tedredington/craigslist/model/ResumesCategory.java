package io.github.tedredington.craigslist.model;

public enum ResumesCategory implements CategoryCode {
  ALL_RESUMES("rrr", "all resumes");

  private final String code;
  private final String displayName;

  ResumesCategory(String code, String displayName) {
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
