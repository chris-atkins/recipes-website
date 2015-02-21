package com.poorknight.business.searchrecipe;

import static com.poorknight.testing.matchers.CustomMatchers.isRequestScoped;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;


@RunWith(Enclosed.class)
public class SearchRecipeServiceTest {

	@RunWith(JUnit4.class)
	public static class SearchRecipeServiceGeneral {

		@Test
		public void requestScoped() throws Exception {
			assertThat(SearchRecipeService.class, isRequestScoped());
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class SearchRecipeService_SearchBy {

		@InjectMocks
		private SearchRecipeService service;

		@Mock
		private RecipeDAO dao;


		@Test
		public void callsDaoWithCorrectArgumentsNoSpaces() throws Exception {
			final String searchString = "hi";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf(searchString);
		}


		@Test
		public void callsDaoWithCorrectArgumentsWithSpaces() throws Exception {
			final String searchString = "hi there";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("hi", "there");
		}


		@Test
		public void callsDaoWithCorrectArgumentsLeadingSpaces() throws Exception {
			final String searchString = "  hi";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("hi");
		}


		@Test
		public void callsDaoWithCorrectArgumentsTrailingSpaces() throws Exception {
			final String searchString = "hi  ";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("hi");
		}


		@Test
		public void callsDaoWithCorrectArgumentsMultipleMiddleSpaces() throws Exception {
			final String searchString = "h   i";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("h", "i");
		}


		@Test
		public void callsDaoWithCorrectArguments_WithRepeatedStrings() throws Exception {
			final String searchString = "hi hi hi";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("hi");
		}


		@Test
		public void callsDaoWithCorrectArguments_WithRepeatedDifferentCaseStrings() throws Exception {
			final String searchString = "hi HI Hi";
			this.service.searchBy(searchString);
			verify(this.dao).findRecipesContainingAnyOf("hi");
		}


		@Test
		public void returnsTheDaoResults() throws Exception {
			final List<Recipe> expectedResults = new ImmutableList.Builder<Recipe>().add(randomRecipe()).add(randomRecipe()).build();
			final String searchString = "searchstring";

			when(this.dao.findRecipesContainingAnyOf(searchString)).thenReturn(expectedResults);

			final List<Recipe> results = this.service.searchBy(searchString);

			assertThat(results, contains(expectedResults.toArray()));
			assertThat(results.size(), equalTo(expectedResults.size()));
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class SearchRecipeService_FindAllRecipes {

		@InjectMocks
		private SearchRecipeService service;

		@Mock
		private RecipeDAO dao;


		@Test
		public void returnsTheDaoResults() throws Exception {
			final List<Recipe> expectedResults = new ImmutableList.Builder<Recipe>().add(randomRecipe()).add(randomRecipe()).build();

			when(this.dao.queryAllRecipes()).thenReturn(expectedResults);

			final List<Recipe> results = this.service.findAllRecipes();

			assertThat(results, contains(expectedResults.toArray()));
			assertThat(results.size(), equalTo(expectedResults.size()));
		}

	}


	static Recipe randomRecipe() {
		return new Recipe(RandomStringUtils.random(10), RandomStringUtils.random(10));
	}
}
