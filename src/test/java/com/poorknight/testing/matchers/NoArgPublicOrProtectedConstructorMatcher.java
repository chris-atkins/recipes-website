package com.poorknight.testing.matchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class NoArgPublicOrProtectedConstructorMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private NoArgPublicOrProtectedConstructorMatcher() {
		super();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a no-arg constructor with visibility of public or protected.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveANoArgConstructor(classToInspect)) {
			appendNoNoArgConstructorMessage(mismatchDescription);
			return false;
		}

		if (noArgConstructorIsNotOfTheCorrectVisibility(classToInspect)) {
			appendWrongVisibilityMessage(mismatchDescription);
			return false;
		}

		return true;

	}


	private boolean doesNotHaveANoArgConstructor(final Class<?> classToInspect) {
		final Constructor<?> constructor = this.findNoArgConstructor(classToInspect);
		if (constructor == null) {
			return true; // does not have it
		}
		return false; // does have it
	}


	private Constructor<?> findNoArgConstructor(final Class<?> classToInspect) {
		try {
			return classToInspect.getDeclaredConstructor();
		} catch (final Exception e) {
			return null;
		}
	}


	private boolean noArgConstructorIsNotOfTheCorrectVisibility(final Class<?> classToInspect) {
		final Constructor<?> constructor = this.findNoArgConstructor(classToInspect);
		final int mod = constructor.getModifiers();
		if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
			return false; // it is of the correct visibility
		}
		return true;  // not of the correct visibility
	}


	private void appendNoNoArgConstructorMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("there is no no-arg constructor.");
	}


	private void appendWrongVisibilityMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the no-arg constructor does not have the correct visibility (only public and protected are allowed).");
	}


	@Factory
	public static NoArgPublicOrProtectedConstructorMatcher hasNoArgConstructorThatIsPublicOrProtected() {
		return new NoArgPublicOrProtectedConstructorMatcher();
	}

}
