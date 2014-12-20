package com.poorknight.pages;

import static com.poorknight.constants.ITConstants.BROWSE_ALL_PAGE;
import static com.poorknight.constants.ITConstants.HOME_PAGE;
import static com.poorknight.constants.ITConstants.VIEW_SINGLE_RECIPE_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
public class ViewSingleRecipePageIT {

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentURL;


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createBasicPageTestDeployment("ViewSinglePageIT");
	}


	@Test
	public void homeButtonExists() {
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id("pageForm:gotoHomeButton"));

		assertThat(homeButton, notNullValue());
	}


	@Test
	public void homeButtonNavigatesToHomePage() {
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id("pageForm:gotoHomeButton"));
		homeButton.click();

		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	@Test
	public void backButtonExists() {
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id("pageForm:goBackButton"));

		assertThat(homeButton, notNullValue());
	}


	@Test
	public void backButtonNavigatesToViewAllPage() {
		this.browser.get(viewSingleRecipePage());
		final WebElement homeButton = this.browser.findElement(By.id("pageForm:goBackButton"));
		homeButton.click();

		assertThat(this.browser.getCurrentUrl(), endsWith(BROWSE_ALL_PAGE));
	}


	private String viewSingleRecipePage() {
		return this.deploymentURL.toExternalForm() + VIEW_SINGLE_RECIPE_PAGE;
	}
}
