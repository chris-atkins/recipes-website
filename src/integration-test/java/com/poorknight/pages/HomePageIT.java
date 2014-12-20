package com.poorknight.pages;

import static com.poorknight.constants.ITConstants.HOME_PAGE;
import static com.poorknight.constants.ITConstants.SEARCH_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

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
public class HomePageIT {

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentUrl;


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createBasicPageTestDeployment("HomePageIT");
	}


	@Test
	public void searchButtonNavigatesToSearchPage() throws Exception {
		this.browser.get(getHomePage());

		final WebElement button = this.browser.findElement(By.id("pageForm:gotoSearchButton"));
		button.click();

		final String newUrl = this.browser.getCurrentUrl();
		assertThat(newUrl, endsWith(SEARCH_PAGE));
	}


	@Test
	public void viewAllButtonNavigatesToViewAllPage() throws Exception {
		this.browser.get(getHomePage());

		final WebElement button = this.browser.findElement(By.id("pageForm:gotoBrowseButton"));
		button.click();

		final String newUrl = this.browser.getCurrentUrl();
		assertThat(newUrl, endsWith("browse/browseAllRecipes.jsf"));
	}


	private String getHomePage() {
		return this.deploymentUrl.toExternalForm() + HOME_PAGE;
	}
}
