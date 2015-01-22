package com.poorknight.navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class NavigationStackTest {

	@Parameters(name = "{0}")
	public static List<Object[]> params() {
		return Arrays.asList(new Object[][] {
				{ "Two different locations pushed should result in two entries in LIFO order.", new Location("path1", "query1"),
						new Location("path2", "query2"), 2, new Location("path2", "query2") },//

				{ "Two of the same locations pushed should result in only one entry.", new Location("path1", "query1"),
						new Location("path1", "query1"), 1, new Location("path1", "query1") },//

				{ "Two locations with same path and different queries keeps only the latest.", new Location("path1", "query1"),
						new Location("path1", "query2"), 1, new Location("path1", "query2") },//

				{ "Two locations with same path and second query is null keeps the first.", new Location("path1", "query1"),
						new Location("path1", null), 1, new Location("path1", "query1") } //
				});
	}

	private final NavigationStack stack;

	protected String description;
	private final Location first;
	private final Location second;
	private final int expectedNumberOfEntries;
	private final Location expectedTopLocation;


	public NavigationStackTest(final String description, final Location first, final Location second, final int expectedNumberOfEntries,
			final Location expectedTopLocation) {
		this.description = description;
		this.first = first;
		this.second = second;
		this.expectedNumberOfEntries = expectedNumberOfEntries;
		this.expectedTopLocation = expectedTopLocation;
		this.stack = new NavigationStack();
	}


	@Test
	public void pushTest() throws Exception {
		this.stack.push(this.first);
		this.stack.push(this.second);

		assertThat(this.stack.size(), equalTo(this.expectedNumberOfEntries));
		assertThat(this.stack.peek(), equalTo(this.expectedTopLocation));
	}
}
