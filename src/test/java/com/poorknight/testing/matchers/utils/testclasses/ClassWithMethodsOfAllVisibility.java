package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithMethodsOfAllVisibility {

	public void publicMethod() {
		// empty on purpose
	}


	void packagePrivateMethod() {
		// empty on purpose
	}


	protected void protectedMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	private void privateMethod() {
		// empty on purpose
	}

}
