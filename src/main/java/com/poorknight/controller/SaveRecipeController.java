package com.poorknight.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.constants.NavigationConstants;
import com.poorknight.domain.Recipe;


/**
 * Responsible for coordinating saving a recipe between business and UI classes.
 */
@Named("saveRecipeController")
@RequestScoped
public class SaveRecipeController {

	@NotBlank
	private String recipeName;

	@NotBlank
	private String recipeContents;

	@Inject
	SaveRecipeService saveRecipeService;


	public String saveRecipeAndNavigateHome() {
		saveRecipe();
		return navigateHome();
	}


	public String navigateHome() {
		return NavigationConstants.HOME;
	}


	private void saveRecipe() {
		final Recipe recipe = new Recipe(this.recipeName, this.recipeContents);
		this.saveRecipeService.saveNewRecipe(recipe);
	}


	public String getRecipeName() {
		return this.recipeName;
	}


	public void setRecipeName(final String recipeName) {
		this.recipeName = recipeName;
	}


	public String getRecipeContents() {
		return this.recipeContents;
	}


	public void setRecipeContents(final String recipeContents) {
		this.recipeContents = recipeContents;
	}

}
