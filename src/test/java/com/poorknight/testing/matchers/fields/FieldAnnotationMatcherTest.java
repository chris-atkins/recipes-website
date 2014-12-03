package com.poorknight.testing.matchers.fields;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;

import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.testing.matchers.utils.testclasses.ClassWithFieldHavingAnnotations;
import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.RuntimeRetention;
import com.poorknight.utils.ReflectionUtils;


@RunWith(Enclosed.class)
public class FieldAnnotationMatcherTest {

	@RunWith(JUnit4.class)
	public static class FieldAnnotationMatcherJunit4Test {

		@Test
		public void usesFactoryMethod() throws Exception {
			assertThat(FieldAnnotationMatcher.class, hasFactoryMethod());
		}

	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class FieldAnnotationMatcherPowerMockTest {

		private static final Class<RuntimeRetention> ANNOTATION_TO_TEST = RuntimeRetention.class;
		private final FieldAnnotationMatcher matcher = FieldAnnotationMatcher.hasAnnotationOnField(ANNOTATION_TO_TEST);
		private Field field;


		@Before
		public void init() throws Exception {
			PowerMockito.mockStatic(ReflectionUtils.class);
			this.field = ClassWithFieldHavingAnnotations.class.getField("fieldWithAnnotation");
		}


		@Test
		public void usesReflectionUtilsToFindAnswerForTrue() {
			PowerMockito.when(ReflectionUtils.fieldHasAnnotation(this.field, ANNOTATION_TO_TEST)).thenReturn(true);

			final boolean result = this.matcher.matchesSafely(this.field, Description.NONE);

			PowerMockito.verifyStatic(times(1));
			ReflectionUtils.fieldHasAnnotation(this.field, ANNOTATION_TO_TEST);

			assertThat(result, is(true));
		}


		@Test
		public void usesReflectionUtilsToFindAnswerForFalse() {
			PowerMockito.when(ReflectionUtils.fieldHasAnnotation(this.field, ANNOTATION_TO_TEST)).thenReturn(false);

			final boolean result = this.matcher.matchesSafely(this.field, Description.NONE);

			PowerMockito.verifyStatic(times(1));
			ReflectionUtils.fieldHasAnnotation(this.field, ANNOTATION_TO_TEST);

			assertThat(result, is(false));
		}
	}
}