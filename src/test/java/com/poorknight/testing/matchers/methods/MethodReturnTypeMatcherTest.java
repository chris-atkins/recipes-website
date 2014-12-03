package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
public class MethodReturnTypeMatcherTest {

	@Test
	public void usesFactoryMethods() throws Exception {
		assertThat(MethodReturnTypeMatcher.class, hasFactoryMethod());
	}


	@Test
	public void returnsVoidSetsExpectedReturnTypeCorrectly() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsVoid();
		final Class<?> expectedReturnType = ReflectionUtils.getFieldFromObject(matcher, "expectedReturnType");
		assertThat(expectedReturnType.getName(), equalTo(void.class.getName()));
	}


	@Test
	public void voidParamPasses() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsVoid();
		final boolean results = matcher.matches(getMethod("returnsVoid"));
		assertThat(results, equalTo(true));

	}


	@Test
	public void voidParamFails() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsVoid();
		final boolean results = matcher.matches(getMethod("returnsString"));
		assertThat(results, equalTo(false));
	}


	@Test
	public void stringParamPasses() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsType(String.class);
		final boolean results = matcher.matches(getMethod("returnsString"));
		assertThat(results, equalTo(true));
	}


	@Test
	public void wrongParamFails() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsType(String.class);
		final boolean results = matcher.matches(getMethod("returnsInt"));
		assertThat(results, equalTo(false));
	}


	@Test
	public void primitiveParamPasses() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsType(int.class);
		final boolean results = matcher.matches(getMethod("returnsInt"));
		assertThat(results, equalTo(true));
	}


	@Test
	public void nonSerializableParamPasses() throws Exception {
		final MethodReturnTypeMatcher matcher = MethodReturnTypeMatcher.returnsType(NonSerializable.class);
		final boolean results = matcher.matches(getMethod("returnsNonSerializable"));
		assertThat(results, equalTo(true));
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

		public void returnsVoid() {
			// empty on purpose
		}


		public String returnsString() {
			return "";
		}


		public int returnsInt() {
			return 0;
		}


		public NonSerializable returnsNonSerializable() {
			return new NonSerializable();
		}

	}

	class NonSerializable {
		// empty on purpose
	}
}
