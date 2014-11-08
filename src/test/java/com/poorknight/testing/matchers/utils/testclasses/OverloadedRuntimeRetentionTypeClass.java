package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;


public class OverloadedRuntimeRetentionTypeClass {

	@RuntimeRetention
	public void overloaded() {
		// empty on purpose
	}


	@SuppressWarnings("unused")
	@RuntimeRetention
	public void overloaded(final String s) {
		// empty on purpose
	}
}