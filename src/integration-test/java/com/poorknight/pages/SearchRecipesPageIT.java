package com.poorknight.pages;

import static com.poorknight.utils.ArquillianUtils.buildLibraryFromPom;
import static com.poorknight.utils.ArquillianUtils.createBasicPageTestDeployment;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.constants.ITConstants;
import com.poorknight.controller.SearchRecipesController;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.domain.Recipe_;
import com.poorknight.exceptions.DaoException;


@RunWith(Arquillian.class)
public class SearchRecipesPageIT {

	private static final String SEARCH_BUTTON_ID = "pageForm:searchButton";
	private static final String SEARCH_TEXT_ID = "pageForm:searchInput";
	private static final String ERROR_MESSAGE_ID = "pageForm:searchInputMessage";
	private static final String NO_RESULTS_MESSAGE_ID = "pageForm:noResultsMessage";

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentUrl;

	@FindBy(id = SEARCH_BUTTON_ID)
	private WebElement searchButton;

	@FindBy(id = SEARCH_TEXT_ID)
	private WebElement searchText;

	@FindBy(id = ERROR_MESSAGE_ID)
	private WebElement errorMessage;

	@FindBy(id = NO_RESULTS_MESSAGE_ID)
	private WebElement noResultsMessage;


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return createBasicPageTestDeployment("SearchRecipesPageIT", SearchRecipesController.class, SearchRecipeService.class, RecipeDAO.class,
				Recipe.class, DaoException.class, CollectionUtils.class, Recipe_.class)//
				.addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml")//
				.addAsLibrary(buildLibraryFromPom("commons-collections", "commons-collections", "3.2.1"));
	}


	@Before
	public void navigateToSearchRecipesPage() {
		final String startURL = this.deploymentUrl.toExternalForm() + ITConstants.SEARCH_RECIPES_PAGE;
		this.browser.get(startURL);
	}


	@Test
	public void searchTextInputStartsEmpty() throws Exception {
		assertThat(this.searchText.getText(), isEmptyString());
	}


	@Test
	public void errorMessageStartsEmpty() throws Exception {
		assertThat(this.errorMessage, notNullValue());
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	@Test
	public void errorMessageOccursOnEmptySearchText() throws Exception {
		Graphene.guardAjax(this.searchButton).click();
		assertThat(this.errorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void errorMessageOnWhitespaceSearchText() throws Exception {
		this.searchText.sendKeys("    ");
		Graphene.guardAjax(this.searchButton).click();
		assertThat(this.errorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void errorMessageGoesAwayAfterGoodSearch() throws Exception {
		this.searchText.sendKeys("    ");
		Graphene.guardAjax(this.searchButton).click();
		assertThat(this.errorMessage.getText(), not(isEmptyString()));

		this.searchText.sendKeys("ohai");
		Graphene.guardAjax(this.searchButton).click();
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	@Test(expected = NoSuchElementException.class)
	public void noEmptyResultsMessageExists_OnPageLoad() throws Exception {
		this.noResultsMessage.getText();  // call the web element - there will be no exception if it exists
	}


	@Test
	public void noResultsMessageExists_OnNoResultSearch() throws Exception {
		this.searchText.sendKeys("ohai");
		Graphene.guardAjax(this.searchButton).click();
		assertThat(this.noResultsMessage.getText(), not(isEmptyString()));
	}
}
