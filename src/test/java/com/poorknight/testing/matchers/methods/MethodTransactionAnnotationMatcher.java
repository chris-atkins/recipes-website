package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


public class MethodTransactionAnnotationMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	public enum TransactionType {
		INSERT, UPDATE, QUERY, DELETE
	}

	private static final Map<TransactionType, List<TransactionAttributeType>> TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES;

	static {
		TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES = new HashMap<>();
		TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES.put(TransactionType.INSERT, Arrays.asList(TransactionAttributeType.REQUIRED));
		TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES.put(TransactionType.QUERY, Arrays.asList(TransactionAttributeType.SUPPORTS));
		TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES.put(TransactionType.UPDATE, Arrays.asList(TransactionAttributeType.REQUIRED));
		TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES.put(TransactionType.DELETE, Arrays.asList(TransactionAttributeType.REQUIRED));
	}

	private final String methodName;
	private final TransactionType transactionType;


	private MethodTransactionAnnotationMatcher(final String methodName, final TransactionType transactionType) {
		super();
		this.methodName = methodName;
		this.transactionType = transactionType;
	}


	@Factory
	public static MethodTransactionAnnotationMatcher hasCorrectTransactionLevelOnMethod(final String methodName, final TransactionType transactionType) {
		return new MethodTransactionAnnotationMatcher(methodName, transactionType);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the correct @TransactionAttribute annotation on the ").appendText(this.methodName)
				.appendText("() method for the type of transaction that if is performing:  ").appendText(this.transactionType.name());
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (noPublicMethodWithGivenNameAndAnnotationExists(classToInspect)) {
			appendNoPublicMethodExistsWithGivenNameMessage(mismatchDescription);
			return false;
		}

		final List<TransactionAttributeType> typesToMatch = getCorrectTransactionAttributeType();
		final Collection<Method> methods = ReflectionUtils.findAllPublicMethodsWithName(classToInspect, this.methodName);

		if (notAllMethodsHaveCorrectTransactionAttributeType(methods, typesToMatch)) {
			appendNotAllMethodsHaveCorrectTransactionAttributeTypeMessage(mismatchDescription);
			return false;
		}
		return true;
	}


	private boolean noPublicMethodWithGivenNameAndAnnotationExists(final Class<?> classToInspect) {
		return !MethodAnnotationMatcher.hasAnnotationOnPublicMethod(TransactionAttribute.class, this.methodName).matches(classToInspect);
	}


	private List<TransactionAttributeType> getCorrectTransactionAttributeType() {
		return TRANSACTIION_TYPE_TO_EXPECTED_ANNOTATION_TYPES.get(this.transactionType);
	}


	private boolean notAllMethodsHaveCorrectTransactionAttributeType(final Collection<Method> methods,
			final List<TransactionAttributeType> typesToMatch) {
		return !allMethodsHaveCorrectAttribute(methods, typesToMatch);
	}


	private boolean allMethodsHaveCorrectAttribute(final Collection<Method> methods, final List<TransactionAttributeType> typesToMatch) {
		for (final Method method : methods) {
			final TransactionAttribute transactionAttribute = method.getAnnotation(TransactionAttribute.class);
			if (!typesToMatch.contains(transactionAttribute.value())) {
				return false;
			}
		}
		return true;
	}


	private void appendNoPublicMethodExistsWithGivenNameMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("No public method with name ").appendText(this.methodName)
				.appendText(" and @TransactionAttribute annotation exists.  This class does not check non-public methods.");
	}


	private void appendNotAllMethodsHaveCorrectTransactionAttributeTypeMessage(final Description mismatchDescription) {
		mismatchDescription
				.appendText("Not all methods with the name ")
				.appendText(this.methodName)
				.appendText(
						" have the correct TransactionAttributeType in the @TransactionAttribute annotation.  Expecting type of "
								+ getCorrectTransactionAttributeType());
	}

}
