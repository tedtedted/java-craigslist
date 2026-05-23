package io.github.tedredington.craigslist;

import io.github.tedredington.craigslist.exception.InvalidFilterException;
import io.github.tedredington.craigslist.filter.FilterKeys;
import io.github.tedredington.craigslist.filter.QueryParam;
import io.github.tedredington.craigslist.model.Area;
import io.github.tedredington.craigslist.model.CategoryCode;
import io.github.tedredington.craigslist.model.GigsCategory;
import io.github.tedredington.craigslist.model.Site;
import io.github.tedredington.craigslist.parser.DefaultResultRowParser;
import io.github.tedredington.craigslist.parser.ResultRowParser;
import java.util.List;

/** Search Craigslist gigs. */
public final class CraigslistGigs extends CraigslistBase {

    private static final ResultRowParser PARSER = new DefaultResultRowParser();

    private CraigslistGigs(
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

    public static final class Builder extends BaseBuilder<CraigslistGigs, Builder> {

        private GigsCategory category = GigsCategory.ALL_GIGS;
        private Boolean isPaid;

        private Builder(Craigslist client) {
            super(client);
        }

        public Builder category(GigsCategory c) {
            if (c == null) {
                throw new InvalidFilterException("category", null, "must be non-null");
            }
            this.category = c;
            return this;
        }

        /** When {@code true}, only paid gigs; when {@code false}, only unpaid. */
        public Builder isPaid(boolean v) {
            this.isPaid = v;
            return this;
        }

        @Override
        public CraigslistGigs build() {
            validateBase();
            if (isPaid != null) {
                params.add(new QueryParam(FilterKeys.IS_PAID, isPaid ? "1" : "0"));
            }
            return new CraigslistGigs(client, site, area, category, List.copyOf(params));
        }
    }
}
