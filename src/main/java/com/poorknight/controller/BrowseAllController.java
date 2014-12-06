package com.poorknight.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;


/**
 * Responsible for coordinating browse functionality between business tier and JSF pages.
 */
@Named("browseAllController")
@ViewScoped
public class BrowseAllController implements Serializable {

	private static final long serialVersionUID = 5146000726191558450L;

	private List<Recipe> allRecipes;

	@Inject
	private RecipeDAO recipeDAO;  // TODO - this is not serializable - should be marked as transient (it is a request scoped bean) - but how will it
									// behave on the server? other option would be to make it serializable - but is entity manager serializable? OR
									// will CDI take care of this for me and always inject a new one and I don't need to worry about it (and I can
									// mark it as transient)? Need an integration test


	@PostConstruct
	public void initializeAllRecipes() {
		this.allRecipes = this.recipeDAO.queryAllRecipes();
	}


	public List<Recipe> getAllRecipes() {
		return this.allRecipes;
	}

}
