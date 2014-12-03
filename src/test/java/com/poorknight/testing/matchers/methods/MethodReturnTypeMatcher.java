package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class MethodReturnTypeMatcher extends TypeSafeDiagnosingMatcher<Method> {

	private final Class<?> expectedReturnType;


	private MethodReturnTypeMatcher(final Class<?> expectedReturnType) {
		super();
		this.expectedReturnType = expectedReturnType;
	}


	@Factory
	public static MethodReturnTypeMatcher returnsType(final Class<?> expectedReturnType) {
		return new MethodReturnTypeMatcher(expectedReturnType);
	}


	@Factory
	public static MethodReturnTypeMatcher returnsVoid() {
		return new MethodReturnTypeMatcher(void.class);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the return type ").appendText(this.expectedReturnType.getSimpleName());
	}


	@Override
	protected boolean matchesSafely(final Method method, final Description mismatchDescription) {

		if (theMethodReturnTypeDoesNotMatchExpected(method)) {
			appendWrongReturnTypeMessage(mismatchDescription, method);
			return false;
		}

		return true;
	}


	private boolean theMethodReturnTypeDoesNotMatchExpected(final Method method) {
		final Class<?> actualReturnType = findActualReturnType(method);
		return !(actualReturnType.equals(this.expectedReturnType));
	}


	private Class<?> findActualReturnType(final Method method) {
		return method.getReturnType();
	}


	private void appendWrongReturnTypeMessage(final Description mismatchDescription, final Method method) {
		final Class<?> actualReturnType = findActualReturnType(method);
		mismatchDescription.appendText("found method return type of ").appendText(actualReturnType.getSimpleName());
	}

}
