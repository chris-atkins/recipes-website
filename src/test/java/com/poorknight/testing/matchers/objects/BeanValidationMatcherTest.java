package com.poorknight.testing.matchers.objects;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.BeanValidationTestClass;


@RunWith(Enclosed.class)
public class BeanValidationMatcherTest {

	@RunWith(JUnit4.class)
	public static class GenericBeanValidationMatcherTest {

		@Test
		public void usesFactoryMethod() throws Exception {
			assertThat(BeanValidationMatcher.class, hasFactoryMethod());
		}


		@Test
		public void doesntThrowException_WithELNotationsHavingInternalELNotations() throws Exception {
			final BeanValidationMatcher matcher = BeanValidationMatcher.failsValidation();

			// will fail validation with this error message: 'must be greater than or equal to {value}'
			// https://svn.apache.org/repos/asf/tapestry/tapestry5/trunk/tapestry-beanvalidator/src/test/resources/ValidationMessages_en.properties
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass(-1);

			final boolean result = matcher.matches(objectToTest);

			assertThat(result, equalTo(true));
		}

	}

	@RunWith(JUnit4.class)
	public static class PassesBeanValidationMatcherTest {

		BeanValidationMatcher matcher = BeanValidationMatcher.passesValidation();


		@Test
		public void passesWhenValidationsShouldPass() throws Exception {
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass("hi", 1);

			final boolean result = this.matcher.matches(objectToTest);

			assertThat(result, equalTo(true));
		}


		@Test
		public void failsWithSingleFailure() throws Exception {
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass(null, 1);

			final boolean result = this.matcher.matches(objectToTest);

			assertThat(result, equalTo(false));
		}


		@Test(expected = IllegalArgumentException.class)
		public void exceptionIfNoValidationIsPresent() throws Exception {
			final Object objectToTest = new Object();
			this.matcher.matches(objectToTest); // expecting exception here, since no validation is present on Object
		}
	}

	@RunWith(JUnit4.class)
	public static class FailsBeanValidationMatcherTest {

		BeanValidationMatcher matcher = BeanValidationMatcher.failsValidation();


		@Test
		public void failsWhenValidationsShouldPass() throws Exception {
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass("hi", 1);

			final boolean result = this.matcher.matches(objectToTest);

			assertThat(result, equalTo(false));
		}


		@Test
		public void passesWithSingleFailure() throws Exception {
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass(null, 1);

			final boolean result = this.matcher.matches(objectToTest);

			assertThat(result, equalTo(true));
		}


		@Test
		public void failsWithMoreThanOneValidationFailure() throws Exception {
			final BeanValidationTestClass objectToTest = new BeanValidationTestClass(null, null);

			final boolean result = this.matcher.matches(objectToTest);

			assertThat(result, equalTo(false));
		}


		@Test(expected = IllegalArgumentException.class)
		public void exceptionIfNoValidationIsPresent() throws Exception {
			final Object objectToTest = new Object();
			this.matcher.matches(objectToTest); // expecting exception here, since no validation is present on Object
		}
	}

}
