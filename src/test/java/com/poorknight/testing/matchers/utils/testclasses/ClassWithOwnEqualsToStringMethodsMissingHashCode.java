package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOwnEqualsToStringMethodsMissingHashCode {

	@Override
	public boolean equals(final Object obj) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public String toString() {
		throw new RuntimeException("Not implemented");
	}

}
