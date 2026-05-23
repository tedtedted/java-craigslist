package io.github.tedredington.craigslist.examples;

import io.github.tedredington.craigslist.Craigslist;
import io.github.tedredington.craigslist.CraigslistJobs;
import io.github.tedredington.craigslist.model.EmploymentType;
import io.github.tedredington.craigslist.model.JobsCategory;
import io.github.tedredington.craigslist.model.Site;

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
          .forEach(l -> System.out.println(l.title() + " — " + l.url()));
    }
  }

  private JobsSearch() {}
}
