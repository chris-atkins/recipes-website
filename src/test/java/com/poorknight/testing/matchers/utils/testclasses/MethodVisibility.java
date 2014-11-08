package com.poorknight.testing.matchers.utils.testclasses;

public class MethodVisibility {

	@SuppressWarnings("unused")
	private void privateMethod() {
		// empty on purpose
	}


	void packagePrivateMethod() {
		// empty on purpose
	}


	protected void protectedMethod() {
		// empty on purpose
	}


	public void publicMethod() {
		// empty on purpose
	}
}
