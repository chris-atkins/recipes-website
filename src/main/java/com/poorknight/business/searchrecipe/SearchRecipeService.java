package com.poorknight.business.searchrecipe;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;


@RequestScoped
public class SearchRecipeService {

	@Inject
	private RecipeDAO dao;


	public List<Recipe> findAllRecipes() {
		return this.dao.queryAllRecipes();
	}


	public List<Recipe> searchBy(final String searchString) {
		String[] searchArray = breakSearchStringOnSpaces(searchString);
		searchArray = cleanArrayOfBadInput(searchArray);
		return this.dao.findRecipesContainingAnyOf(searchArray);
	}


	private String[] breakSearchStringOnSpaces(final String searchString) {
		return searchString.split(" ");
	}


	private String[] cleanArrayOfBadInput(final String[] searchArray) {
		final List<String> cleanedResults = new LinkedList<>();

		for (final String stringToClean : searchArray) {

			final String cleanedString = cleanString(stringToClean);
			if (isValidString(cleanedString)) {
				cleanedResults.add(cleanedString);
			}
		}
		return cleanedResults.toArray(new String[cleanedResults.size()]);
	}


	private String cleanString(final String stringToClean) {
		return stringToClean.trim();
	}


	private boolean isValidString(final String stringToCheck) {
		return !stringToCheck.isEmpty();
	}
}
