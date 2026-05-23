package io.github.tedredington.craigslist;

import io.github.tedredington.craigslist.exception.InvalidFilterException;
import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.CategoryCode;
import io.github.tedredington.craigslist.model.EmploymentType;
import io.github.tedredington.craigslist.model.ResumesCategory;
import io.github.tedredington.craigslist.model.Site;
import io.github.tedredington.craigslist.parser.DefaultResultRowParser;
import io.github.tedredington.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist resumes. */
public final class CraigslistResumes extends CraigslistBase {

    private static final ResultRowParser PARSER = new DefaultResultRowParser();

    private CraigslistResumes(
            Craigslist client,
            Site site,
            Area area,
            CategoryCode category,
            List<QueryParam> params) {
        super(client, site, area, category, params);
    }

    @Override
    protected ResultRowParser parser() {
        return PARSER;
    }

    public static Builder builder(Craigslist client) {
        return new Builder(client);
    }

    public static final class Builder extends BaseBuilder<CraigslistResumes, Builder> {

        private ResumesCategory category = ResumesCategory.ALL_RESUMES;
        private EmploymentType[] employmentTypes;

        private Builder(Craigslist client) {
            super(client);
        }

        public Builder category(ResumesCategory c) {
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

        @Override
        public CraigslistResumes build() {
            validateBase();
            if (employmentTypes != null) {
                for (EmploymentType t : employmentTypes) {
                    params.add(new QueryParam(FilterKeys.EMPLOYMENT_TYPE, t.urlValue()));
                }
            }
            return new CraigslistResumes(client, site, area, category, List.copyOf(params));
        }
    }
}
