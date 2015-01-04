package com.poorknight.controller;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.Serializable;
import java.util.List;

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

	@NotBlank
	private String searchString;

	private List<Recipe> foundRecipes;

	@Inject
	private SearchRecipeService searchService;

	private boolean searchHasOccurred = false;


	public void search() {
		this.foundRecipes = this.searchService.searchBy(this.searchString);
		this.searchHasOccurred = true;
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
