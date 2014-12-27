/**
 *
 */
package com.poorknight.testing.matchers.classes;

import java.lang.annotation.Annotation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


/**
 * Responsible for checking whether a class meets the project standards for a request scoped controller (backing bean). This includes checking whether
 * the class has the @RequestScoped annotation, and the @Named annotation.
 */
public class RequestScopedControllerMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private RequestScopedControllerMatcher() {
		super();
	}


	@Factory
	public static RequestScopedControllerMatcher isAProperRequestScopedController() {
		return new RequestScopedControllerMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("meets the project criteria for an @RequestScoped controller: \n")//
				.appendText("\thas the  @javax.enterprise.context.RequestScoped annotation, not @javax.faces.bean.RequestScoped ")//
				.appendText("\thas the @Named annotation");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (theCorrectRequestScopedAnnotationIsMissing(classToInspect)) {
			appendMissingCorrectScopeAnnotationMessage(mismatchDescription);
			return false;
		}

		if (theNamedAnnotationIsMissing(classToInspect)) {
			appendMissingNamedAnnotationMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean theCorrectRequestScopedAnnotationIsMissing(final Class<?> classToInspect) {
		return !(hasAnnotation(classToInspect, RequestScoped.class));
	}


	private boolean theNamedAnnotationIsMissing(final Class<?> classToInspect) {
		return !(hasAnnotation(classToInspect, Named.class));
	}


	private boolean hasAnnotation(final Class<?> classToInspect, final Class<? extends Annotation> annotationClass) {
		return classToInspect.getAnnotation(annotationClass) != null;
	}


	private void appendMissingCorrectScopeAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the @javax.enterprise.context.RequestScoped annotation does not exist on the class.");
	}


	private void appendMissingNamedAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the @Named annotation does not exist on the class.");
	}

}
