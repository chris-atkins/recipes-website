package com.poorknight.testing.matchers.classes;

import java.io.Serializable;

import javax.inject.Named;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;


abstract class SerializableControllerMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	SerializableControllerMatcher() {
		super();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("meets the project criteria for an ").appendText(annotation()).appendText(" controller:  \n")//
				.appendText("----uses ").appendText(scopeDescription()).appendText("\n") //
				.appendText("----has the @Named annotation\n")//
				.appendText("----implements Serializable");
	}


	abstract String annotation();


	abstract String scopeDescription();


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveTheCorrectScopedAnnotation(classToInspect)) {
			appendWrongViewScopedAnnotationMessage(mismatchDescription);
			return false;
		}

		if (doesNotImplementSerializable(classToInspect)) {
			appendNotImplementingSerializableMessage(mismatchDescription);
			return false;
		}

		if (doesNotHaveNamedAnnotation(classToInspect)) {
			appendMissingNamedAnnotationMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	abstract boolean doesNotHaveTheCorrectScopedAnnotation(final Class<?> classToInspect);


	private boolean doesNotImplementSerializable(final Class<?> classToInspect) {
		return !(implementsSerializable(classToInspect));
	}


	private boolean implementsSerializable(final Class<?> classToInspect) {
		return Serializable.class.isAssignableFrom(classToInspect);
	}


	private boolean doesNotHaveNamedAnnotation(final Class<?> classToInspect) {
		final Named annotation = classToInspect.getAnnotation(Named.class);
		return annotation == null;
	}


	private void appendWrongViewScopedAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText(wrongScopeDescription());
	}


	abstract String wrongScopeDescription();


	private void appendNotImplementingSerializableMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the class does not implement Serializable.");
	}


	private void appendMissingNamedAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the class does not have the @Named annotation.");
	}

}
