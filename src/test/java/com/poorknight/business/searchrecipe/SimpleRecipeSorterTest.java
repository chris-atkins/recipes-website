package com.poorknight.business.searchrecipe;

import static com.poorknight.testing.matchers.CustomMatchers.isRequestScoped;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.poorknight.domain.Recipe;
import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
public class SimpleRecipeSorterTest {

	private final Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());

	private final SimpleRecipeSorter sorter = new SimpleRecipeSorter();


	@Test
	public void requestScoped() throws Exception {
		assertThat(SimpleRecipeSorter.class, isRequestScoped());
	}


	@Test
	public void similarRecipes_AreNotRemovedFromList() throws Exception {
		final Recipe oneFound = buildRecipe("found1", "hi");
		final Recipe twoFound = buildRecipe("found1", "hi");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found" }, listToSort);

		assertThat(results.size(), equalTo(2));
	}


	@Test
	public void similarRecipes_MaintainOriginalOrdering() throws Exception {
		final Recipe oneFound = buildRecipe("found1", "hi");
		final Recipe twoFound = buildRecipe("found1", "hi");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found" }, listToSort);

		assertThat(results.size(), equalTo(2));
		assertThat(results.indexOf(oneFound), lessThan(results.indexOf(twoFound)));
	}


	@Test
	public void twoSearchElementsFoundInName_SortedBeforeSingleElementFound() throws Exception {
		final Recipe oneFound = buildRecipe("found1", "hi");
		final Recipe twoFound = buildRecipe("found1 found2", "hi");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found1", "found2" }, listToSort);

		assertThat(results.indexOf(twoFound), lessThan(results.indexOf(oneFound)));
		assertThat(results.size(), equalTo(2));
	}


	@Test
	public void twoSearchElementsFoundInContent_SortedBeforeSingleElementFound() throws Exception {
		final Recipe oneFound = buildRecipe("hi", "found1");
		final Recipe twoFound = buildRecipe("hi", "found1 found2");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found1", "found2" }, listToSort);

		assertThat(results.size(), equalTo(2));
		assertThat(results.indexOf(twoFound), lessThan(results.indexOf(oneFound)));
	}


	@Test
	public void twoElementsFoundInContent_SortedBeforeOneInName() throws Exception {
		final Recipe oneFound = buildRecipe("found1", "hi");
		final Recipe twoFound = buildRecipe("hi", "found1 found2");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found1", "found2" }, listToSort);

		assertThat(results.size(), equalTo(2));
		assertThat(results.indexOf(twoFound), lessThan(results.indexOf(oneFound)));
	}


	@Test
	public void twoElementsFoundInName_SortedBeforeOneInContent() throws Exception {
		final Recipe oneFound = buildRecipe("hi", "found1");
		final Recipe twoFound = buildRecipe("found1 found2", "hi");
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(oneFound).add(twoFound).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found1", "found2" }, listToSort);

		assertThat(results.size(), equalTo(2));
		assertThat(results.indexOf(twoFound), lessThan(results.indexOf(oneFound)));
	}


	@Test
	public void moreRecentRecipes_SortedFirstWhenAllElseIsEqual() throws Exception {
		final Recipe first = buildRecipe("found2", "found1", -1);
		final Recipe moreRecent = buildRecipe("found2", "found1", 0);
		final List<Recipe> listToSort = new ImmutableList.Builder<Recipe>().add(moreRecent).add(first).build();

		final List<Recipe> results = this.sorter.sortBySearchString(new String[] { "found1", "found2" }, listToSort);

		assertThat(results.size(), equalTo(2));
		assertThat(results.indexOf(moreRecent), lessThan(results.indexOf(first)));
	}


	private Recipe buildRecipe(final String name, final String content, final int timeAdjustment) {
		final Recipe recipe = new Recipe();
		recipe.setRecipeName(name);
		recipe.setRecipeContent(content);
		ReflectionUtils.setFieldInClass(recipe, "searchableRecipeText", name.toLowerCase() + content.toLowerCase());
		ReflectionUtils.setFieldInClass(recipe, "lastUpdatedOn", new Timestamp(this.testTimestamp.getTime() + timeAdjustment));
		ReflectionUtils.setFieldInClass(recipe, "recipeId", RandomUtils.nextLong());
		return recipe;
	}


	private Recipe buildRecipe(final String name, final String content) {
		return buildRecipe(name, content, 0);
	}
}
