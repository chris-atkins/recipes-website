package com.poorknight.pages.browse;

import static com.poorknight.constants.ITConstants.BROWSE_ALL_PAGE;
import static com.poorknight.constants.ITConstants.HOME_PAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.poorknight.utils.ArquillianUtils;


@RunWith(Arquillian.class)
public class BrowseAllRecipesPageIT {

	private static final String HOME_BUTTON_ID = "pageForm:gotoHomeButton";

	@Drone
	private WebDriver browser;

	@ArquillianResource
	private URL deploymentUrl;

	@FindBy(id = HOME_BUTTON_ID)
	private WebElement homeButton;


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ArquillianUtils.createBasicPageTestDeployment("BrowseAllRecipesPageIT");
	}


	@Before
	public void navigateToStartPage() {
		this.browser.get(getStartPage());
	}


	@Test
	public void testHomePageButtonExists() {
		assertThat(this.homeButton, notNullValue());
	}


	@Test
	public void testHomePageNavigation() {
		this.homeButton.click();
		assertThat(this.browser.getCurrentUrl(), endsWith(HOME_PAGE));
	}


	private String getStartPage() {
		return (this.deploymentUrl.toExternalForm() + BROWSE_ALL_PAGE);
	}
}
