package com.poorknight.testing.matchers.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


public class BeanValidationMatcher extends TypeSafeDiagnosingMatcher<Object> {

	private final int expectedNumberOfErrors;

	private final Validator validator;
	private int numberOfViolations;


	private BeanValidationMatcher(final int expectedNumberOfErrors) {
		super();
		this.expectedNumberOfErrors = expectedNumberOfErrors;
		this.validator = createValidator();
	}


	@Factory
	public static BeanValidationMatcher passesValidation() {
		return new BeanValidationMatcher(0);
	}


	@Factory
	public static BeanValidationMatcher failsValidation() {
		return new BeanValidationMatcher(1);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("returns the expected number of validation errors (").appendText(Integer.toString(this.expectedNumberOfErrors))
				.appendText(") when BeanValidation is performed on the object.");
	}


	@Override
	protected boolean matchesSafely(final Object objectToInspect, final Description mismatchDescription) {

		checkObjectUsesValidationThrowingException(objectToInspect);

		if (wrongNumberOfValidationErrorsExist(objectToInspect)) {
			appendWrongNumberOfViolationsFoundMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private void checkObjectUsesValidationThrowingException(final Object objectToInspect) {
		if (noValidationExistsInObject(objectToInspect)) {
			throw new IllegalArgumentException(
					"Expecting an object that has some kind of bean validation existing on it (either class or field level), but no validation exists.  Object type: "
							+ objectToInspect.getClass().getSimpleName());
		}
	}


	private boolean noValidationExistsInObject(final Object objectToInspect) {
		final BeanDescriptor constraints = this.validator.getConstraintsForClass(objectToInspect.getClass());
		return !(constraints.isBeanConstrained());
	}


	private boolean wrongNumberOfValidationErrorsExist(final Object objectToInspect) {
		final Set<ConstraintViolation<Object>> violations = performValidation(objectToInspect);
		this.numberOfViolations = violations.size();
		return this.numberOfViolations != this.expectedNumberOfErrors;
	}


	private Set<ConstraintViolation<Object>> performValidation(final Object objectToInspect) {
		removeELNotationsFromValidationErrorMessages(objectToInspect); // junit validation error messages don't work with embedded EL
		return this.validator.validate(objectToInspect);
	}


	private void removeELNotationsFromValidationErrorMessages(final Object objectToInspect) {
		final BeanDescriptor constraints = this.validator.getConstraintsForClass(objectToInspect.getClass());
		for (final PropertyDescriptor property : constraints.getConstrainedProperties()) {
			removeELNotationFromSinglePropertyValidations(property);
		}
	}


	private void removeELNotationFromSinglePropertyValidations(final PropertyDescriptor property) {
		final Set<ConstraintDescriptor<?>> constraintDescriptors = property.getConstraintDescriptors();
		for (final ConstraintDescriptor<?> constraint : constraintDescriptors) {
			removeELNotationsFromSingleConstraint(constraint);
		}
	}


	private void removeELNotationsFromSingleConstraint(final ConstraintDescriptor<?> constraint) {
		final Map<String, Object> newAttributes = buildNewConstraintAttributeMap(constraint);
		replaceErrorMessage(newAttributes);
		ReflectionUtils.setFieldInClass(constraint, "attributes", newAttributes);
	}


	private Map<String, Object> buildNewConstraintAttributeMap(final ConstraintDescriptor<?> constraint) {
		final Map<String, Object> attributes = constraint.getAttributes();
		final Map<String, Object> newAttributes = new HashMap<>();
		for (final Entry<String, Object> entry : attributes.entrySet()) {
			newAttributes.put(entry.getKey(), entry.getValue());
		}
		return newAttributes;
	}


	private void replaceErrorMessage(final Map<String, Object> newAttributes) {
		newAttributes.put("message", "temp hacked message - see BeanValidationMatcher.removeELNotationsFromValidations()");
	}


	private Validator createValidator() {
		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		final Validator newValidator = validatorFactory.getValidator();
		validatorFactory.close();
		return newValidator;
	}


	private void appendWrongNumberOfViolationsFoundMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("found ").appendText(Integer.toString(this.numberOfViolations)).appendText(" validation errors.");
	}
}
