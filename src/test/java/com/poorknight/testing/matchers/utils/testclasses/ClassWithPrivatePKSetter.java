package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;


public class ClassWithPrivatePKSetter {

	@Id
	private Long pk;


	public Long getPk() {
		return this.pk;
	}


	@SuppressWarnings("unused")
	private void setPk(final Long pk) {
		this.pk = pk;
	}

}
