package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static com.poorknight.testing.matchers.methods.MethodTransactionAnnotationMatcher.hasCorrectTransactionLevelOnMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.poorknight.testing.matchers.methods.MethodTransactionAnnotationMatcher.TransactionType;


@RunWith(Enclosed.class)
public class MethodTransactionAnnotationMatcherTest {

	@RunWith(JUnit4.class)
	public static class NonParameterizedTests {

		@Test
		public void hasCorrectFactoryMethod() {
			assertThat(MethodTransactionAnnotationMatcher.class, hasFactoryMethod());
		}


		@Test
		public void failsIfMethodDoesNotExist() {
			final boolean results = hasCorrectTransactionLevelOnMethod("nonExistantMethod", TransactionType.INSERT).matches(Object.class);
			assertThat(results, is(false));
		}


		@Test
		public void failsIfAllOverloadedMethodsDoNotHaveRequiredTransactionType() {
			final boolean results = hasCorrectTransactionLevelOnMethod("insertMethod", TransactionType.INSERT).matches(
					OverloadedInsertMethodWithMismatchedType.class);
			assertThat(results, is(false));
		}


		@Test
		public void passesIfAllOverloadedMethodsHaveRequiredTransactionType() {
			final boolean results = hasCorrectTransactionLevelOnMethod("insertMethod", TransactionType.INSERT).matches(
					OverloadedInsertMethodWithRequiredType.class);
			assertThat(results, is(true));
		}

	}

	@RunWith(Parameterized.class)
	public static class ParameterizedTests {

		private final Class<?> classToMatch;
		private final String methodToCheck;
		private final TransactionType transactionType;
		private final boolean shouldPassMatcherTest;


		public ParameterizedTests(final Class<?> classToMatch, final String methodToCheck, final TransactionType transactionType,
				final boolean shouldPassMatcherTest) {
			this.classToMatch = classToMatch;
			this.methodToCheck = methodToCheck;
			this.transactionType = transactionType;
			this.shouldPassMatcherTest = shouldPassMatcherTest;
		}


		@Parameters
		public static List<Object[]> generateData() {
			return Arrays.asList(new Object[][] {
					{ RequiredTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.TRUE },
					{ RequiredTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.TRUE },
					{ RequiredTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.FALSE },
					{ RequiredTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.TRUE },
					//
					{ RequiresNewTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.FALSE },
					{ RequiresNewTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.FALSE },
					{ RequiresNewTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.FALSE },
					{ RequiresNewTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.FALSE },
					//
					{ MandatoryTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.FALSE },
					{ MandatoryTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.FALSE },
					{ MandatoryTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.FALSE },
					{ MandatoryTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.FALSE },
					//
					{ NotSupportedTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.FALSE },
					{ NotSupportedTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.FALSE },
					{ NotSupportedTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.FALSE },
					{ NotSupportedTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.FALSE },
					//
					{ SupportsTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.FALSE },
					{ SupportsTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.FALSE },
					{ SupportsTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.TRUE },
					{ SupportsTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.FALSE },
					//
					{ NeverTestClass.class, "insertMethod", TransactionType.INSERT, Boolean.FALSE },
					{ NeverTestClass.class, "updateMethod", TransactionType.UPDATE, Boolean.FALSE },
					{ NeverTestClass.class, "queryMethod", TransactionType.QUERY, Boolean.FALSE },
					{ NeverTestClass.class, "deleteMethod", TransactionType.DELETE, Boolean.FALSE } });

		}


		@Test
		public void insertTransactionPassesForRequiredAttribute() {
			final boolean results = hasCorrectTransactionLevelOnMethod(this.methodToCheck, this.transactionType).matches(this.classToMatch);
			assertThat(results, is(this.shouldPassMatcherTest));
		}

	}
}


class RequiredTestClass {

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteMethod() {
		// empty on purpose
	}
}


class RequiresNewTestClass {

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteMethod() {
		// empty on purpose
	}
}


class MandatoryTestClass {

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void deleteMethod() {
		// empty on purpose
	}
}


class NotSupportedTestClass {

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void deleteMethod() {
		// empty on purpose
	}
}


class SupportsTestClass {

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void deleteMethod() {
		// empty on purpose
	}
}


class NeverTestClass {

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void insertMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void updateMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void queryMethod() {
		// empty on purpose
	}


	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void deleteMethod() {
		// empty on purpose
	}
}


class OverloadedInsertMethodWithRequiredType {

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void insertMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void insertMethod(final String s) {
		// empty on purpose
	}
}


class OverloadedInsertMethodWithMismatchedType {

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void insertMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void insertMethod(final String s) {
		// empty on purpose
	}
}
