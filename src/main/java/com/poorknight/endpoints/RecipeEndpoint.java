package com.poorknight.endpoints;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recipe> getAllRecipes() {
		return recipeDAO.queryAllRecipes();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe postRecipe(final Recipe recipe) {
		saveRecipeService.saveNewRecipe(recipe);
		return recipe;
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe getRecipe(@PathParam("id") final Long recipeId) {
		final Recipe recipe = recipeDAO.queryRecipeById(recipeId);
		if (recipe == null) {
			throw new NotFoundException();
		}
		return recipe;
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void deleteRecipe(@PathParam("id") final Long recipeId) {
		recipeDAO.deleteRecipe(recipeId);
	}
}
