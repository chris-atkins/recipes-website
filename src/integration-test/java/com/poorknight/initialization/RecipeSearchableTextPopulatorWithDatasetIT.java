package com.poorknight.initialization;

import static com.poorknight.utils.ArquillianUtils.createBasicPageTestDeployment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.exceptions.DaoException;


@RunWith(Arquillian.class)
@PersistenceTest
public class RecipeSearchableTextPopulatorWithDatasetIT {

	@Inject
	private RecipeSearchableTextPopulator process;

	@Inject
	private RecipeDAO dao;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return createBasicPageTestDeployment("RecipeSearchableTextPopulatorWithDatasetIT", RecipeSearchableTextPopulator.class, RecipeDAO.class,
				Recipe.class, DaoException.class, InitializationProcess.class)//
				.addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml");
	}


	@Test
	@InSequence(1)
	@Transactional
	@Cleanup(phase = TestExecutionPhase.NONE)
	@UsingDataSet("RecipeSearchableTextPopulatorITData.yml")
	public void recipesStartWithNoSearchableContent() throws Exception {
		final List<Recipe> originalRecipes = this.dao.queryAllRecipes();
		for (final Recipe recipe : originalRecipes) {
			assertThat(recipe.getSearchableRecipeText(), isEmptyOrNullString());
		}
	}


	@Test
	@InSequence(2)
	@Transactional
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void runTheProcess() throws Exception {
		this.process.execute();
	}


	@Test
	@InSequence(3)
	@Transactional
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void recipesEndWithSearchableContent() throws Exception {
		final List<Recipe> alteredRecipes = this.dao.queryAllRecipes();
		for (final Recipe recipe : alteredRecipes) {
			assertThat(recipe.getSearchableRecipeText(), equalTo(findExpectedSearchableText(recipe)));
		}

	}


	private String findExpectedSearchableText(final Recipe recipe) {
		return recipe.getRecipeName().trim().toLowerCase() + recipe.getRecipeContent().trim().toLowerCase();
	}
}
