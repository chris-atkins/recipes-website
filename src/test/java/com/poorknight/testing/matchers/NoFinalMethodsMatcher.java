package com.poorknight.testing.matchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class NoFinalMethodsMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private NoFinalMethodsMatcher() {
		super();
	}


	@Override
	public void describeTo(final Description description) {
		description
				.appendText("has no methods that are final in this class and all parent classes (with the exception of Object class - its final methods are ignored).");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		final Collection<Method> methods = ReflectionUtils.getAllMethodsInClassAndSuperClasses(classToInspect);

		if (anyMethodsAreFinalAddingFailDescriptions(methods, mismatchDescription)) {
			return false;
		}
		return true;
	}


	private boolean anyMethodsAreFinalAddingFailDescriptions(final Collection<Method> methods, final Description mismatchDescription) {

		for (final Method method : methods) {
			final int modifiers = method.getModifiers();

			// seems Object methods must be exempt from this rule of no final methods (rule comes from Java EE 7 book),
			// since Object violates the rule, and everything extends Object
			if (Modifier.isFinal(modifiers) && methodIsNotFromObjectClass(method)) {
				mismatchDescription.appendText("found a final method: ").appendText(method.getName());
				return true;
			}
		}
		return false;
	}


	private boolean methodIsNotFromObjectClass(final Method method) {
		return !(method.getDeclaringClass().equals(Object.class));
	}


	@Factory
	public static NoFinalMethodsMatcher hasNoFinalMethods() {
		return new NoFinalMethodsMatcher();
	}
}