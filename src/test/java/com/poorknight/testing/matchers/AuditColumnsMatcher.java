package com.poorknight.testing.matchers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Version;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.domain.annotations.AuditColumns;
import com.poorknight.testing.matchers.utils.ReflectionUtils;


public class AuditColumnsMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private SingleAuditFieldMatcher timestampFieldMatcher = null;
	private SingleAuditFieldMatcher userFieldMatcher = null;

	private Class<?> classToInspect;


	private AuditColumnsMatcher() {
		super();
		this.timestampFieldMatcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
		this.userFieldMatcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the @AuditColumns annotation, and follows the rules for it:\n" + //
				"  - has 4 fields for the 4 audit columns\n" + //
				"  - the 4 fields are of the correct type\n" + //
				"  - has getters but no setters for the fields\n" + //
				"  - the date fields have the @Temporal annotation\n" + //
				"  - each field has an @Column annotation\n" + "  - the lastUpdatedOn column has the @Version annotation");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToMatch, final Description mismatchDescription) {
		this.classToInspect = classToMatch;

		if (doesNotHaveAuditColumnAnnotation()) {
			appendMissingAnnotationMessage(mismatchDescription);
			return false;
		}

		if (isMissingAnyAuditFields()) {
			appendMissingAuditFieldsMessage(mismatchDescription);
			return false;
		}

		if (lastUpdateFieldDoesNotHaveVersionAnnotation()) {
			appendMissingVersionAnnotationOnLastUpdateFeildMessage(mismatchDescription);
			return false;
		}

		if (doesNotPassAuditFieldValidations(mismatchDescription)) {
			// above method will add its own messages
			return false;
		}

		return true;
	}


	private boolean doesNotHaveAuditColumnAnnotation() {
		return !(ReflectionUtils.classHasAnnotation(this.classToInspect, AuditColumns.class));
	}


	private boolean isMissingAnyAuditFields() {

		final List<String> expectedFields = getExpectedFieldNames();
		for (final String fieldName : expectedFields) {
			if (!(fieldExists(fieldName))) {
				return true;
			}
		}
		return false;
	}


	private boolean fieldExists(final String fieldName) {
		final Field field = ReflectionUtils.findFieldInClassWithNullReturns(this.classToInspect, fieldName);
		return field != null;
	}


	private boolean lastUpdateFieldDoesNotHaveVersionAnnotation() {
		final Field field = getLastUpdateField();
		return !(ReflectionUtils.fieldHasAnnotation(field, Version.class));
	}


	private Field getLastUpdateField() {
		return ReflectionUtils.findFieldInClass(this.classToInspect, getLastUpdateFieldName());
	}


	private String getLastUpdateFieldName() {
		return getAuditColumnsAnnotation().lastUpdatedOn();
	}


	private AuditColumns getAuditColumnsAnnotation() {
		final AuditColumns annotation = this.classToInspect.getAnnotation(AuditColumns.class);
		return annotation;
	}


	private boolean doesNotPassAuditFieldValidations(final Description mismatchDescription) {
		if (doesNotPassTimestampFieldValidations(mismatchDescription)) {
			return true;
		}
		return doesNotPassUserNameFieldValidations(mismatchDescription);
	}


	private boolean doesNotPassTimestampFieldValidations(final Description mismatchDescription) {

		for (final String fieldName : getExpectedTimestampFieldNames()) {

			final Field fieldToInspect = ReflectionUtils.findFieldInClass(this.classToInspect, fieldName);
			if (!(this.timestampFieldMatcher.matchesSafely(fieldToInspect, mismatchDescription))) {
				// matcher will add its own comments
				return true;
			}
		}

		return false;
	}


	private boolean doesNotPassUserNameFieldValidations(final Description mismatchDescription) {

		for (final String fieldName : getExpectedUserNameFieldNames()) {

			final Field fieldToInspect = ReflectionUtils.findFieldInClass(this.classToInspect, fieldName);
			if (!(this.userFieldMatcher.matchesSafely(fieldToInspect, mismatchDescription))) {
				// matcher will add its own comments
				return true;
			}
		}

		return false;
	}


	private boolean doesNotPassFieldValidations(final List<String> fieldNames, final SingleAuditFieldMatcher matcher,
			final Description mismatchDescription) {

		for (final String fieldName : fieldNames) {

			final Field fieldToInspect = ReflectionUtils.findFieldInClass(this.classToInspect, fieldName);
			if (!(matcher.matchesSafely(fieldToInspect, mismatchDescription))) {
				// matcher will add its own comments
				return true;
			}
		}

		return false;
	}


	private List<String> getExpectedFieldNames() {
		final List<String> fieldNames = new ArrayList<>(4);
		fieldNames.addAll(getExpectedTimestampFieldNames());
		fieldNames.addAll(getExpectedUserNameFieldNames());
		return fieldNames;
	}


	private List<String> getExpectedTimestampFieldNames() {
		final AuditColumns annotation = getAuditColumnsAnnotation();
		final List<String> fieldNames = new ArrayList<>(2);
		fieldNames.add(annotation.createdOn());
		fieldNames.add(annotation.lastUpdatedOn());
		return fieldNames;
	}


	private List<String> getExpectedUserNameFieldNames() {
		final AuditColumns annotation = getAuditColumnsAnnotation();
		final List<String> fieldNames = new ArrayList<>(2);
		fieldNames.add(annotation.createdBy());
		fieldNames.add(annotation.lastUpdatedBy());
		return fieldNames;
	}


	private void appendMissingAnnotationMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("The class ").appendText(this.classToInspect.getSimpleName())
				.appendText(" does not have the @AuditColumns annotation.");
	}


	private void appendMissingAuditFieldsMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("at least one of the four expected fields does not exist in the class: ").appendText(
				getExpectedFieldNames().toString());
	}


	private void appendMissingVersionAnnotationOnLastUpdateFeildMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("The last updated field (").appendText(getLastUpdateFieldName())
				.appendText(") is missing the @Version annotation.");
	}


	@Factory
	public static AuditColumnsMatcher correctlyImplementsAuditColumns() {
		return new AuditColumnsMatcher();
	}

}
