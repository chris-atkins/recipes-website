package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;


public class ClassWithPackagePrivatePKSetter {

	@Id
	private Long pk;


	public Long getPk() {
		return this.pk;
	}


	void setPk(final Long pk) {
		this.pk = pk;
	}

}
