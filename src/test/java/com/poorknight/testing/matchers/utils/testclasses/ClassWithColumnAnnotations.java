package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Column;


public class ClassWithColumnAnnotations {

	@Column(name = "columnName")
	public String fieldWithColumnAndName;

	public String fieldWithNoColumn;

	@Column
	public String fieldWithColumnAndNoName;

	@Column(name = "wrongName")
	public String fieldWithColumnAndWrongName;

	@Column(precision = 55)
	public Long fieldWithColumnAndPrecision;
}
