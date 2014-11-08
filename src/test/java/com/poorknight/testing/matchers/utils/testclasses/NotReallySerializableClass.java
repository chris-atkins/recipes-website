package com.poorknight.testing.matchers.utils.testclasses;

import java.io.Serializable;

import lombok.Data;


@Data
public class NotReallySerializableClass implements Serializable {

	private static final long serialVersionUID = -5428288199705557575L;

	private final Object object;


	public NotReallySerializableClass() {
		this.object = new Object();
	}
}
