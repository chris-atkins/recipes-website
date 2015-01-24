package com.poorknight.controller;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;

import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.domain.Recipe;


@Named("searchRecipesController")
@ViewScoped
public class SearchRecipesController implements Serializable {

	private static final long serialVersionUID = -4846192338092712687L;

	@Inject
	private SearchRecipeService searchService;

	@Inject
	private LatestSearch latestSearch;

	@NotBlank
	private String searchString;

	private List<Recipe> foundRecipes;
	private boolean searchHasOccurred = false;


	@PostConstruct
	public void initExistingSearchResults() {
		if (noPreviousSearchExists()) {
			return;
		}
		retrievePreviousSearch();
		performSearch();
	}


	public void search() {
		performSearch();
		updateLastSearchString();
	}


	private boolean noPreviousSearchExists() {
		return isEmpty(this.latestSearch.getLatestSearch());
	}


	private void retrievePreviousSearch() {
		this.searchString = this.latestSearch.getLatestSearch();
	}


	private void performSearch() {
		this.foundRecipes = this.searchService.searchBy(this.searchString);
		this.searchHasOccurred = true;
	}


	private void updateLastSearchString() {
		if (isNotEmpty(this.foundRecipes)) {
			this.latestSearch.setLatestSearch(this.searchString);
		}
	}


	public boolean getShouldDisplaySearchResults() {
		return isNotEmpty(this.foundRecipes);
	}


	public boolean getShouldDisplayNoResultsMessage() {
		return isEmpty(this.foundRecipes) && this.searchHasOccurred;
	}


	public String getSearchString() {
		return this.searchString;
	}


	public void setSearchString(final String searchString) {
		this.searchString = searchString;
	}


	public List<Recipe> getFoundRecipes() {
		return this.foundRecipes;
	}
}
