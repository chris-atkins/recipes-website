package com.poorknight.endpoints;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;

@Path("/recipe")
public class RecipeEndpoint {

	@Inject
	private SaveRecipeService saveRecipeService;
	
	@Inject
	private RecipeDAO recipeDAO;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe postRecipe(Recipe recipe) {
		saveRecipeService.saveNewRecipe(recipe);
		return recipe;
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe getRecipe(@PathParam("id") Long recipeId) {
		return recipeDAO.queryRecipeById(recipeId);
	}
}
