package com.poorknight.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;


/**
 * Responsible for coordinating browse functionality between business tier and JSF pages.
 */
@Named("browseAllController")
@ViewScoped
public class BrowseAllController {

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
