package com.poorknight.pages;

import static com.poorknight.utils.ArquillianUtils.buildLibraryFromPom;
import static com.poorknight.utils.ArquillianUtils.createBasicPageTestDeployment;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
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


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return createBasicPageTestDeployment("SearchRecipesPageWithDatasetIT", SearchRecipesController.class, SearchRecipeService.class,
				RecipeDAO.class, Recipe.class, DaoException.class, CollectionUtils.class, Recipe_.class, WebDriver.class, SearchContext.class)//
				.addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml")//
				.addAsLibrary(buildLibraryFromPom("commons-collections", "commons-collections", "3.2.1"));
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
		navigateToSearchRecipesPage(deploymentURL, browser);
		final WebElement searchText = browser.findElement(By.id(SEARCH_TEXT_ID));
		final WebElement searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));

		searchText.sendKeys("Recipe");
		Graphene.guardAjax(searchButton).click();
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), containsString("Recipe2 Name"));
	}


	@Test
	@InSequence(3)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void findsRecipesByContent(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		navigateToSearchRecipesPage(deploymentURL, browser);
		final WebElement searchText = browser.findElement(By.id(SEARCH_TEXT_ID));
		final WebElement searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));

		searchText.sendKeys("OHAI");
		Graphene.guardAjax(searchButton).click();
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), not(containsString("Recipe2 Name")));
	}


	@Test
	@InSequence(4)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void findsRecipes_ForEitherOfSpaceDelimitedSearchStrings(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		navigateToSearchRecipesPage(deploymentURL, browser);
		final WebElement searchText = browser.findElement(By.id(SEARCH_TEXT_ID));
		final WebElement searchButton = browser.findElement(By.id(SEARCH_BUTTON_ID));

		searchText.sendKeys("Recipe1 Recipe2");
		Graphene.guardAjax(searchButton).click();
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
		assertThat(browser.getPageSource(), containsString("Recipe2 Name"));
	}


	public void navigateToSearchRecipesPage(final URL deploymentURL, final WebDriver browser) {
		final String startURL = deploymentURL.toExternalForm() + ITConstants.SEARCH_RECIPES_PAGE;
		browser.get(startURL);
	}
}
