package com.poorknight.initialization;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;


@RequestScoped
public class RecipeSearchableTextPopulator implements InitializationProcess {

	@Inject
	private RecipeDAO dao;


	@Override
	public void execute() {
		final List<Recipe> recipes = this.dao.queryAllRecipes();

		for (final Recipe recipe : recipes) {
			addSearchableText(recipe);
		}
	}


	private void addSearchableText(final Recipe recipe) {
		final Long recipeId = recipe.getRecipeId();
		final String recipeContent = recipe.getRecipeContent();
		updateRecipe(recipeId, recipeContent, "");
	}


	private void updateRecipe(final Long id, final String newContents, final String suffix) {
		this.dao.updateRecipeContents(id, newContents + suffix);
	}
}
