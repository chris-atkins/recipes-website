package com.poorknight.testing.matchers.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


/**
 * Responsible for checking whether an object is Serializable. It only checks the instance of the object that was passed, so the test using this
 * matcher must ensure that the object is representative of what will be found at runtime. One concern is that if the object uses CDI, the test should
 * be run inside of a CDI container.
 */
public class IsSerializableMatcher extends TypeSafeDiagnosingMatcher<Object> {

	private IsSerializableMatcher() {
		super();
	}


	@Factory
	public static IsSerializableMatcher isSerializable() {
		return new IsSerializableMatcher();
	}


	@Override
	public void describeTo(final Description description) {
		description.appendText("is Serializable.");
	}


	@Override
	protected boolean matchesSafely(final Object objectToInspect, final Description mismatchDescription) {

		if (objectCannotBeSerialized(objectToInspect)) {
			mismatchDescription.appendText("the object cannot be serialized.");
			return false;
		}

		return true;
	}


	private boolean objectCannotBeSerialized(final Object objectToInspect) {
		return !(objectCanBeSerialized(objectToInspect));
	}


	private boolean objectCanBeSerialized(final Object objectToInspect) {
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos);) {

			oos.writeObject(objectToInspect);
			return true;

		} catch (final IOException e) {
			return false;
		}
	}

}
