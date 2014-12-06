package com.poorknight.testing.matchers.classes;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class ViewScopedControllerMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private ViewScopedControllerMatcher() {
		super();
	}


	@Factory
	public static ViewScopedControllerMatcher isAProperViewScopedController() {
		return new ViewScopedControllerMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("meets the project criteria for an @ViewScoped controller:  \n")//
				.appendText("\tuses javax.faces.view.ViewScoped, not javax.faces.bean.ViewScoped\n") //
				.appendText("\timplements Serializable\n")//
				.appendText("\thas the @Named annotation.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveTheCorrectViewScopedAnnotation(classToInspect)) {
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


	private boolean doesNotHaveTheCorrectViewScopedAnnotation(final Class<?> classToInspect) {
		final ViewScoped annotation = classToInspect.getAnnotation(javax.faces.view.ViewScoped.class);
		return annotation == null;
	}


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
		mismatchDescription.appendText("either the class is missing the @ViewScoped annotation, or it is using javax.faces.bean.ViewScoped "
				+ "(it should be using javax.faces.view.ViewScoped).");
	}


	private void appendNotImplementingSerializableMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the class does not implement Serializable.");
	}


	private void appendMissingNamedAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the class does not have the @Named annotation.");
	}

}
