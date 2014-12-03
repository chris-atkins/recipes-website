package com.poorknight.testing.matchers.fields;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.testing.matchers.utils.testclasses.ChildOfClassWithPrimitiveLongField;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithNoPrimitiveFields;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOnlyFinalPrimitiveFields;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPrimitiveLongField;


@RunWith(Enclosed.class)
public class NoNonFinalFieldsArePrimitivesMatcherTest {

	@RunWith(JUnit4.class)
	public static class NoNonFinalFieldsArePrimitivesMatcherJunitTest {

		NoNonFinalFieldsArePrimitivesMatcher matcher = NoNonFinalFieldsArePrimitivesMatcher.hasNonFinalFieldsThatArePrimitives();


		@Test
		public void usesFactoryMethodForInstantiation() throws Exception {
			assertThat(NoNonFinalFieldsArePrimitivesMatcher.class, hasFactoryMethod());
		}


		@Test
		public void failsWithNonFinalPrimitiveFieldsInClass() throws Exception {
			final boolean result = this.matcher.matches(ClassWithPrimitiveLongField.class);
			assertThat(result, is(false));
		}


		@Test
		public void failsWithNonFinalPrimitiveFieldsInParentClass() throws Exception {
			final boolean result = this.matcher.matches(ChildOfClassWithPrimitiveLongField.class);
			assertThat(result, is(false));
		}


		@Test
		public void passesWithClassWithNonPrimitiveFields() throws Exception {
			final boolean result = this.matcher.matches(ClassWithNoPrimitiveFields.class);
			assertThat(result, is(true));
		}


		@Test
		public void passesWithClassWithOnlyFinalPrimitiveFields() throws Exception {
			final boolean result = this.matcher.matches(ClassWithOnlyFinalPrimitiveFields.class);
			assertThat(result, is(true));
		}
	}

	@RunWith(MockitoJUnitRunner.class)
	public static class NoNonFinalFieldsArePrimitivesMatcherMockitoTest {

		@InjectMocks
		NoNonFinalFieldsArePrimitivesMatcher matcher;

		@Mock
		List<Class<? extends Serializable>> primitiveTypes;


		@Test
		public void failsIfFieldTypeIsInList() throws Exception {
			when(this.primitiveTypes.contains(long.class)).thenReturn(true);

			final boolean result = this.matcher.matches(ClassWithPrimitiveLongField.class);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfFieldTypeIsNotInList() {
			when(this.primitiveTypes.contains(long.class)).thenReturn(false);

			final boolean result = this.matcher.matches(ClassWithPrimitiveLongField.class);
			assertThat(result, is(true));
		}
	}

	@RunWith(Parameterized.class)
	public static class ExpectedPrimitivesAreInListTest {

		Class<? extends Serializable> expectedClass;

		NoNonFinalFieldsArePrimitivesMatcher matcher = NoNonFinalFieldsArePrimitivesMatcher.hasNonFinalFieldsThatArePrimitives();


		@Parameters
		public static List<Object[]> params() {
			return Arrays.asList(new Object[][] { { byte.class }, //
					{ short.class }, //
					{ int.class }, //
					{ long.class }, //
					{ float.class }, //
					{ double.class }, //
					{ char.class }, //
					{ boolean.class }, //
					{ byte[].class }, //
					{ short[].class }, //
					{ int[].class }, //
					{ long[].class }, //
					{ float[].class }, //
					{ double[].class }, //
					{ char[].class }, //
					{ boolean[].class } //
					});
		}


		public ExpectedPrimitivesAreInListTest(final Class<? extends Serializable> expectedClass) {
			this.expectedClass = expectedClass;
		}


		@Test
		public void primitiveListHasExpectedClass() throws Exception {
			MatcherAssert.assertThat(this.matcher.primitiveTypes.contains(this.expectedClass), is(true));
		}


		@Test
		public void testNoExtraElementsAreInTheList() {
			assertThat(this.matcher.primitiveTypes.size(), equalTo(params().size()));
		}
	}
}
