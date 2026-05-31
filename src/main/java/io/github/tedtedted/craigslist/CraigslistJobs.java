package io.github.tedtedted.craigslist;

import io.github.tedtedted.craigslist.exception.InvalidFilterException;
import io.github.tedtedted.craigslist.filter.FilterKeys;
import io.github.tedtedted.craigslist.filter.QueryParam;
import io.github.tedtedted.craigslist.model.Area;
import io.github.tedtedted.craigslist.model.CategoryCode;
import io.github.tedtedted.craigslist.model.EmploymentType;
import io.github.tedtedted.craigslist.model.JobsCategory;
import io.github.tedtedted.craigslist.model.Site;
import io.github.tedtedted.craigslist.parser.DefaultResultRowParser;
import io.github.tedtedted.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist job postings. */
public final class CraigslistJobs extends CraigslistBase {

  private static final ResultRowParser PARSER = new DefaultResultRowParser();

  private CraigslistJobs(
      Craigslist client, Site site, Area area, CategoryCode category, List<QueryParam> params) {
    super(client, site, area, category, params);
  }

  @Override
  protected ResultRowParser parser() {
    return PARSER;
  }

  public static Builder builder(Craigslist client) {
    return new Builder(client);
  }

  public static final class Builder extends BaseBuilder<CraigslistJobs, Builder> {

    private JobsCategory category = JobsCategory.ALL_JOBS;
    private EmploymentType[] employmentTypes;
    private Boolean internship;
    private Boolean telecommute;

    private Builder(Craigslist client) {
      super(client);
    }

    public Builder category(JobsCategory c) {
      if (c == null) {
        throw new InvalidFilterException("category", null, "must be non-null");
      }
      this.category = c;
      return this;
    }

    public Builder employmentType(EmploymentType... types) {
      this.employmentTypes = types;
      return this;
    }

    public Builder internship(boolean v) {
      this.internship = v;
      return this;
    }

    public Builder telecommute(boolean v) {
      this.telecommute = v;
      return this;
    }

    @Override
    public CraigslistJobs build() {
      validateBase();
      List<QueryParam> params = baseParams();
      if (employmentTypes != null) {
        for (EmploymentType t : employmentTypes) {
          params.add(new QueryParam(FilterKeys.EMPLOYMENT_TYPE, t.urlValue()));
        }
      }
      if (Boolean.TRUE.equals(internship)) {
        params.add(new QueryParam(FilterKeys.INTERNSHIP, "1"));
      }
      if (Boolean.TRUE.equals(telecommute)) {
        params.add(new QueryParam(FilterKeys.TELECOMMUTE, "1"));
      }
      return new CraigslistJobs(client, site, area, category, List.copyOf(params));
    }
  }
}
