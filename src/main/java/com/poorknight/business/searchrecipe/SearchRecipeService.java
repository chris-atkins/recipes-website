package com.poorknight.business.searchrecipe;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;


/**
 * Responsible for searching for recipes based on a search string. Returned recipes are ordered.
 */
@RequestScoped
public class SearchRecipeService {

	@Inject
	private RecipeDAO dao;

	@Inject
	private RecipeSorter sorter;


	public List<Recipe> findAllRecipes() {
		return this.dao.queryAllRecipes();
	}


	public List<Recipe> searchBy(final String searchString) {
		String[] searchArray = breakSearchStringOnSpaces(searchString);
		searchArray = cleanArrayOfBadInput(searchArray);
		final List<Recipe> searchResults = this.dao.findRecipesContainingAnyOf(searchArray);
		return this.sorter.sortBySearchString(searchArray, searchResults);
	}


	private String[] breakSearchStringOnSpaces(final String searchString) {
		return searchString.split(" ");
	}


	private String[] cleanArrayOfBadInput(final String[] searchArray) {
		final Set<String> cleanedResults = new LinkedHashSet<>();

		for (final String stringToClean : searchArray) {

			final String cleanedString = cleanString(stringToClean);
			if (isValidString(cleanedString)) {
				cleanedResults.add(cleanedString);
			}
		}
		return cleanedResults.toArray(new String[cleanedResults.size()]);
	}


	private String cleanString(final String stringToClean) {
		return stringToClean.trim().toLowerCase();
	}


	private boolean isValidString(final String stringToCheck) {
		return !stringToCheck.isEmpty();
	}
}
