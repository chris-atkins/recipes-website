package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;


public class ClassWithProtectedPKSetter {

	@Id
	private Long pk;


	public Long getPk() {
		return this.pk;
	}


	protected void setPk(final Long pk) {
		this.pk = pk;
	}

}
