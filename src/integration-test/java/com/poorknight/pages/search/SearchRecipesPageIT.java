package com.poorknight.pages.search;

import static com.poorknight.utils.ArquillianUtils.commonsLang;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
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
import com.poorknight.controller.LatestSearch;
import com.poorknight.controller.SearchRecipesController;
import com.poorknight.utils.ArquillianUtils;


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
		return ArquillianUtils.createRecipePersistenceEnabledPageTestDeployment("SearchRecipesPageIT", SearchRecipesController.class,
				SearchRecipeService.class, LatestSearch.class, StringUtils.class).addAsLibrary(commonsLang());
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
	public void searchTextInputStartsWithFocus() throws Exception {
		assertThat(this.searchText.getAttribute("class"), containsString("start-with-focus"));
	}


	@Test
	public void onlyOneElement_StartsWithFocus() throws Exception {
		String pageSource = this.browser.getPageSource();
		assertThat(pageSource.lastIndexOf("start-with-focus"), equalTo(pageSource.indexOf("start-with-focus")));
	}


	@Test
	public void errorMessageStartsEmpty() throws Exception {
		assertThat(this.errorMessage, notNullValue());
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	@Test
	public void errorMessageOccursOnEmptySearchText() throws Exception {
		search("");
		assertThat(this.errorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void errorMessageOnWhitespaceSearchText() throws Exception {
		search("    ");
		assertThat(this.errorMessage.getText(), not(isEmptyString()));
	}


	@Test
	public void errorMessageGoesAwayAfterGoodSearch() throws Exception {
		search("    ");
		assertThat(this.errorMessage.getText(), not(isEmptyString()));

		search("ohai");
		assertThat(this.errorMessage.getText(), isEmptyString());
	}


	@Test(expected = NoSuchElementException.class)
	public void emptyResultsMessage_DoesNotExistOnPageLoad() throws Exception {
		this.noResultsMessage.getText();  // call the web element - there will not be an exception if it exists, so the test would fail in that case
	}


	@Test
	public void emptyResultsMessage_ExistsOnNoResultSearch() throws Exception {
		search("ohai");
		assertThat(this.noResultsMessage.getText(), not(isEmptyString()));
	}


	private void search(final String searchString) {
		this.searchText.sendKeys(searchString);
		Graphene.guardAjax(this.searchButton).click();
	}
}
