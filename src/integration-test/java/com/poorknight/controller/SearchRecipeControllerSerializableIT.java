package com.poorknight.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.constants.ITConstants;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.testing.matchers.objects.IsSerializableMatcher;
import com.poorknight.utils.ArquillianUtils;


@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class SearchRecipeControllerSerializableIT {

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL deploymentUrl;


	@Deployment(testable = true)
	public static Archive<?> createDeployment() {
		return ArquillianUtils.createRecipePersistenceEnabledPageTestDeployment("SearchRecipeControllerSerializableIT",
				SearchRecipesController.class, SearchRecipeService.class, LatestSearch.class, IsSerializableMatcher.class);
	}


	@Test
	public void controllerIsSerializableInContainer() throws Exception {
		Warp.initiate(new Activity() {

			@Override
			public void perform() {
				SearchRecipeControllerSerializableIT.this.browser.get(getSearchPage());
			}


			private String getSearchPage() {
				return SearchRecipeControllerSerializableIT.this.deploymentUrl.toExternalForm() + ITConstants.SEARCH_RECIPES_PAGE;
			}

		}).inspect(new Inspection() {

			private static final long serialVersionUID = -8709047973585367940L;

			@Inject
			private RecipeDAO dao;

			@Inject
			private UserTransaction transaction;


			// @BeforePhase(Phase.INVOKE_APPLICATION)
			@BeforeServlet
			public void populateDatabase() throws Exception {
				this.transaction.begin();

				this.dao.saveNewRecipe(new Recipe("testName1", "testDesc1"));
				this.dao.saveNewRecipe(new Recipe("testName2", "testDesc2"));

				this.transaction.commit();

				assertThat(this.dao.queryAllRecipes().size(), Matchers.equalTo(2));
			}


			@AfterPhase(Phase.RENDER_RESPONSE)
			// @AfterServlet
			public void testSerializability() {

				final FacesContext context = FacesContext.getCurrentInstance();
				final SearchRecipesController controller = context.getApplication().evaluateExpressionGet(context, "#{searchRecipesController}",
						SearchRecipesController.class);

				controller.setSearchString("test");
				controller.search();
				System.out.println(controller.toString());
				assertThat(controller.getFoundRecipes().size(), equalTo(2));
				assertThat(controller, IsSerializableMatcher.isTrulySerializable());
			}
		});
	}

}
