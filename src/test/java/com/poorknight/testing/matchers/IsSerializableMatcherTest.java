package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.testing.matchers.utils.testclasses.NotReallySerializableClass;


@RunWith(MockitoJUnitRunner.class)
public class IsSerializableMatcherTest {

	IsSerializableMatcher matcher = IsSerializableMatcher.isSerializable();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(IsSerializableMatcher.class, hasFactoryMethod());
	}


	@Test
	public void returnsTrueIfSerializable() throws Exception {
		final boolean result = this.matcher.matches("hi");
		assertThat(result, is(true));
	}


	@Test
	public void returnsFalseIfObjectIsNotSerializable() throws Exception {
		final boolean result = this.matcher.matches(new Object());
		assertThat(result, is(false));
	}


	@Test
	public void returnsFalseIfNotSerializableEvenIfMarkedSerializable() throws Exception {
		final boolean result = this.matcher.matches(new NotReallySerializableClass());
		assertThat(result, is(false));
	}

}
