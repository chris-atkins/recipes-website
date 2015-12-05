package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.domain.Recipe;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEndpointTest {

	@InjectMocks
	private RecipeEndpoint recipeEndpoint;
	
	@Mock
	private SaveRecipeService recipeService;
	
	@Mock
	private Recipe recipe;
	
	@Test
	public void postRecipe_SavesAndReturnsResults() throws Exception {
		Recipe response = recipeEndpoint.postRecipe(recipe);
		
		verify(recipeService).saveNewRecipe(recipe);
		assertThat(response, equalTo(recipe));
	}
}
