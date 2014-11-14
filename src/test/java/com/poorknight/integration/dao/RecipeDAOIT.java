package com.poorknight.integration.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.poorknight.dao.RecipeDAO;
import com.poorknight.domain.Recipe;
import com.poorknight.exceptions.DaoException;


@RunWith(Arquillian.class)
public class RecipeDAOIT {

	@Inject
	private RecipeDAO dao;

	@PersistenceContext
	private EntityManager em;  // used for cleanup between tests

	@Inject
	private UserTransaction transaction;  // used for cleanup between tests


	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClass(RecipeDAO.class).addClass(DaoException.class)
				.addPackage(Recipe.class.getPackage()).addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
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
		assertTrue(recipe.getRecipeId() != 0);  // the id was populated from the DB

		// make sure fields of inserted recipe are correct
		assertEquals(1, this.dao.queryAllRecipes().size());
		final Recipe savedRecipe = this.dao.queryAllRecipes().get(0);
		assertEquals("testName", savedRecipe.getRecipeName());
		assertEquals("testDesc", savedRecipe.getRecipeContent());
		assertTrue(savedRecipe.getRecipeId() != 0);  // the id was populated from the DB
	}


	@Test
	public void testSimpleUpdateRecipeContents() {
		final Recipe recipe = buildAndSaveNewRecipe("testName", "testContents");
		final Recipe alteredRecipe = this.dao.updateRecipeContents(recipe.getRecipeId(), "newContents");

		assertEquals(1, this.dao.queryAllRecipes().size()); // still only one recipe in DB

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

}