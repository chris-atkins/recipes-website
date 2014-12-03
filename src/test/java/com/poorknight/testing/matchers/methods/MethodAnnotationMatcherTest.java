package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static com.poorknight.testing.matchers.methods.MethodAnnotationMatcher.hasAnnotationOnPublicMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.ClassRetention;
import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.NoRetentionSpecified;
import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.RuntimeRetention;
import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.SourceRetention;


public class MethodAnnotationMatcherTest {

	@Test
	public void hasCorrectFactoryMethod() {
		assertThat(MethodAnnotationMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithSimplestCase() {
		final boolean matches = hasAnnotationOnPublicMethod(RuntimeRetention.class, "testMethod").matches(SimpleCase.class);
		assertThat(matches, is(true));
	}


	@Test
	public void failsWithoutAnnotation() {
		final boolean matches = hasAnnotationOnPublicMethod(RuntimeRetention.class, "testMethod").matches(NoAnnotation.class);
		assertThat(matches, is(false));
	}


	@Test
	public void failsWithNoMethodFound() {
		final boolean matches = hasAnnotationOnPublicMethod(RuntimeRetention.class, "nonExistentMethod").matches(SimpleCase.class);
		assertThat(matches, is(false));
	}


	@Test
	public void passesOnOverloadedMethodsWithBothHavingAnnotation() {
		final boolean matches = hasAnnotationOnPublicMethod(RuntimeRetention.class, "testMethod").matches(OverloadedWithAnnotations.class);
		assertThat(matches, is(true));
	}


	@Test
	public void failsOnOverloadedMethodsWithOnlyOneHavingAnnotation() {
		final boolean matches = hasAnnotationOnPublicMethod(RuntimeRetention.class, "testMethod").matches(OverloadedWithMismatchedAnnotations.class);
		assertThat(matches, is(false));
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithClassRetention() {
		hasAnnotationOnPublicMethod(ClassRetention.class, "testMethod").matches(SimpleCase.class);
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithSourceRetention() {
		hasAnnotationOnPublicMethod(SourceRetention.class, "testMethod").matches(SimpleCase.class);
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithNoRetention() {
		hasAnnotationOnPublicMethod(NoRetentionSpecified.class, "testMethod").matches(SimpleCase.class);
	}

}


class SimpleCase {

	@RuntimeRetention
	public void testMethod() {
		// empty on purpose
	}
}


class NoAnnotation {

	public void testMethod() {
		// empty on purpose
	}
}


class OverloadedWithAnnotations {

	@RuntimeRetention
	public void testMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	@RuntimeRetention
	public void testMethod(final String s) {
		// empty on purpose
	}
}


class OverloadedWithMismatchedAnnotations {

	public void testMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	@RuntimeRetention
	public void testMethod(final String s) {
		// empty on purpose
	}
}