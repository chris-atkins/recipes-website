package com.poorknight.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.poorknight.exceptions.DaoException;

@RunWith(Arquillian.class)
public class RecipeDAOIT {

	private static final String CHICKEN_NAME = "Chicken And Things";
	private static final String CHICKEN_CONTENT = "1 tsp. Chicken\n\tOne giant Lemon\n\n - cook until warm";
	private static final String MAC_AND_CHEESE_NAME = "Macaroni and Cheese";
	private static final String MAC_AND_CHEESE_CONTENT = "1 Recipe for Macaroni and Cheese, the ability to follow Directions";
	private static final String NEVER_FOUND_STRING = "Can't find me";

	@Inject
	private RecipeDAO dao;

	@PersistenceContext
	private EntityManager em; // used for cleanup between tests

	@Inject
	private UserTransaction transaction; // used for cleanup between tests

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClass(RecipeDAO.class).addClass(DaoException.class).addClass(Recipe.class).addClass(Recipe_.class)
				.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Before
	public void cleanupRecipeTableAndStartTransaction() throws Exception {
		this.transaction.begin();
		this.em.joinTransaction();
		this.em.createQuery("delete from Recipe").executeUpdate();
		this.transaction.commit();

		this.transaction.begin();
	}

	@After
	public void closeTransaction() throws Exception {
		this.transaction.commit();
	}

	@Test
	public void testSaveNewAndQueryAllWorks() {
		// make sure DB starts out empty
		assertEquals(0, this.dao.queryAllRecipes().size());

		// insert a recipe and make sure query returns 1 recipe
		final Recipe recipe = new Recipe("testName", "testDesc");
		this.dao.saveNewRecipe(recipe);
		assertTrue(recipe.getRecipeId() != 0); // the id was populated from the DB

		// make sure fields of inserted recipe are correct
		assertEquals(1, this.dao.queryAllRecipes().size());
		final Recipe savedRecipe = this.dao.queryAllRecipes().get(0);
		assertEquals("testName", savedRecipe.getRecipeName());
		assertEquals("testDesc", savedRecipe.getRecipeContent());
		assertTrue(savedRecipe.getRecipeId() != 0); // the id was populated from the DB
	}

	@Test
	public void saveRecipe_GeneratesAndSavesTheSearchableText() {
		final Recipe recipe = new Recipe("testName", "testDesc");

		this.dao.saveNewRecipe(recipe);
		final Recipe savedRecipe = this.dao.queryAllRecipes().get(0);

		assertThat(savedRecipe.getSearchableRecipeText(), equalTo("testnametestdesc"));
	}

	@Test
	public void updateRecipeContents_AlsoUpdatesTheSearchableText() {
		final Recipe recipe = new Recipe("testName", "testDesc");

		this.dao.saveNewRecipe(recipe);
		final Recipe savedRecipe = this.dao.queryAllRecipes().get(0);

		this.dao.updateRecipeContents(savedRecipe.getRecipeId(), "OHAI ME!!");
		final Recipe updatedRecipe = this.dao.queryAllRecipes().get(0);

		assertThat(updatedRecipe.getSearchableRecipeText(), equalTo("testnameohai me!!"));
	}

	@Test
	public void testSimpleUpdateRecipeContents() {
		final Recipe recipe = buildAndSaveNewRecipe("testName", "testContents");
		final Recipe alteredRecipe = this.dao.updateRecipeContents(recipe.getRecipeId(), "newContents");

		assertEquals(1, this.dao.queryAllRecipes().size());

		final Recipe queriedRecipe = this.dao.queryAllRecipes().get(0);
		assertThat(queriedRecipe, is(alteredRecipe));

		assertThat(queriedRecipe.getRecipeName(), is("testName"));
		assertThat(queriedRecipe.getRecipeContent(), is("newContents"));
	}

	private Recipe buildAndSaveNewRecipe(final String name, final String contents) {
		final Recipe recipe = new Recipe(name, contents);
		this.dao.saveNewRecipe(recipe);
		return recipe;
	}

	@Test
	public void findRecipesContainingAnyOf_findsSingleMatchInName() throws Exception {
		setUpDatabaseForFindRecipesTests();

		final List<Recipe> results = this.dao.findRecipesContainingAnyOf("Chicken");

		assertThat(results.size(), equalTo(1));
		final Recipe recipe = results.get(0);
		assertThat(recipe.getRecipeName(), equalTo(CHICKEN_NAME));
		assertThat(recipe.getRecipeContent(), equalTo(CHICKEN_CONTENT));
	}

	@Test
	public void findRecipesContainingAnyOf_findsSingleMatchInName_IgnoringCase() throws Exception {
		setUpDatabaseForFindRecipesTests();

		final List<Recipe> results = this.dao.findRecipesContainingAnyOf("chicken");

		assertThat(results.size(), equalTo(1));
		final Recipe recipe = results.get(0);
		assertThat(recipe.getRecipeName(), equalTo(CHICKEN_NAME));
		assertThat(recipe.getRecipeContent(), equalTo(CHICKEN_CONTENT));
	}

	@Test
	public void findRecipesContainingAnyOf_findsSingleMatchInContents_IgnoringCase() throws Exception {
		setUpDatabaseForFindRecipesTests();

		final List<Recipe> results = this.dao.findRecipesContainingAnyOf("lemOn");

		assertThat(results.size(), equalTo(1));
		final Recipe recipe = results.get(0);
		assertThat(recipe.getRecipeName(), equalTo(CHICKEN_NAME));
		assertThat(recipe.getRecipeContent(), equalTo(CHICKEN_CONTENT));
	}

	@Test
	public void findRecipesContainingAnyOf_findsMatchesIfEitherParamMatches_FromNameOrContent() throws Exception {
		setUpDatabaseForFindRecipesTests();

		final List<Recipe> results = this.dao.findRecipesContainingAnyOf("CHICKEN", "directions");

		assertThat(results.size(), equalTo(2));
	}

	@Test
	public void findRecipesContainingAnyOf_failsValidationWithNullParam() throws Exception {
		this.thrown.expect(ConstraintViolationException.class);
		this.dao.findRecipesContainingAnyOf(null);
	}

	@Test
	public void findRecipesContainingAnyOf_failsValidationWithEmptyParam() throws Exception {
		this.thrown.expect(ConstraintViolationException.class);
		this.dao.findRecipesContainingAnyOf();
	}

	@Test
	public void deleteDeletesCorrectly() throws Exception {
		final Recipe recipe = new Recipe("TEST", "123");
		dao.saveNewRecipe(recipe);
		assertThat(dao.queryAllRecipes().size(), equalTo(1));

		dao.deleteRecipe(recipe.getRecipeId());

		assertThat(dao.queryAllRecipes().size(), equalTo(0));
	}

	private void setUpDatabaseForFindRecipesTests() {
		final Recipe chickenRecipe = new Recipe(CHICKEN_NAME, CHICKEN_CONTENT);
		final Recipe macAndCheeseRecipe = new Recipe(MAC_AND_CHEESE_NAME, MAC_AND_CHEESE_CONTENT);
		final Recipe neverFoundRecipe = new Recipe(NEVER_FOUND_STRING, NEVER_FOUND_STRING);

		this.dao.saveNewRecipe(chickenRecipe);
		this.dao.saveNewRecipe(macAndCheeseRecipe);
		this.dao.saveNewRecipe(neverFoundRecipe);
	}

}