package com.poorknight.testing.matchers.utils.testclasses;

public class ClassWithOneOfEachVisibilityAccessorFields {

	private String publicString;
	private String protectedString;
	private String packagePrivateString;
	private String privateString;
	private String publicGetterProtectedSetter;


	public String getPublicString() {
		return this.publicString;
	}


	public void setPublicString(final String publicString) {
		this.publicString = publicString;
	}


	protected String getProtectedString() {
		return this.protectedString;
	}


	protected void setProtectedString(final String protectedString) {
		this.protectedString = protectedString;
	}


	String getPackagePrivateString() {
		return this.packagePrivateString;
	}


	void setPackagePrivateString(final String packagePrivateString) {
		this.packagePrivateString = packagePrivateString;
	}


	@SuppressWarnings("unused")
	private String getPrivateString() {
		return this.privateString;
	}


	@SuppressWarnings("unused")
	private void setPrivateString(final String privateString) {
		this.privateString = privateString;
	}


	public String getPublicGetterProtectedSetter() {
		return this.publicGetterProtectedSetter;
	}


	protected void setPublicGetterProtectedSetter(final String publicGetterProtectedSetter) {
		this.publicGetterProtectedSetter = publicGetterProtectedSetter;
	}
}
