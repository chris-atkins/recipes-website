package com.poorknight.navigation;

import static com.poorknight.testing.matchers.CustomMatchers.isTrulySerializable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Enclosed.class)
public class PageNavigationStackTest {

	private static final String PATH_1 = "/pages/path1";
	private static final String QUERY_1 = "/pages/query1";

	private static final String PATH_2 = "/pages/path2";
	private static final String QUERY_2 = "/pages/query2";

	private static final String NON_PAGES_PATH = "non/pages/Path";

	@RunWith(Parameterized.class)
	public static class PageNavigationStackParameterizedTest {

		@Parameters(name = "{0}")
		public static List<Object[]> params() {
			return Arrays.asList(new Object[][] {
					{ "Two different locations pushed should result in two entries in LIFO order.", new Location(PATH_1, QUERY_1),
							new Location(PATH_2, QUERY_2), 2, new Location(PATH_2, QUERY_2) },//

					{ "Two of the same locations pushed should result in only one entry.", new Location(PATH_1, QUERY_1),
							new Location(PATH_1, QUERY_1), 1, new Location(PATH_1, QUERY_1) },//

					{ "Two locations with same path and different queries keeps only the latest.", new Location(PATH_1, QUERY_1),
							new Location(PATH_1, QUERY_2), 1, new Location(PATH_1, QUERY_2) },//

					{ "Two locations with same path and second query is null keeps the first.", new Location(PATH_1, QUERY_1),
							new Location(PATH_1, null), 1, new Location(PATH_1, QUERY_1) }, //

					{ "Locations that don't start with /pages/ are ignored.", new Location(PATH_1, QUERY_1), new Location(NON_PAGES_PATH, QUERY_2),
							1, new Location(PATH_1, QUERY_1) }, //

					{ "Locations that don't start with /pages/ are ignored, even if they are the first ones.", new Location(NON_PAGES_PATH, QUERY_2),
							new Location(PATH_1, QUERY_1), 1, new Location(PATH_1, QUERY_1) } //
					});
		}

		private final PageNavigationStack stack;

		protected String description;
		private final Location first;
		private final Location second;
		private final int expectedNumberOfEntries;
		private final Location expectedTopLocation;


		public PageNavigationStackParameterizedTest(final String description, final Location first, final Location second,
				final int expectedNumberOfEntries, final Location expectedTopLocation) {
			this.description = description;
			this.first = first;
			this.second = second;
			this.expectedNumberOfEntries = expectedNumberOfEntries;
			this.expectedTopLocation = expectedTopLocation;
			this.stack = new PageNavigationStack();
		}


		@Test
		public void pushTest() throws Exception {
			this.stack.push(this.first);
			this.stack.push(this.second);

			assertThat(this.stack.size(), equalTo(this.expectedNumberOfEntries));
			assertThat(this.stack.peek(), equalTo(this.expectedTopLocation));
		}
	}

	@RunWith(JUnit4.class)
	public static class PageNavigationStackNonParameterizedTest {

		private final PageNavigationStack stack = new PageNavigationStack();


		@Test
		public void trulySerializable() throws Exception {
			final Location first = new Location(PATH_1, QUERY_1);
			final Location second = new Location(PATH_2, QUERY_2);

			this.stack.push(first);
			this.stack.push(second);

			assertThat(this.stack, isTrulySerializable());
		}
	}
}
