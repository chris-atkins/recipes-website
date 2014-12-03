package com.poorknight.testing.matchers.utils.testclasses;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;


public class MethodAnnotationTestClass {

	@Transactional
	public void publicMethod() {
		// empty on purpose
	}


	@Transactional
	void packageMethod() {
		// empty on purpose
	}


	@Transactional
	protected void protectedMethod() {
		// empty on purpose
	}


	@Transactional
	private void privateMethod() {
		// empty on purpose
	}


	@PostConstruct
	private void overloadedMethod() {
		// empty on purpose
	}


	@PostConstruct
	private void overloadedMethod(@SuppressWarnings("unused") final String s) {
		// empty on purpose
	}
}
