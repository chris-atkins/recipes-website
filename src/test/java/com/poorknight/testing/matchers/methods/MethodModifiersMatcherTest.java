package com.poorknight.testing.matchers.methods;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.poorknight.testing.matchers.methods.MethodModifiersMatcher.Modifiers;
import com.poorknight.utils.ReflectionUtils;


@RunWith(Enclosed.class)
public class MethodModifiersMatcherTest {

	@RunWith(JUnit4.class)
	public static class NonParameterizedTests {

		@Test
		public void usesFactoryMethods() throws Exception {
			assertThat(MethodModifiersMatcher.class, hasFactoryMethod());
		}


		@Test
		public void havingModifiersCorrectlySetsFields() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.havingModifiers(Modifiers.ABSTRACT, Modifiers.FINAL);
			final Modifiers[] fromMatcher = ReflectionUtils.getFieldFromObject(matcher, "havingModifiers");
			assertThat(fromMatcher, arrayContaining(Modifiers.ABSTRACT, Modifiers.FINAL));
		}


		@Test
		public void withoutModifiersCorrectlySetsFields() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.withoutModifiers(Modifiers.ABSTRACT, Modifiers.PUBLIC);
			final Modifiers[] fromMatcher = ReflectionUtils.getFieldFromObject(matcher, "withoutModifiers");
			assertThat(fromMatcher, arrayContaining(Modifiers.ABSTRACT, Modifiers.PUBLIC));
		}


		@Test
		public void isPackageScoped_SetsFieldsCorrectly() {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.isPackageScoped();
			final Modifiers[] fromMatcher = ReflectionUtils.getFieldFromObject(matcher, "havingModifiers");
			assertThat(fromMatcher, arrayContaining(Modifiers.PACKAGE));
		}


		@Test
		public void passesWithTwoHavingModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.havingModifiers(Modifiers.PUBLIC, Modifiers.FINAL);
			final boolean result = matcher.matchesSafely(getMethod("finalMethod"), Description.NONE);
			assertThat(result, equalTo(true));
		}


		@Test
		public void passesWithTwoWithoutModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.withoutModifiers(Modifiers.PUBLIC, Modifiers.FINAL);
			final boolean result = matcher.matchesSafely(getMethod("privateMethod"), Description.NONE);
			assertThat(result, equalTo(true));
		}


		@Test
		public void failsWithMixedHavingModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.havingModifiers(Modifiers.PRIVATE, Modifiers.FINAL);
			final boolean result = matcher.matchesSafely(getMethod("finalMethod"), Description.NONE);
			assertThat(result, equalTo(false));
		}


		@Test
		public void failsWithMixedWithoutModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.withoutModifiers(Modifiers.PRIVATE, Modifiers.FINAL);
			final boolean result = matcher.matchesSafely(getMethod("finalMethod"), Description.NONE);
			assertThat(result, equalTo(false));
		}
	}

	@RunWith(Parameterized.class)
	public static class ParameterizedTests {

		final String testName;
		final Modifiers modifierToCheck;
		final String methodName;
		final boolean expectedResultForHaving;


		@Parameters(name = "{0}")
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {//
					{ "Works checking for ABSTRACT method when it exists.", Modifiers.ABSTRACT, "abstractMethod", true }, //
							{ "Works checking for FINAL method when it exists.", Modifiers.FINAL, "finalMethod", true },//
							{ "Works checking for NATIVE method when it exists.", Modifiers.NATIVE, "nativeMethod", true },//
							{ "Works checking for PRIVATE method when it exists.", Modifiers.PRIVATE, "privateMethod", true },//
							{ "Works checking for PRIVATE method when it exists.", Modifiers.PACKAGE, "packageMethod", true },//
							{ "Works checking for PROTECTED method when it exists.", Modifiers.PROTECTED, "protectedMethod", true },//
							{ "Works checking for PUBLIC method when it exists.", Modifiers.PUBLIC, "publicMethod", true },//
							{ "Works checking for STATIC method when it exists.", Modifiers.STATIC, "staticMethod", true },//
							{ "Works checking for STRICT method when it exists.", Modifiers.STRICT, "strictMethod", true },//
							{ "Works checking for SYNCHRONIZED method when it exists.", Modifiers.SYNCHRONIZED, "synchronizedMethod", true }, //

							{ "Works checking for ABSTRACT method when it does not exist.", Modifiers.ABSTRACT, "publicMethod", false }, //
							{ "Works checking for FINAL method when it does not exist.", Modifiers.FINAL, "publicMethod", false },//
							{ "Works checking for NATIVE method when it does not exist.", Modifiers.NATIVE, "publicMethod", false },//
							{ "Works checking for PRIVATE method when it does not exist.", Modifiers.PRIVATE, "publicMethod", false },//
							{ "Works checking for PRIVATE method when it does not exist.", Modifiers.PACKAGE, "publicMethod", false },//
							{ "Works checking for PROTECTED method when it does not exist.", Modifiers.PROTECTED, "publicMethod", false },//
							{ "Works checking for PUBLIC method when it does not exist.", Modifiers.PUBLIC, "privateMethod", false },//
							{ "Works checking for STATIC method when it does not exist.", Modifiers.STATIC, "publicMethod", false },//
							{ "Works checking for STRICT method when it does not exist.", Modifiers.STRICT, "publicMethod", false },//
							{ "Works checking for SYNCHRONIZED method when it exists.", Modifiers.SYNCHRONIZED, "publicMethod", false } //
					});
		}


		public ParameterizedTests(final String testName, final Modifiers modifierToCheck, final String methodName, final boolean expectedResult) {
			this.testName = testName;
			this.modifierToCheck = modifierToCheck;
			this.methodName = methodName;
			this.expectedResultForHaving = expectedResult;
		}


		@Test
		public void correctResultsForHavingModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.havingModifiers(this.modifierToCheck);
			final boolean result = matcher.matchesSafely(getMethod(this.methodName), Description.NONE);
			assertThat(result, equalTo(this.expectedResultForHaving));
		}


		@Test
		public void correctResultsForWithoutModifiers() throws Exception {
			final MethodModifiersMatcher matcher = MethodModifiersMatcher.withoutModifiers(this.modifierToCheck);
			final boolean result = matcher.matchesSafely(getMethod(this.methodName), Description.NONE);
			assertThat(result, equalTo(!(this.expectedResultForHaving)));
		}

	}


	static Method getMethod(final String name) throws Exception {
		final Class<?> classUnderTest = ModifiersTestClass.class;
		return classUnderTest.getDeclaredMethod(name);
	}
}


abstract class ModifiersTestClass {

	public abstract void abstractMethod();


	public final void finalMethod() {
		// empty on purpose
	}


	public native void nativeMethod();


	private void privateMethod() {
		// empty on purpose
	}


	/* package */void packageMethod() {
		// empty on purpose
	}


	protected void protectedMethod() {
		// empty on purpose
	}


	public void publicMethod() {
		// empty on purpose
	}


	public static void staticMethod() {
		// empty on purpose
	}


	public strictfp void strictMethod() {
		// empty on purpose
	}


	public synchronized void synchronizedMethod() {
		// empty on purpose
	}
}