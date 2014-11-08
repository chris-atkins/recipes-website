package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static com.poorknight.testing.matchers.MethodAnnotationMatcher.hasAnnotationOnMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.poorknight.testing.matchers.TestAnnotations.ClassRetention;
import com.poorknight.testing.matchers.TestAnnotations.NoRetentionSpecified;
import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;
import com.poorknight.testing.matchers.TestAnnotations.SourceRetention;


public class MethodAnnotationMatcherTest {

	@Test
	public void hasCorrectFactoryMethod() {
		assertThat(MethodAnnotationMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithSimplestCase() {
		boolean matches = hasAnnotationOnMethod(RuntimeRetention.class, "testMethod").matches(SimpleCase.class);
		assertThat(matches, is(true));
	}


	@Test
	public void failsWithoutAnnotation() {
		boolean matches = hasAnnotationOnMethod(RuntimeRetention.class, "testMethod").matches(NoAnnotation.class);
		assertThat(matches, is(false));
	}


	@Test
	public void failsWithNoMethodFound() {
		boolean matches = hasAnnotationOnMethod(RuntimeRetention.class, "nonExistentMethod").matches(SimpleCase.class);
		assertThat(matches, is(false));
	}


	@Test
	public void passesOnOverloadedMethodsWithBothHavingAnnotation() {
		boolean matches = hasAnnotationOnMethod(RuntimeRetention.class, "testMethod").matches(
				OverloadedWithAnnotations.class);
		assertThat(matches, is(true));
	}


	@Test
	public void failsOnOverloadedMethodsWithOnlyOneHavingAnnotation() {
		boolean matches = hasAnnotationOnMethod(RuntimeRetention.class, "testMethod").matches(
				OverloadedWithMismatchedAnnotations.class);
		assertThat(matches, is(false));
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithClassRetention() {
		hasAnnotationOnMethod(ClassRetention.class, "testMethod").matches(SimpleCase.class);
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithSourceRetention() {
		hasAnnotationOnMethod(SourceRetention.class, "testMethod").matches(SimpleCase.class);
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnAnnotationWithNoRetention() {
		hasAnnotationOnMethod(NoRetentionSpecified.class, "testMethod").matches(SimpleCase.class);
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