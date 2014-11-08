package com.poorknight.testing.matchers;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class EqualsHashCodeToStringClassImplementationMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private EqualsHashCodeToStringClassImplementationMatcher() {
		super();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has its own implementation of equals(), hashCode(), and toString().");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotImplementOwnEquals(classToInspect)) {
			appendNoEqualsImplementationMessage(mismatchDescription);
			return false;
		}
		if (doesNotImplementOwnHashCode(classToInspect)) {
			appendNoHashCodeImplementationMessage(mismatchDescription);
			return false;
		}
		if (doesNotImplementOwnToString(classToInspect)) {
			appendNoToStringImplementationMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean doesNotImplementOwnEquals(final Class<?> classToInspect) {
		return doesNotImplementOwnMethod(classToInspect, "equals", Object.class);
	}


	private boolean doesNotImplementOwnHashCode(final Class<?> classToInspect) {
		return doesNotImplementOwnMethod(classToInspect, "hashCode");
	}


	private boolean doesNotImplementOwnToString(final Class<?> classToInspect) {
		return doesNotImplementOwnMethod(classToInspect, "toString");
	}


	private boolean doesNotImplementOwnMethod(final Class<?> classToInspect, final String methodName, final Class<?>... parameterTypes) {
		try {

			final Method method = classToInspect.getMethod(methodName, parameterTypes);
			return !(method.getDeclaringClass().equals(classToInspect));

		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Problems with reflection.", e);
		}
	}


	private void appendNoEqualsImplementationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("it does not implement its own equals() method.");
	}


	private void appendNoHashCodeImplementationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("it does not implement its own hashCode() method.");
	}


	private void appendNoToStringImplementationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("it does not implement its own toString() method.");
	}


	@Factory
	public static EqualsHashCodeToStringClassImplementationMatcher implementsOwnEqualsHashCodeToStringMethods() {
		return new EqualsHashCodeToStringClassImplementationMatcher();
	}

}
