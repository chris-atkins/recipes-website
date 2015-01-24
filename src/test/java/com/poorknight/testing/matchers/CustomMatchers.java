package com.poorknight.testing.matchers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hamcrest.Matcher;

import com.poorknight.navigation.NavigationTracker;
import com.poorknight.testing.matchers.classes.ClassAnnotationMatcher;
import com.poorknight.testing.matchers.classes.EntityObjectMatcher;
import com.poorknight.testing.matchers.classes.RequestScopedControllerMatcher;
import com.poorknight.testing.matchers.classes.SessionScopeMatcher;
import com.poorknight.testing.matchers.classes.SessionScopedControllerMatcher;
import com.poorknight.testing.matchers.classes.SingletonMatcher;
import com.poorknight.testing.matchers.classes.ViewScopedControllerMatcher;
import com.poorknight.testing.matchers.fields.FieldAnnotationMatcher;
import com.poorknight.testing.matchers.fields.FieldAnnotationValueMatcher;
import com.poorknight.testing.matchers.fields.FieldTypeMatcher;
import com.poorknight.testing.matchers.fields.ReadOnlyFieldMatcher;
import com.poorknight.testing.matchers.matchers.FactoryMethodMatcher;
import com.poorknight.testing.matchers.methods.AnnotatedMethodsMatcher;
import com.poorknight.testing.matchers.methods.MethodAnnotationMatcher;
import com.poorknight.testing.matchers.methods.MethodModifiersMatcher;
import com.poorknight.testing.matchers.methods.MethodTransactionAnnotationMatcher;
import com.poorknight.testing.matchers.methods.MethodTransactionAnnotationMatcher.TransactionType;
import com.poorknight.testing.matchers.methods.TransactionalMethodMatcher;
import com.poorknight.testing.matchers.objects.BeanValidationMatcher;
import com.poorknight.testing.matchers.objects.IsSerializableMatcher;


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


	public static Matcher<Class<?>> hasPreSaveEntityMethod(final String methodName) {
		return hasAnnotationOnMethod(PrePersist.class, methodName);
	}


	public static Matcher<Class<?>> hasPreUpdateEntityMethod(final String methodName) {
		return hasAnnotationOnMethod(PreUpdate.class, methodName);
	}


	public static Matcher<Class<?>> hasAPostConstructMethod() {
		return AnnotatedMethodsMatcher.hasAPostConstructMethod();
	}


	public static Matcher<Class<? extends Matcher<?>>> hasFactoryMethod() {
		return FactoryMethodMatcher.hasFactoryMethod();
	}


	public static Matcher<Class<?>> hasAReadOnlyField(final String fieldName) {
		return ReadOnlyFieldMatcher.hasAReadOnlyField(fieldName);
	}


	public static Matcher<Object> isSerializable() {
		return IsSerializableMatcher.isSerializable();
	}


	public static Matcher<Class<?>> isAProperRequestScopedController() {
		return RequestScopedControllerMatcher.isAProperRequestScopedController();
	}


	public static Matcher<Class<?>> isAProperViewScopedController() {
		return ViewScopedControllerMatcher.isAProperViewScopedController();
	}


	public static Matcher<? super Class<NavigationTracker>> isAProperSessionScopedController() {
		return SessionScopedControllerMatcher.isAProperSessionScopedController();
	}


	public static Matcher<Class<?>> isRequestScoped() {
		return ClassAnnotationMatcher.isRequestScoped();
	}


	public static Matcher<Class<?>> isSessionScoped() {
		return SessionScopeMatcher.isSessionScoped();
	}


	public static Matcher<Class<?>> isAProperSingleton() {
		return SingletonMatcher.isAProperSingleton();
	}


	public static Matcher<Object> passesValidation() {
		return BeanValidationMatcher.passesValidation();
	}


	public static Matcher<Object> failsValidation() {
		return BeanValidationMatcher.failsValidation();
	}


	public static Matcher<Method> isPackageScoped() {
		return MethodModifiersMatcher.isPackageScoped();
	}


	/*
	 * For now intended to only be used by other matchers - so is protected. The idea is that other matchers should have specific business logic
	 * around which annotations belong where. Actual prod code should not be testing 'is this annotation that I put there actually there' - it should
	 * be more like 'are the annotations that are appropriate given the business functionality of the method on it'
	 */
	protected static Matcher<Class<?>> hasAnnotationOnMethod(final Class<? extends Annotation> annotationClass, final String methodName) {
		return MethodAnnotationMatcher.hasAnnotationOnPublicMethod(annotationClass, methodName);
	}


	protected static Matcher<Field> isFieldOfType(final Class<?> expectedClass) {
		return FieldTypeMatcher.isFieldOfType(expectedClass);
	}


	protected static Matcher<Field> hasAnnotationOnField(final Class<? extends Annotation> annotationToCheckFor) {
		return FieldAnnotationMatcher.hasAnnotationOnField(annotationToCheckFor);
	}


	protected static Matcher<Field> hasCorrectStrategyOnGeneratedFieldAnnotation() {
		return FieldAnnotationValueMatcher.isAnIdentityAutoGeneratedEntityField();
	}

}
