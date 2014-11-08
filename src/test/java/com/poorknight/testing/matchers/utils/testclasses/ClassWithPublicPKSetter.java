package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;


public class ClassWithPublicPKSetter {

	@Id
	private Long pk;


	public Long getPk() {
		return this.pk;
	}


	public void setPk(final Long pk) {
		this.pk = pk;
	}

}
