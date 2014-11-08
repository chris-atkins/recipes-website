package com.poorknight.testing.matchers.utils.testclasses;

import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;

public class ClassWithFieldHavingAnnotations {

	@RuntimeRetention
	public String fieldWithAnnotation;

	public String fieldWithoutAnnotation;
}
