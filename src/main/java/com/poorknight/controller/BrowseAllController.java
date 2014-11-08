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
	RecipeDAO recipeDAO;


	@PostConstruct
	public void initializeAllRecipes() {
		this.allRecipes = this.recipeDAO.queryAllRecipes();
	}


	public List<Recipe> getAllRecipes() {
		return this.allRecipes;
	}


	public void setAllRecipes(final List<Recipe> allRecipes) {
		this.allRecipes = allRecipes;
	}
}
