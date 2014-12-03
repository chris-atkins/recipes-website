package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

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

import com.poorknight.testing.matchers.Cardinality;
import com.poorknight.utils.ReflectionUtils;


@RunWith(Enclosed.class)
public class AnnotatedMethodsMatcherTest {

	@RunWith(JUnit4.class)
	public static class BasicAnnotatedMethodMatcherTests {

		@Test
		public void usesFactoryMethod() throws Exception {
			assertThat(AnnotatedMethodsMatcher.class, hasFactoryMethod());
		}
	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AnnotatedMethodsMatcherTestForExactlyOne {

		AnnotatedMethodsMatcher matcher = AnnotatedMethodsMatcher.hasAnnotationOnExactlyOneMethod(PostConstruct.class);


		@Before
		public void init() {
			PowerMockito.mockStatic(ReflectionUtils.class);
		}


		@Test
		public void passesWhenOnlyOneMethodHasAnnotation() throws Exception {
			setUpMockToReturnListOfSize(1);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(true));
		}


		@Test
		public void failsWhenNoMethodHasAnnotation() throws Exception {
			setUpMockToReturnListOfSize(0);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(false));
		}


		@Test
		public void failsWhenTwoMethodsHaveAnnotation() throws Exception {
			setUpMockToReturnListOfSize(2);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(false));
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllMethodsInClassWithAnnotation(Object.class, PostConstruct.class)).thenReturn(
					MethodBuilder.methodListOfSize(size));
		}
	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AnnotatedMethodsMatcherTestForAtLeastOne {

		AnnotatedMethodsMatcher matcher = AnnotatedMethodsMatcher.hasAnnotationOnAtLeastOneMethod(PostConstruct.class);


		@Before
		public void init() {
			PowerMockito.mockStatic(ReflectionUtils.class);
		}


		@Test
		public void passesWhenOnlyOneMethodHasAnnotation() throws Exception {
			setUpMockToReturnListOfSize(1);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(true));
		}


		@Test
		public void failsWhenNoMethodHasAnnotation() throws Exception {
			setUpMockToReturnListOfSize(0);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(false));
		}


		@Test
		public void passesWhenTwoMethodsHaveAnnotation() throws Exception {
			setUpMockToReturnListOfSize(2);

			final boolean result = this.matcher.matchesSafely(Object.class, Description.NONE);
			assertThat(result, equalTo(true));
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllMethodsInClassWithAnnotation(Object.class, PostConstruct.class)).thenReturn(
					MethodBuilder.methodListOfSize(size));
		}
	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class AnnotatedMethodsMatcherTestForAllSubMatchersCalled {

		private AnnotatedMethodsMatcher matcher;

		private final Class<Object> classBeingTested = Object.class;

		private Method methodBeingReturned;


		@Before
		public void initializeMocks() {
			PowerMockito.mockStatic(ReflectionUtils.class);
			setUpMockToReturnListOfSize(1);
			this.methodBeingReturned = ReflectionUtils.findAllMethodsInClassWithAnnotation(this.classBeingTested, PostConstruct.class).get(0);
		}


		@Test
		public void allSubMatchersAreCalledWhenTheyAllPassTheMatchTest() {
			final Matcher<Method>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.matcher = AnnotatedMethodsMatcher.hasAnnotationOnMethod(PostConstruct.class, Cardinality.EXACTLY_ONE, mockMatchers);

			this.matcher.matches(this.classBeingTested);

			for (final Matcher<Method> mock : mockMatchers) {
				Mockito.verify(mock).matches(this.methodBeingReturned);
			}
		}


		@Test
		public void failsIfAnyOneSubMatcherFailsTheMatchTest() {
			final Matcher<Method>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.matcher = AnnotatedMethodsMatcher.hasAnnotationOnMethod(PostConstruct.class, Cardinality.EXACTLY_ONE, mockMatchers);

			// set up one to not match
			final Matcher<Method> notMatching = mockMatchers[9];
			Mockito.when(notMatching.matches(this.methodBeingReturned)).thenReturn(false);

			// check that whole result is false
			final boolean result = this.matcher.matches(this.classBeingTested);
			assertThat(result, is(false));

			// also check that the mismatch description is called on the one that fails
			Mockito.verify(notMatching).describeMismatch(Matchers.eq(this.methodBeingReturned), Matchers.any(Description.class));
		}


		@Test
		public void passesIfAllSubMatcherPassTheMatchTest() {
			final Matcher<Method>[] mockMatchers = buildMockMatchersOfSizeReturning(10, true);
			this.matcher = AnnotatedMethodsMatcher.hasAnnotationOnMethod(PostConstruct.class, Cardinality.EXACTLY_ONE, mockMatchers);

			final boolean result = this.matcher.matches(this.classBeingTested);
			assertThat(result, is(true));
		}


		@SuppressWarnings("unchecked")
		private Matcher<Method>[] buildMockMatchersOfSizeReturning(final int size, final boolean result) {

			final Matcher<Method>[] matchers = new Matcher[size];
			for (int i = 0; i < size; i++) {

				final Matcher<Method> template = buildTemplateMatcher();
				matchers[i] = Mockito.mock(template.getClass());
				Mockito.when(matchers[i].matches(this.methodBeingReturned)).thenReturn(result);
			}
			return matchers;
		}


		private void setUpMockToReturnListOfSize(final int size) {
			PowerMockito.when(ReflectionUtils.findAllMethodsInClassWithAnnotation(Object.class, PostConstruct.class)).thenReturn(
					MethodBuilder.methodListOfSize(size));
		}


		private Matcher<Method> buildTemplateMatcher() {
			return new Matcher<Method>() {

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

	@RunWith(JUnit4.class)
	public static class SpecialFactoryMethodTests {

		@Test
		public void exactlyOneConstructor_checksForExactlyOne() throws Exception {
			final AnnotatedMethodsMatcher matcher = AnnotatedMethodsMatcher.hasAnnotationOnExactlyOneMethod(PostConstruct.class);
			final Cardinality fieldFromObject = ReflectionUtils.getFieldFromObject(matcher, "cardinality");
			assertThat(fieldFromObject, equalTo(Cardinality.EXACTLY_ONE));
		}


		@Test
		public void atLeastOneConstructor_checksForAtLeastOne() throws Exception {
			final AnnotatedMethodsMatcher matcher = AnnotatedMethodsMatcher.hasAnnotationOnAtLeastOneMethod(PostConstruct.class);
			final Cardinality fieldFromObject = ReflectionUtils.getFieldFromObject(matcher, "cardinality");
			assertThat(fieldFromObject, equalTo(Cardinality.AT_LEAST_ONE));
		}
	}
}