package com.poorknight.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.constants.NavigationConstants;
import com.poorknight.domain.Recipe;


/**
 * Responsible for coordinating saving a recipe between business and UI classes.
 */
@Named("saveRecipeController")
@RequestScoped
public class SaveRecipeController {

	private String recipeName;
	private String recipeContents;

	@Inject
	SaveRecipeService saveRecipeService;


	public String saveRecipeAndNavigateHome() {
		validateUserInputThrowingExceptions();
		saveRecipe();
		return navigateHome();
	}


	public String navigateHome() {
		return NavigationConstants.HOME;
	}


	private void validateUserInputThrowingExceptions() {
		validateName();
		validateContents();
	}


	private void saveRecipe() {
		final Recipe recipe = new Recipe(this.recipeName, this.recipeContents);
		this.saveRecipeService.saveNewRecipe(recipe);
	}


	private void validateName() {
		if (hasNoContent(this.recipeName)) {
			throw new FacesException("Recipe name must not be empty.");
		}
	}


	private void validateContents() {
		if (hasNoContent(this.recipeContents)) {
			throw new FacesException("Recipe contents must not be empty.");
		}
	}


	private boolean hasNoContent(final String valueToCheck) {
		return StringUtils.isEmpty(valueToCheck) || StringUtils.isWhitespace(valueToCheck);
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
