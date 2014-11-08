package com.poorknight.testing.matchers;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class SingleAuditFieldMatcher extends TypeSafeDiagnosingMatcher<Field> {

	private Field fieldToInspect;
	private AuditFieldType fieldType = null;
	private FieldTypeMatcher fieldTypeMatcher = null;
	private FieldAnnotationMatcher columnAnnotationMatcher = null;
	private FieldAnnotationMatcher temporalAnnotationMatcher = null;
	private FieldAnnotationValueMatcher temporalAnnotationValueMatcher = null;


	private SingleAuditFieldMatcher(final AuditFieldType fieldType) {
		super();
		this.fieldType = fieldType;
		this.fieldTypeMatcher = FieldTypeMatcher.isFieldOfType(fieldType.expectedClass());
		this.columnAnnotationMatcher = FieldAnnotationMatcher.hasAnnotation(Column.class);
		this.temporalAnnotationMatcher = FieldAnnotationMatcher.hasAnnotation(Temporal.class);
		this.temporalAnnotationValueMatcher = FieldAnnotationValueMatcher.hasAnnotationWithFieldOfValue(Temporal.class, "value",
				TemporalType.TIMESTAMP);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("matches the criteria for an Audit Column:\n" + //
				"  - is of the correct type (" + this.fieldType.name() + ")\n" + //
				"  - has a getter but no setter \n" + //
				"  - if it is a date field, it has the @Temporal annotation\n" + //
				"  - it has an @Column annotation");
	}


	@Override
	protected boolean matchesSafely(final Field fieldToMatch, final Description mismatchDescription) {
		this.fieldToInspect = fieldToMatch;

		if (doesNotHaveCorrectFieldType(mismatchDescription)) {
			// above method will add its own mismatch description
			return false;
		}

		if (doesNotHaveAGetter()) {
			appendNoGetterMessage(mismatchDescription);
			return false;
		}

		if (hasASetter()) {
			appendHasSetterMessage(mismatchDescription);
			return false;
		}

		if (doesNotHaveAColumnAnnotation(mismatchDescription)) {
			// above method will add its own mismatch description
			return false;
		}

		// if (isDateField() && doesNotHaveTemporalAnnotationWithCorrectValue(mismatchDescription)) {
		// // above method will add its own mismatch description
		// return false;
		// }

		return true;
	}


	private boolean doesNotHaveCorrectFieldType(final Description mismatchDescription) {
		return !(this.fieldTypeMatcher.matchesSafely(this.fieldToInspect, mismatchDescription));
	}


	private boolean doesNotHaveAGetter() {
		final String getterName = findGetterMethodName();
		return !(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(this.fieldToInspect.getDeclaringClass(), getterName));
	}


	private String findGetterMethodName() {
		return findPrefixedMethodName("get");
	}


	private String findPrefixedMethodName(final String prefix) {
		final String fieldName = this.fieldToInspect.getName();
		final String firstLetter = fieldName.substring(0, 1).toUpperCase();
		final String theRest = fieldName.substring(1);
		return prefix + firstLetter + theRest;
	}


	private boolean hasASetter() {
		final String setterName = findSetterMethodName();
		return ReflectionUtils.hasAnyVisibleMethodsInClassWithName(this.fieldToInspect.getDeclaringClass(), setterName);
	}


	private String findSetterMethodName() {
		return findPrefixedMethodName("set");
	}


	private boolean doesNotHaveAColumnAnnotation(final Description mismatchDescription) {
		return !(this.columnAnnotationMatcher.matchesSafely(this.fieldToInspect, mismatchDescription));
	}


	// private boolean isDateField() {
	// return AuditFieldType.TIMESTAMP_FIELD.equals(this.fieldType);
	// }
	//
	//
	// private boolean doesNotHaveTemporalAnnotationWithCorrectValue(final Description mismatchDescription) {
	// if (!(this.temporalAnnotationMatcher.matchesSafely(this.fieldToInspect, mismatchDescription))) {
	// return true;
	// }
	//
	// return !(this.temporalAnnotationValueMatcher.matchesSafely(this.fieldToInspect, mismatchDescription));
	// }

	private void appendNoGetterMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the field ").appendText(this.fieldToInspect.getName())
				.appendText(" does not have a getter method, and it should have one.");
	}


	private void appendHasSetterMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the field ").appendText(this.fieldToInspect.getName()).appendText(" should not have a setter, but it does.");
	}


	@Factory
	public static SingleAuditFieldMatcher isACorrectUserNameAuditFieldForAnEntity() {
		return new SingleAuditFieldMatcher(AuditFieldType.STRING_FIELD);
	}


	@Factory
	public static SingleAuditFieldMatcher isACorrectTimestampAuditFieldForAnEntity() {
		return new SingleAuditFieldMatcher(AuditFieldType.TIMESTAMP_FIELD);
	}

	protected enum AuditFieldType {

		TIMESTAMP_FIELD(Timestamp.class), //
		STRING_FIELD(String.class);

		private Class<?> expectedClassType;


		private AuditFieldType(final Class<?> expectedClassType) {
			this.expectedClassType = expectedClassType;
		}


		protected Class<?> expectedClass() {
			return this.expectedClassType;
		}
	}
}
