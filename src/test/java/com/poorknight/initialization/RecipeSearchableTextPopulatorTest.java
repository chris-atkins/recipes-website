package com.poorknight.initialization;

import static com.poorknight.testing.matchers.CustomMatchers.isRequestScoped;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.utils.ReflectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class RecipeSearchableTextPopulatorTest {

	@InjectMocks
	private RecipeSearchableTextPopulator process;

	@Mock
	private RecipeDAO dao;


	@Before
	public void initMocks() {
		final List<Recipe> returnList = new ImmutableList.Builder<Recipe>().add(buildRecipe(1L, "Recipe1", "Content1"),
				buildRecipe(2L, "Recipe2", "Content2")).build();
		when(this.dao.queryAllRecipes()).thenReturn(returnList);
	}


	private Recipe buildRecipe(final Long id, final String name, final String content) {
		final Recipe recipe = new Recipe(name, content);
		ReflectionUtils.setFieldInClass(recipe, "recipeId", id);
		return recipe;
	}


	@Test
	public void requestScoped() throws Exception {
		assertThat(RecipeSearchableTextPopulator.class, isRequestScoped());
	}


	@Test
	public void recipesAreUpdatedWithSameContent() throws Exception {

		this.process.execute();

		verify(this.dao).updateRecipeContents(1L, "Content1");
		verify(this.dao).updateRecipeContents(2L, "Content2");
	}
}
