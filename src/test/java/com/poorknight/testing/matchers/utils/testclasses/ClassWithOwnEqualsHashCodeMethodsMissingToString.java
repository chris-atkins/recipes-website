package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOwnEqualsHashCodeMethodsMissingToString {

	@Override
	public boolean equals(final Object obj) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public int hashCode() {
		throw new RuntimeException("Not implemented");
	}

}
