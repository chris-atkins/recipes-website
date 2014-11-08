package com.poorknight.testing.matchers.utils.testclasses;

public class ParentOfAttributeFinderTestObject {

	private final String findMe = "found";
	private final String nullField = null;


	public String getRidOfFindbugsError() {
		return this.findMe + this.nullField;
	}
}
