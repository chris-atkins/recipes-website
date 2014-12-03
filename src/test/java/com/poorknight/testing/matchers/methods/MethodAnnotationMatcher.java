package com.poorknight.testing.matchers.methods;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


/**
 * Responsible for checking whether any public methods exist on a class with the passed method name, and whether it has
 * the passed annotation on it. Will throw an exception if the annotation does not have runtime retention.
 */
public class MethodAnnotationMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private final Class<? extends Annotation> annotationToCheckFor;

	private final String methodName;


	private MethodAnnotationMatcher(final Class<? extends Annotation> annotationToCheckFor, final String methodName) {
		super();
		throwExceptionIfAnnotationToCheckForIsNotRuntimeRetention(annotationToCheckFor);
		this.annotationToCheckFor = annotationToCheckFor;
		this.methodName = methodName;
	}


	private void throwExceptionIfAnnotationToCheckForIsNotRuntimeRetention(final Class<? extends Annotation> annotation) {
		if (!ReflectionUtils.annotationHasRuntimeRetention(annotation)) {
			throw new RuntimeException("Annotation must have @Retention type of Runtime in order to work with this class.");
		}
	}


	@Factory
	public static MethodAnnotationMatcher hasAnnotationOnPublicMethod(final Class<? extends Annotation> annotationClass, final String methodName) {
		return new MethodAnnotationMatcher(annotationClass, methodName);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a method ").appendText(this.methodName).appendText(" with an annotation of @")
				.appendText(this.annotationToCheckFor.getName());
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		final Collection<Method> methodsToCheck = ReflectionUtils.findAllPublicMethodsWithName(classToInspect, this.methodName);

		if (isEmpty(methodsToCheck)) {
			appendNoPublicMethodWithNameExistsMessage(classToInspect, mismatchDescription);
			return false;
		}

		if (anyMethodsDontHaveCorrectAnnotation(methodsToCheck)) {
			appendAtLeastOneMethodIsMissingTheAnnotationMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean anyMethodsDontHaveCorrectAnnotation(final Collection<Method> methodsToCheck) {
		return !allMethodsHaveCorrectAnnotation(methodsToCheck);
	}


	private boolean allMethodsHaveCorrectAnnotation(final Collection<Method> methodsToCheck) {
		for (final Method method : methodsToCheck) {
			if (method.getAnnotation(this.annotationToCheckFor) == null) {
				return false;
			}
		}
		return true;
	}


	private void appendNoPublicMethodWithNameExistsMessage(final Class<?> classToInspect, final Description mismatchDescription) {
		mismatchDescription.appendText("does not have a public method with the name").appendText(classToInspect.getName())
				.appendText(".  You could 1) change the method to public 2) Change this matcher to check for non-public methods 3) something else.");
	}


	private void appendAtLeastOneMethodIsMissingTheAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("At least one of the methods with the name ").appendText(this.methodName).appendText(" does not have the @")
				.appendText(this.annotationToCheckFor.getName()).appendText(" annotation.");
	}
}
