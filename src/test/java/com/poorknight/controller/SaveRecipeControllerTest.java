package com.poorknight.controller;

import static com.poorknight.testing.matchers.CustomMatchers.isAProperRequestScopedController;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.faces.FacesException;

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
	public void isProperRequestScopedBean() throws Exception {
		assertThat(SaveRecipeController.class, isAProperRequestScopedController());
	}


	@Test
	public void saveRecipeAndNavigateHomeCallsSaveRecipeManagerWithCorrectRecipeValues() {

		this.saveRecipeController.setRecipeName(RECIPE_NAME);
		this.saveRecipeController.setRecipeContents(RECIPE_CONTENTS);

		final ArgumentCaptor<Recipe> argument = ArgumentCaptor.forClass(Recipe.class);

		this.saveRecipeController.saveRecipeAndNavigateHome();

		Mockito.verify(this.mockSaveRecipeService).saveNewRecipe(argument.capture());
		assertThat(argument.getValue().getRecipeName(), is(equalTo(RECIPE_NAME)));
		assertThat(argument.getValue().getRecipeContent(), is(equalTo(RECIPE_CONTENTS)));
	}


	@Test
	public void saveRecipeAndNavigateHomeReturnsCorrectValue() {
		this.saveRecipeController.setRecipeName(RECIPE_NAME);
		this.saveRecipeController.setRecipeContents(RECIPE_CONTENTS);

		final String navigationResults = this.saveRecipeController.saveRecipeAndNavigateHome();
		assertThat(navigationResults, is(equalTo(NavigationConstants.HOME)));
	}


	@Test
	public void navigateHomeReturnsCorrectValue() throws Exception {
		assertThat(this.saveRecipeController.navigateHome(), equalTo(NavigationConstants.HOME));
	}


	@Test(expected = FacesException.class)
	public void whiteSpaceOnlyRecipeNameThrowsException() throws Exception {

		this.saveRecipeController.setRecipeName("\t ");
		this.saveRecipeController.setRecipeContents(RECIPE_CONTENTS);

		this.saveRecipeController.saveRecipeAndNavigateHome();
	}


	@Test(expected = FacesException.class)
	public void nullRecipeNameThrowsException() throws Exception {

		this.saveRecipeController.setRecipeName(null);
		this.saveRecipeController.setRecipeContents(RECIPE_CONTENTS);

		this.saveRecipeController.saveRecipeAndNavigateHome();
	}


	@Test(expected = FacesException.class)
	public void whiteSpaceOnlyRecipeContentThrowsException() throws Exception {

		this.saveRecipeController.setRecipeName(RECIPE_NAME);
		this.saveRecipeController.setRecipeContents("\t ");

		this.saveRecipeController.saveRecipeAndNavigateHome();
	}


	@Test(expected = FacesException.class)
	public void nullRecipeContentThrowsException() throws Exception {

		this.saveRecipeController.setRecipeName(RECIPE_NAME);
		this.saveRecipeController.setRecipeContents(null);

		this.saveRecipeController.saveRecipeAndNavigateHome();
	}
}
