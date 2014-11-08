package com.poorknight.business.saverecipe;

import static com.poorknight.testing.matchers.CustomMatchers.createsTransactionBoundaryOnMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.business.TextToHtmlTranslator;
import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;


@RunWith(MockitoJUnitRunner.class)
public class SaveRecipeServiceTest {

	private static final String RECIPE_CONTENT = "recipe content";

	private static final String TRANSLATED_CONTENT = "translated content";

	@InjectMocks
	SaveRecipeService saveRecipeService;

	@Mock
	RecipeDAO mockRecipeDAO;

	@Mock
	TextToHtmlTranslator mockTextToHtmlTranslator;


	@Test
	public void saveNewRecipeCallsTextToHtmlTranslatorBeforeSave() {

		final Recipe mockRecipe = Mockito.mock(Recipe.class);
		when(mockRecipe.getRecipeContent()).thenReturn(RECIPE_CONTENT);

		this.saveRecipeService.saveNewRecipe(mockRecipe);

		final InOrder inOrder = inOrder(this.mockTextToHtmlTranslator, this.mockRecipeDAO);
		inOrder.verify(this.mockTextToHtmlTranslator).translate(RECIPE_CONTENT);
		inOrder.verify(this.mockRecipeDAO).saveNewRecipe(mockRecipe);
	}


	@Test
	public void saveNewRecipeSetsRecipeContentCorrectlyBeforeSaving() {

		final Recipe mockRecipe = Mockito.mock(Recipe.class);
		when(mockRecipe.getRecipeContent()).thenReturn(RECIPE_CONTENT);
		when(this.mockTextToHtmlTranslator.translate(RECIPE_CONTENT)).thenReturn(TRANSLATED_CONTENT);

		this.saveRecipeService.saveNewRecipe(mockRecipe);

		final InOrder inOrder = inOrder(mockRecipe, this.mockRecipeDAO);
		inOrder.verify(mockRecipe).setRecipeContent(TRANSLATED_CONTENT);
		inOrder.verify(this.mockRecipeDAO).saveNewRecipe(mockRecipe);
	}


	@Test
	public void saveNewUsesTransactionalAnnotation() {
		assertThat(SaveRecipeService.class, createsTransactionBoundaryOnMethod("saveNewRecipe"));
	}
}
