package com.poorknight.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.constants.NavigationConstants;
import com.poorknight.domain.Recipe;


@RunWith(MockitoJUnitRunner.class)
public class SaveRecipeControllerTest {

	private static final String RECIPE_NAME = "recipe name";

	private static final String RECIPE_CONTENTS = "recipe contents";

	@InjectMocks
	private SaveRecipeController saveRecipeController;

	@Mock
	private SaveRecipeService mockSaveRecipeService;


	@Test
	public void saveRecipeAndNavigateHomeCallsSaveRecipeManagerWithCorrectRecipeValues() {

		this.saveRecipeController.setRecipeName(RECIPE_NAME);
		this.saveRecipeController.setRecipeContents(RECIPE_CONTENTS);

		ArgumentCaptor<Recipe> argument = ArgumentCaptor.forClass(Recipe.class);

		this.saveRecipeController.saveRecipeAndNavigateHome();

		Mockito.verify(this.mockSaveRecipeService).saveNewRecipe(argument.capture());
		assertThat(argument.getValue().getRecipeName(), is(equalTo(RECIPE_NAME)));
		assertThat(argument.getValue().getRecipeContent(), is(equalTo(RECIPE_CONTENTS)));
	}


	@Test
	public void saveRecipeAndNavigateHomeReturnsCorrectValue() {

		String navigationResults = this.saveRecipeController.saveRecipeAndNavigateHome();

		assertThat(navigationResults, is(equalTo(NavigationConstants.HOME)));
	}
}
