package com.poorknight.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class UnitTestUtilsTest {

	@Test
	public void correctlyCallsPostConstructMethod() throws Exception {
		final ClassWithPostConstructMethod objectToTest = new ClassWithPostConstructMethod();
		assertThat(objectToTest.o, nullValue());

		UnitTestUtils.callPostConstructMethod(objectToTest);

		assertThat(objectToTest.o, notNullValue());
	}


	@Test(expected = RuntimeException.class)
	public void exceptionOnMissingPostConstructMethod() throws Exception {
		UnitTestUtils.callPostConstructMethod(new Object());
	}

}


// /////////////////////////////////////////////////

// /////////////////////////////////////////////////
class ClassWithPostConstructMethod {

	Object o = null;


	@PostConstruct
	public void init() {
		this.o = new Object();
	}
}
