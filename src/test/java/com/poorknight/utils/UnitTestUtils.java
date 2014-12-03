package com.poorknight.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.annotation.PostConstruct;

import com.poorknight.testing.matchers.methods.AnnotatedMethodsMatcher;


public class UnitTestUtils {

	private static AnnotatedMethodsMatcher hasPostConstructMethodMatcher = AnnotatedMethodsMatcher.hasAPostConstructMethod();


	private UnitTestUtils() {
		// to avoid instantiation
	}


	public static void callPostConstructMethod(final Object objectToUse) {
		assertThatObjectToUseIsValid(objectToUse.getClass());
		final Method method = findPostConstructMethod(objectToUse.getClass());
		invokeMethod(objectToUse, method);
	}


	private static void assertThatObjectToUseIsValid(final Class<?> classToUse) {
		if (failsPostConstructMatcher(classToUse)) {
			throw new RuntimeException("The object does not have a proper @PostConstruct method: " + classToUse.getSimpleName());
		}
	}


	private static boolean failsPostConstructMatcher(final Class<?> annotationClass) {
		return !(hasPostConstructMethodMatcher.matches(annotationClass));
	}


	private static Method findPostConstructMethod(final Class<?> classToUse) {
		final Collection<Method> methods = ReflectionUtils.findAllMethodsInClassWithAnnotation(classToUse, PostConstruct.class);
		assertMethodsSizeOfOne(methods);
		return methods.iterator().next();
	}


	private static void assertMethodsSizeOfOne(final Collection<Method> methods) {
		if (methods.size() != 1) {
			throw new RuntimeException("Not exactly one @PostConstruct method was found.  Found " + methods.size() + ".");
		}
	}


	private static void invokeMethod(final Object objectToUse, final Method method) {
		try {
			method.invoke(objectToUse);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
