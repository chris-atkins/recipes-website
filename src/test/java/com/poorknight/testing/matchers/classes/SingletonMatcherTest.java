package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.SingletonTestClassGood;
import com.poorknight.testing.matchers.utils.testclasses.SingletonTestClassMissingAnnotation;
import com.poorknight.testing.matchers.utils.testclasses.SingletonTestClassNotImplementingSerializable;
import com.poorknight.testing.matchers.utils.testclasses.SingletonTestClassWrongAnnotation;


@RunWith(JUnit4.class)
public class SingletonMatcherTest {

	SingletonMatcher matcher = SingletonMatcher.isAProperSingleton();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(SingletonMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWith_GoodSingletonAnnotation_AndSerializableClass() throws Exception {
		final boolean result = this.matcher.matches(SingletonTestClassGood.class);
		assertThat(result, equalTo(true));
	}


	@Test
	public void failsForWrongSingletonAnnotation() throws Exception {
		final boolean result = this.matcher.matches(SingletonTestClassWrongAnnotation.class);
		assertThat(result, equalTo(false));
	}


	@Test
	public void failsForMissingSingletonAnnotation() throws Exception {
		final boolean result = this.matcher.matches(SingletonTestClassMissingAnnotation.class);
		assertThat(result, equalTo(false));
	}


	@Test
	public void failsForClassNotImplementingSerializable() throws Exception {
		final boolean result = this.matcher.matches(SingletonTestClassNotImplementingSerializable.class);
		assertThat(result, equalTo(false));
	}
}
