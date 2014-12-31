package com.poorknight.testing.matchers.utils.testclasses;

import org.hibernate.validator.constraints.NotEmpty;


public class BeanValidationTestClassWithNotEmpty {

	@NotEmpty
	private final String s;


	public BeanValidationTestClassWithNotEmpty(final String s) {
		this.s = s;
	}
}
