package com.poorknight.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.typeCompatibleWith;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ReflectionExceptionTest {

	@Test
	public void testIsRuntimeException() {
		assertThat(ReflectionException.class, typeCompatibleWith(RuntimeException.class)); //
	}
}
