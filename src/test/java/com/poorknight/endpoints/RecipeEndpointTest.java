package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEndpointTest {

	@InjectMocks
	private RecipeEndpoint recipeEndpoint;
	
	@Mock
	private SaveRecipeService recipeService;
	
	@Mock
	private RecipeDAO recipeDao;
	
	@Mock
	private Recipe recipe;
	
	@Test
	public void postRecipe_SavesAndReturnsResults() throws Exception {
		Recipe response = recipeEndpoint.postRecipe(recipe);
		
		verify(recipeService).saveNewRecipe(recipe);
		assertThat(response, equalTo(recipe));
	}
	
	@Test
	public void getRecipe_getsRecipeByPassedLong() throws Exception {
		long recipeId = RandomUtils.nextLong();
		when(recipeDao.queryRecipeById(recipeId)).thenReturn(recipe);
		
		Recipe response = recipeEndpoint.getRecipe(recipeId);
		
		assertThat(response, equalTo(recipe));
	}
}
