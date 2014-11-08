package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.ChildOfClassWithFinalAttribute;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithFinal$jacocoDataFinalAttribute;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithFinalAttribute;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithTransientAnnotatedFinalAttribute;


@RunWith(JUnit4.class)
public class NoFinalAttributesNotMarkedAsTransientMatcherTest {

	private final NoFinalAttributesNotMarkedAsTransientMatcher hasNoFinalAttributesMatcher = NoFinalAttributesNotMarkedAsTransientMatcher
			.hasNoFinalNonJPATransientAttributes();


	@Test
	public void usesFactoryMethod() {
		assertThat(NoFinalAttributesNotMarkedAsTransientMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithNoFinalAttributes() {
		final boolean results = this.hasNoFinalAttributesMatcher.matches(Object.class);
		assertThat(results, is(true));
	}


	@Test
	public void failsWithFinalAttributes() {
		final boolean results = this.hasNoFinalAttributesMatcher.matches(ClassWithFinalAttribute.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWithFinalAttributeInSuper() {
		final boolean results = this.hasNoFinalAttributesMatcher.matches(ChildOfClassWithFinalAttribute.class);
		assertThat(results, is(false));
	}


	@Test
	public void passesIfFinalAttributeIsAnnotatedAsTransient() throws Exception {
		final boolean results = this.hasNoFinalAttributesMatcher.matches(ClassWithTransientAnnotatedFinalAttribute.class);
		assertThat(results, is(true));
	}


	@Test
	public void passesIf$jacocoDataExists() throws Exception {
		final boolean results = this.hasNoFinalAttributesMatcher.matches(ClassWithFinal$jacocoDataFinalAttribute.class);
		assertThat(results, is(true));
	}
}
