package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;


public class MulitpleWithInheritedRuntimeRetentionTypeClass extends SingleAnnotationOfEachRetentionTypeClass {

	@RuntimeRetention
	public void method5() {
		// empty on purpose
	}
}