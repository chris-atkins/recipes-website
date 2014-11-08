package com.poorknight.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.inject.Inject;
import javax.inject.Named;

import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;


/**
 * Responsible for coordinating view recipe functionality between the UI and business classes.
 */
@Named("viewRecipeController")
@RequestScoped
public class ViewRecipeController {

	private Long currentRecipeId;

	private Recipe currentRecipe;

	@Inject
	RecipeDAO recipeDAO;


	public Recipe getCurrentRecipe() {
		if (this.currentRecipe == null) {
			this.currentRecipe = retrieveRecipe();
		}
		return this.currentRecipe;
	}


	private Recipe retrieveRecipe() {

		if (this.getCurrentRecipeId() == null) {
			throw new FacesException("ViewRecipeController:  No Recipe id is available");
		}

		return this.recipeDAO.queryRecipeById(this.getCurrentRecipeId());
	}


	public Long getCurrentRecipeId() {
		return this.currentRecipeId;
	}


	public void setCurrentRecipeId(final Long currentRecipeId) {
		this.currentRecipeId = currentRecipeId;
	}

}
