package com.poorknight.testing.matchers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class ClassAnnotationMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private final Class<? extends Annotation> annotationToCheckFor;
	private final Object[] expectedValues;


	private ClassAnnotationMatcher(final Class<? extends Annotation> annotationToCheckFor, final Object... expectedValues) {

		if (annotationDoesNotHaveRuntimeRetentionPolicy(annotationToCheckFor)) {
			throw new RuntimeException("This matcher only supports searching for annotations with a RetentionPolicy of RUNTIME. @"
					+ annotationToCheckFor.getSimpleName() + " does not have runtime retention.");
		}

		this.annotationToCheckFor = annotationToCheckFor;
		this.expectedValues = expectedValues;
	}


	private boolean annotationDoesNotHaveRuntimeRetentionPolicy(final Class<? extends Annotation> annotationToCheck) {
		return !(ReflectionUtils.annotationHasRuntimeRetention(annotationToCheck));
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has an annotation of type @").appendText(this.annotationToCheckFor.getSimpleName())
				.appendText(" that has a value of ").appendText(Arrays.toString(this.expectedValues));
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (classDoesNotHaveAnnotation(classToInspect)) {
			appendMissingAnnotationMessage(mismatchDescription, classToInspect);
			return false;
		}

		if (annotationDoesNotHaveExpectedValue(classToInspect)) {
			appendWrongAnnotationValueMessage(mismatchDescription, classToInspect);
			return false;
		}

		return true;
	}


	private boolean classDoesNotHaveAnnotation(final Class<?> classToInspect) {
		return classToInspect.getAnnotation(this.annotationToCheckFor) == null;
	}


	private boolean annotationDoesNotHaveExpectedValue(final Class<?> classToInspect) {
		final Object actualValue = findActualValue(classToInspect);

		if (neitherValueExists(this.expectedValues, actualValue)) {
			return false;
		}

		if (actualValueIsAnArray(actualValue)) {
			return !(equalsIgnoringOrder(this.expectedValues, asArray(actualValue)));
		}

		return !(equalsIgnoringOrder(this.expectedValues, newArray(actualValue)));
	}


	private boolean neitherValueExists(final Object[] expected, final Object actual) {
		final boolean expectedDoesNotExist = ((expected == null) || (expected.length == 0));
		boolean actualDoesNotExist = (actual == null);
		if (actualValueIsAnArray(actual)) {
			actualDoesNotExist = asArray(actual).length == 0;
		}
		return expectedDoesNotExist && actualDoesNotExist;
	}


	private boolean equalsIgnoringOrder(final Object[] first, final Object[] second) {
		Arrays.sort(first);
		Arrays.sort(second);
		return Arrays.equals(first, second);
	}


	private Object findActualValue(final Class<?> classToInspect) {
		final Annotation proxyAnnotation = classToInspect.getAnnotation(this.annotationToCheckFor);

		final InvocationHandler annotationHandler = ReflectionUtils.getAttributeFromObject(proxyAnnotation, "h");
		final Map<String, Object> memberValues = ReflectionUtils.getAttributeFromObject(annotationHandler, "memberValues");

		return memberValues.get("value");
	}


	private boolean actualValueIsAnArray(final Object actualValue) {
		if (actualValue == null) {
			return false;
		}
		return actualValue.getClass().isArray() && this.expectedValues.getClass().isArray();
	}


	private Object[] asArray(final Object actualValue) {
		return (Object[]) actualValue;
	}


	private Object[] newArray(final Object actualValue) {
		return new Object[] { actualValue };
	}


	private void appendMissingAnnotationMessage(final Description mismatchDescription, final Class<?> classToInspect) {
		mismatchDescription.appendText("the class ").appendText(classToInspect.getSimpleName()).appendText(" does not have the @")
				.appendText(this.annotationToCheckFor.getSimpleName()).appendText(" annotation.");
	}


	private void appendWrongAnnotationValueMessage(final Description mismatchDescription, final Class<?> classToInspect) {
		mismatchDescription.appendText("the class ").appendText(classToInspect.getSimpleName()).appendText(" has the @")
				.appendText(this.annotationToCheckFor.getSimpleName())
				.appendText(" annotation, but does not have the expected value.\nExpected value: ").appendText(Arrays.toString(this.expectedValues))
				.appendText("\nFound value: ").appendText(findActualValue(classToInspect).toString());
	}


	@Factory
	public static ClassAnnotationMatcher hasAnnotationWithValue(final Class<? extends Annotation> annotationToCheckFor,
			final Object... expectedValues) {
		return new ClassAnnotationMatcher(annotationToCheckFor, expectedValues);
	}


	@Factory
	public static ClassAnnotationMatcher hasAnnotationOnClass(final Class<? extends Annotation> annotationToCheckFor) {
		return new ClassAnnotationMatcher(annotationToCheckFor);
	}

}
