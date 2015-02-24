package com.poorknight.business.searchrecipe;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;

import lombok.Getter;
import lombok.Setter;

import com.poorknight.domain.Recipe;


/**
 * Responsible for sorting recipes based on search elements.
 */
@RequestScoped
public class SimpleRecipeSorter implements RecipeSorter {

	@Override
	public List<Recipe> sortBySearchString(final String[] searchElements, final List<Recipe> recipes) {
		final Set<RecipeWithSortStats> sortedSet = createSortedSet(searchElements, recipes);
		return createResultsFromSet(sortedSet);
	}


	private Set<RecipeWithSortStats> createSortedSet(final String[] searchElements, final List<Recipe> recipes) {
		final Set<RecipeWithSortStats> sortedSet = new TreeSet<>();
		int index = 0;
		for (final Recipe recipe : recipes) {
			sortedSet.add(new RecipeWithSortStats(recipe, searchElements, index));
			index++;
		}
		return sortedSet;
	}


	private List<Recipe> createResultsFromSet(final Set<RecipeWithSortStats> sortedSet) {
		final List<Recipe> results = new ArrayList<>(sortedSet.size());
		for (final RecipeWithSortStats recipeHolder : sortedSet) {
			results.add(recipeHolder.getRecipe());
		}
		return results;
	}

	static class RecipeWithSortStats implements Comparable<RecipeWithSortStats> {

		private final int totalOccurrences;
		private final int occurrencesInName;
		private final int occurrencesInContent;
		private final Long timeInMillis;
		private final int defaultOrdering;

		@Getter(PACKAGE)
		@Setter(NONE)
		private final Recipe recipe;


		public RecipeWithSortStats(final Recipe recipe, final String[] searchElements, final int defaultOrdering) {
			this.recipe = recipe;
			this.totalOccurrences = findTotalOccurrences(searchElements);
			this.occurrencesInName = findOccurrencesInName(searchElements);
			this.occurrencesInContent = findOccurrencesInContent(searchElements);
			this.timeInMillis = recipe.getLastUpdatedOn().getTime();
			this.defaultOrdering = defaultOrdering;
		}


		private int findTotalOccurrences(final String[] searchElements) {
			return findOccurrences(searchElements, this.recipe.getSearchableRecipeText());
		}


		private int findOccurrencesInName(final String[] searchElements) {
			final String lowerCaseTitle = this.recipe.getRecipeName().toLowerCase();
			return findOccurrences(searchElements, lowerCaseTitle);
		}


		private int findOccurrencesInContent(final String[] searchElements) {
			final String lowerCaseContent = this.recipe.getRecipeContent().toLowerCase();
			return findOccurrences(searchElements, lowerCaseContent);
		}


		private int findOccurrences(final String[] searchElements, final String stringToSearch) {
			int count = 0;
			for (final String s : searchElements) {
				count += (stringToSearch.contains(s) ? 1 : 0);
			}
			return count;
		}


		@Override
		public int compareTo(final RecipeWithSortStats other) {
			int results = other.totalOccurrences - this.totalOccurrences;
			if (results != 0) {
				return results;
			}
			results = other.occurrencesInName - this.occurrencesInName;
			if (results != 0) {
				return results;
			}

			results = other.occurrencesInContent - this.occurrencesInContent;
			if (results != 0) {
				return results;
			}

			results = other.timeInMillis.compareTo(this.timeInMillis);
			if (results != 0) {
				return results;
			}

			return this.defaultOrdering - other.defaultOrdering;
		}
	}
}