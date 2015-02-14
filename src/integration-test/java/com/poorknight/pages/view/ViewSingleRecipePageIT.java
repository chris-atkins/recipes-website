package com.poorknight.pages.view;

import static com.poorknight.constants.ITConstants.HOME_PAGE;
import static com.poorknight.constants.ITConstants.VIEW_SINGLE_RECIPE_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
public class ViewSingleRecipePageIT {

	private static final String HOME_BUTTON_ID = "pageForm:gotoHomeButton";
	private static final String BACK_BUTTON_ID = "pageForm:goBackButton";

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentURL;

	@FindBy(id = HOME_BUTTON_ID)
	WebElement homeButton;

	@FindBy(id = BACK_BUTTON_ID)
	WebElement backButton;


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createPageTestDeploymentWithBackNavigation();
	}


	@Test
	public void homeButtonExists() {
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id(HOME_BUTTON_ID));

		assertThat(homeButton, notNullValue());
	}


	@Test
	public void homeButtonNavigatesToHomePage() {
		this.browser.get(viewSingleRecipePage());
		homeButton.click();

		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	@Test
	@InSequence(-1)
	// must be run first or other tests will have been navigated to
	public void backButtonDoesNotExist_IfNoPreviousPageHasBeenNavigatedTo() {
		this.browser.get(viewSingleRecipePage());
		final List<WebElement> backButtons = this.browser.findElements(By.id(BACK_BUTTON_ID));

		assertThat(backButtons, empty());
	}


	@Test
	public void backButtonDoesExist_IfAPreviousPageHasBeenNavigatedTo() {
		this.browser.get(homePage());
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id(BACK_BUTTON_ID));

		assertThat(homeButton, notNullValue());
	}


	@Test
	public void backButtonNavigatesToLastPage() {
		this.browser.get(homePage());
		this.browser.get(viewSingleRecipePage());
		backButton.click();

		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	private String viewSingleRecipePage() {
		return this.deploymentURL.toExternalForm() + VIEW_SINGLE_RECIPE_PAGE;
	}


	private String homePage() {
		return this.deploymentURL.toExternalForm() + HOME_PAGE;
	}
}
