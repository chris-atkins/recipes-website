package com.poorknight.business.searchrecipe;

import java.util.List;

import com.poorknight.domain.Recipe;


/**
 * Responsible for sorting recipes based on a search request.
 */
public interface RecipeSorter {

	public List<Recipe> sortBySearchString(final String[] searchElements, final List<Recipe> recipes);

}
