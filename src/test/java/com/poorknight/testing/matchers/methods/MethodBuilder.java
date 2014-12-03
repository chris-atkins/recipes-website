package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;


public class MethodBuilder {

	public static List<Method> methodListOfSize(final int size) {
		final List<Method> methods = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			methods.add(buildMethodWithDefaultValues("" + i));
		}
		return methods;
	}


	public static Method buildMethodWithDefaultValues(final String methodName) {
		return buildMethod(Object.class, methodName, new Class<?>[0], void.class, new Class<?>[0], 0, 1, methodName, new byte[0], new byte[0],
				new byte[0]);
	}


	public static Method buildMethod(final Class<?> declaringClass, final String name, final Class<?>[] parameterTypes, final Class<?> returnType,
			final Class<?>[] checkedExceptions, final int modifiers, final int slot, final String signature, final byte[] annotations,
			final byte[] parameterAnnotations, final byte[] annotationDefault) {

		final Class<Method> methodClass = Method.class;
		final Constructor<?> constructor = methodClass.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		try {
			return (Method) constructor.newInstance(declaringClass, name, parameterTypes, returnType, checkedExceptions, modifiers, slot, signature,
					annotations, parameterAnnotations, annotationDefault);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
