package com.poorknight.testing.matchers.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class FactoryMethodMatcherTest {

	private FactoryMethodMatcher factoryMethodMatcher;


	@Before
	public void init() {
		this.factoryMethodMatcher = FactoryMethodMatcher.hasFactoryMethod();
	}


	@Test
	public void passesWithFactoryMethodAndNoPublicConstructors() {
		boolean matches = this.factoryMethodMatcher.matches(PrivateOnlyConstructorWithFactoryMatcher.class);
		assertThat(matches, Matchers.is(true));
	}


	@Test
	public void failsWithNoFactoryMethod() {
		boolean matches = this.factoryMethodMatcher.matches(NoFactoryMatcher.class);
		assertThat(matches, Matchers.is(false));
	}


	@Test
	public void failsWithPublicConstructor() {
		boolean matches = this.factoryMethodMatcher.matches(PublicConstructorWithFactoryMatcher.class);
		assertThat(matches, Matchers.is(false));
	}


	@Test
	public void failsWithDefaultConstructor() {
		// using this class to avoid creating a new class outside of this file.
		// The classes being used the way all other tests are written are not public, but are package private. The
		// default constructor is then package private
		boolean matches = this.factoryMethodMatcher.matches(FactoryMethodMatcherTest.class);
		assertThat(matches, Matchers.is(false));
	}


	@Factory
	public static void fakeFactoryMethod() {
		// empty on purpose - please ignore this method - see comments in previous method
	}


	@Test
	public void testFalseForReturnsObjectOfWrongType() {
		boolean matches = this.factoryMethodMatcher.matches(FactoryReturnsWrongTypeMatcher.class);
		assertThat(matches, is(false));
	}


	@Test
	public void testsReturnsFalseForNullObjectReturnedByFactory() {
		boolean matches = this.factoryMethodMatcher.matches(FactoryReturnsNullMatcher.class);
		assertThat(matches, is(false));
	}


	@Test
	public void testsReturnsFalseForNonStaticFactory() {
		boolean matches = this.factoryMethodMatcher.matches(NonStaticFactoryMatcher.class);
		assertThat(matches, Matchers.is(false));
	}


	@Test
	public void testHasFactoryMethod() {
		assertThat(FactoryMethodMatcher.class, hasFactoryMethod());
	}
}


class NoFactoryMatcher extends BaseMatcher<Object> {

	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}
}


class NoConstructorWithFactoryMatcher extends BaseMatcher<Object> {

	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public static NoConstructorWithFactoryMatcher factory() {
		return new NoConstructorWithFactoryMatcher();
	}
}


class PublicConstructorWithFactoryMatcher extends BaseMatcher<Object> {

	public PublicConstructorWithFactoryMatcher() {
		// no need for anything here
	}


	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public static PublicConstructorWithFactoryMatcher factory() {
		return new PublicConstructorWithFactoryMatcher();
	}
}


class PrivateOnlyConstructorWithFactoryMatcher extends BaseMatcher<Object> {

	private PrivateOnlyConstructorWithFactoryMatcher() {
		// no need for anything here
	}


	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public static PrivateOnlyConstructorWithFactoryMatcher factory() {
		return new PrivateOnlyConstructorWithFactoryMatcher();
	}
}


class FactoryReturnsWrongTypeMatcher extends BaseMatcher<Object> {

	private FactoryReturnsWrongTypeMatcher() {
		// no need for anything here
	}


	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public static Object factory() {
		return new FactoryReturnsWrongTypeMatcher();
	}
}


class NonStaticFactoryMatcher extends BaseMatcher<Object> {

	private NonStaticFactoryMatcher() {
		// no need for anything here
	}


	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public NonStaticFactoryMatcher factory() {
		return new NonStaticFactoryMatcher();
	}
}


class FactoryReturnsNullMatcher extends BaseMatcher<Object> {

	private FactoryReturnsNullMatcher() {
		// no need for anything here
	}


	@Override
	public boolean matches(final Object item) {
		return false; // don't need any logic here for the test
	}


	@Override
	public void describeTo(final Description description) {
		// empty on purpose - don't need any logic here
	}


	@Factory
	public FactoryReturnsNullMatcher factory() {
		return null;
	}
}
