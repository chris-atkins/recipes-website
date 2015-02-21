package com.poorknight.pages.search;

import static com.poorknight.pages.search.SearchPageITConstants.SEARCH_BUTTON_ID;
import static com.poorknight.pages.search.SearchPageITConstants.SEARCH_TEXT_ID;
import static com.poorknight.pages.search.SearchPageITConstants.VIEW_RECIPE_LINK_TEXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;
import java.util.LinkedList;
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

import com.google.common.collect.ImmutableList;
import com.poorknight.constants.ITConstants;
import com.poorknight.controller.LatestSearch;
import com.poorknight.controller.SearchRecipesController;
import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
@PersistenceTest
public class SearchPageOrderingIT {

	private static final String SEARCH_TEXT = "chicken basil";
	public static final String EXPECTED_1 = "(1)Chicken basil";
	public static final String EXPECTED_2 = "(2)Chicken basil";
	public static final String EXPECTED_3 = "(3)Chicken";
	public static final String EXPECTED_4 = "(4)chicken";
	public static final String EXPECTED_5 = "(5)Chicken";
	public static final String EXPECTED_6 = "(6)Chicken Chicken Chicken";
	public static final String EXPECTED_7 = "(7)hi";

	private WebElement searchText;
	private WebElement searchButton;
	private List<WebElement> recipeLinks;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createRecipePersistenceEnabledPageTestDeployment("SearchPageOrderingIT", SearchRecipesController.class,
				LatestSearch.class);
	}


	@Test
	@InSequence(1)
	@UsingDataSet("SearchPageOrderingIT.yml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void prepopulateDatabase() throws Exception {
		// empty on purpose, just letting @UsingDataSet do its thing
		// Check https://issues.jboss.org/browse/ARQ-1077
	}


	@Test
	@InSequence(2)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void searchResultsOrderedCorrectly(@ArquillianResource URL deploymentURL, @Drone WebDriver browser) throws Exception {
		List<String> expectedRecipeTitles = buildExpectedRecipeTitles();

		initPage(deploymentURL, browser);
		performSearch();

		List<String> actualRecipeTitles = findRecipeTitlesFromScreen();

		assertThat(actualRecipeTitles, equalTo(expectedRecipeTitles));
	}


	private List<String> findRecipeTitlesFromScreen() {
		List<String> recipeTitles = new LinkedList<>();
		for (WebElement recipeLink : this.recipeLinks) {
			String title = findRecipeTitleFromLink(recipeLink);
			recipeTitles.add(title);
		}
		return recipeTitles;
	}


	private String findRecipeTitleFromLink(WebElement recipeLink) {
		return recipeLink.findElement(By.xpath("../../*[1]")).getText(); // parent/parent/firstChild
	}


	private void performSearch() {
		searchText.clear();
		searchText.sendKeys(SEARCH_TEXT);
		Graphene.guardAjax(searchButton).click();
	}


	private void initPage(URL deploymentURL, WebDriver browser) {
		navigateToSearchPage(browser, deploymentURL);
		populateScreenElements(browser);
	}


	private void navigateToSearchPage(WebDriver browser, URL deploymentURL) {
		String searchPageURL = deploymentURL.toExternalForm() + ITConstants.SEARCH_RECIPES_PAGE;
		browser.navigate().to(searchPageURL);
	}


	private void populateScreenElements(WebDriver browser) {
		this.searchText = browser.findElement(By.id(SEARCH_TEXT_ID));
		this.searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));
		this.recipeLinks = browser.findElements(By.linkText(VIEW_RECIPE_LINK_TEXT));
	}


	private List<String> buildExpectedRecipeTitles() {
		return new ImmutableList.Builder<String>()//
				.add(EXPECTED_1)//
				.add(EXPECTED_2)//
				.add(EXPECTED_3)//
				.add(EXPECTED_4)//
				.add(EXPECTED_5)//
				.add(EXPECTED_6)//
				.add(EXPECTED_7).build();
	}
}
