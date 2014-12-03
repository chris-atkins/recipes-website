package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;
import javax.transaction.Transactional;

import lombok.Data;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.domain.annotations.AuditColumns;
import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
public class ClassAnnotationMatcherTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(ClassAnnotationMatcher.class, hasFactoryMethod());
	}


	@Test
	public void matchesWithNormalCase() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Retention.class, RetentionPolicy.RUNTIME);
		final boolean result = matcher.matchesSafely(Retention.class, Description.NONE);

		assertThat(result, is(true));
	}


	@Test
	public void matchesWithSingleValueInArrayValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Target.class, ElementType.ANNOTATION_TYPE);
		final boolean result = matcher.matchesSafely(Retention.class, Description.NONE);

		assertThat(result, is(true));
	}


	@Test
	public void matchesWithArrayAsValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Target.class, ElementType.TYPE, ElementType.METHOD);
		final boolean result = matcher.matchesSafely(Transactional.class, Description.NONE);

		assertThat(result, is(true));
	}


	@Test
	public void matchesWithOutOfOrderArrayAsValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Target.class, ElementType.METHOD, ElementType.TYPE);
		final boolean result = matcher.matchesSafely(Transactional.class, Description.NONE);

		assertThat(result, is(true));
	}


	@Test
	public void failsWithWrongValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Retention.class, RetentionPolicy.SOURCE);
		final boolean result = matcher.matchesSafely(Retention.class, Description.NONE);

		assertThat(result, is(false));
	}


	@Test
	public void failsWithOneValueMissingInArrayValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Target.class, ElementType.TYPE);
		final boolean result = matcher.matchesSafely(Transactional.class, Description.NONE);

		assertThat(result, is(false));
	}


	@Test
	public void failsWithOneValueWrongInArrayValue() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Target.class, ElementType.TYPE, ElementType.PACKAGE);
		final boolean result = matcher.matchesSafely(Transactional.class, Description.NONE);

		assertThat(result, is(false));
	}


	@Test
	public void failsWithMissingAnnotation() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(Retention.class, RetentionPolicy.SOURCE);
		final boolean result = matcher.matchesSafely(Object.class, Description.NONE);

		assertThat(result, is(false));
	}


	@Test
	public void failsWithGoodMessageWhenNonRuntimeRetentionAnnotation() throws Exception {
		this.thrown.expect(RuntimeException.class);
		this.thrown.expectMessage(containsString("only supports searching for annotations with a RetentionPolicy of RUNTIME"));

		// the new Object here does not matter - it is not what is being tested
		ClassAnnotationMatcher.hasAnnotationWithValue(Data.class, new Object());
	}


	@Test
	public void passesWithNoValueExpectedOrExisting() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationWithValue(InterceptorBinding.class);
		final boolean result = matcher.matchesSafely(AuditColumns.class, Description.NONE);

		assertThat(result, is(true));
	}


	@Test
	public void classOnlyFactoryMethodHasEmptyExpectedValues() throws Exception {
		final ClassAnnotationMatcher matcher = ClassAnnotationMatcher.hasAnnotationOnClass(Retention.class);
		final Object[] attributeFromObject = ReflectionUtils.getFieldFromObject(matcher, "expectedValues");
		assertThat(attributeFromObject.length, equalTo(0));
	}
}
