package com.poorknight.domain;

import static com.poorknight.testing.matchers.CustomMatchers.meetsProjectEntityObjectStandards;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class RecipeTest {

	@Test
	public void followsProjectEntityObjectStandards() {
		assertThat(Recipe.class, meetsProjectEntityObjectStandards());
	}
}
