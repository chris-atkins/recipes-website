package com.poorknight.testing.matchers.matchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


/**
 * Responsible for checking that a Matcher class appropriately implements an @Factory method. This means that there are
 * no public constructors, and that there is at least one @Factory method that is public and returns an object of the
 * correct type that is not null.
 */
public class FactoryMethodMatcher extends TypeSafeDiagnosingMatcher<Class<? extends Matcher<?>>> {

	private FactoryMethodMatcher() {
		super();
	}


	@Factory
	public static FactoryMethodMatcher hasFactoryMethod() {
		return new FactoryMethodMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a static @Factory method that returns non-null, and has no public constructors");
	}


	@Override
	protected boolean matchesSafely(final Class<? extends Matcher<?>> matcherClass, final Description mismatchDescription) {

		if (hasNoMethodsWithFactoryAnnotation(matcherClass)) {
			appendNoFactoryAnnotationMessage(matcherClass, mismatchDescription);
			return false;
		}

		if (ReflectionUtils.hasAnyPublicConstructor(matcherClass)) {
			appendPublicConstructorMessage(mismatchDescription);
			return false;
		}

		if (factoryMethodReturnsWrongType(matcherClass)) {
			appendWrongFactoryMethodReturnTypeMessage(mismatchDescription);
			return false;
		}

		if (factoryMethodIsNotStatic(matcherClass)) {
			appendNonStaticFactoryMethodMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean hasNoMethodsWithFactoryAnnotation(final Class<? extends Matcher<?>> matcherClass) {
		return !ReflectionUtils.hasAnyPublicMethodsWithAnnotation(matcherClass, Factory.class);
	}


	private void appendNoFactoryAnnotationMessage(final Class<? extends Matcher<?>> matcherClass, final Description mismatchDescription) {
		mismatchDescription.appendText("no methods in the ").appendText(matcherClass.getSimpleName())
				.appendText(" class have the @Factory annotation.");
	}


	private boolean factoryMethodReturnsWrongType(final Class<? extends Matcher<?>> expectedReturnType) {
		return !factoryMethodReturnsCorrectType(expectedReturnType);
	}


	private boolean factoryMethodReturnsCorrectType(final Class<? extends Matcher<?>> expectedReturnType) {
		final Collection<Method> factoryMethods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(expectedReturnType, Factory.class);
		for (final Method method : factoryMethods) {
			if (methodReturnTypeDoesNotMatch(method, expectedReturnType)) {
				return false;
			}
		}
		return true;
	}


	private boolean methodReturnTypeDoesNotMatch(final Method method, final Class<? extends Matcher<?>> expectedReturnType) {
		return !method.getReturnType().equals(expectedReturnType);
	}


	private boolean factoryMethodIsNotStatic(final Class<? extends Matcher<?>> matcherClass) {
		final Collection<Method> factoryMethods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(matcherClass, Factory.class);
		for (final Method method : factoryMethods) {
			if (methodIsNotStatic(method)) {
				return true;
			}
		}
		return false;
	}


	private boolean methodIsNotStatic(final Method method) {
		return !Modifier.isStatic(method.getModifiers());
	}


	private void appendPublicConstructorMessage(final Description mismatchDescription) {
		mismatchDescription
				.appendText("a public constructor exists.  Public constructors should not exist with a factory method (per development norms for this project). Constructors should be non-public.");
	}


	private void appendWrongFactoryMethodReturnTypeMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the @Factory methods must return an object of the type of class it resides in.");
	}


	private void appendNonStaticFactoryMethodMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the @Factory methods should be static.");
	}
}
