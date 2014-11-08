package com.poorknight.testing.matchers.utils.testclasses;

import lombok.Data;

@Data
public class ClassWithOneOfEachVisibilityFields {

	private String privateString;
	String packagePrivateString;
	protected String protectedString;
	public String publicString;
}
