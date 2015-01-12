package com.poorknight.pages;

import static com.poorknight.utils.ArquillianUtils.buildLibraryFromPom;
import static com.poorknight.utils.ArquillianUtils.createBasicPageTestDeployment;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.constants.ITConstants;
import com.poorknight.controller.SearchRecipesController;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.domain.Recipe_;
import com.poorknight.exceptions.DaoException;


@RunWith(Arquillian.class)
@PersistenceTest
public class SearchRecipesPageWithDatasetIT {

	private static final String SEARCH_BUTTON_ID = "pageForm:searchButton";
	private static final String SEARCH_TEXT_ID = "pageForm:searchInput";
	private static final String ERROR_MESSAGE_ID = "pageForm:searchInputMessage";
	private static final String NO_RESULTS_MESSAGE_ID = "pageForm:noResultsMessage";

	private WebElement searchText;
	private WebElement searchButton;
	private WebElement errorMessage;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return createBasicPageTestDeployment("SearchRecipesPageWithDatasetIT", SearchRecipesController.class, SearchRecipeService.class,
				RecipeDAO.class, Recipe.class, DaoException.class, CollectionUtils.class, Recipe_.class, WebDriver.class, SearchContext.class,
				WebElement.class)//
				.addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml")//
				.addAsLibrary(buildLibraryFromPom("commons-collections", "commons-collections", "3.2.1"));
	}


	private void initPage(final URL deploymentURL, final WebDriver browser) {
		navigateToSearchRecipesPage(deploymentURL, browser);
		populateScreenElements(browser);
	}


	@Test
	@InSequence(1)
	@UsingDataSet("SearchRecipesPageITData.yml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void prepopulateDatabase() {
		// empty on purpose, just letting @UsingDataSet do its thing
		// Check https://issues.jboss.org/browse/ARQ-1077
	}


	@Test
	@InSequence(2)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void hasResultsOnScreen_ForEveryFoundRecipe(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		initPage(deploymentURL, browser);

		search("Recipe");
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), containsString("Recipe2 Name"));
	}


	@Test
	@InSequence(3)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void findsRecipesByContent(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		initPage(deploymentURL, browser);

		search("OHAI");
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), not(containsString("Recipe2 Name")));
	}


	@Test
	@InSequence(4)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void findsRecipes_ForEitherOfSpaceDelimitedSearchStrings(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);

		search("Recipe1 Recipe2");
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), containsString("Recipe2 Name"));
	}


	@Test
	@InSequence(5)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void errorMessage_GoesAwayAfterSearchNotFound(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);

		search(" ");
		assertThat(this.errorMessage.getText(), not(isEmptyString()));

		search("notFoundString");
		assertThat(retrieveNoResultsMessage(browser).getText(), not(isEmptyString()));
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	@Test
	@InSequence(6)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void errorMessage_GoesAwayAfterGoodSearch(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		initPage(deploymentURL, browser);

		search(" ");
		assertThat(this.errorMessage.getText(), not(isEmptyString()));

		search("Recipe1");
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	// TODO - Leaving here because it is needed for another user story (one which is not being played yet)
	// @Test
	// @InSequence(7)
	// @RunAsClient
	// @Cleanup(phase = TestExecutionPhase.NONE)
	// public void noSearchResultsMessage_GoesAwayAfterErrorSearch(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
	// throws Exception {
	// initPage(deploymentURL, browser);
	//
	// search("notFoundString");
	// assertThat(retrieveNoResultsMessage(browser).getText(), not(isEmptyString()));
	//
	// search(" ");
	// assertThat(this.errorMessage.getText(), not(isEmptyString()));
	// assertThat(retrieveNoResultsMessage(browser), nullValue());
	// }

	@Test
	@InSequence(8)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void noSearchResultsMessage_GoesAwayAfterGoodSearch(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);

		search("notFoundString");
		assertThat(retrieveNoResultsMessage(browser).getText(), not(isEmptyString()));

		search("Recipe1");
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(retrieveNoResultsMessage(browser), nullValue());
	}


	private WebElement retrieveNoResultsMessage(final WebDriver browser) {
		try {
			return browser.findElement(By.id(NO_RESULTS_MESSAGE_ID));
		} catch (final Exception ex) {
			return null;
		}
	}


	public void navigateToSearchRecipesPage(final URL deploymentURL, final WebDriver browser) {
		final String startURL = deploymentURL.toExternalForm() + ITConstants.SEARCH_RECIPES_PAGE;
		browser.get(startURL);
	}


	private void populateScreenElements(final WebDriver browser) {
		this.searchText = browser.findElement(By.id(SEARCH_TEXT_ID));
		this.searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));
		this.errorMessage = browser.findElement(By.id(ERROR_MESSAGE_ID));
	}


	private void search(final String searchString) {
		this.searchText.clear();
		this.searchText.sendKeys(searchString);
		Graphene.guardAjax(this.searchButton).click();
	}
}
