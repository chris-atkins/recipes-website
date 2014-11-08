package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOverloadedMethodsOfDifferentVisibility extends OverloadedMethodSuperClass {

	@SuppressWarnings("unused")
	private void overloadedMethod() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	void overloadedMethod(final int i) {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	protected void overloadedMethod(final String s) {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	public void overloadedMethod(final double i) {
		// empty on purpose
	}

}
