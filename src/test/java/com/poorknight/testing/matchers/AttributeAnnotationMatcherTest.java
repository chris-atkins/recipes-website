package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.testing.matchers.AttributeAnnotationMatcher.Cardinality;
import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;
import com.poorknight.testing.matchers.utils.ReflectionUtils;


@RunWith(Enclosed.class)
public class AttributeAnnotationMatcherTest {

	@RunWith(JUnit4.class)
	public static class BasicAttributeAnnotationMatcherTests {

		@Test
		public void usesFactoryMethod() {
			assertThat(AttributeAnnotationMatcher.class, hasFactoryMethod());
		}

	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AttributeAnnotationMatcherTestForExactlyOne {

		private final AttributeAnnotationMatcher attributeAnnotationMatcher = AttributeAnnotationMatcher.hasAnnotationOnAttribute(
				RuntimeRetention.class, Cardinality.EXACTLY_ONE);


		@Before
		public void initializeMocks() {
			PowerMockito.mockStatic(ReflectionUtils.class);
		}


		@Test
		public void passesWhenExactlyOneAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(1);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(true));
		}


		@Test
		public void failsWhenNoAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(0);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(false));
		}


		@Test
		public void failsWhenTwoAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(2);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(false));
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllFieldsWithAnnotation(Object.class, RuntimeRetention.class)).thenReturn(
					FieldBuilder.fieldListOfSize(size));
		}
	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AttributeAnnotationMatcherTestForAtLeastOne {

		private final AttributeAnnotationMatcher attributeAnnotationMatcher = AttributeAnnotationMatcher.hasAnnotationOnAttribute(
				RuntimeRetention.class, Cardinality.AT_LEAST_ONE);


		@Before
		public void initializeMocks() {
			PowerMockito.mockStatic(ReflectionUtils.class);
		}


		@Test
		public void passesWhenExactlyOneAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(1);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(true));
		}


		@Test
		public void failsWhenNoAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(0);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(false));
		}


		@Test
		public void passesWhenTwoAttributeHasAnnotation() {
			setUpMockToReturnListOfSize(2);

			final boolean result = this.attributeAnnotationMatcher.matches(Object.class);
			assertThat(result, is(true));
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllFieldsWithAnnotation(Object.class, RuntimeRetention.class)).thenReturn(
					FieldBuilder.fieldListOfSize(size));
		}
	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AttributeAnnotationMatcherTestForAllSubMatchersCalled {

		private AttributeAnnotationMatcher attributeAnnotationMatcher;

		private final Class<Object> classBeingTested = Object.class;

		private Field fieldBeingReturned;


		@Before
		public void initializeMocks() {
			PowerMockito.mockStatic(ReflectionUtils.class);
			setUpMockToReturnListOfSize(1);
			this.fieldBeingReturned = ReflectionUtils.findAllFieldsWithAnnotation(this.classBeingTested, RuntimeRetention.class).get(0);
		}


		@Test
		public void allSubMatchersAreCalledWhenTheyAllPassTheMatchTest() {
			final Matcher<Field>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.attributeAnnotationMatcher = AttributeAnnotationMatcher.hasAnnotationOnAttribute(RuntimeRetention.class, Cardinality.EXACTLY_ONE,
					mockMatchers);

			this.attributeAnnotationMatcher.matches(this.classBeingTested);

			for (final Matcher<Field> mock : mockMatchers) {
				Mockito.verify(mock).matches(this.fieldBeingReturned);
			}
		}


		@Test
		public void failsIfAnyOneSubMatcherFailsTheMatchTest() {
			final Matcher<Field>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.attributeAnnotationMatcher = AttributeAnnotationMatcher.hasAnnotationOnAttribute(RuntimeRetention.class, Cardinality.EXACTLY_ONE,
					mockMatchers);

			// set up one to not match
			final Matcher<Field> notMatching = mockMatchers[9];
			Mockito.when(notMatching.matches(this.fieldBeingReturned)).thenReturn(false);

			// check that whole result is false
			final boolean result = this.attributeAnnotationMatcher.matches(this.classBeingTested);
			assertThat(result, is(false));

			// also check that the mismatch description is called on the one that fails
			Mockito.verify(notMatching).describeMismatch(Matchers.eq(this.fieldBeingReturned), Matchers.any(Description.class));
		}


		@Test
		public void passesIfAllSubMatcherPassTheMatchTest() {
			final Matcher<Field>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.attributeAnnotationMatcher = AttributeAnnotationMatcher.hasAnnotationOnAttribute(RuntimeRetention.class, Cardinality.EXACTLY_ONE,
					mockMatchers);

			final boolean result = this.attributeAnnotationMatcher.matches(this.classBeingTested);
			assertThat(result, is(true));
		}


		@SuppressWarnings("unchecked")
		private Matcher<Field>[] buildMockMatchersOfSizeReturning(final int size, final boolean result) {

			final Matcher<Field>[] matchers = new Matcher[size];
			for (int i = 0; i < size; i++) {

				final Matcher<Field> template = buildTemplateMatcher();
				matchers[i] = Mockito.mock(template.getClass());
				Mockito.when(matchers[i].matches(this.fieldBeingReturned)).thenReturn(result);
			}
			return matchers;
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllFieldsWithAnnotation(this.classBeingTested, RuntimeRetention.class)).thenReturn(
					FieldBuilder.fieldListOfSize(size));
		}


		private Matcher<Field> buildTemplateMatcher() {
			return new Matcher<Field>() {

				@Override
				public void describeTo(final Description description) {
					throw new RuntimeException("Not implemented");
				}


				@Override
				public boolean matches(final Object item) {
					throw new RuntimeException("Not implemented");
				}


				@Override
				public void describeMismatch(final Object item, final Description mismatchDescription) {
					throw new RuntimeException("Not implemented");
				}


				@Override
				public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
					throw new RuntimeException("Not implemented");
				}
			};
		}
	}

}
