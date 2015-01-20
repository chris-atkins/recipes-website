package com.poorknight.utils;

import java.io.File;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.business.searchrecipe.SearchRecipeService;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.Recipe_;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.exceptions.DaoException;
import com.poorknight.listeners.servlet.HttpRequestListener;
import com.poorknight.navigation.Location;
import com.poorknight.navigation.NavigationRequestListener;
import com.poorknight.navigation.NavigationTracker;


public class ArquillianUtils {

	private static final String MANDATORY_WAR_NAME_FOR_NAV_TO_WORK_LOCALHOST_PREFIX = "recipee7";
	private static final String PAGES_ROOT = "src/main/webapp";
	private static final String CONFIG_ROOT = "src/main/webapp";
	private static final String MESSAGES_ROOT = "src/main/resources/messages";


	public static WebArchive createBasicPageTestDeployment(final String webArchiveName, final Class<?>... classes) {
		return ShrinkWrap.create(WebArchive.class, buildArchiveName(webArchiveName)) //

				// classes
				.addClasses(classes)//

				// include all pages
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(PAGES_ROOT).as(GenericArchive.class), "/",
						Filters.include(".*\\.xhtml$")) //

				// .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml")//

				// config files
				.addAsWebInfResource(new File(CONFIG_ROOT, "WEB-INF/web.xml"))//
				.addAsWebInfResource(new File(CONFIG_ROOT, "WEB-INF/beans.xml"))//
				.addAsWebInfResource(new File(CONFIG_ROOT, "WEB-INF/navigation.xml"))//
				.addAsWebInfResource(new File(CONFIG_ROOT, "WEB-INF/faces-config.xml"))//

				// messages
				.addAsResource(new File(MESSAGES_ROOT));
	}


	public static WebArchive createPageTestDeploymentWithBackNavigation(final Class<?>... classes) {
		return createBasicPageTestDeployment(MANDATORY_WAR_NAME_FOR_NAV_TO_WORK_LOCALHOST_PREFIX, classes).addClasses(
				NavigationRequestListener.class, NavigationTracker.class, Location.class, HttpRequestListener.class);
	}


	public static WebArchive createRecipePersistenceEnabledPageTestWithNavigation(final Class<?>... classes) {

		return createPageTestDeploymentWithBackNavigation(classes)
				.addClasses(SearchRecipeService.class, SaveRecipeService.class, RecipeDAO.class, Recipe.class, DaoException.class,
						CollectionUtils.class, Recipe_.class, WebDriver.class, SearchContext.class, WebElement.class)//
				.addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml")//
				.addAsLibrary(buildLibraryFromPom("commons-collections", "commons-collections", "3.2.1"));
	}


	public static File buildLibraryFromPom(final String groupId, final String artifactId, final String version) {
		return Maven.resolver().resolve(groupId + ":" + artifactId + ":" + version).withTransitivity().asSingleFile();
	}


	private static String buildArchiveName(final String webArchiveName) {
		return webArchiveName + ".war";
	}

}
