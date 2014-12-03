package com.poorknight.controller;

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
	public void isAProperViewScopedController() throws Exception {
		// has correct ViewScoped annotation (faces, not beans)
		// implements serializable
		// has the @Named annotation (maybe check for same name as the class with lowercase first letter)

		// would be great to have an integration test that starts the container and checks that the controller is truly serializable after everything
		// is injected into it
		// fail("Implement me");
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
		// assertThat(BrowseAllController.class, CustomMatchers.hasAReadOnlyField("allRecipes"));
	}
}
