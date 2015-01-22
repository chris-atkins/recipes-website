package com.poorknight.pages.browse;

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
import com.poorknight.controller.BrowseAllController;
import com.poorknight.controller.ViewRecipeController;
import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
@PersistenceTest
public class BrowseAllRecipesPageWithDatasetIT {

	private static final String BACK_BUTTON_ID = "pageForm:goBackButton";
	private static final String LINK_TEXT = "View";
	private List<WebElement> viewRecipeLinks;


	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createRecipePersistenceEnabledPageTestWithNavigation(BrowseAllController.class, ViewRecipeController.class);
	}


	@Test
	@InSequence(1)
	@UsingDataSet("BrowseAllRecipesPageITData.yml")
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void populateDatabase() throws Exception {
		// empty on purpose, just letting @UsingDataSet do its thing
		// Check https://issues.jboss.org/browse/ARQ-1077
	}


	@Test
	@InSequence(2)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.NONE)
	public void testTheCorrectNumberOfLinksExistOnScreen(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser)
			throws Exception {
		initPage(deploymentURL, browser);
		assertThat(this.viewRecipeLinks.size(), equalTo(3));
	}


	@Test
	@InSequence(3)
	@RunAsClient
	@Cleanup(phase = TestExecutionPhase.AFTER)
	public void testLinkTwoNavigationAndBack(@ArquillianResource final URL deploymentURL, @Drone final WebDriver browser) throws Exception {
		initPage(deploymentURL, browser);

		navigateToViewRecipeOne_WhenViewIsClicked(browser);
		returnsToBrowsePage_WhenBackIsClicked(browser);
		allTheLinksAreStillDisplayed(browser);
	}


	private void initPage(final URL deploymentURL, final WebDriver browser) {
		navigateToBrowseAllPage(deploymentURL, browser);
		initFields(browser);
	}


	private void navigateToBrowseAllPage(final URL fromRoot, final WebDriver browser) {
		browser.navigate().to(browsePage(fromRoot));
	}


	private String browsePage(final URL rootUrl) {
		return rootUrl.toExternalForm().concat(ITConstants.BROWSE_ALL_PAGE);
	}


	private void initFields(final WebDriver browser) {
		this.viewRecipeLinks = browser.findElements(By.linkText(LINK_TEXT));
	}


	private void navigateToViewRecipeOne_WhenViewIsClicked(final WebDriver browser) {
		final WebElement firstLink = this.viewRecipeLinks.get(0);
		Graphene.guardHttp(firstLink).click();
		assertThat(browser.getCurrentUrl(), containsString(ITConstants.VIEW_SINGLE_RECIPE_PAGE));
		assertThat(browser.getPageSource(), containsString("Recipe1 Name"));
	}


	private void returnsToBrowsePage_WhenBackIsClicked(final WebDriver browser) {
		final WebElement backButton = browser.findElement(By.id(BACK_BUTTON_ID));
		Graphene.guardHttp(backButton).click();
		assertThat(browser.getCurrentUrl(), endsWith(ITConstants.BROWSE_ALL_PAGE));
	}


	private void allTheLinksAreStillDisplayed(final WebDriver browser) {
		final List<WebElement> newLinks = browser.findElements(By.linkText(LINK_TEXT));
		assertThat(newLinks.size(), equalTo(3));
	}

}
