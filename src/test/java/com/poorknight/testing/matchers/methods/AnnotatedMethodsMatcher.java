package com.poorknight.testing.matchers.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.Cardinality;
import com.poorknight.utils.ReflectionUtils;


public class AnnotatedMethodsMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private final Class<? extends Annotation> annotationClass;
	private final Cardinality cardinality;
	private final Matcher<Method>[] furtherMatchersToApplyToAnnotatedMethods;


	@SafeVarargs
	private AnnotatedMethodsMatcher(final Class<? extends Annotation> annotationClass, final Cardinality cardinality,
			final Matcher<Method>... furtherMatchersToApplyToAnnotatedMethods) {
		super();
		this.annotationClass = annotationClass;
		this.cardinality = cardinality;
		this.furtherMatchersToApplyToAnnotatedMethods = furtherMatchersToApplyToAnnotatedMethods;
	}


	/**
	 * Not necessarily valid for Interceptor post-construct methods, which may that an InvocationContext as a parameter
	 */
	@Factory
	public static AnnotatedMethodsMatcher hasAPostConstructMethod() {
		// //https://docs.oracle.com/javaee/7/api/javax/annotation/PostConstruct.html
		return new AnnotatedMethodsMatcher(PostConstruct.class, Cardinality.EXACTLY_ONE, MethodParametersMatcher.hasNoParameters(),
				MethodReturnTypeMatcher.returnsVoid(), MethodModifiersMatcher.notStatic());
	}


	@Factory
	@SafeVarargs
	public static AnnotatedMethodsMatcher hasAnnotationOnMethod(final Class<? extends Annotation> annotationClass, final Cardinality cardinality,
			final Matcher<Method>... furtherMatchersToApplyToAnnotatedMethods) {
		return new AnnotatedMethodsMatcher(annotationClass, cardinality, furtherMatchersToApplyToAnnotatedMethods);
	}


	@Factory
	@SafeVarargs
	public static AnnotatedMethodsMatcher hasAnnotationOnExactlyOneMethod(final Class<? extends Annotation> annotationClass,
			final Matcher<Method>... furtherMatchersToApplyToAnnotatedMethods) {
		return new AnnotatedMethodsMatcher(annotationClass, Cardinality.EXACTLY_ONE, furtherMatchersToApplyToAnnotatedMethods);
	}


	@Factory
	@SafeVarargs
	public static AnnotatedMethodsMatcher hasAnnotationOnAtLeastOneMethod(final Class<? extends Annotation> annotationClass,
			final Matcher<Method>... furtherMatchersToApplyToAnnotatedMethods) {
		return new AnnotatedMethodsMatcher(annotationClass, Cardinality.AT_LEAST_ONE, furtherMatchersToApplyToAnnotatedMethods);
	}


	@Override
	public void describeTo(final Description description) {

		description.appendText("has ")//
				.appendText(this.cardinality.name())//
				.appendText(" method in the class with the @")//
				.appendText(this.annotationClass.getName())//
				.appendText(" annotation on it.");

		if (hasChildValidators()) {
			description.appendText("Annotated attribute(s) also meet the following criteria: ");
		}

		for (final Matcher<Method> additionalMatcher : this.furtherMatchersToApplyToAnnotatedMethods) {
			additionalMatcher.describeTo(description);
			description.appendText("\n");
		}
	}


	private boolean hasChildValidators() {
		return this.furtherMatchersToApplyToAnnotatedMethods != null && this.furtherMatchersToApplyToAnnotatedMethods.length > 0;
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		final Collection<Method> allAnnotatedMethods = ReflectionUtils.findAllMethodsInClassWithAnnotation(classToInspect, this.annotationClass);

		if (hasWrongNumberOfAnnotatedMethods(allAnnotatedMethods.size())) {
			appendWrongNumberOfMethodsMessage(mismatchDescription);
			return false;
		}

		for (final Method method : allAnnotatedMethods) {
			if (failsAnyExtraMatchersAppendingFailureMessage(method, mismatchDescription)) {
				return false;
			}
		}

		return true;
	}


	private boolean hasWrongNumberOfAnnotatedMethods(final int numberOfMethods) {
		return !(hasCorrectNumberOfAnnotatedMethods(numberOfMethods));
	}


	private boolean hasCorrectNumberOfAnnotatedMethods(final int numberOfMethods) {
		switch (this.cardinality) {
		case EXACTLY_ONE:
			return numberOfMethods == 1;
		case AT_LEAST_ONE:
			return numberOfMethods > 0;
		default:
			throw new RuntimeException("Unhandled Cardinality enum type in AnnotatedMethodsMatcher.");
		}

	}


	private boolean failsAnyExtraMatchersAppendingFailureMessage(final Method method, final Description mismatchDescription) {
		return !passesExtraMatchers(method, mismatchDescription);
	}


	private boolean passesExtraMatchers(final Method method, final Description mismatchDescription) {
		for (final Matcher<Method> matcher : this.furtherMatchersToApplyToAnnotatedMethods) {
			final boolean results = matcher.matches(method);
			if (results == false) {
				appendFailSubMatcherMessage(method, mismatchDescription, matcher);
				return false;
			}
		}
		return true;
	}


	private void appendWrongNumberOfMethodsMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the wrong number of annotated methods was found.");
	}


	private void appendFailSubMatcherMessage(final Method method, final Description mismatchDescription, final Matcher<Method> matcher) {
		matcher.describeMismatch(method, mismatchDescription);
	}

}
