package com.poorknight.pages.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;
import java.util.List;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.poorknight.constants.ITConstants;
import com.poorknight.controller.LatestSearch;
import com.poorknight.controller.SearchRecipesController;
import com.poorknight.controller.ViewRecipeController;
import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
@PersistenceTest
public class SearchRecipesPageNavigationIT {

	private static final String RECIPE_NAME = "Recipe1";

	private static final String SEARCH_TEXT_ID = "pageForm:searchInput";
	private static final String SEARCH_BUTTON_ID = "pageForm:searchButton";
	private static final String VIEW_RECIPE_LINK_TEXT = "View";
	private static final String BACK_BUTTON_ID = "pageForm:goBackButton";
	private static final String HOME_BUTTON_ID = "pageForm:gotoHomeButton";

	private WebElement searchTextfield;
	private WebElement searchButton;
	private List<WebElement> viewRecipeLink;
	private WebElement homeButton;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createRecipePersistenceEnabledPageTestWithNavigation(SearchRecipesController.class, ViewRecipeController.class,
				LatestSearch.class);
	}


	private void initPage(final URL deploymentURL, final WebDriver browser) {
		navigateToSearchPage(deploymentURL, browser);
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
	public void navigatesToRecipeView_AndBack(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		initPage(deploymentURL, browser);
		searchToFindOneRecipe();
		navigatesToViewTheCorrectRecipe_WhenViewIsClicked(browser);
		returnsToTheSearchPage_WhenBackIsClicked(browser);
	}


	@Test
	@InSequence(3)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void retainsSearchStringAndResults_WhenNavigatingBack(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);
		searchToFindOneRecipe();
		navigateForwardAndBack(browser);

		populateScreenElements(browser);
		assertThat(this.viewRecipeLink.size(), equalTo(1));
		assertThat(this.searchTextfield.getAttribute("value"), equalTo(RECIPE_NAME));
	}


	@Test
	@InSequence(4)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void navigatesToHomePage_WhenHomeButtonIsPressed(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);

		Graphene.guardHttp(this.homeButton).click();
		assertThat(browser.getCurrentUrl(), endsWith(ITConstants.HOME_PAGE));
	}


	private void searchToFindOneRecipe() {
		searchFor(RECIPE_NAME);
		assertThat(this.viewRecipeLink.size(), equalTo(1));
	}


	private void navigatesToViewTheCorrectRecipe_WhenViewIsClicked(final WebDriver browser) {
		Graphene.guardHttp(this.viewRecipeLink.get(0)).click();
		assertThat(browser.getCurrentUrl(), containsString(ITConstants.VIEW_SINGLE_RECIPE_PAGE));
		assertThat(browser.getPageSource(), containsString(RECIPE_NAME));
	}


	private void returnsToTheSearchPage_WhenBackIsClicked(final WebDriver browser) {
		Graphene.guardHttp(browser.findElement(By.id(BACK_BUTTON_ID))).click();
		assertThat(browser.getCurrentUrl(), endsWith(ITConstants.SEARCH_RECIPES_PAGE));
	}


	private void navigateForwardAndBack(final WebDriver browser) {
		Graphene.guardHttp(this.viewRecipeLink.get(0)).click();
		Graphene.guardHttp(browser.findElement(By.id(BACK_BUTTON_ID))).click();
	}


	private void searchFor(final String searchString) {
		this.searchTextfield.clear();
		this.searchTextfield.sendKeys(searchString);
		Graphene.guardAjax(this.searchButton).click();
	}


	public void navigateToSearchPage(final URL fromRoot, final WebDriver browser) {
		browser.navigate().to(searchPage(fromRoot));
	}


	private String searchPage(final URL deploymentUrl) {
		return deploymentUrl.toExternalForm().concat(ITConstants.SEARCH_RECIPES_PAGE);
	}


	private void populateScreenElements(final WebDriver browser) {
		this.searchTextfield = browser.findElement(By.id(SEARCH_TEXT_ID));
		this.searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));
		this.viewRecipeLink = browser.findElements(By.linkText(VIEW_RECIPE_LINK_TEXT));
		this.homeButton = browser.findElement(By.id(HOME_BUTTON_ID));
	}

}
