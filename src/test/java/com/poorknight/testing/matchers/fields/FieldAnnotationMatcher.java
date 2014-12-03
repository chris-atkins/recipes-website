package com.poorknight.testing.matchers.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


public class FieldAnnotationMatcher extends TypeSafeDiagnosingMatcher<Field> {

	private final Class<? extends Annotation> annotationToCheckFor;


	private FieldAnnotationMatcher(final Class<? extends Annotation> annotationToCheckFor) {
		this.annotationToCheckFor = annotationToCheckFor;
	}


	@Factory
	public static FieldAnnotationMatcher hasAnnotationOnField(final Class<? extends Annotation> annotationToCheckFor) {
		return new FieldAnnotationMatcher(annotationToCheckFor);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("has the annotation @").appendText(this.annotationToCheckFor.getSimpleName());
	}


	@Override
	protected boolean matchesSafely(final Field fieldToInspect, final Description mismatchDescription) {
		if (fieldDoesNotHaveAnnotation(fieldToInspect)) {
			appendFieldDoesNotHaveAnnotationMessage(mismatchDescription, fieldToInspect);
			return false;
		}
		return true;
	}


	private boolean fieldDoesNotHaveAnnotation(final Field fieldToInspect) {
		return !(ReflectionUtils.fieldHasAnnotation(fieldToInspect, this.annotationToCheckFor));
	}


	private void appendFieldDoesNotHaveAnnotationMessage(final Description mismatchDescription, final Field fieldToInspect) {
		mismatchDescription.appendText("the annotation @").appendText(this.annotationToCheckFor.getSimpleName())
				.appendText(" does not exist on the field '").appendText(fieldToInspect.getName()).appendText("'");
	}

}
