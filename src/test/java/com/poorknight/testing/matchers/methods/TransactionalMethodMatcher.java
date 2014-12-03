package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.transaction.Transactional;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


/**
 * Responsible for checking that a method in a class has the @Transactional method annotation. It checks all methods
 * with the same name, regardless of the arguments passed (all overloaded methods with the same name will be checked).
 */
public class TransactionalMethodMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	String methodName;


	private TransactionalMethodMatcher(final String methodName) {
		super();
		this.methodName = methodName;
	}


	@Factory
	public static TransactionalMethodMatcher createsTransactionBoundaryOnMethod(final String methodName) {
		return new TransactionalMethodMatcher(methodName);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a method named ").appendText(this.methodName)
				.appendText(" that creates a transactional boundary, by being annotated with @Transactional.");

	}


	@Override
	protected boolean matchesSafely(final Class<?> classBeingInspected, final Description mismatchDescription) {
		if (classBeingInspectedDoesNotHaveMethodOfGivenName(classBeingInspected)) {
			appendNoMethodOfGivenNameExistsMessage(mismatchDescription);
			return false;
		}

		if (notAllFoundMethodsAreTransactional(classBeingInspected)) {
			appendNotAllFoundMethodsAreTransactionalMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean classBeingInspectedDoesNotHaveMethodOfGivenName(final Class<?> classBeingInspected) {
		if (ReflectionUtils.hasAnyVisibleMethodsInClassWithName(classBeingInspected, this.methodName)) {
			return false;
		}
		return true;
	}


	private void appendNoMethodOfGivenNameExistsMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("no method with the given name was found: ").appendText(this.methodName);
	}


	private boolean notAllFoundMethodsAreTransactional(final Class<?> classBeingInspected) {
		return !alllFoundMethodsAreTransactional(classBeingInspected);
	}


	private boolean alllFoundMethodsAreTransactional(final Class<?> classBeingInspected) {
		final Collection<Method> methodsWithCorrectName = ReflectionUtils.findAllVisibleMethodsInClassWithName(classBeingInspected, this.methodName);
		for (final Method method : methodsWithCorrectName) {
			if (methodHasTransactionalAnnotation(method)) {
				return true;
			}
		}
		return false;
	}


	private boolean methodHasTransactionalAnnotation(final Method method) {
		return method.getAnnotation(Transactional.class) != null;
	}


	private void appendNotAllFoundMethodsAreTransactionalMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("Not all methods named " + this.methodName + "() have the @Transactional annotation.  "
				+ "It is possible that there are overloaded methods and only one of them should have the "
				+ "annotation - in which case you should probably rewrite this class (TransactionalMethodMatcher)");
	}
}
