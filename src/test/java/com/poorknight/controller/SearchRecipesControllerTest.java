package com.poorknight.controller;

import static com.poorknight.testing.matchers.CustomMatchers.failsValidation;
import static com.poorknight.testing.matchers.CustomMatchers.hasAReadOnlyField;
import static com.poorknight.testing.matchers.CustomMatchers.isAProperViewScopedController;
import static com.poorknight.testing.matchers.CustomMatchers.passesValidation;
import static org.apache.commons.lang.RandomStringUtils.random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.domain.Recipe;


@RunWith(Enclosed.class)
public class SearchRecipesControllerTest {

	@RunWith(JUnit4.class)
	public static class SearchRecipesControllerGeneric {

		@Test
		public void properViewScopedController() throws Exception {
			assertThat(SearchRecipesController.class, isAProperViewScopedController());
		}


		@Test
		public void foundRecipesIsReadOnly() throws Exception {
			assertThat(SearchRecipesController.class, hasAReadOnlyField("foundRecipes"));
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class SearchRecipeControllerWiring {

		@InjectMocks
		private SearchRecipesController controller;

		@Mock
		private SearchRecipeService service;

		private final String searchString = random(10);
		private final List<Recipe> expectedResults = new ImmutableList.Builder<Recipe>()//
				.add(new Recipe(random(10), random(10)))//
				.add(new Recipe(random(10), random(10))).build();


		@Before
		public void init() {
			this.controller.setSearchString(this.searchString);
			when(this.service.searchBy(this.searchString)).thenReturn(this.expectedResults);
		}


		@Test
		public void searchCallsControllerWithCorrectValue() throws Exception {
			this.controller.search();
			verify(this.service).searchBy(this.searchString);
		}


		@Test
		public void searchPopulatesFoundRecipesWithResponseFromService() throws Exception {
			this.controller.search();
			assertThat(this.controller.getFoundRecipes(), contains(this.expectedResults.toArray()));
			assertThat(this.controller.getFoundRecipes().size(), equalTo(this.expectedResults.size()));
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class SearchRecipesControllerUIVisibility {

		@InjectMocks
		private SearchRecipesController controller;

		@Mock
		private SearchRecipeService service;

		private static final List<Recipe> TWO_RECIPES = new ImmutableList.Builder<Recipe>()//
				.add(new Recipe(random(10), random(10)))//
				.add(new Recipe(random(10), random(10))).build();

		private static final List<Recipe> NO_RESULTS = new ArrayList<>();

		private static final String SEARCH_STRING = "searchString";


		@Before
		public void setupSearchString() {
			this.controller.setSearchString(SEARCH_STRING);
		}


		@Test
		public void shouldDisplaySearchResults_IfAnyExist() throws Exception {
			searchAndFind(TWO_RECIPES);
			final Boolean shouldDisplay = this.controller.getShouldDisplaySearchResults();
			assertThat(shouldDisplay, equalTo(true));
		}


		@Test
		public void shouldNotDisplaySearchResults_IfNoneExist() throws Exception {
			searchAndFind(NO_RESULTS);
			final Boolean shouldDisplay = this.controller.getShouldDisplaySearchResults();
			assertThat(shouldDisplay, equalTo(false));
		}


		@Test
		public void shouldNotDisplaySearchResults_IfNoSearchHasBeenAttempted() throws Exception {
			final Boolean shouldDisplay = this.controller.getShouldDisplaySearchResults();
			assertThat(shouldDisplay, equalTo(false));
		}


		@Test
		public void shouldDisplayNoResultsMessage_IfNoResultsExist_AndASearchHasBeenAttempted() throws Exception {
			searchAndFind(NO_RESULTS);
			final Boolean shouldDisplay = this.controller.getShouldDisplayNoResultsMessage();
			assertThat(shouldDisplay, equalTo(true));
		}


		@Test
		public void shouldNotDisplayNoResultsMessage_IfNoSearchHasBeenAttempted() throws Exception {
			final Boolean shouldDisplay = this.controller.getShouldDisplayNoResultsMessage();
			assertThat(shouldDisplay, equalTo(false));
		}


		@Test
		public void shouldNotDisplayNoResultsMessage_IfASearchHasResults() throws Exception {
			searchAndFind(TWO_RECIPES);
			final Boolean shouldDisplay = this.controller.getShouldDisplayNoResultsMessage();
			assertThat(shouldDisplay, equalTo(false));
		}


		private void searchAndFind(final List<Recipe> recipesToReturn) {
			when(this.service.searchBy(SEARCH_STRING)).thenReturn(recipesToReturn);
			this.controller.search();
			assertThat(this.controller.getFoundRecipes(), equalTo(recipesToReturn));
		}

	}

	@RunWith(JUnit4.class)
	public static class SearchRecipesControllerValidation {

		private final SearchRecipesController controller = new SearchRecipesController();


		@Test
		public void passesValidation_WithAStringInValueToSearchFor() throws Exception {
			this.controller.setSearchString(RandomStringUtils.random(10));
			assertThat(this.controller, passesValidation());
		}


		@Test
		public void failsValidation_WithNullValueToSearchFor() throws Exception {
			this.controller.setSearchString(null);
			assertThat(this.controller, failsValidation());
		}


		@Test
		public void failsValidation_WithEmptyValueToSearchFor() throws Exception {
			this.controller.setSearchString("");
			assertThat(this.controller, failsValidation());
		}


		@Test
		public void failsValidation_WithWhitespaceValueToSearchFor() throws Exception {
			this.controller.setSearchString("   \t ");
			assertThat(this.controller, failsValidation());
		}
	}
}
