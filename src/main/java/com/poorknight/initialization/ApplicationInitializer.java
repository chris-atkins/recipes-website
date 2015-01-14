package com.poorknight.initialization;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


/**
 * Responsible for performing any application initialization that needs to happen on startup of the app.
 */
@Startup
@Singleton
public class ApplicationInitializer implements Serializable {

	private static final long serialVersionUID = 3104579660773177596L;

	@Inject
	private InitializationProcessesFactory factory;


	@PostConstruct
	public void initializeApplication() {
		for (final InitializationProcess initProcess : this.factory.getInitializationProcesses()) {
			initProcess.execute();
		}
	}
}
