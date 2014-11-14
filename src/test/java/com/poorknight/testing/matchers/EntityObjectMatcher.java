package com.poorknight.testing.matchers;

import java.io.Serializable;

import javax.persistence.Entity;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.poorknight.utils.ReflectionUtils;


/**
 * Responsible for checking that an @Entity object conforms to all the necessary Java EE standards, as well as project
 * specific standards for entity objects.
 */
public class EntityObjectMatcher extends TypeSafeDiagnosingMatcher<Class<?>> {

	// setting null (and initializing in constructor) stops eclipse adding 'final', which breaks mocking
	private EntityPrimaryKeyMatcher primaryKeyVerifier = null;
	private NoArgPublicOrProtectedConstructorMatcher noArgPublicOrProtectedConstructorMatcher = null;
	private NoFinalMethodsMatcher noFinalMethodsMatcher = null;
	private NoFinalAttributesNotMarkedAsTransientMatcher noFinalAttributesMatcher = null;
	private NoPublicSetterOnPrimaryKeyFieldsMatcher noPublicSetterMatcher = null;
	private EqualsHashCodeToStringClassImplementationMatcher equalsHashCodeToStringMatcher = null;
	private NoNonFinalFieldsArePrimitivesMatcher noPrimitiveFieldsMatcher = null;
	private AuditColumnsMatcher auditColumnsMatcher = null;


	private EntityObjectMatcher() {
		this.primaryKeyVerifier = EntityPrimaryKeyMatcher.hasValidPrimaryKey();
		this.noArgPublicOrProtectedConstructorMatcher = NoArgPublicOrProtectedConstructorMatcher.hasNoArgConstructorThatIsPublicOrProtected();
		this.noFinalMethodsMatcher = NoFinalMethodsMatcher.hasNoFinalMethods();
		this.noFinalAttributesMatcher = NoFinalAttributesNotMarkedAsTransientMatcher.hasNoFinalNonJPATransientAttributes();
		this.noPublicSetterMatcher = NoPublicSetterOnPrimaryKeyFieldsMatcher.doesNotHavePublicSetterForPrimaryKey();
		this.equalsHashCodeToStringMatcher = EqualsHashCodeToStringClassImplementationMatcher.implementsOwnEqualsHashCodeToStringMethods();
		this.noPrimitiveFieldsMatcher = NoNonFinalFieldsArePrimitivesMatcher.hasNonFinalFieldsThatArePrimitives();
		this.auditColumnsMatcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
	}


	@Factory
	public static EntityObjectMatcher meetsProjectEntityObjectStandards() {
		return new EntityObjectMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("meets Java EE and project standards for an @Entity object.");
	}


	@Override
	protected boolean matchesSafely(final Class<?> classToInspect, final Description mismatchDescription) {

		if (doesNotHaveEntityAnnotation(classToInspect)) {
			appendNoEntityAnnotationMessage(mismatchDescription);
			return false;
		}

		// Primary Key attribute is of type Long and has @Id annotation, and is auto-generated with an Identity strategy
		if (failsPrimaryKeyValidationMatcher(classToInspect, mismatchDescription)) {
			// the primary key matcher will add its own descriptions - no extra ones are needed here
			return false;
		}

		// no arg constructor that is public or protected
		if (failsNoArgPublicOrProtectedConstructorMatcher(classToInspect, mismatchDescription)) {
			// matcher will add its own descriptions
			return false;
		}

		// must not be final class
		if (isAFinalClass(classToInspect)) {
			appendFinalClassMessage(mismatchDescription);
			return false;
		}

		// // no methods can be final
		if (hasAnyFinalMethods(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		// no mapped attributes can be final
		if (hasAnyFinalAttributes(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		// must be serializable (only checks whether annotation exists - not whether truly serializable)
		if (doesNotImplementSerializable(classToInspect)) {
			appendDoesNotImplementSerializableMessage(classToInspect, mismatchDescription);
			return false;
		}

		// /////////////////////////////////////////////////////////

		// Primary key does not have public setter
		if (primaryKeyHasPublicSetter(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		// equals/hashCode/toString methods are implemented
		if (equalsHashCodeToStringAreNotImplemented(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		// ideally checks for no business logic in getters/setters - might not be really doable, but could check calling
		// getter returns same object as passed to setter YAGNI?? certainly not implementing now

		// Does not use primitives for any attributes (avoids any possible confusion with non-nullable fields)
		if (anyFieldsArePrimitiveTypes(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		// HAS AUDIT FIELDS - SEE http://blog.octo.com/en/audit-with-jpa-creation-and-update-date/ for other options
		if (auditColumnsAreNotImplementedCorrectly(classToInspect, mismatchDescription)) {
			// matcher will add its own description
			return false;
		}

		return true;
	}


	private boolean doesNotHaveEntityAnnotation(final Class<?> classBeingInspected) {
		return !(ReflectionUtils.classHasAnnotation(classBeingInspected, Entity.class));
	}


	private void appendNoEntityAnnotationMessage(final Description mismatchDiscription) {
		mismatchDiscription.appendText("the class does not have the @Entity annotation.");
	}


	private void appendFinalClassMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("the class is a final class.");
	}


	private boolean failsPrimaryKeyValidationMatcher(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.primaryKeyVerifier.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean failsNoArgPublicOrProtectedConstructorMatcher(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.noArgPublicOrProtectedConstructorMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean isAFinalClass(final Class<?> classToInspect) {
		return ReflectionUtils.classIsFinal(classToInspect);
	}


	private boolean hasAnyFinalMethods(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.noFinalMethodsMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean hasAnyFinalAttributes(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.noFinalAttributesMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean doesNotImplementSerializable(final Class<?> classToInspect) {
		return !(Serializable.class.isAssignableFrom(classToInspect));
	}


	private void appendDoesNotImplementSerializableMessage(final Class<?> classToInspect, final Description mismatchDescription) {
		mismatchDescription.appendText("the class ").appendText(classToInspect.getSimpleName()).appendText(" does not implement Serializable.");
	}


	private boolean primaryKeyHasPublicSetter(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.noPublicSetterMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean equalsHashCodeToStringAreNotImplemented(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.equalsHashCodeToStringMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean anyFieldsArePrimitiveTypes(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.noPrimitiveFieldsMatcher.matchesSafely(classToInspect, mismatchDescription));
	}


	private boolean auditColumnsAreNotImplementedCorrectly(final Class<?> classToInspect, final Description mismatchDescription) {
		return !(this.auditColumnsMatcher.matchesSafely(classToInspect, mismatchDescription));
	}
}
