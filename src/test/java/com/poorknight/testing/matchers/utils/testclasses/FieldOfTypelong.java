package com.poorknight.testing.matchers.utils.testclasses;

import javax.persistence.Id;

import lombok.Data;


@Data
public class FieldOfTypelong {

	@Id
	private long testField;
}
