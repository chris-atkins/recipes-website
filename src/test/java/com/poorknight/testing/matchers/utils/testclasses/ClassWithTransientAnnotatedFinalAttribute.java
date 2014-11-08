package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Transient;


public class ClassWithTransientAnnotatedFinalAttribute {

	@Transient
	protected final int testField = 1;
}
