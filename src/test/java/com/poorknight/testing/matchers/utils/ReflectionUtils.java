package com.poorknight.testing.matchers.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.poorknight.exceptions.ReflectionException;


public class ReflectionUtils {

	public static boolean hasAnyPublicMethodsWithAnnotation(final Class<?> classToInspect, final Class<? extends Annotation> annotationClass) {
		validateAnnotationHasRuntimeRetentionThrowingExceptionOnFail(annotationClass);

		final Method[] methods = classToInspect.getMethods();
		for (final Method method : methods) {
			if (methodHasAnnotation(method, annotationClass)) {
				return true;
			}
		}
		return false;
	}


	private static boolean methodHasAnnotation(final Method method, final Class<? extends Annotation> annotationClass) {
		return method.getAnnotation(annotationClass) != null;
	}


	public static boolean hasAnyPublicConstructor(final Class<?> classToCheck) {
		return (classToCheck.getConstructors().length > 0);
	}


	public static Collection<Method> findPublicMethodsInClassWithAnnotation(final Class<?> classToInspect,
			final Class<? extends Annotation> annotationToCheckFor) {
		validateAnnotationHasRuntimeRetentionThrowingExceptionOnFail(annotationToCheckFor);
		final Collection<Method> methodsWithAnnotation = new HashSet<Method>();

		final Collection<Method> methods = Arrays.asList(classToInspect.getMethods());
		for (final Method method : methods) {
			if (methodHasAnnotation(method, annotationToCheckFor)) {
				methodsWithAnnotation.add(method);
			}
		}

		return Collections.unmodifiableCollection(methodsWithAnnotation);
	}


	private static void validateAnnotationHasRuntimeRetentionThrowingExceptionOnFail(final Class<? extends Annotation> annotationToCheckFor) {
		if (annotationDoesNotHaveRuntimeRetention(annotationToCheckFor)) {
			throw new RuntimeException(
					"Cannot pass an annotation class that is does not have @Runtime retention.  This method will not perform accurately under those conditions.");
		}
	}


	private static boolean annotationDoesNotHaveRuntimeRetention(final Class<? extends Annotation> annotationToCheckFor) {
		return !annotationHasRuntimeRetention(annotationToCheckFor);
	}


	public static boolean annotationHasRuntimeRetention(final Class<? extends Annotation> annotationToCheckFor) {
		final Retention retention = annotationToCheckFor.getAnnotation(Retention.class);
		if (retention == null) {  // if null, will default to RetentionPolicy.CLASS per
			return false;		  // http://docs.oracle.com/javase/7/docs/api/java/lang/annotation/RetentionPolicy.html
		}

		if (retention.value().equals(RetentionPolicy.RUNTIME)) {
			return true;
		}

		return false;
	}


	public static Collection<Method> findAllPublicMethodsWithName(final Class<?> classToInspect, final String methodName) {
		final Method[] methods = classToInspect.getMethods();
		final Collection<Method> results = new LinkedList<Method>();
		for (final Method method : methods) {
			if (method.getName().equals(methodName)) {
				results.add(method);
			}
		}
		return Collections.unmodifiableCollection(results);
	}


	public static boolean hasAnyVisibleMethodsInClassWithName(final Class<?> classToInspect, final String methodName) {
		if (findAllPublicMethodsWithName(classToInspect, methodName).size() > 0) {
			return true;
		}

		if (findAllProtectedMethodsWithName(classToInspect, methodName).size() > 0) {
			return true;
		}

		final String packageName = classToInspect.getPackage().getName();
		if (findAllPackagePrivateMethodsWithNameInPackage(classToInspect, methodName, packageName).size() > 0) {
			return true;
		}

		if (findAllPrivateMethodsWithNameWithoutInspectingSuperClass(classToInspect, methodName).size() > 0) {
			return true;
		}

		return false;

	}


	private static Collection<Method> findAllProtectedMethodsWithName(final Class<?> classToInspect, final String methodName) {

		final List<Method> results = new LinkedList<Method>();
		final Method[] methods = classToInspect.getDeclaredMethods();
		for (final Method method : methods) {
			if (method.getName().equals(methodName) && Modifier.isProtected(method.getModifiers())) {
				results.add(method);
			}
		}
		if (classToInspect.equals(Object.class)) {
			return results;
		}
		results.addAll(findAllProtectedMethodsWithName(classToInspect.getSuperclass(), methodName));
		return results;
	}


	private static Collection<Method> findAllPackagePrivateMethodsWithNameInPackage(final Class<?> classToInspect, final String methodName,
			final String packageName) {

		final List<Method> results = new LinkedList<Method>();
		final Method[] methods = classToInspect.getDeclaredMethods();
		for (final Method method : methods) {
			if (method.getName().equals(methodName) && isPackagePrivate(method.getModifiers())
					&& classToInspect.getPackage().getName().equals(packageName)) {
				results.add(method);
			}
		}
		if (classToInspect.equals(Object.class)) {
			return results;
		}
		results.addAll(findAllPackagePrivateMethodsWithNameInPackage(classToInspect.getSuperclass(), methodName, packageName));
		return results;
	}


	private static boolean isPackagePrivate(final int modifiers) {
		return !Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers) && !Modifier.isPublic(modifiers);
	}


	private static Collection<Method> findAllPrivateMethodsWithNameWithoutInspectingSuperClass(final Class<?> classToInspect, final String methodName) {
		final List<Method> results = new LinkedList<Method>();
		for (final Method method : classToInspect.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && Modifier.isPrivate(method.getModifiers())) {
				results.add(method);
			}
		}
		return results;
	}


	public static Collection<Method> findAllVisibleMethodsInClassWithName(final Class<?> classToInspect, final String methodName) {
		final Collection<Method> foundMethods = new LinkedList<Method>();
		foundMethods.addAll(findAllPrivateMethodsWithNameWithoutInspectingSuperClass(classToInspect, methodName));
		foundMethods.addAll(findAllPackagePrivateMethodsWithNameInPackage(classToInspect, methodName, classToInspect.getPackage().getName()));
		foundMethods.addAll(findAllProtectedMethodsWithName(classToInspect, methodName));
		foundMethods.addAll(findAllPublicMethodsWithName(classToInspect, methodName));
		return foundMethods;
	}


	public static boolean classHasAnnotation(final Class<?> classToTest, final Class<? extends Annotation> annotationToCheckFor) {

		if (!annotationHasRuntimeRetention(annotationToCheckFor)) {
			throw new RuntimeException("Inside method 'ReflectionUtils.classHasAnnotation()' - "
					+ "the annotationToCheckFor must have RuntimeRetention.  It does not: " + annotationToCheckFor.getName());
		}

		final Annotation annotation = classToTest.getAnnotation(annotationToCheckFor);
		return annotation != null;

	}


	public static List<Field> findAllFieldsWithAnnotation(final Class<?> classToInspect, final Class<? extends Annotation> annotationClass) {
		return Collections.unmodifiableList(findAllFieldsWithAnnotationRecursiveCall(classToInspect, annotationClass));
	}


	private static List<Field> findAllFieldsWithAnnotationRecursiveCall(final Class<?> classToInspect,
			final Class<? extends Annotation> annotationClass) {

		final List<Field> results = new LinkedList<>();

		if (classToInspect == null || classToInspect.equals(Object.class)) {
			return results;
		}

		final Field[] fields = classToInspect.getDeclaredFields();
		for (final Field field : fields) {
			if (fieldHasAnnotation(field, annotationClass)) {
				results.add(field);
			}
		}

		results.addAll(findAllFieldsWithAnnotationRecursiveCall(classToInspect.getSuperclass(), annotationClass));
		return results;
	}


	public static <T> T getAttributeFromObject(final Object objectToInspect, final String attributeName) {
		try {

			return getAttributeFromObjectThrowingExceptions(objectToInspect, attributeName);

		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Could not find the requested field: " + attributeName + " for this object: " + objectToInspect.toString(), e);
		} catch (final ClassCastException e) {
			throw new RuntimeException(
					"The call to getAttributeFromObject() returns an object that the caller is expecting by performing a cast on the object in the "
							+ "attribute... It seems that the wrong type of class was requested.", e);
		}

	}


	@SuppressWarnings("unchecked")
	public static <T> T getAttributeFromObject(final Object objectToInspect, final Field field) {
		return (T) getAttributeFromObject(objectToInspect, field.getName());
	}


	@SuppressWarnings("unchecked")
	private static <T> T getAttributeFromObjectThrowingExceptions(final Object objectToInspect, final String attributeName)
			throws IllegalArgumentException, IllegalAccessException, ClassCastException {

		final Class<?> classToInspect = objectToInspect.getClass();
		final Field field = findFieldInClass(classToInspect, attributeName);
		field.setAccessible(true);
		final Object fieldContents = field.get(objectToInspect);
		return (T) fieldContents;
	}


	private static Field findFieldInClassRecursively(final Class<?> classToInspect, final String attributeName) {

		if (classToInspect.equals(Object.class)) {
			return null;
		}

		final Field[] fields = classToInspect.getDeclaredFields();
		for (final Field field : fields) {
			if (field.getName().equals(attributeName)) {
				return field;
			}
		}

		return findFieldInClassRecursively(classToInspect.getSuperclass(), attributeName);
	}


	public static boolean classIsFinal(final Class<?> classToInspect) {
		final int modifiers = classToInspect.getModifiers();
		return Modifier.isFinal(modifiers);
	}


	public static Collection<Method> getAllMethodsInClassAndSuperClasses(final Class<?> classToInspect) {
		final Set<Method> results = new HashSet<>();
		addMethodsFromClassToSetRecursively(classToInspect, results);
		return results;
	}


	private static void addMethodsFromClassToSetRecursively(final Class<?> classToInspect, final Set<Method> results) {

		if (!classToInspect.equals(Object.class)) {
			// stop recursion if is object, otherwise call super
			addMethodsFromClassToSetRecursively(classToInspect.getSuperclass(), results);
		}

		results.addAll(Arrays.asList(classToInspect.getDeclaredMethods()));
	}


	public static List<Field> findAllFieldsInClass(final Class<?> classToInspect) {
		return findAllFieldsInClassRecursively(classToInspect);
	}


	private static List<Field> findAllFieldsInClassRecursively(final Class<?> classToInspect) {

		final List<Field> list = new LinkedList<>(Arrays.asList(classToInspect.getDeclaredFields()));

		if (classToInspect.equals(Object.class)) {
			return list;
		}

		list.addAll(findAllFieldsInClassRecursively(classToInspect.getSuperclass()));
		return list;
	}


	public static boolean fieldHasAnnotation(final Field field, final Class<? extends Annotation> annotationToCheckFor) {
		validateAnnotationHasRuntimeRetentionThrowingExceptionOnFail(annotationToCheckFor);
		return field.getAnnotation(annotationToCheckFor) != null;
	}


	public static Field findFieldInClass(final Class<?> classToInspect, final String fieldName) {
		final Field field = findFieldInClassRecursively(classToInspect, fieldName);
		if (field == null) {
			throw new ReflectionException("ReflectionUtils.findFieldInClass() could not find the field '" + fieldName + "' in the class "
					+ classToInspect.getSimpleName());
		}
		return field;
	}


	public static Field findFieldInClassWithNullReturns(final Class<?> classToInspect, final String fieldName) {
		return findFieldInClassRecursively(classToInspect, fieldName);
	}

}
