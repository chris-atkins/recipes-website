package com.poorknight.navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.CustomMatchers;


@RunWith(JUnit4.class)
public class NavigationTrackerTest {

	private static final String VALID_PATH = "/pages/number";
	private static final String QUERY_STRING = "queryString";

	private final NavigationTracker navigator = new NavigationTracker();


	@Test
	public void sessionScoped() throws Exception {
		assertThat(NavigationTracker.class, CustomMatchers.isAProperSessionScopedController());
	}


	@Test
	public void returnsNull_IfNoLocations() throws Exception {
		final String results = this.navigator.lastPage();
		assertThat(results, nullValue());
	}


	@Test
	public void returnsNull_IfOnlyOneLocation() throws Exception {
		this.navigator.registerNavigationTo(VALID_PATH, QUERY_STRING);

		final String results = this.navigator.lastPage();
		assertThat(results, nullValue());
	}


	@Test
	public void returnsPenultimateLocation_WhenLastPageIsCalled() throws Exception {
		final String firstPath = VALID_PATH + 1;
		this.navigator.registerNavigationTo(firstPath, QUERY_STRING);
		this.navigator.registerNavigationTo(VALID_PATH + "2", QUERY_STRING + "2");

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith(firstPath));
		assertThat(results, containsString(QUERY_STRING));
	}


	@Test
	public void trulySerializable() throws Exception {
		this.navigator.registerNavigationTo(VALID_PATH, QUERY_STRING);
		this.navigator.registerNavigationTo(VALID_PATH + "1", QUERY_STRING + "1");
		this.navigator.registerNavigationTo(VALID_PATH + "2", QUERY_STRING + "2");

		assertThat(this.navigator, CustomMatchers.isTrulySerializable());
	}


	@Test
	public void hasPreviousPagesReturnsFalse_WithNoNavigations() throws Exception {
		assertThat(navigator.hasPreviousPage(), is(false));
	}


	@Test
	public void hasPreviousPagesReturnsFalse_WithOnlyOneNavigation() throws Exception {
		this.navigator.registerNavigationTo(VALID_PATH, QUERY_STRING);
		assertThat(navigator.hasPreviousPage(), is(false));
	}


	@Test
	public void hasPreviousPagesReturnsTrue_WithTwoDistinctNavigations() throws Exception {
		this.navigator.registerNavigationTo(VALID_PATH, QUERY_STRING);
		this.navigator.registerNavigationTo(VALID_PATH + "1", QUERY_STRING + "1");

		assertThat(navigator.hasPreviousPage(), is(true));
	}
}
