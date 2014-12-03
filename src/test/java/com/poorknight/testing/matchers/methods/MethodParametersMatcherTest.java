package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
public class MethodParametersMatcherTest {

	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(MethodParametersMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesForMethodWithNoParameters() throws Exception {
		final Method method = getMethod("noParams");
		final boolean results = MethodParametersMatcher.hasNoParameters().matches(method);

		assertThat(results, equalTo(true));
	}


	@Test
	public void failsWhenExpectingNoParametersButOneExists() throws Exception {
		final Method method = getMethod("stringParam");
		final boolean results = MethodParametersMatcher.hasNoParameters().matches(method);

		assertThat(results, equalTo(false));
	}


	@Test
	public void passesOnSimpleCase() throws Exception {
		final Method method = getMethod("stringParam");
		final boolean results = MethodParametersMatcher.hasParameters(String.class).matches(method);

		assertThat(results, equalTo(true));
	}


	@Test
	public void failsOnWrongSingleParamType() throws Exception {
		final Method method = getMethod("integerParam");
		final boolean results = MethodParametersMatcher.hasParameters(String.class).matches(method);

		assertThat(results, equalTo(false));
	}


	@Test
	public void passesOnPrimitiveTypes() throws Exception {
		final Method method = getMethod("intParam");
		final boolean results = MethodParametersMatcher.hasParameters(int.class).matches(method);

		assertThat(results, equalTo(true));
	}


	@Test
	public void passesOnNonSerializableParamTypes() throws Exception {
		final Method method = getMethod("nonSerializableParam");
		final boolean results = MethodParametersMatcher.hasParameters(NonSerializable.class).matches(method);

		assertThat(results, equalTo(true));
	}


	@Test
	public void passesWithMulitpleOrderedParams() throws Exception {
		final Method method = getMethod("stringIntParams");
		final boolean results = MethodParametersMatcher.hasParameters(String.class, int.class).matches(method);

		assertThat(results, equalTo(true));
	}


	@Test
	public void failsWithOutOfOrderParams() throws Exception {
		final Method method = getMethod("stringIntParams");
		final boolean results = MethodParametersMatcher.hasParameters(int.class, String.class).matches(method);

		assertThat(results, equalTo(false));
	}


	private Method getMethod(final String methodName) {
		final Collection<Method> methods = ReflectionUtils.getAllMethodsInClassAndSuperClasses(ClassToTest.class);
		for (final Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	class ClassToTest {

		public void noParams() {
			// emptyOnPurpose
		}


		@SuppressWarnings("unused")
		public void stringParam(final String s) {
			// empty on purpose
		}


		@SuppressWarnings("unused")
		public void integerParam(final Integer i) {
			// empty on purpose
		}


		@SuppressWarnings("unused")
		public void intParam(final int i) {
			// empty on purpose
		}


		@SuppressWarnings("unused")
		public void nonSerializableParam(final NonSerializable ns) {
			// empty on purpose
		}


		@SuppressWarnings("unused")
		public void stringIntParams(final String s, final int i) {
			// empty on purpose
		}

	}

	class NonSerializable {
		// empty on purpose
	}
}
