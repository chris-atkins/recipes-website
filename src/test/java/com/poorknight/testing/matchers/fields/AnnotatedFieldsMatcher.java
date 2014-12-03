package com.poorknight.testing.matchers.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.testing.matchers.Cardinality;
import com.poorknight.utils.ReflectionUtils;


/**
 * Responsible for checking whether a class has fields (attributes) that are annotated with a specific annotation. It is also possible to pass other
 * Matcher<Field> objects that will be applied to each field that has the annotation being checked for.
 *
 * Ex: It would be possible to check whether the fields with the @AttributeBeingSearchedFor are of type String by passing in a 'typeOf(String.class)'
 * matcher. (once making such a matcher)
 */
public class AnnotatedFieldsMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private final Cardinality cardinality;
	private final Class<? extends Annotation> annotationClass;
	private final Matcher<Field>[] furtherMatchersToApplyToAnnotatedAttributes;
	private List<Field> foundFields;
	private Class<?> classToInspect;


	@SafeVarargs
	private AnnotatedFieldsMatcher(final Class<? extends Annotation> annotationClass, final Cardinality cardinality,
			final Matcher<Field>... furtherMatchersToApplyToAnnotatedAttributes) {
		super();
		this.cardinality = cardinality;
		this.annotationClass = annotationClass;
		this.furtherMatchersToApplyToAnnotatedAttributes = furtherMatchersToApplyToAnnotatedAttributes;
	}


	@Factory
	@SafeVarargs
	public static AnnotatedFieldsMatcher hasAnnotationOnAttribute(final Class<? extends Annotation> annotationClass, final Cardinality cardinality,
			final Matcher<Field>... furtherMatchersToApplyToAnnotatedAttributes) {

		return new AnnotatedFieldsMatcher(annotationClass, cardinality, furtherMatchersToApplyToAnnotatedAttributes);
	}


	@Factory
	@SafeVarargs
	public static AnnotatedFieldsMatcher hasAnnotationOnExactlyOneAttribute(final Class<? extends Annotation> annotationClass,
			final Matcher<Field>... furtherMatchersToApplyToAnnotatedAttributes) {
		return new AnnotatedFieldsMatcher(annotationClass, Cardinality.EXACTLY_ONE, furtherMatchersToApplyToAnnotatedAttributes);
	}


	@Factory
	@SafeVarargs
	public static AnnotatedFieldsMatcher hasAnnotationOnAtLeastOneAttribute(final Class<? extends Annotation> annotationClass,
			final Matcher<Field>... furtherMatchersToApplyToAnnotatedAttributes) {
		return new AnnotatedFieldsMatcher(annotationClass, Cardinality.AT_LEAST_ONE, furtherMatchersToApplyToAnnotatedAttributes);
	}


	@Override
	public void describeTo(final Description description) {

		description.appendText("has ")//
				.appendText(this.cardinality.name())//
				.appendText(" field in the class with the @")//
				.appendText(this.annotationClass.getName())//
				.appendText(" annotation on it.");

		if (hasChildValidators()) {
			description.appendText("Annotated attribute(s) also meet the following criteria: ");
		}

		for (final Matcher<Field> additionalMatcher : this.furtherMatchersToApplyToAnnotatedAttributes) {
			additionalMatcher.describeTo(description);
		}
	}


	private boolean hasChildValidators() {
		return this.furtherMatchersToApplyToAnnotatedAttributes != null && this.furtherMatchersToApplyToAnnotatedAttributes.length > 0;
	}


	@Override
	protected boolean matchesSafely(final Class<?> classBeingInspected, final Description mismatchDescription) {

		this.classToInspect = classBeingInspected;

		if (failsToMeetNumberOfAttributesWithAnnotationRequirement()) {
			appendWrongNumberOfAttributesMessage(mismatchDescription);
			return false;
		}

		// call all the sub-matchers, returning false if any of them fail to match
		for (final Matcher<Field> matcher : this.furtherMatchersToApplyToAnnotatedAttributes) {
			if (doesNotMatchAppendingMismatchDescriptions(matcher, mismatchDescription)) {
				return false;
			}
		}

		return true;
	}


	private boolean failsToMeetNumberOfAttributesWithAnnotationRequirement() {
		return !(numberOfAttrubutesWithAnnotationsMeetsRequirements());
	}


	private boolean numberOfAttrubutesWithAnnotationsMeetsRequirements() {
		final int numberOfFields = getNumberOfAttributesWithAnnotation();
		switch (this.cardinality) {
		case EXACTLY_ONE:
			return exactlyOneMatches(numberOfFields);
		case AT_LEAST_ONE:
			return atLeastOneMatches(numberOfFields);
		default:
			throw new RuntimeException("Unhandled Cardinality enum type!");
		}
	}


	private int getNumberOfAttributesWithAnnotation() {
		return this.getFieldsWithAnnotation().size();
	}


	private List<Field> getFieldsWithAnnotation() {
		if (this.foundFields == null) {
			this.foundFields = ReflectionUtils.findAllFieldsWithAnnotation(this.classToInspect, this.annotationClass);
		}
		return this.foundFields;
	}


	private boolean exactlyOneMatches(final int numberOfFields) {
		return numberOfFields == 1;
	}


	private boolean atLeastOneMatches(final int numberOfFields) {
		return numberOfFields > 0;
	}


	private boolean doesNotMatchAppendingMismatchDescriptions(final Matcher<Field> matcher, final Description mismatchDescription) {
		boolean tempResults = false;
		for (final Field field : this.getFieldsWithAnnotation()) {
			tempResults = matcher.matches(field);
			if (tempResults == false) {
				appendSubMatcherMismatchMessage(field, mismatchDescription, matcher);
				return true;  // does not match
			}
		}
		return false; // matches all of them
	}


	private void appendWrongNumberOfAttributesMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("Was expecting ").appendText(this.cardinality.name()).appendText(" attributes with the @")
				.appendText(this.annotationClass.getName()).appendText(", but instead found ").appendText("" + getNumberOfAttributesWithAnnotation());
	}


	private void appendSubMatcherMismatchMessage(final Field fieldBeingInspected, final Description mismatchDescription, final Matcher<Field> matcher) {
		matcher.describeMismatch(fieldBeingInspected, mismatchDescription);
	}

}
