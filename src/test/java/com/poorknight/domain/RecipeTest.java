package com.poorknight.domain;

import static com.poorknight.testing.matchers.CustomMatchers.hasAReadOnlyField;
import static com.poorknight.testing.matchers.CustomMatchers.isPackageScoped;
import static com.poorknight.testing.matchers.CustomMatchers.meetsProjectEntityObjectStandards;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class RecipeTest {

	@Test
	public void followsProjectEntityObjectStandards() {
		assertThat(Recipe.class, meetsProjectEntityObjectStandards());
	}


	@Test
	public void searchableText_isReadOnly() throws Exception {
		assertThat(Recipe.class, hasAReadOnlyField("searchableRecipeText"));
	}


	@Test
	public void searchableText_hasPackageScopedSetter() throws Exception {
		assertThat(findSearchableTextSetter(), isPackageScoped());
	}


	private Method findSearchableTextSetter() throws NoSuchMethodException {
		return Recipe.class.getDeclaredMethod("setSearchableRecipeText", String.class);
	}
}
