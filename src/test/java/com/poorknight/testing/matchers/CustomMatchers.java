package com.poorknight.testing.matchers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.hamcrest.Matcher;

import com.poorknight.testing.matchers.MethodTransactionAnnotationMatcher.TransactionType;


/**
 * A facade for easy access to all custom matchers in this project.
 */
public class CustomMatchers {

	public static Matcher<Class<?>> hasCorrectTransactionLevelOnMethod(final String methodName, final TransactionType transactionType) {
		return MethodTransactionAnnotationMatcher.hasCorrectTransactionLevelOnMethod(methodName, transactionType);
	}


	public static Matcher<Class<?>> createsTransactionBoundaryOnMethod(final String methodName) {
		return TransactionalMethodMatcher.createsTransactionBoundaryOnMethod(methodName);
	}


	public static Matcher<Class<?>> meetsProjectEntityObjectStandards() {
		return EntityObjectMatcher.meetsProjectEntityObjectStandards();
	}


	public static Matcher<Class<?>> hasAnnotationWithValue(final Class<? extends Annotation> annotationType, final Object... expectedValue) {
		return ClassAnnotationMatcher.hasAnnotationWithValue(annotationType, expectedValue);
	}


	public static Matcher<Class<?>> hasAnnotation(final Class<? extends Annotation> annotationToCheckFor) {
		return ClassAnnotationMatcher.hasAnnotationOnClass(annotationToCheckFor);
	}


	/*
	 * For now intended to only be used by other matchers - so is protected. The idea is that other matchers should have
	 * specific business logic around which annotations belong where. Actual prod code should not be testing 'is this
	 * annotation that I put there actually there' - it should be more like 'are the annotations that are appropriate
	 * given the business functionality of the method on it'
	 */
	protected static Matcher<Class<?>> hasAnnotationOnMethod(final Class<? extends Annotation> annotationClass, final String methodName) {
		return MethodAnnotationMatcher.hasAnnotationOnMethod(annotationClass, methodName);
	}


	protected static Matcher<Class<? extends Matcher<?>>> hasFactoryMethod() {
		return FactoryMethodMatcher.hasFactoryMethod();
	}


	@SafeVarargs
	protected static AttributeAnnotationMatcher hasAnnotationOnExactlyOneAttribute(final Class<? extends Annotation> annotationClass,
			final Matcher<Field>... furtherMatchersToApplyToAnnotatedAttributes) {

		return AttributeAnnotationMatcher.hasAnnotationOnAttribute(annotationClass, AttributeAnnotationMatcher.Cardinality.EXACTLY_ONE,
				furtherMatchersToApplyToAnnotatedAttributes);
	}


	protected static Matcher<Field> isFieldOfType(final Class<?> expectedClass) {
		return FieldTypeMatcher.isFieldOfType(expectedClass);
	}


	protected static Matcher<Field> hasAnnotationOnField(final Class<? extends Annotation> annotationToCheckFor) {
		return FieldAnnotationMatcher.hasAnnotation(annotationToCheckFor);
	}


	protected static Matcher<Field> hasCorrectStrategyOnGeneratedFieldAnnotation() {
		return FieldAnnotationValueMatcher.isAnIdentityAutoGeneratedEntityField();
	}
}
