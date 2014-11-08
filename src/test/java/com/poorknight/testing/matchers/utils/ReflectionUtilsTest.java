package com.poorknight.testing.matchers.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.exceptions.ReflectionException;
import com.poorknight.testing.matchers.TestAnnotations.ClassRetention;
import com.poorknight.testing.matchers.TestAnnotations.NoRetentionSpecified;
import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;
import com.poorknight.testing.matchers.TestAnnotations.SourceRetention;
import com.poorknight.testing.matchers.utils.packageprivatetesting.ExtendsMethodVisibilityInDifferentPackage;
import com.poorknight.testing.matchers.utils.testclasses.AttributeAnnotationFinderTestClass;
import com.poorknight.testing.matchers.utils.testclasses.AttributeFinderTestObject;
import com.poorknight.testing.matchers.utils.testclasses.ChildClassWithMethodsOfAllVisibility;
import com.poorknight.testing.matchers.utils.testclasses.ChildClassWithMethodsOfAllVisibilityAndOverload;
import com.poorknight.testing.matchers.utils.testclasses.ChildClassWithMethodsOfAllVisibilityAndOverride;
import com.poorknight.testing.matchers.utils.testclasses.ChildOfClassWithOneOfEachVisibilityFields;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithDefaultConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithFieldHavingAnnotations;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithMethodsOfAllVisibility;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOneOfEachVisibilityFields;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithOverloadedMethodsOfDifferentVisibility;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPackagePrivateConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPrivateConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithProtectedConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPublicConstructor;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithRuntimeAnnotation;
import com.poorknight.testing.matchers.utils.testclasses.ExtendsAttributeAnnotationFinderTestClass;
import com.poorknight.testing.matchers.utils.testclasses.ExtendsExtendsMethodVisibility;
import com.poorknight.testing.matchers.utils.testclasses.ExtendsMethodVisibility;
import com.poorknight.testing.matchers.utils.testclasses.FinalClass;
import com.poorknight.testing.matchers.utils.testclasses.InheritedRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.MethodVisibility;
import com.poorknight.testing.matchers.utils.testclasses.MulitpleWithInheritedRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.OverloadedRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.ParentOfAttributeFinderTestObject;
import com.poorknight.testing.matchers.utils.testclasses.PrivateRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.PublicAndProtectedRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.PublicRuntimeRetentionTypeClass;
import com.poorknight.testing.matchers.utils.testclasses.SingleAnnotationOfEachRetentionTypeClass;


@RunWith(Enclosed.class)
public class ReflectionUtilsTest {

	@RunWith(JUnit4.class)
	public static class HasAnyPublicMethodsWithAnnotationTest {

		@Test
		public void returnsTrueForRuntimeRetentionPolicy() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(SingleAnnotationOfEachRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(testResults, is(true));
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForClassRetentionPolicy() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(SingleAnnotationOfEachRetentionTypeClass.class,
					ClassRetention.class);
			assertThat(testResults, is(false));
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForSourceRetentionPolicy() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(SingleAnnotationOfEachRetentionTypeClass.class,
					SourceRetention.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void returnsFalseForNoAnnotations() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(Object.class, RuntimeRetention.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void returnsFalseForPrivateMethods() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(PrivateRuntimeRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void returnsTrueForInheritedMethods() {
			final boolean testResults = ReflectionUtils.hasAnyPublicMethodsWithAnnotation(InheritedRuntimeRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(testResults, is(true));
		}
	}

	@RunWith(JUnit4.class)
	public static class HasAnyPublicConstructorTest {

		@Test
		public void returnsTrueForPublicConstructor() {
			final boolean testResults = ReflectionUtils.hasAnyPublicConstructor(ClassWithPublicConstructor.class);
			assertThat(testResults, is(true));
		}


		@Test
		public void returnsFalseForPrivateConstructor() {
			final boolean testResults = ReflectionUtils.hasAnyPublicConstructor(ClassWithPrivateConstructor.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void returnsFalseForProtectedConstructor() {
			final boolean testResults = ReflectionUtils.hasAnyPublicConstructor(ClassWithProtectedConstructor.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void returnsFalseForPackagePrivateConstructor() {
			final boolean testResults = ReflectionUtils.hasAnyPublicConstructor(ClassWithPackagePrivateConstructor.class);
			assertThat(testResults, is(false));
		}


		@Test
		public void testTrueForDefaultConstructorOnPublicMethod() {
			final boolean testResults = ReflectionUtils.hasAnyPublicConstructor(ClassWithDefaultConstructor.class);
			assertThat(testResults, is(true));
		}
	}

	@RunWith(JUnit4.class)
	public static class FindMethodsInClassWithAnnotation {

		@Test
		public void returnsSingleMethodCorrectly() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(PublicRuntimeRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(methods, hasSize(1));
			assertThat(methods.iterator().next().getName(), is("method2"));
		}


		@Test
		public void returnsMultipleMethodsCorrectly() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(
					MulitpleWithInheritedRuntimeRetentionTypeClass.class, RuntimeRetention.class);
			assertThat(methods, hasSize(2));
			// could test for names here, but does not seem to be worth the complicated code, since single names came
			// correctly
		}


		@Test
		public void returnsInheritedPublicMethods() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(InheritedRuntimeRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(methods, hasSize(1));
			assertThat(methods.iterator().next().getName(), is("method3"));

		}


		@Test
		public void returnsNoMethodsCorrectly() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(Object.class, RuntimeRetention.class);
			assertThat(methods, notNullValue());
			assertThat(methods, emptyCollectionOf(Method.class));
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForSourceRetentionAnnotations() {
			ReflectionUtils.findPublicMethodsInClassWithAnnotation(Object.class, SourceRetention.class);
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForClassRetentionAnnotations() {
			ReflectionUtils.findPublicMethodsInClassWithAnnotation(Object.class, ClassRetention.class);
		}


		@Test
		public void returnsMethodWhereOtherAnnotationsArePresentCorrectly() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(SingleAnnotationOfEachRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(methods, hasSize(1));
			assertThat(methods.iterator().next().getName(), is("method3"));
		}


		@Test
		public void doesNotReturnPrivateMethods() {
			final Collection<Method> methods = ReflectionUtils.findPublicMethodsInClassWithAnnotation(PrivateRuntimeRetentionTypeClass.class,
					RuntimeRetention.class);
			assertThat(methods, notNullValue());
			assertThat(methods, emptyCollectionOf(Method.class));
		}
	}

	@RunWith(JUnit4.class)
	public static class FindAllPublicMethodsWithName {

		@Test
		public void findsSinglePublicMethod() {
			final Collection<Method> results = ReflectionUtils.findAllPublicMethodsWithName(PublicAndProtectedRuntimeRetentionTypeClass.class,
					"method3");

			assertThat(results, hasSize(1));
			assertThat(results.iterator().next().getName(), is("method3"));
		}


		@Test
		public void doesNotFindProtectedMethods() {
			final Collection<Method> results = ReflectionUtils.findAllPublicMethodsWithName(PublicAndProtectedRuntimeRetentionTypeClass.class,
					"method2");

			assertThat(results, hasSize(0));
		}


		@Test
		public void findsMethodFromParentClass() {
			final Collection<Method> results = ReflectionUtils.findAllPublicMethodsWithName(PublicAndProtectedRuntimeRetentionTypeClass.class,
					"method3");

			assertThat(results, hasSize(1));
			assertThat(results.iterator().next().getName(), is("method3"));
		}


		@Test
		public void findsOverloadedMethods() {
			final Collection<Method> results = ReflectionUtils.findAllPublicMethodsWithName(OverloadedRuntimeRetentionTypeClass.class, "overloaded");

			assertThat(results, hasSize(2));
			final Iterator<Method> iterator = results.iterator();
			assertThat(iterator.next().getName(), is("overloaded"));
			assertThat(iterator.next().getName(), is("overloaded"));
		}
	}

	@RunWith(JUnit4.class)
	public static class AnnotationHasRuntimeRetentionTest {

		@Test
		public void returnsTrueForRuntimeRetentionAnnotation() {
			assertThat(ReflectionUtils.annotationHasRuntimeRetention(RuntimeRetention.class), is(true));
		}


		@Test
		public void returnsFalseForClassRetentionAnnotation() {
			assertThat(ReflectionUtils.annotationHasRuntimeRetention(ClassRetention.class), is(false));
		}


		@Test
		public void returnsFalseForSourceRetentionAnnotation() {
			assertThat(ReflectionUtils.annotationHasRuntimeRetention(SourceRetention.class), is(false));
		}


		@Test
		public void returnsFalseForNoRetentionSpecifiedAnnotation() {
			assertThat(ReflectionUtils.annotationHasRuntimeRetention(NoRetentionSpecified.class), is(false));
		}
	}

	@RunWith(JUnit4.class)
	public static class HasAnyVisibleMethodsInClassWithNameTest {

		@Test
		public void returnsFalseWhenMethodNameDoesNotExist() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(MethodVisibility.class, "nonexistent"), is(false));
		}


		@Test
		public void returnsTrueForPrivateMethodInActualClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(MethodVisibility.class, "privateMethod"), is(true));
		}


		@Test
		public void returnsTrueForPackagePrivateMethodInActualClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(MethodVisibility.class, "packagePrivateMethod"), is(true));
		}


		@Test
		public void returnsTrueForProtectedMethodInActualClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(MethodVisibility.class, "protectedMethod"), is(true));
		}


		@Test
		public void returnsTrueForPublicMethodInActualClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(MethodVisibility.class, "publicMethod"), is(true));
		}


		@Test
		public void returnsFalseForPrivateMethodInSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "privateMethod"), is(false));
		}


		@Test
		public void returnsTrueForPackagePrivateMethodInSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "packagePrivateMethod"), is(true));
		}


		@Test
		public void returnsTrueForProtectedMethodInSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "protectedMethod"), is(true));
		}


		@Test
		public void returnsTrueForPublicMethodInSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "publicMethod"), is(true));
		}


		@Test
		public void returnsTrueForProtectedMethodInSuperSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsExtendsMethodVisibility.class, "protectedMethod"), is(true));
		}


		@Test
		public void returnsTrueForPackagePrivateMethodInSuperSuperClass() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsExtendsMethodVisibility.class, "packagePrivateMethod"), is(true));
		}


		@Test
		public void returnsFalseForPackagePrivateMethodWhereSuperClassIsInDifferentPackage() {
			assertThat(ReflectionUtils.hasAnyVisibleMethodsInClassWithName(ExtendsMethodVisibilityInDifferentPackage.class, "packagePrivateMethod"),
					is(false));
		}
	}

	@RunWith(JUnit4.class)
	public static class FindAllVisibleMethodsInClassWithNameTest {

		@Test
		public void returnsEmptyListWhenMethodNameDoesNotExist() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(MethodVisibility.class, "nonexistent"), empty());
		}


		@Test
		public void findsPrivateMethodInActualClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(MethodVisibility.class, "privateMethod"), hasSize(1));
		}


		@Test
		public void findsPackagePrivateMethodInActualClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(MethodVisibility.class, "packagePrivateMethod"), hasSize(1));
		}


		@Test
		public void findsProtectedMethodInActualClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(MethodVisibility.class, "protectedMethod"), hasSize(1));
		}


		@Test
		public void findsPublicMethodInActualClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(MethodVisibility.class, "publicMethod"), hasSize(1));
		}


		@Test
		public void returnsFalseForPrivateMethodInSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "privateMethod"), empty());
		}


		@Test
		public void findsPackagePrivateMethodInSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "packagePrivateMethod"), hasSize(1));
		}


		@Test
		public void findsProtectedMethodInSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "protectedMethod"), hasSize(1));
		}


		@Test
		public void findsPublicMethodInSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsMethodVisibility.class, "publicMethod"), hasSize(1));
		}


		@Test
		public void findsProtectedMethodInSuperSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsExtendsMethodVisibility.class, "protectedMethod"), hasSize(1));
		}


		@Test
		public void findsPackagePrivateMethodInSuperSuperClass() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsExtendsMethodVisibility.class, "packagePrivateMethod"), hasSize(1));
		}


		@Test
		public void doesNotFindPackagePrivateMethodWhereSuperClassIsInDifferentPackage() {
			assertThat(ReflectionUtils.findAllVisibleMethodsInClassWithName(ExtendsMethodVisibilityInDifferentPackage.class, "packagePrivateMethod"),
					empty());
		}


		@Test
		public void findsOverloadedMethodsWithDifferentVisibility() {
			assertThat(
					ReflectionUtils.findAllVisibleMethodsInClassWithName(ClassWithOverloadedMethodsOfDifferentVisibility.class, "overloadedMethod"),
					hasSize(5));
		}
	}

	@RunWith(JUnit4.class)
	public static class ClassHasAnnotationTest {

		@Test
		public void returnsFalseIfAnnotationDoesNotExist() {
			assertThat(ReflectionUtils.classHasAnnotation(Object.class, RuntimeRetention.class), is(false));
		}


		@Test
		public void returnsTrueIfAnnotationExists() {
			assertThat(ReflectionUtils.classHasAnnotation(ClassWithRuntimeAnnotation.class, RuntimeRetention.class), is(true));
		}


		@Test(expected = RuntimeException.class)
		public void throwsRuntimeExceptionForClassRetentionAnnotation() {
			ReflectionUtils.classHasAnnotation(ClassWithRuntimeAnnotation.class, ClassRetention.class);
		}


		@Test(expected = RuntimeException.class)
		public void throwsRuntimeExceptionForSourceRetentionAnnotation() {
			ReflectionUtils.classHasAnnotation(ClassWithRuntimeAnnotation.class, ClassRetention.class);
		}


		@Test(expected = RuntimeException.class)
		public void throwsRuntimeExceptionForDefaultRetentionAnnotation() {
			ReflectionUtils.classHasAnnotation(ClassWithRuntimeAnnotation.class, ClassRetention.class);
		}
	}

	@RunWith(JUnit4.class)
	public static class FindAllAttributesWithAnnotationTest {

		@Test
		public void findsAllAttributesInSameClassRegardlessOfVisibility() {
			final List<Field> fields = ReflectionUtils.findAllFieldsWithAnnotation(AttributeAnnotationFinderTestClass.class, RuntimeRetention.class);
			assertThat(fields.size(), is(4));
		}


		@Test
		public void findsAllAttributesInSuperClassRegardlessOfVisibility() {
			final List<Field> fields = ReflectionUtils.findAllFieldsWithAnnotation(ExtendsAttributeAnnotationFinderTestClass.class,
					RuntimeRetention.class);
			assertThat(fields.size(), is(4));
		}
	}

	@RunWith(JUnit4.class)
	public static class GetAttributeFromObjectTest {

		@Test
		public void getAttributeFromObjectTest() {
			final AttributeFinderTestObject object = new AttributeFinderTestObject();
			final String result = ReflectionUtils.getAttributeFromObject(object, "findMe");

			assertThat(result, is("found"));
		}


		@Test
		public void worksOkWithNull() {
			final AttributeFinderTestObject object = new AttributeFinderTestObject();
			final String result = ReflectionUtils.getAttributeFromObject(object, "nullField");

			assertThat(result, nullValue());
		}


		@Test
		public void getAttributeFromObjectByFieldTest() throws Exception {
			final ParentOfAttributeFinderTestObject object = new ParentOfAttributeFinderTestObject();
			final Field field = object.getClass().getDeclaredField("findMe");
			final String result = ReflectionUtils.getAttributeFromObject(object, field);

			assertThat(result, is("found"));
		}


		@Test(expected = ReflectionException.class)
		public void doesNotThrowNullPointerExceptionWhenNonFoundAttribute() throws Exception {
			try {
				final AttributeFinderTestObject object = new AttributeFinderTestObject();
				ReflectionUtils.getAttributeFromObject(object, "cantFindMe");

			} catch (final NullPointerException e) {
				fail("Should not get a NullPointerException, but did get one.");
			}
		}
	}

	@RunWith(JUnit4.class)
	public static class ClassIsFinalTest {

		@Test
		public void returnsFalseForNonFinalClass() {
			final boolean result = ReflectionUtils.classIsFinal(Object.class);

			assertThat(result, is(false));
		}


		@Test
		public void returnsTrueForFinalClass() {
			final boolean result = ReflectionUtils.classIsFinal(FinalClass.class);

			assertThat(result, is(true));
		}
	}

	@RunWith(JUnit4.class)
	public static class GetAllMethodsInClassAndSuperClassesTest {

		private static final int NUMBER_OF_METHODS_IN_OBJECT_CLASS = Object.class.getDeclaredMethods().length;
		private static final int NUMBER_OF_METHODS_IN_BASE = ClassWithMethodsOfAllVisibility.class.getDeclaredMethods().length;
		private static final int NUMBER_OF_METHODS_IN_CHILD = ChildClassWithMethodsOfAllVisibilityAndOverload.class.getDeclaredMethods().length;
		private static final int NUMBER_OF_METHODS_IN_OVERLOAD_CLASS = ChildClassWithMethodsOfAllVisibilityAndOverride.class.getDeclaredMethods().length;


		@Test
		public void getsAllMethodsInSameClass() throws Exception {
			final Collection<Method> results = ReflectionUtils.getAllMethodsInClassAndSuperClasses(ClassWithMethodsOfAllVisibility.class);
			assertThat(results, hasSize(NUMBER_OF_METHODS_IN_BASE + NUMBER_OF_METHODS_IN_OBJECT_CLASS));
		}


		@Test
		public void getsAllMethodsInSuperClassesPlusSameClass() throws Exception {
			final Collection<Method> results = ReflectionUtils.getAllMethodsInClassAndSuperClasses(ChildClassWithMethodsOfAllVisibility.class);
			assertThat(results, hasSize(NUMBER_OF_METHODS_IN_CHILD + NUMBER_OF_METHODS_IN_BASE + NUMBER_OF_METHODS_IN_OBJECT_CLASS));
		}


		@Test
		public void overlaodedMethodsAreNotHidden() throws Exception {
			final Collection<Method> results = ReflectionUtils
					.getAllMethodsInClassAndSuperClasses(ChildClassWithMethodsOfAllVisibilityAndOverload.class);
			assertThat(results, hasSize(NUMBER_OF_METHODS_IN_CHILD + NUMBER_OF_METHODS_IN_BASE + NUMBER_OF_METHODS_IN_OBJECT_CLASS
					+ NUMBER_OF_METHODS_IN_OVERLOAD_CLASS));
		}


		@Test
		public void overriddenMethodsAreNotHidden() throws Exception {
			final Collection<Method> results = ReflectionUtils
					.getAllMethodsInClassAndSuperClasses(ChildClassWithMethodsOfAllVisibilityAndOverride.class);
			assertThat(results, hasSize(NUMBER_OF_METHODS_IN_CHILD + NUMBER_OF_METHODS_IN_BASE + NUMBER_OF_METHODS_IN_OBJECT_CLASS
					+ NUMBER_OF_METHODS_IN_OVERLOAD_CLASS));
		}

	}

	@RunWith(JUnit4.class)
	public static class FindAllFieldsInClassTest {

		private static final int NUMBER_IN_BASE_CLASS = ClassWithOneOfEachVisibilityFields.class.getDeclaredFields().length;
		private static final int NUMBER_IN_CHILD_CLASS = ChildOfClassWithOneOfEachVisibilityFields.class.getDeclaredFields().length;


		@Test
		public void findsAllFieldsOfAnyVisibility() throws Exception {
			final List<Field> fields = ReflectionUtils.findAllFieldsInClass(ClassWithOneOfEachVisibilityFields.class);
			assertThat(fields.size(), is(NUMBER_IN_BASE_CLASS));
		}


		@Test
		public void findsAllFieldsOfAnyVisibilityInSuperClass() {
			final List<Field> fields = ReflectionUtils.findAllFieldsInClass(ChildOfClassWithOneOfEachVisibilityFields.class);
			assertThat(fields.size(), is(NUMBER_IN_BASE_CLASS + NUMBER_IN_CHILD_CLASS));
		}
	}

	@RunWith(JUnit4.class)
	public static class FindFieldInClassTest {

		@Test
		public void findsFieldOfPrivateVisibility() throws Exception {
			final String fieldName = "privateString";
			final Field field = ReflectionUtils.findFieldInClass(ClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}


		@Test
		public void findsFieldOfPackagePrivateVisibility() throws Exception {
			final String fieldName = "packagePrivateString";
			final Field field = ReflectionUtils.findFieldInClass(ClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}


		@Test
		public void findsFieldOfProtectedVisibility() throws Exception {
			final String fieldName = "protectedString";
			final Field field = ReflectionUtils.findFieldInClass(ClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}


		@Test
		public void findsFieldOfPublicVisibility() throws Exception {
			final String fieldName = "publicString";
			final Field field = ReflectionUtils.findFieldInClass(ClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}


		@Test
		public void findsFieldOfPrivateVisibilityInSuperClass() {
			final String fieldName = "privateString";
			final Field field = ReflectionUtils.findFieldInClass(ChildOfClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}


		@Test(expected = ReflectionException.class)
		public void throwsExceptionForUnfoundField() {
			ReflectionUtils.findFieldInClass(ChildOfClassWithOneOfEachVisibilityFields.class, "cantFindMe");
		}


		@Test
		public void similarMethodReturnsNullForUnfoundField() {
			final Field field = ReflectionUtils.findFieldInClassWithNullReturns(ChildOfClassWithOneOfEachVisibilityFields.class, "cantFindMe");
			assertThat(field, nullValue());
		}


		@Test
		public void similarMethodFindsFieldOfPrivateVisibilityInSuperClass() {
			final String fieldName = "privateString";
			final Field field = ReflectionUtils.findFieldInClassWithNullReturns(ChildOfClassWithOneOfEachVisibilityFields.class, fieldName);
			assertThat(field, notNullValue());
			assertThat(field.getName(), equalTo(fieldName));
		}
	}

	@RunWith(JUnit4.class)
	public static class FieldHasAnnotationTest {

		private Field fieldWithAnnotation;

		private Field fieldWithoutAnnotation;


		@Before
		public void setup() throws Exception, SecurityException {
			this.fieldWithAnnotation = ClassWithFieldHavingAnnotations.class.getField("fieldWithAnnotation");
			this.fieldWithoutAnnotation = ClassWithFieldHavingAnnotations.class.getField("fieldWithoutAnnotation");
		}


		@Test
		public void findsAnnotationIfExists() throws Exception {
			final boolean result = ReflectionUtils.fieldHasAnnotation(this.fieldWithAnnotation, RuntimeRetention.class);
			assertThat(result, is(true));
		}


		@Test
		public void doesNotFindAnnotationIfNotExists() {
			final boolean result = ReflectionUtils.fieldHasAnnotation(this.fieldWithoutAnnotation, RuntimeRetention.class);
			assertThat(result, is(false));
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForSourceRetention() {
			ReflectionUtils.fieldHasAnnotation(this.fieldWithAnnotation, SourceRetention.class);
		}


		@Test(expected = RuntimeException.class)
		public void throwsExceptionForClassRetention() {
			ReflectionUtils.fieldHasAnnotation(this.fieldWithAnnotation, ClassRetention.class);
		}
	}
}
