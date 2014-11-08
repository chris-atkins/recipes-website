package com.poorknight.initialization;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;


/**
 * Responsible for performing any application initialization that needs to happen on startup of the app.
 */
@Startup
@Singleton
public class ApplicationInitializer {

	@PostConstruct
	public void initializeApplication() {
		// empty on purpose
	}
}
