package com.poorknight.controller;

import static com.poorknight.testing.matchers.CustomMatchers.hasAReadOnlyField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.faces.FacesException;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.testing.matchers.CustomMatchers;


@RunWith(MockitoJUnitRunner.class)
public class ViewRecipeControllerTest {

	@InjectMocks
	ViewRecipeController controller;

	@Mock
	RecipeDAO recipeDAO;


	@Test
	public void isRequestScoped() throws Exception {
		assertThat(ViewRecipeController.class, CustomMatchers.isAProperRequestScopedController());
	}


	@Test
	public void currentRecipeField_IsReadOnly() throws Exception {
		assertThat(ViewRecipeController.class, hasAReadOnlyField("currentRecipe"));
	}


	@Test(expected = FacesException.class)
	public void throwsExceptionWhileRetrievingRecipe_whenRecipeIdIsNull() throws Exception {
		this.controller.getCurrentRecipe();
	}


	@Test
	public void callsDAOWithCorrectRecipeId_toPopulateRecipe() throws Exception {
		final Long randomId = RandomUtils.nextLong();
		this.controller.setCurrentRecipeId(randomId);

		this.controller.getCurrentRecipe();

		verify(this.recipeDAO).queryRecipeById(randomId);
	}


	@Test
	public void doesNotCallDaoMoreThanOneTIme_ifGetRecipeIsCalledMoreThanOnce() throws Exception {
		final Long randomId = RandomUtils.nextLong();
		this.controller.setCurrentRecipeId(randomId);
		when(this.recipeDAO.queryRecipeById(randomId)).thenReturn(new Recipe("hi", "there"));

		this.controller.getCurrentRecipe();
		this.controller.getCurrentRecipe(); // call two times

		verify(this.recipeDAO, times(1)).queryRecipeById(randomId); // only queries DB one time
	}
}
