package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.RuntimeRetention;


public class PublicAndPrivateRuntimeRetentionTypeClass {

	public void method1() {
		// empty on purpose
	}


	@RuntimeRetention
	private void method2() {
		// empty on purpose
	}


	@RuntimeRetention
	public void method3() {
		// empty on purpose
	}
}