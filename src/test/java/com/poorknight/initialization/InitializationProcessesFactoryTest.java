package com.poorknight.initialization;

import static com.poorknight.testing.matchers.CustomMatchers.isRequestScoped;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class InitializationProcessesFactoryTest {

	private final InitializationProcessesFactory factory = new InitializationProcessesFactory();


	@Test
	public void requestScoped() throws Exception {
		assertThat(InitializationProcessesFactory.class, isRequestScoped());
	}

	// @Test
	// public void returnsEmptyList() {
	// assertThat(this.factory.getInitializationProcesses(), emptyCollectionOf(InitializationProcess.class));
	// }

}
