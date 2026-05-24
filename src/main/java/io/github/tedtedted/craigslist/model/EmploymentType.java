package io.github.tedtedted.craigslist.model;

public enum EmploymentType {
  FULL_TIME("1"),
  PART_TIME("2"),
  CONTRACT("3"),
  EMPLOYEE_S_CHOICE("4");

  private final String urlValue;

  EmploymentType(String urlValue) {
    this.urlValue = urlValue;
  }

  public String urlValue() {
    return urlValue;
  }
}
