package com.poorknight.pages;

import static com.poorknight.constants.ITConstants.SAVE_RECIPE_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.poorknight.business.TextToHtmlTranslator;
import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.controller.SaveRecipeController;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.exceptions.DaoException;
import com.poorknight.utils.ArquillianUtils;


@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class SaveNewRecipePageWarpIT {

	private static final String SAVE_BUTTON_ID = "pageForm:saveAndGotoHomeButton";
	private static final String RECIPE_NAME_TEXTFIELD_ID = "pageForm:recipeName";
	private static final String RECIPE_CONTENT_TEXTFIELD_ID = "pageForm:recipeContents";

	private static final String TEST_RECIPE_NAME_TO_INPUT = "recipe name from SaveNewRecipePageIT";
	private static final String TEST_RECIPE_CONTENT_TO_INPUT = "recipe content from SaveNewRecipePageIT";

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentUrl;

	@FindBy(id = SAVE_BUTTON_ID)
	WebElement saveButton;

	@FindBy(id = RECIPE_NAME_TEXTFIELD_ID)
	WebElement recipeNameInput;

	@FindBy(id = RECIPE_CONTENT_TEXTFIELD_ID)
	WebElement recipeContentInput;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		final WebArchive war = ArquillianUtils.createBasicPageTestDeployment("SaveNewRecipePageIT", SaveRecipeController.class,
				SaveRecipeService.class, RecipeDAO.class, TextToHtmlTranslator.class, Recipe.class, DaoException.class).addAsWebInfResource(
				"META-INF/test-persistence.xml", "classes/META-INF/persistence.xml");

		// System.out.println(war.toString(Formatters.VERBOSE));
		return war;
	}


	@Before
	public void navigateToSaveRecipePage() {
		final String startURL = this.deploymentUrl.toExternalForm() + SAVE_RECIPE_PAGE;
		this.browser.get(startURL);
	}


	@Test
	public void saveRecipePutsExpectedContentIntoDatabase() {

		Warp.initiate(new Activity() {

			@Override
			public void perform() {
				SaveNewRecipePageWarpIT.this.recipeNameInput.sendKeys(TEST_RECIPE_NAME_TO_INPUT);
				SaveNewRecipePageWarpIT.this.recipeContentInput.sendKeys(TEST_RECIPE_CONTENT_TO_INPUT);

				SaveNewRecipePageWarpIT.this.saveButton.click();
			}

		}).inspect(new Inspection() {

			private static final long serialVersionUID = -7280698047716645593L;

			@Inject
			private RecipeDAO dao;


			@BeforeServlet
			public void databaseIsEmpty() throws Exception {
				assertThat(this.dao.queryAllRecipes(), emptyCollectionOf(Recipe.class));
			}


			@AfterServlet
			public void databaseHasExpectedRecipe() throws Exception {
				final List<Recipe> recipes = this.dao.queryAllRecipes();
				assertThat(recipes.size(), equalTo(1));

				final Recipe recipe = recipes.get(0);
				assertThat(recipe.getRecipeName(), equalTo(TEST_RECIPE_NAME_TO_INPUT));
				assertThat(recipe.getRecipeContent(), equalTo(TEST_RECIPE_CONTENT_TO_INPUT));
			}

		});
	}
}