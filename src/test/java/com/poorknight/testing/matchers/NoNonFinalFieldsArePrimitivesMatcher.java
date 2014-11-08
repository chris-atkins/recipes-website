package com.poorknight.testing.matchers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class NoNonFinalFieldsArePrimitivesMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	List<Class<? extends Serializable>> primitiveTypes = null;
	private static final List<Class<? extends Serializable>> PRIMITIVE_TYPES = Arrays.asList(//
			byte.class, //
			short.class, //
			int.class, //
			long.class, //
			float.class, //
			double.class, //
			char.class, //
			boolean.class, //
			byte[].class, //
			short[].class, //
			int[].class, //
			long[].class, //
			float[].class, //
			double[].class, //
			char[].class, //
			boolean[].class //
			);


	private NoNonFinalFieldsArePrimitivesMatcher() {
		super();
		this.primitiveTypes = PRIMITIVE_TYPES;
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has no non-final fields that are primitive types.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		final List<Field> fields = ReflectionUtils.findAllFieldsInClass(classToInspect);

		for (final Field field : fields) {
			if (fieldIsNonFinalPrimitive(field)) {
				appendNonFinalPrimitiveFieldMessage(field, mismatchDescription);
				return false;
			}
		}
		return true;
	}


	private boolean fieldIsNonFinalPrimitive(final Field field) {
		if (fieldIsNonFinal(field)) {
			return fieldIsPrimitive(field);
		}
		return false;
	}


	private boolean fieldIsNonFinal(final Field field) {
		final int mod = field.getModifiers();
		return !(Modifier.isFinal(mod));
	}


	private boolean fieldIsPrimitive(final Field field) {
		return this.primitiveTypes.contains(field.getType());
	}


	private void appendNonFinalPrimitiveFieldMessage(final Field field, final Description mismatchDescription) {
		mismatchDescription.appendText("the field ").appendText(field.getName()).appendText(" is a non-final primitive type.");
	}


	@Factory
	public static NoNonFinalFieldsArePrimitivesMatcher hasNonFinalFieldsThatArePrimitives() {
		return new NoNonFinalFieldsArePrimitivesMatcher();
	}

}
