package com.poorknight.navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.CustomMatchers;


@RunWith(JUnit4.class)
public class NavigationTrackerTest {

	private static final String LOCALHOST_PREFIX = "/recipee7";
	private static final String ALONE_REDIRECT_SUFFIX = "?faces-redirect=true";
	private static final String OTHER_PARAMS_REDIRECT_SUFFIX = "&faces-redirect=true";

	private final NavigationTracker navigator = new NavigationTracker();


	@Test
	public void sessionScoped() throws Exception {
		assertThat(NavigationTracker.class, CustomMatchers.isAProperSessionScopedController());
	}


	@Test
	public void lastPageReturnsNull_IfNoNavigationHasHappened() throws Exception {
		final String results = this.navigator.lastPage();
		assertThat(results, nullValue());
	}


	@Test
	public void lastPageReturnsNull_IfOneNavigationHasHappened() throws Exception {
		navigateTo("nav1", null);

		final String results = this.navigator.lastPage();
		assertThat(results, nullValue());
	}


	@Test
	public void returnsTheLastPage_WithOnePriorPage() throws Exception {
		navigateTo("nav1", null);
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("nav1"));
	}


	@Test
	public void returnsTheLastPage_WithQueryString() throws Exception {
		navigateTo("nav1", "query");
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("nav1?query"));
	}


	@Test
	public void stripsOffLocalhostPrefix() throws Exception {
		navigateTo(LOCALHOST_PREFIX + "/nav1", "query");
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("/nav1?query"));
	}


	@Test
	public void stripsOffLocalhostPrefixTwice() throws Exception {
		navigateTo(LOCALHOST_PREFIX + "/nav1", "query");
		navigateTo(LOCALHOST_PREFIX + "/nav2", "query");
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("/nav2?query"));
	}


	@Test
	public void twoSameNavigationsInARowAreTreatedAsOne() throws Exception {
		navigateTo("nav1", "query");
		navigateTo("lastPage", "lastQuery");
		navigateTo("lastPage", "lastQuery");

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("nav1?query"));
	}


	@Test
	public void secondNavigationWithSamePageButNullQuery_IsIgnored() throws Exception {
		navigateTo("nav1", "query");
		navigateTo("nav1", null);
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, startsWith("nav1?query"));
	}


	@Test
	public void secondNavigationWithSamePageAndDifferentQuery_ReplacesTheLastOne() throws Exception {
		navigateTo("nav1", null);
		navigateTo("nav2", "firstQuery");
		navigateTo("nav2", "lastQuery");
		navigateToNextPage();

		assertThat(this.navigator.lastPage(), startsWith("nav2?lastQuery"));
		navigateTo("nav2", "lastQuery"); // simulates actually navigating to nav2 (in real life it would get a navigating to the page message again)
		assertThat(this.navigator.lastPage(), startsWith("nav1"));  // to confirm that nav2?firstQuery has been ignored
	}


	@Test
	public void urlsWithNoQueryString_UseFacesRedirect() throws Exception {
		navigateTo("nav1", null);
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, endsWith(ALONE_REDIRECT_SUFFIX));
	}


	@Test
	public void urlsWithAQueryString_UseFacesRedirect() throws Exception {
		navigateTo("nav1", "query");
		navigateToNextPage();

		final String results = this.navigator.lastPage();
		assertThat(results, endsWith(OTHER_PARAMS_REDIRECT_SUFFIX));
	}


	private void navigateTo(final String path, final String queryString) {
		this.navigator.registerNavigationTo(path, queryString);
	}


	private void navigateToNextPage() {
		navigateTo("nextPage", null);
	}
}
