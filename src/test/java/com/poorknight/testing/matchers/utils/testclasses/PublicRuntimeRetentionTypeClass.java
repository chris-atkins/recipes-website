package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.utils.testclasses.TestAnnotations.RuntimeRetention;


public class PublicRuntimeRetentionTypeClass {

	public void method1() {
		// empty on purpose
	}


	@RuntimeRetention
	public void method2() {
		// empty on purpose
	}
}