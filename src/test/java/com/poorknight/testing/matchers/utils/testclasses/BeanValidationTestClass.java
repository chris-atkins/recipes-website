package com.poorknight.testing.matchers.utils.testclasses;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class BeanValidationTestClass {

	@NotNull
	private final String first;

	@NotNull
	private final Integer second;

	@Min(0)
	private final Integer third;


	public BeanValidationTestClass(final String first, final Integer second) {
		super();
		this.first = first;
		this.second = second;
		this.third = 1; // passes validation
	}


	public BeanValidationTestClass(final Integer third) {
		super();
		this.first = "hi"; // passes validation
		this.second = 1; // passes validation
		this.third = third;
	}

}
