package com.poorknight.endpoints;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.domain.Recipe;

@Path("/recipe")
public class RecipeEndpoint {

	@Inject
	private SaveRecipeService recipeService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe postRecipe(Recipe recipe) {
		recipeService.saveNewRecipe(recipe);
		return recipe;
	}
}
