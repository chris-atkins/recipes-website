package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class MethodParametersMatcher extends TypeSafeDiagnosingMatcher<Method> {

	private final Class<?>[] expectedParameterTypes;


	private MethodParametersMatcher(final Class<?>... expectedParameterTypes) {
		super();
		this.expectedParameterTypes = expectedParameterTypes;
	}


	@Factory
	public static MethodParametersMatcher hasNoParameters() {
		return new MethodParametersMatcher();
	}


	@Factory
	public static MethodParametersMatcher hasParameters(final Class<?>... expectedParameterTypes) {
		return new MethodParametersMatcher(expectedParameterTypes);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the expected parameter types.");
	}


	@Override
	protected boolean matchesSafely(final Method method, final Description mismatchDescription) {
		if (methodArgumentsDontMatch(method)) {
			appendMismatchArgumentsMessage(mismatchDescription);
			return false;
		}
		return true;
	}


	private boolean methodArgumentsDontMatch(final Method method) {
		final Class<?>[] foundParameters = method.getParameterTypes();
		return !(Arrays.equals(this.expectedParameterTypes, foundParameters));
	}


	private void appendMismatchArgumentsMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("expected parameters").appendText(this.expectedParameterTypes.toString())
				.appendText(", but the parameter types did not match.");
	}
}
