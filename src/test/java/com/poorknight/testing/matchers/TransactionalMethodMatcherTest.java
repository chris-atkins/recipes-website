package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.utils.ReflectionUtils;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ReflectionUtils.class)
public class TransactionalMethodMatcherTest {

	private static final String TEST_METHOD_NAME = "testMethod";
	TransactionalMethodMatcher transactionalMethodMatcher;


	@Test
	public void usesFactoryMethod() {
		assertThat(TransactionalMethodMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesMethodWithTransactionAnnotation() {
		this.transactionalMethodMatcher = TransactionalMethodMatcher.createsTransactionBoundaryOnMethod("methodWithTransactionAnnotation");

		final boolean results = this.transactionalMethodMatcher.matches(ClassUnderTest.class);
		assertThat(results, is(true));
	}


	@Test
	public void failsMethodWithoutTransactionAnnotation() {
		this.transactionalMethodMatcher = TransactionalMethodMatcher.createsTransactionBoundaryOnMethod("methodWithoutTransactionAnnotation");

		final boolean results = this.transactionalMethodMatcher.matches(ClassUnderTest.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWhenNoVisibleMethodNameIsFound() {
		PowerMockito.mockStatic(ReflectionUtils.class);
		when(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ClassUnderTest.class, TEST_METHOD_NAME)).thenReturn(false);

		this.transactionalMethodMatcher = TransactionalMethodMatcher.createsTransactionBoundaryOnMethod(TEST_METHOD_NAME);

		final boolean results = this.transactionalMethodMatcher.matches(ClassUnderTest.class);
		assertThat(results, is(false));
	}

	// ////////////////////////////////////////////////////////////

	static class ClassUnderTest {

		public void methodWithoutTransactionAnnotation() {
			// empty on purpose
		}


		@Transactional
		public void methodWithTransactionAnnotation() {
			// empty on purpose
		}
	}

}
