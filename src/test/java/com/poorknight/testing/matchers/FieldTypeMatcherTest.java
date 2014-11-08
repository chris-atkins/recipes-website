package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.FieldOfTypelong;


@RunWith(JUnit4.class)
public class FieldTypeMatcherTest {

	@Test
	public void usesFactoryMethod() {
		assertThat(FieldTypeMatcher.class, hasFactoryMethod());
	}


	@Test
	public void simplePassingCase() {
		final FieldTypeMatcher matcher = FieldTypeMatcher.isFieldOfType(String.class);
		final Field field = FieldBuilder.buildField(Object.class, "testField", String.class, Modifier.PRIVATE, 0, "", new byte[] {});

		final boolean results = matcher.matches(field);
		assertThat(results, is(true));
	}


	@Test
	public void simpleFailingCase() {
		final FieldTypeMatcher matcher = FieldTypeMatcher.isFieldOfType(Object.class);
		final Field field = FieldBuilder.buildField(Object.class, "testField", String.class, Modifier.PRIVATE, 0, "", new byte[] {});

		final boolean results = matcher.matches(field);
		assertThat(results, is(false));
	}


	@Test
	public void failsWhenLookingForLongIflongIsFound() throws Exception {
		final FieldTypeMatcher matcher = FieldTypeMatcher.isFieldOfType(FieldOfTypelong.class);
		final Field field = FieldOfTypelong.class.getDeclaredField("testField");

		final boolean results = matcher.matches(field);
		assertThat(results, is(false));
	}
}
