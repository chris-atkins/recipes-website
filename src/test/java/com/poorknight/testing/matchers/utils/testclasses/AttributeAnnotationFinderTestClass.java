package com.poorknight.testing.matchers.utils.testclasses;

import java.util.Arrays;
import java.util.List;

import com.poorknight.testing.matchers.TestAnnotations.RuntimeRetention;


public class AttributeAnnotationFinderTestClass {

	@RuntimeRetention
	public String publicAttribute;

	@RuntimeRetention
	protected String protectedAttribute;

	@RuntimeRetention
	String packagePrivateAttribute;

	@RuntimeRetention
	private String privateAttribute;

	public String unAnnotatedPublicAttribute;
	protected String unAnnotatedProtectedAttribute;
	String unAnnotatedPackagePrivateAttribute;
	private String unAnnotatedPrivateAttribute;


	public List<String> getRidOfFindbugsErrors(final String value) {
		this.unAnnotatedPackagePrivateAttribute = value;
		this.unAnnotatedPrivateAttribute = value;
		this.unAnnotatedProtectedAttribute = value;
		this.unAnnotatedPublicAttribute = value;
		return Arrays.asList(this.unAnnotatedPackagePrivateAttribute, this.unAnnotatedPrivateAttribute, this.unAnnotatedProtectedAttribute,
				this.unAnnotatedPublicAttribute);
	}
}
