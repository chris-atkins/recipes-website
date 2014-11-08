package com.poorknight.testing.matchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;


public class FieldBuilder {

	public static List<Field> fieldListOfSize(final int size) {
		final List<Field> fields = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			fields.add(buildFieldWithDefaultValues("" + i));
		}
		return fields;
	}


	public static Field buildFieldWithDefaultValues(final String fieldName) {
		return buildField(Object.class, fieldName, Object.class, Modifier.PUBLIC, Integer.valueOf(1), fieldName, new byte[] {});
	}


	public static Field buildField(final Class<?> owningClass, final String fieldName, final Class<?> type, final int modifiers, final int slot,
			final String signature, final byte[] annotations) {

		final Class<Field> fieldClass = Field.class;
		final Constructor<?> constructor = fieldClass.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		try {
			return (Field) constructor.newInstance(owningClass, fieldName, type, modifiers, slot, signature, annotations);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
