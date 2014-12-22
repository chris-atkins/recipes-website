package com.poorknight.domain.entitylisteners;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import javax.inject.Inject;
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
import com.poorknight.domain.annotations.AuditColumns;
import com.poorknight.domain.entitylisteners.AuditColumnsListener;
import com.poorknight.exceptions.DaoException;
import com.poorknight.exceptions.ReflectionException;
import com.poorknight.utils.ReflectionUtils;
import com.poorknight.utils.TimestampGenerator;


@RunWith(Arquillian.class)
public class AuditColumnsListenerIT {

	@Inject
	RecipeDAO dao;

	@Inject
	private UserTransaction transaction;


	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClass(RecipeDAO.class).addClass(DaoException.class).addClass(Recipe.class)
				.addClass(ReflectionException.class).addClass(ReflectionUtils.class).addClass(AuditColumns.class).addClass(TimestampGenerator.class)
				.addClass(AuditColumnsListener.class).addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")
				.addAsManifestResource("META-INF/test-listeners.xml", "META-INF/listeners.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}


	@Before
	public void cleanupRecipeTableAndStartTransaction() throws Exception {
		this.transaction.begin();
	}


	@After
	public void closeTransaction() throws Exception {
		this.transaction.commit();
	}


	@Test
	public void testSaveNewAndQueryAllWorks() {
		assertThat(this.dao.queryAllRecipes().size(), equalTo(0));

		final Recipe recipe = new Recipe("testName", "testDesc");
		makeSureAuditFieldsAreNull(recipe);
		this.dao.saveNewRecipe(recipe);

		final Recipe savedRecipe = this.dao.queryAllRecipes().get(0);

		assertThat(savedRecipe.getCreatedBy(), notNullValue());
		assertThat(savedRecipe.getCreatedOn(), notNullValue());
		assertThat(savedRecipe.getLastUpdatedBy(), notNullValue());
		assertThat(savedRecipe.getLastUpdatedOn(), notNullValue());
	}


	private void makeSureAuditFieldsAreNull(final Recipe recipe) {
		assertThat(recipe.getCreatedBy(), nullValue());
		assertThat(recipe.getCreatedOn(), nullValue());
		assertThat(recipe.getLastUpdatedBy(), nullValue());
		assertThat(recipe.getLastUpdatedOn(), nullValue());
	}
}
