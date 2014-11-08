package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.ClassWithDefaultConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithMultiArgConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPackagePrivateConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPrivateConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithProtectedConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPublicConstructor;


@RunWith(JUnit4.class)
public class NoArgPublicOrProtectedConstructorMatcherTest {

	@Test
	public void usesFactoryMethod() {
		assertThat(NoArgPublicOrProtectedConstructorMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithNoArgPublicConstructor() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithPublicConstructor.class);
		assertThat(results, is(true));
	}


	@Test
	public void passesWithNoArgProtectedConstructor() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithProtectedConstructor.class);
		assertThat(results, is(true));
	}


	@Test
	public void passesWithDefaultConstructor() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithDefaultConstructor.class);
		assertThat(results, is(true));
	}


	@Test
	public void failsWithNoArgPackagePrivateConstructor() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithPackagePrivateConstructor.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWithNoArgPrivateConstructor() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithPrivateConstructor.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWithArgConstructorsOnly() {
		final boolean results = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected().matches(
				ClassWithMultiArgConstructor.class);
		assertThat(results, is(false));
	}
}
