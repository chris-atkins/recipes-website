package com.poorknight.testing.matchers.classes;

import java.io.Serializable;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class SessionScopeMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private SessionScopeMatcher() {
		super();
	}


	@Factory
	public static SessionScopeMatcher isSessionScoped() {
		return new SessionScopeMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the @javax.enterprise.context.SessionScoped annotation, and implements Serializable.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveTheCorrectSessionScopedAnnotation(classToInspect)) {
			appendMissingCorrectAnnotationMessage(mismatchDescription);
			return false;
		}

		if (isNotSerializable(classToInspect)) {
			appendNotSerializableMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean doesNotHaveTheCorrectSessionScopedAnnotation(final Class<?> classToInspect) {
		return classToInspect.getAnnotation(javax.enterprise.context.SessionScoped.class) == null;
	}


	private boolean isNotSerializable(final Class<?> classToInspect) {
		return !(isSerializable(classToInspect));
	}


	private boolean isSerializable(final Class<?> classToInspect) {
		return Serializable.class.isAssignableFrom(classToInspect);
	}


	private void appendMissingCorrectAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("it does not have the @javax.enterprise.context.SessionScoped annotation (it is missing or uses"
				+ " @SessionScoped from a different package).");
	}


	private void appendNotSerializableMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("it does not implement Serializable.");
	}
}
