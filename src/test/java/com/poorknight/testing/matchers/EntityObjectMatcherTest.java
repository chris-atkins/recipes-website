package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static com.poorknight.testing.matchers.CustomMatchers.meetsProjectEntityObjectStandards;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.persistence.Entity;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.poorknight.testing.matchers.utils.ReflectionUtils;
import com.poorknight.testing.matchers.utils.testclasses.ProperEntityTestClass;


@RunWith(Enclosed.class)
public class EntityObjectMatcherTest {

	@RunWith(JUnit4.class)
	public static class UnMockedEntityObjectMatcherTests {

		@Test
		public void usesFactoryMethod() {
			assertThat(EntityObjectMatcher.class, hasFactoryMethod());
		}


		@Test
		public void testGoodRealObjectActuallyPasses() {
			assertThat(ProperEntityTestClass.class, meetsProjectEntityObjectStandards());
		}

	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(ReflectionUtils.class)
	public static class MockedEntityObjectMatcherTests {

		private static final Class<?> CLASS_TO_TEST = String.class;
		private static final Class<?> NON_SERIALIZABLE_CLASS_TO_TEST = Object.class;

		@InjectMocks
		private EntityObjectMatcher entityMatcher;

		@Mock
		private EntityPrimaryKeyMatcher primaryKeyVerifier;

		@Mock
		private NoArgPublicOrProtectedConstructorMatcher noArgPublicOrProtectedConstructorMatcher;

		@Mock
		private NoFinalMethodsMatcher noFinalMethodsMatcher;

		@Mock
		private NoFinalAttributesNotMarkedAsTransientMatcher noFinalAttributesMatcher;

		@Mock
		private NoPublicSetterOnPrimaryKeyFieldsMatcher noPublicSetterMatcher;

		@Mock
		private EqualsHashCodeToStringClassImplementationMatcher equalsHashCodeToStringMatcher;

		@Mock
		private NoNonFinalFieldsArePrimitivesMatcher noPrimitiveFieldsMatcher;

		@Mock
		private AuditColumnsMatcher auditColumnsMatcher;


		@Before
		public void setup() {
			// sets up all mocks so that Object.class will pass the test as true - each test method should override
			// pertinent mocks
			PowerMockito.mockStatic(ReflectionUtils.class);
			setUpMockHasEntityAnnotationResponse(true);
			setUpMockAttributeAnnotationMatcher(true);
			setUpMockNoArgConstructorMatcher(true);
			setUpMockFinalClassResponse(false);
			setUpMockNoFinalMethodsMatcher(true);
			setUpMockNoFinalAttributesMatcher(true);
			setUpMockNoPublicSetterMatcher(true);
			setUpMockEqualsHashCodeToStringMatcher(true);
			setUpMockNoPrimitiveFieldsMatcher(true);
			setUpMockAuditColumnsMatcher(true);
		}


		private void setUpMockHasEntityAnnotationResponse(final boolean response) {
			PowerMockito.when(ReflectionUtils.classHasAnnotation(CLASS_TO_TEST, Entity.class)).thenReturn(response);
		}


		private void setUpMockAttributeAnnotationMatcher(final boolean result) {
			when(this.primaryKeyVerifier.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		private void setUpMockNoArgConstructorMatcher(final boolean result) {
			when(this.noArgPublicOrProtectedConstructorMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class)))
					.thenReturn(result);
		}


		private void setUpMockFinalClassResponse(final boolean result) {
			PowerMockito.when(ReflectionUtils.classIsFinal(CLASS_TO_TEST)).thenReturn(result);
		}


		private void setUpMockNoFinalMethodsMatcher(final boolean result) {
			when(this.noFinalMethodsMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		private void setUpMockNoFinalAttributesMatcher(final boolean result) {
			when(this.noFinalAttributesMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		private void setUpMockNoPublicSetterMatcher(final boolean result) {
			when(this.noPublicSetterMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		private void setUpMockEqualsHashCodeToStringMatcher(final boolean result) {
			when(this.equalsHashCodeToStringMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);

		}


		private void setUpMockNoPrimitiveFieldsMatcher(final boolean result) {
			when(this.noPrimitiveFieldsMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		private void setUpMockAuditColumnsMatcher(final boolean result) {
			when(this.auditColumnsMatcher.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(result);
		}


		@Test
		public void failsIfClassIsNotAnnotatedAsEntity() {
			setUpMockHasEntityAnnotationResponse(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfClassIsAnnotatedAsEntity() {
			setUpMockHasEntityAnnotationResponse(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void passesIfIdMeetsStandards() {
			when(this.primaryKeyVerifier.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfIdDoesNotMeetStandards() {
			when(this.primaryKeyVerifier.matchesSafely(Matchers.eq(CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfConstructerExistsWithCorrectVisibility() {
			setUpMockNoArgConstructorMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfNoConstructerExistsWithCorrectVisibility() {
			setUpMockNoArgConstructorMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfNotAFinalClass() {
			setUpMockFinalClassResponse(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfIsAFinalClass() {
			setUpMockFinalClassResponse(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesWithNoFinalMethods() throws Exception {
			setUpMockNoFinalMethodsMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfHasFinalMethods() {
			setUpMockNoFinalMethodsMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesWithNoFinalAttributes() throws Exception {
			setUpMockNoFinalAttributesMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfHasFinalAttributes() {
			setUpMockNoFinalAttributesMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesWithSerializableClass() throws Exception {
			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfNotImplementsSerializable() {
			final boolean result = this.entityMatcher.matches(NON_SERIALIZABLE_CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfNoPublicSetterOnPrimaryKey() {
			setUpMockNoPublicSetterMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsWithPublicSetterOnPrimaryKey() {
			setUpMockNoPublicSetterMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfImplementsOwnEqualsHashCodeToStringMethods() {
			setUpMockEqualsHashCodeToStringMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfDoesNotImplementOwnEqualsHashCodeToStringMethods() {
			setUpMockEqualsHashCodeToStringMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesIfHasNoFieldsThatArePrimitives() {
			setUpMockNoPrimitiveFieldsMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsIfHasFieldsThatArePrimitives() {
			setUpMockNoPrimitiveFieldsMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void passesWithAuditColumns() throws Exception {
			setUpMockAuditColumnsMatcher(true);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(true));
		}


		@Test
		public void failsWithNoAuditColumns() {
			setUpMockAuditColumnsMatcher(false);

			final boolean result = this.entityMatcher.matches(CLASS_TO_TEST);
			assertThat(result, is(false));
		}


		@Test
		public void constructorSetsUpAllFields() throws Exception {
			final EntityObjectMatcher matcher = EntityObjectMatcher.meetsProjectEntityObjectStandards();
			for (final Field field : ReflectionUtils.findAllFieldsInClass(matcher.getClass())) {
				assertThat(ReflectionUtils.getAttributeFromObject(matcher, field), notNullValue());
			}
		}
	}

}
