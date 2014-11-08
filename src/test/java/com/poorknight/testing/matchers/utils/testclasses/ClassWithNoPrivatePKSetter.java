package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;


public class ClassWithNoPrivatePKSetter {

	@Id
	private String pk;
}
