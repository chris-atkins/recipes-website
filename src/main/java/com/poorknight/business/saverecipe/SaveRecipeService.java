package com.poorknight.business.saverecipe;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.poorknight.business.TextToHtmlTranslator;
import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;


@RequestScoped
public class SaveRecipeService {

	@Inject
	private RecipeDAO recipeDAO;

	@Inject
	TextToHtmlTranslator textToHtmlTranslator;


	@Transactional
	public void saveNewRecipe(final Recipe recipe) {
		final String translatedContent = this.translateTextToHtml(recipe.getRecipeContent());
		recipe.setRecipeContent(translatedContent);
		this.recipeDAO.saveNewRecipe(recipe);
	}


	private String translateTextToHtml(final String recipeContent) {
		return this.textToHtmlTranslator.translate(recipeContent);
	}

}
