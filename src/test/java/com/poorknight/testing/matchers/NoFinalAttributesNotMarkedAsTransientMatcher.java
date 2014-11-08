package com.poorknight.testing.matchers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Transient;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class NoFinalAttributesNotMarkedAsTransientMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private final List<String> exceptions = Arrays.asList("$jacocoData");


	private NoFinalAttributesNotMarkedAsTransientMatcher() {
		super();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has no private attributes in the class or any super classes.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		final List<Field> allFieldsInClass = ReflectionUtils.findAllFieldsInClass(classToInspect);

		for (final Field field : allFieldsInClass) {
			if (fieldFailsCriteria(field)) {
				mismatchDescription.appendText("found a final field that is not annotated with @Transient: ").appendText(field.getName());
				return false;
			}
		}
		return true;
	}


	private boolean fieldFailsCriteria(final Field field) {
		final int modifiers = field.getModifiers();

		return Modifier.isFinal(modifiers) && fieldIsNotAnnotatedWithTransient(field) && isNotExceptionCase(field);

	}


	private boolean isNotExceptionCase(final Field field) {
		return !(this.exceptions.contains(field.getName()));
	}


	private boolean fieldIsNotAnnotatedWithTransient(final Field field) {
		return !(ReflectionUtils.fieldHasAnnotation(field, Transient.class));
	}


	@Factory
	public static NoFinalAttributesNotMarkedAsTransientMatcher hasNoFinalNonJPATransientAttributes() {
		return new NoFinalAttributesNotMarkedAsTransientMatcher();
	}

}
