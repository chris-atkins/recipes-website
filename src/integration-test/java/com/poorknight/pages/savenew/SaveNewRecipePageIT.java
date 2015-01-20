package com.poorknight.pages.savenew;

import static com.poorknight.constants.ITConstants.HOME_PAGE;
import static com.poorknight.constants.ITConstants.SAVE_RECIPE_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
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


@RunWith(Arquillian.class)
public class SaveNewRecipePageIT {

	private static final String HOME_BUTTON_ID = "pageForm:gotoHomeButton";
	private static final String SAVE_BUTTON_ID = "pageForm:saveAndGotoHomeButton";
	private static final String RECIPE_NAME_TEXTFIELD_ID = "pageForm:recipeName";
	private static final String RECIPE_CONTENT_TEXTFIELD_ID = "pageForm:recipeContents";
	private static final String RECIPE_NAME_ERROR_MESSAGE_ID = "pageForm:messageForRecipeName";
	private static final String RECIPE_CONTENT_ERROR_MESSAGE_ID = "pageForm:messageForRecipeContents";

	private static final String TEST_RECIPE_NAME_TO_INPUT = "recipe name from SaveNewRecipePageIT";
	private static final String TEST_RECIPE_CONTENT_TO_INPUT = "recipe content from SaveNewRecipePageIT";
	private static final String WHITESPACE = "    ";

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentUrl;

	@FindBy(id = HOME_BUTTON_ID)
	private WebElement homeButton;

	@FindBy(id = SAVE_BUTTON_ID)
	private WebElement saveButton;

	@FindBy(id = RECIPE_NAME_TEXTFIELD_ID)
	private WebElement recipeNameInput;

	@FindBy(id = RECIPE_CONTENT_TEXTFIELD_ID)
	private WebElement recipeContentInput;

	@FindBy(id = RECIPE_NAME_ERROR_MESSAGE_ID)
	private WebElement recipeNameErrorMessage;

	@FindBy(id = RECIPE_CONTENT_ERROR_MESSAGE_ID)
	private WebElement recipeContentErrorMessage;


	@Deployment(testable = false)
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
	public void homePageButtonExists() throws Exception {
		assertThat(this.homeButton, notNullValue());
	}


	@Test
	public void saveButtonExists() throws Exception {
		assertThat(this.saveButton, notNullValue());
	}


	@Test
	public void recipeNameInputStartsEmpty() throws Exception {
		assertThat(this.recipeNameInput, notNullValue());
		assertThat(this.recipeNameInput.getText(), isEmptyString());
	}


	@Test
	public void recipeContentInputStartsEmpty() throws Exception {
		assertThat(this.recipeContentInput, notNullValue());
		assertThat(this.recipeContentInput.getText(), isEmptyString());
	}


	@Test
	public void recipeNameMessageStartsEmpty() throws Exception {
		assertThat(this.recipeNameErrorMessage, notNullValue());
		assertThat(this.recipeNameErrorMessage.getText(), isEmptyString());
	}


	@Test
	public void recipeContentMessageStartsEmpty() throws Exception {
		assertThat(this.recipeContentErrorMessage, notNullValue());
		assertThat(this.recipeContentErrorMessage.getText(), isEmptyString());
	}


	@Test
	public void homeButtonNavigatesHome() throws Exception {
		this.homeButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	@Test
	public void saveRecipeNavigatesHome() throws Exception {
		this.recipeNameInput.sendKeys(TEST_RECIPE_NAME_TO_INPUT);
		this.recipeContentInput.sendKeys(TEST_RECIPE_CONTENT_TO_INPUT);

		this.saveButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	@Test
	public void validationErrorOnEmptyRecipeName() throws Exception {
		this.recipeContentInput.sendKeys(TEST_RECIPE_CONTENT_TO_INPUT);

		this.saveButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(SAVE_RECIPE_PAGE)); // stays on same page
		assertThat(this.recipeNameErrorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void validationErrorOnEmptyRecipeContent() throws Exception {
		this.recipeNameInput.sendKeys(TEST_RECIPE_NAME_TO_INPUT);

		this.saveButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(SAVE_RECIPE_PAGE)); // stays on same page
		assertThat(this.recipeContentErrorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void validationErrorOnWhitespaceRecipeName() throws Exception {
		this.recipeNameInput.sendKeys(WHITESPACE);
		this.recipeContentInput.sendKeys(TEST_RECIPE_CONTENT_TO_INPUT);

		this.saveButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(SAVE_RECIPE_PAGE)); // stays on same page
		assertThat(this.recipeNameErrorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void validationErrorOnWhitespaceRecipeContent() throws Exception {
		this.recipeNameInput.sendKeys(TEST_RECIPE_NAME_TO_INPUT);
		this.recipeContentInput.sendKeys(WHITESPACE);

		this.saveButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(SAVE_RECIPE_PAGE)); // stays on same page
		assertThat(this.recipeContentErrorMessage.getText(), not(isEmptyString()));
	}
}
