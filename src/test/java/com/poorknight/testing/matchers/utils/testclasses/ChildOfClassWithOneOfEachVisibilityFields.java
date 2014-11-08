package com.poorknight.testing.matchers.utils.testclasses;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ChildOfClassWithOneOfEachVisibilityFields extends ClassWithOneOfEachVisibilityFields {

	private String parentString;
}
