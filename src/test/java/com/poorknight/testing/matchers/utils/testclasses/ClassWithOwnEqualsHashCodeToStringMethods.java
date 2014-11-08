package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOwnEqualsHashCodeToStringMethods {

	@Override
	public boolean equals(final Object obj) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public int hashCode() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public String toString() {
		throw new RuntimeException("Not implemented");
	}

}
