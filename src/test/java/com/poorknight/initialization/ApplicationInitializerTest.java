package com.poorknight.initialization;

import static com.poorknight.testing.matchers.CustomMatchers.hasAPostConstructMethod;
import static com.poorknight.testing.matchers.CustomMatchers.hasAnnotation;
import static com.poorknight.testing.matchers.CustomMatchers.isAProperSingleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Startup;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.utils.UnitTestUtils;


@RunWith(Enclosed.class)
public class ApplicationInitializerTest {

	@RunWith(JUnit4.class)
	public static class ApplicationInitializerGeneralTests {

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

	@RunWith(MockitoJUnitRunner.class)
	public static class ApplicationInitializerMockitoTests {

		@InjectMocks
		private ApplicationInitializer applicationInitializer;

		@Mock
		private InitializationProcessesFactory factory;

		private List<InitializationProcess> mockProcesses;


		@Before
		public void initMocks() {
			final int numberOfResults = RandomUtils.nextInt(10);
			this.mockProcesses = buildMockProcesses(numberOfResults);
			when(this.factory.getInitializationProcesses()).thenReturn(this.mockProcesses);
		}


		@Test
		public void postConstructMethod_CallsAllInitializationProcesses() throws Exception {

			UnitTestUtils.callPostConstructMethod(this.applicationInitializer);

			for (final InitializationProcess initializationProcess : this.mockProcesses) {
				verify(initializationProcess).execute();
			}
		}


		private List<InitializationProcess> buildMockProcesses(final int count) {
			final List<InitializationProcess> results = new LinkedList<>();
			for (int i = 0; i < count; i++) {
				results.add(mock(InitializationProcess.class));
			}
			return results;
		}
	}
}
