package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;


public class PublicAndProtectedRuntimeRetentionTypeClass {

	public void method1() {
		// empty on purpose
	}


	@RuntimeRetention
	protected void method2() {
		// empty on purpose
	}


	@RuntimeRetention
	public void method3() {
		// empty on purpose
	}
}