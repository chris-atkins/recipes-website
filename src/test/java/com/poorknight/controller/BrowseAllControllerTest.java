package com.poorknight.controller;

import static com.poorknight.testing.matchers.CustomMatchers.hasAReadOnlyField;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;
import com.poorknight.testing.matchers.CustomMatchers;
import com.poorknight.utils.UnitTestUtils;


@RunWith(MockitoJUnitRunner.class)
public class BrowseAllControllerTest {

	@InjectMocks
	private BrowseAllController controller;

	@Mock
	private RecipeDAO recipeDAO;


	@Test
	public void properViewScopedController() throws Exception {
		assertThat(BrowseAllController.class, CustomMatchers.isAProperViewScopedController());
	}


	@Test
	public void usesAPostConstructMethod() throws Exception {
		assertThat(BrowseAllController.class, CustomMatchers.hasAPostConstructMethod());
	}


	@Test
	public void thePostConstructMethod_InitalizesAllRecipes() throws Exception {
		final List<Recipe> expectedList = ImmutableList.of(new Recipe(random(10), random(10)), new Recipe(random(10), random(10)));
		when(this.recipeDAO.queryAllRecipes()).thenReturn(expectedList);

		UnitTestUtils.callPostConstructMethod(this.controller);

		assertThat(this.controller.getAllRecipes(), equalTo(expectedList));
	}


	@Test
	public void theAllRecipesField_IsReadOnly() throws Exception {
		assertThat(BrowseAllController.class, hasAReadOnlyField("allRecipes"));
	}
}
