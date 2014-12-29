package com.poorknight.testing.matchers.classes;

import java.io.Serializable;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class SingletonMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private SingletonMatcher() {
		super();
	}


	@Factory
	public static SingletonMatcher isAProperSingleton() {
		return new SingletonMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("meets project standards for an @Singleton object:  \n")//
				.appendText("\tuses @javax.ejb.Singleton, not @javax.inject.Singleton")//
				.appendText("\n\timplements Serializable");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveCorrectAnnotation(classToInspect)) {
			appendMissingCorrectAnnotationMessage(mismatchDescription);
			return false;
		}

		if (doesNotImplementSerializable(classToInspect)) {
			appendNotImplementingSerializableMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean doesNotHaveCorrectAnnotation(final Class<?> classToInspect) {
		return !(hasTheCorrectAnnotation(classToInspect));
	}


	private boolean hasTheCorrectAnnotation(final Class<?> classToInspect) {
		return classToInspect.getAnnotation(javax.ejb.Singleton.class) != null;
	}


	private boolean doesNotImplementSerializable(final Class<?> classToInspect) {
		return !(implementsSerializable(classToInspect));
	}


	private boolean implementsSerializable(final Class<?> classToInspect) {
		return Serializable.class.isAssignableFrom(classToInspect);
	}


	private void appendMissingCorrectAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("does not have the @javax.ejb.Singleton annotation on the class.");
	}


	private void appendNotImplementingSerializableMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("does not implement Serializable.");
	}

}
