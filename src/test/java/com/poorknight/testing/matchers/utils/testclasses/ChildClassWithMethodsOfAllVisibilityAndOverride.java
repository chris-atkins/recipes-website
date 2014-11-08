package com.poorknight.testing.matchers.utils.testclasses;

public class ChildClassWithMethodsOfAllVisibilityAndOverride extends ChildClassWithMethodsOfAllVisibility {

	@Override
	public void protectedMethod() {
		throw new RuntimeException("Not implemented");
	}
}
