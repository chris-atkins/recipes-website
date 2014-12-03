package com.poorknight.testing.matchers.fields;

import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class FieldTypeMatcher extends TypeSafeDiagnosingMatcher<Field> {

	private final Class<?> expectedClass;


	private FieldTypeMatcher(final Class<?> expectedClass) {
		super();
		this.expectedClass = expectedClass;
	}


	@Factory
	public static FieldTypeMatcher isFieldOfType(final Class<?> expectedClass) {
		return new FieldTypeMatcher(expectedClass);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("is of type " + this.expectedClass.getName());
	}


	@Override
	protected boolean matchesSafely(final Field field, final Description mismatchDescription) {
		if (fieldIsNotOfTheCorrectType(field)) {
			appendBadFieldTypeMessage(mismatchDescription, field);
			return false;
		}
		return true;
	}


	private boolean fieldIsNotOfTheCorrectType(final Field field) {
		return !(fieldTypeMatchesExpectedType(field));
	}


	private boolean fieldTypeMatchesExpectedType(final Field field) {
		return field.getType().equals(this.expectedClass);
	}


	private void appendBadFieldTypeMessage(final Description mismatchDescription, final Field field) {
		mismatchDescription.appendText("Bad field type!  Expecting field of type ").appendText(this.expectedClass.getName())
				.appendText(", but found field of type ").appendText(field.getType().getName());
		mismatchDescription.appendText(". Note that we are not checking for 'castable into', but are checking for the exact type.");
	}

}
