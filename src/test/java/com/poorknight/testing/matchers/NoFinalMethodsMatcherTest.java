package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.ChildOfClassWithFinalPrivateMethod;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithFinalPrivateMethod;


@RunWith(JUnit4.class)
public class NoFinalMethodsMatcherTest {

	private final NoFinalMethodsMatcher matcher = NoFinalMethodsMatcher.hasNoFinalMethods();


	@Test
	public void usesFactoryMethod() {
		assertThat(NoFinalMethodsMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWhenNoFinalMethodsExist() {
		final boolean result = this.matcher.matches(Object.class);
		assertThat(result, is(true));
	}


	@Test
	public void failsWithFinalPrivateMethodInBaseClass() {
		final boolean results = this.matcher.matches(ClassWithFinalPrivateMethod.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWithFinalPrivateMethodInSuperClass() {
		final boolean results = this.matcher.matches(ChildOfClassWithFinalPrivateMethod.class);
		assertThat(results, is(false));
	}
}
