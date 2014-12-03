package com.poorknight.testing.matchers.methods;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class MethodModifiersMatcher extends TypeSafeDiagnosingMatcher<Method> {

	private static final Modifiers[] EMPTY = new Modifiers[0];

	private final Modifiers[] havingModifiers;
	private final Modifiers[] withoutModifiers;

	public static enum Modifiers {
		ABSTRACT, FINAL, NATIVE, PRIVATE, PACKAGE, PROTECTED, PUBLIC, STATIC, STRICT, SYNCHRONIZED
	}


	private MethodModifiersMatcher(final Modifiers[] havingModifiers, final Modifiers[] withoutModifiers) {
		super();
		this.havingModifiers = havingModifiers;
		this.withoutModifiers = withoutModifiers;
	}


	@Factory
	public static MethodModifiersMatcher notStatic() {
		return new MethodModifiersMatcher(EMPTY, new Modifiers[] { Modifiers.STATIC });
	}


	@Factory
	public static MethodModifiersMatcher withoutModifiers(final Modifiers... modifiers) {
		return new MethodModifiersMatcher(EMPTY, modifiers);
	}


	@Factory
	public static MethodModifiersMatcher havingModifiers(final Modifiers... modifiers) {
		return new MethodModifiersMatcher(modifiers, EMPTY);
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("Checks method modifiers to ensure it does or does not have the expected modifiers:\n\tExpected: ")
				.appendText(Arrays.toString(this.havingModifiers)).appendText("\n\tNot Having: ").appendText(Arrays.toString(this.withoutModifiers));
	}


	@Override
	protected boolean matchesSafely(final Method method, final Description mismatchDescription) {
		if (doesNotHaveExpectedModifiers(method)) {
			appendNotHavingExpectedModifiersMessage(mismatchDescription);
			return false;
		}

		if (doesHaveUnexpectedModifiers(method)) {
			appendDoesHaveUnexpectedModifiersMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean doesNotHaveExpectedModifiers(final Method method) {
		for (final Modifiers modifier : this.havingModifiers) {
			if (!hasModifier(method, modifier)) {
				return true;
			}
		}
		return false;
	}


	private boolean doesHaveUnexpectedModifiers(final Method method) {
		for (final Modifiers modifier : this.withoutModifiers) {
			if (hasModifier(method, modifier)) {
				return true;
			}
		}
		return false;
	}


	private boolean hasModifier(final Method method, final Modifiers modifier) {
		final int actualMethodModifiers = method.getModifiers();

		switch (modifier) {
		case ABSTRACT:
			return Modifier.isAbstract(actualMethodModifiers);

		case FINAL:
			return Modifier.isFinal(actualMethodModifiers);

		case NATIVE:
			return Modifier.isNative(actualMethodModifiers);

		case PRIVATE:
			return Modifier.isPrivate(actualMethodModifiers);

		case PACKAGE:
			return isPackage(actualMethodModifiers);

		case PROTECTED:
			return Modifier.isProtected(actualMethodModifiers);

		case PUBLIC:
			return Modifier.isPublic(actualMethodModifiers);

		case STATIC:
			return Modifier.isStatic(actualMethodModifiers);

		case STRICT:
			return Modifier.isStrict(actualMethodModifiers);

		case SYNCHRONIZED:
			return Modifier.isSynchronized(actualMethodModifiers);

		default:
			throw new RuntimeException("In MethodModifiersMatcher.hasModifier() - Could not find the modifier: " + modifier.name());
		}
	}


	private boolean isPackage(final int actualMethodModifiers) {
		return !(Modifier.isPrivate(actualMethodModifiers) || Modifier.isProtected(actualMethodModifiers) || Modifier.isPublic(actualMethodModifiers));
	}


	private void appendNotHavingExpectedModifiersMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("did not have all of the expected modifiers.");
	}


	private void appendDoesHaveUnexpectedModifiersMessage(final Description mismatchDescription) {
		mismatchDescription.appendText("had modifiers that should not be there.");
	}
}
