package com.poorknight.testing.matchers.fields;

import java.lang.reflect.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class EntityPrimaryKeyMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private AnnotatedFieldsMatcher idAttributeVerifier = null;


	private EntityPrimaryKeyMatcher() {
		super();
		final Matcher<Field> idLongFieldVerifier = FieldTypeMatcher.isFieldOfType(Long.class);
		final Matcher<Field> idIsAutoGeneratedFieldVerifier = FieldAnnotationMatcher.hasAnnotationOnField(GeneratedValue.class);
		final Matcher<Field> generatedFieldUsesCorrectStrategyVerifier = FieldAnnotationValueMatcher.isAnIdentityAutoGeneratedEntityField();

		this.idAttributeVerifier = AnnotatedFieldsMatcher.hasAnnotationOnExactlyOneAttribute(Id.class, idLongFieldVerifier,
				idIsAutoGeneratedFieldVerifier, generatedFieldUsesCorrectStrategyVerifier);
	}


	@Factory
	public static EntityPrimaryKeyMatcher hasValidPrimaryKey() {
		return new EntityPrimaryKeyMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has a single field of type Long that is annotated as the primary key for this class, and is auto-generated.");
	}


	@Override
	public boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotContainSingleIdAttributeOfTypeLong(classToInspect, mismatchDescription)) {
			// message will be applied as child matchers are called by the above method
			return false;
		}
		return true;
	}


	private boolean doesNotContainSingleIdAttributeOfTypeLong(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.idAttributeVerifier.matchesSafely(classToInspect, mismatchDescription));
	}

}
