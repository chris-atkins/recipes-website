package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.ClassWithOwnEqualsHashCodeMethodsMissingToString;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOwnEqualsHashCodeToStringMethods;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOwnEqualsToStringMethodsMissingHashCode;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOwnHashCodeToStringMethodsMissingEquals;


@RunWith(JUnit4.class)
public class EqualsHashCodeToStringClassImplementationMatcherTest {

	private final EqualsHashCodeToStringClassImplementationMatcher matcher = EqualsHashCodeToStringClassImplementationMatcher
			.implementsOwnEqualsHashCodeToStringMethods();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(EqualsHashCodeToStringClassImplementationMatcher.class, hasFactoryMethod());
	}


	@Test
	public void returnsTrueForAllMethodsImplemented() {
		final boolean result = this.matcher.matches(ClassWithOwnEqualsHashCodeToStringMethods.class);
		assertThat(result, is(true));
	}


	@Test
	public void returnsFalseIfMissingEquals() throws Exception {
		final boolean result = this.matcher.matches(ClassWithOwnHashCodeToStringMethodsMissingEquals.class);
		assertThat(result, is(false));
	}


	@Test
	public void returnsFalseIfMissingHashCode() throws Exception {
		final boolean result = this.matcher.matches(ClassWithOwnEqualsToStringMethodsMissingHashCode.class);
		assertThat(result, is(false));
	}


	@Test
	public void returnsFalseIfMissingToString() throws Exception {
		final boolean result = this.matcher.matches(ClassWithOwnEqualsHashCodeMethodsMissingToString.class);
		assertThat(result, is(false));
	}

}
