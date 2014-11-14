package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.SingleAuditFieldMatcher.AuditFieldType;
import com.poorknight.testing.matchers.utils.testclasses.AuditTestClasses;
import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
public class SingleAuditFieldMatcherTest {

	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(SingleAuditFieldMatcher.class, hasFactoryMethod());
	}


	@Test
	public void auditStringColumnFactoryMethodSetsFieldTypeCorrectly() {
		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final AuditFieldType auditFieldType = ReflectionUtils.getFieldFromObject(matcher, "fieldType");
		assertThat(auditFieldType, equalTo(AuditFieldType.STRING_FIELD));
	}


	@Test
	public void auditTimestampColumnFactoryMethodSetsFieldTypeCorrectly() {
		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
		final AuditFieldType auditFieldType = ReflectionUtils.getFieldFromObject(matcher, "fieldType");
		assertThat(auditFieldType, equalTo(AuditFieldType.TIMESTAMP_FIELD));
	}


	@Test
	public void failsOnWrongFieldTypeForTimestampField() {
		final Field field = getField("intFieldWithGetterAndColumn");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsOnWrongFieldTypeForUserNameField() {
		final Field field = getField("intFieldWithGetterAndColumn");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsOnNoGetter() {
		final Field field = getField("noGetter");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsWithSetter() {
		final Field field = getField("withSetter");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsWithNoColumnAnnotation() {
		final Field field = getField("noColumnAnnotation");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(false));
	}


	//
	//
	// @Test
	// public void failsForDateWithNoTemporalAnnotation() {
	// final Field field = getField("missingTemporal");
	//
	// final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
	// final boolean results = matcher.matchesSafely(field, Description.NONE);
	//
	// assertThat(results, equalTo(false));
	// }
	//
	//
	// @Test
	// public void failsForDateWithWrongTemporalType() {
	// final Field field = getField("wrongTemporalType");
	//
	// final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
	// final boolean results = matcher.matchesSafely(field, Description.NONE);
	//
	// assertThat(results, equalTo(false));
	// }

	@Test
	public void passesOnGoodTimestampField() throws Exception {
		final Field field = getField("lastUpdatedOn");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectTimestampAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(true));
	}


	@Test
	public void passesOnGoodUserNameField() throws Exception {
		final Field field = getField("lastUpdatedBy");

		final SingleAuditFieldMatcher matcher = SingleAuditFieldMatcher.isACorrectUserNameAuditFieldForAnEntity();
		final boolean results = matcher.matchesSafely(field, Description.NONE);

		assertThat(results, equalTo(true));
	}


	private Field getField(final String fieldName) {
		return ReflectionUtils.findFieldInClass(AuditTestClasses.FieldLevelTestClass.class, fieldName);
	}

}
