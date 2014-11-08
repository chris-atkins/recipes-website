package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.TestAnnotations.ClassRetention;
import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;
import com.poorknight.testing.matchers.TestAnnotations.SourceRetention;


/**********************************
 *
 * Helper classes used for testing.
 *
 **********************************/

public class SingleAnnotationOfEachRetentionTypeClass {

	public void method1() {
		// empty on purpose
	}


	@SourceRetention
	public void method2() {
		// empty on purpose
	}


	@RuntimeRetention
	public void method3() {
		// empty on purpose
	}


	@ClassRetention
	public void method4() {
		// empty on purpose
	}
}