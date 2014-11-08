package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOwnHashCodeToStringMethodsMissingEquals {

	@Override
	public int hashCode() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public String toString() {
		throw new RuntimeException("Not implemented");
	}

}
