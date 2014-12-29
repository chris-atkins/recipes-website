package com.poorknight.initialization;

import static com.poorknight.testing.matchers.CustomMatchers.hasAPostConstructMethod;
import static com.poorknight.testing.matchers.CustomMatchers.hasAnnotation;
import static com.poorknight.testing.matchers.CustomMatchers.isAProperSingleton;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ejb.Startup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ApplicationInitializerTest {

	@Test
	public void properSingleton() throws Exception {
		assertThat(ApplicationInitializer.class, isAProperSingleton());
	}


	@Test
	public void hasStartupAnnotation() throws Exception {
		assertThat(ApplicationInitializer.class, hasAnnotation(Startup.class));
	}


	@Test
	public void hasPostConstructMethod() throws Exception {
		assertThat(ApplicationInitializer.class, hasAPostConstructMethod());
	}
}
