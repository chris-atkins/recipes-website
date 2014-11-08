package com.poorknight.integration.utils;

import java.io.File;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;


public class ArquillianUtils {

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


	private static String buildArchiveName(final String webArchiveName) {
		return webArchiveName + ".war";
	}

}
