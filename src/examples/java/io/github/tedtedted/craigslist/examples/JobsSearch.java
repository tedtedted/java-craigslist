package io.github.tedtedted.craigslist.examples;

import io.github.tedtedted.craigslist.Craigslist;
import io.github.tedtedted.craigslist.CraigslistJobs;
import io.github.tedtedted.craigslist.model.EmploymentType;
import io.github.tedtedted.craigslist.model.JobsCategory;
import io.github.tedtedted.craigslist.model.Site;

/** Prints the first 10 full-time NYC software jobs. */
public final class JobsSearch {

  public static void main(String[] args) {
    try (Craigslist cl = Craigslist.create()) {
      CraigslistJobs.builder(cl)
          .site(Site.NEW_YORK)
          .category(JobsCategory.SOFTWARE_QA_DBA)
          .employmentType(EmploymentType.FULL_TIME)
          .query("java")
          .build()
          .stream()
          .limit(10)
          .forEach(System.out::println);
    }
  }

  private JobsSearch() {}
}
