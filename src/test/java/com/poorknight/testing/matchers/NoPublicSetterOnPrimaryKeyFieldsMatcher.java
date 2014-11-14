package com.poorknight.testing.matchers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


public class NoPublicSetterOnPrimaryKeyFieldsMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	private static final boolean RESULT_WHEN_NOT_FOUND = false;


	private NoPublicSetterOnPrimaryKeyFieldsMatcher() {
		// no public constructor
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("does not have a public setter.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {
		final List<Field> primaryKeyFields = ReflectionUtils.findAllFieldsWithAnnotation(classToInspect, Id.class);

		for (final Field field : primaryKeyFields) {
			if (hasPublicSetter(field)) {
				appendHasPublicSetterOnPrimaryKeyFieldMessage(field, mismatchDescription);
				return false;
			}
		}
		return true;
	}


	protected boolean hasPublicSetter(final Field field) {

		final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(field.getDeclaringClass());

		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {

			if (itDescribesTheFieldWeAreLookingFor(propertyDescriptor, field)) {
				return hasPublicSetter(propertyDescriptor);
			}
		}
		return RESULT_WHEN_NOT_FOUND;
	}


	private boolean itDescribesTheFieldWeAreLookingFor(final PropertyDescriptor propertyDescriptor, final Field field) {
		return propertyDescriptor.getName().equals(field.getName());
	}


	private boolean hasPublicSetter(final PropertyDescriptor propertyDescriptor) {
		final Method writeMethod = propertyDescriptor.getWriteMethod();
		return publicSetterExists(writeMethod);
	}


	private boolean publicSetterExists(final Method writeMethod) {
		return writeMethod != null;
	}


	private void appendHasPublicSetterOnPrimaryKeyFieldMessage(final Field field, final Description mismatchDescription) {
		mismatchDescription.appendText("a public setter was found for a primary key field: " + field.getName());
	}


	@Factory
	public static NoPublicSetterOnPrimaryKeyFieldsMatcher doesNotHavePublicSetterForPrimaryKey() {
		return new NoPublicSetterOnPrimaryKeyFieldsMatcher();
	}

}
