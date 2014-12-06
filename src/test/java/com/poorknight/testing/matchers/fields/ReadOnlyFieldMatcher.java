package com.poorknight.testing.matchers.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


public class ReadOnlyFieldMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private static List<Class<?>> POSSIBLE_BOOLEAN_TYPES;
	private final String fieldName;

	{
		POSSIBLE_BOOLEAN_TYPES = new ArrayList<>(2);
		POSSIBLE_BOOLEAN_TYPES.add(Boolean.class);
		POSSIBLE_BOOLEAN_TYPES.add(boolean.class);
	}


	private ReadOnlyFieldMatcher(final String fieldName) {
		super();
		this.fieldName = fieldName;
	}


	@Factory
	public static ReadOnlyFieldMatcher hasAReadOnlyField(final String fieldName) {
		return new ReadOnlyFieldMatcher(fieldName);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a field that is non-public, has a public getter, and no public setter.  Expected field name: ").appendText(
				this.fieldName);
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToTest, final Description mismatchDescription) {

		if (theFieldIsPublic(classToTest)) {
			appendPublicFieldMessage(mismatchDescription);
			return false;
		}

		if (theFieldIsMissingAPublicGetter(classToTest)) {
			appendMissingGetterMessage(mismatchDescription, classToTest);
			return false;
		}

		if (theFieldHasAPublicSetter(classToTest)) {
			appendPublicSetterMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean theFieldIsPublic(final Class<?> classToTest) {
		final Field field = ReflectionUtils.findFieldInClass(classToTest, this.fieldName);
		return Modifier.isPublic(field.getModifiers());
	}


	private boolean theFieldIsMissingAPublicGetter(final Class<?> classToTest) {
		return !fieldHasAPublicGetter(classToTest);
	}


	private boolean fieldHasAPublicGetter(final Class<?> classToTest) {
		final List<String> potentialGetterNames = findPotentialGetterNames(classToTest);
		final Method method = findVisibleMethodFromPotentialNames(potentialGetterNames, classToTest);
		if (method == null) {
			return false;
		}

		return methodIsPublic(method);
	}


	private boolean theFieldHasAPublicSetter(final Class<?> classToTest) {
		final Method method = findVisibleMethod(classToTest, this.findSetterName());
		if (method == null) {
			return false;
		}

		return methodIsPublic(method);
	}


	private Method findVisibleMethodFromPotentialNames(final List<String> potentialGetterNames, final Class<?> classToInspect) {
		for (final String methodName : potentialGetterNames) {
			final Method method = findVisibleMethod(classToInspect, methodName);
			if (method != null) {
				return method;
			}
		}
		return null;
	}


	private Method findVisibleMethod(final Class<?> classToInspect, final String methodName) {
		final Collection<Method> methods = ReflectionUtils.findAllVisibleMethodsInClassWithName(classToInspect, methodName);
		if (methods.size() == 0) {
			return null;
		}

		if (methods.size() > 1) {
			throw new RuntimeException("more than one getter method found: " + methodName
					+ ".  This might not be a problem, but you should then change this matcher to "
					+ "verify that at least one of the found getter methods is public.");
		}

		return methods.iterator().next();
	}


	private boolean methodIsPublic(final Method method) {
		return Modifier.isPublic(method.getModifiers());
	}


	private List<String> findPotentialGetterNames(final Class<?> classToTest) {
		final String normalGetterName = "get" + getCapitalizedFieldName();
		if (fieldIsBoolean(classToTest)) {
			return Arrays.asList(normalGetterName, "is" + getCapitalizedFieldName());
		}

		return Arrays.asList(normalGetterName);
	}


	private String findSetterName() {
		return "set" + getCapitalizedFieldName();
	}


	private String getCapitalizedFieldName() {
		final String firstLetter = this.fieldName.substring(0, 1).toUpperCase();
		return firstLetter.concat(this.fieldName.substring(1));
	}


	private boolean fieldIsBoolean(final Class<?> classToTest) {
		final Field field = ReflectionUtils.findFieldInClass(classToTest, this.fieldName);
		return POSSIBLE_BOOLEAN_TYPES.contains(field.getType());
	}


	private void appendPublicFieldMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the field '").appendText(this.fieldName).appendText("' is a public field.");
	}


	private void appendMissingGetterMessage(final Description mismatchDescription, final Class<?> classToTest) {
		mismatchDescription.appendText("the field '").appendText(this.fieldName)
				.appendText("' does not have a getter that is public.  Expected getter name(s): ")
				.appendText(findPotentialGetterNames(classToTest).toString());
	}


	private void appendPublicSetterMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the field '").appendText(this.fieldName).appendText("' has a public setter: ").appendText(findSetterName());
	}

}
