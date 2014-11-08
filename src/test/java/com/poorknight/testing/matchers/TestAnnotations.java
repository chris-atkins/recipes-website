package com.poorknight.testing.matchers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class TestAnnotations {

	@Retention(RetentionPolicy.SOURCE)
	public @interface SourceRetention {
		// empty on purpose
	}

	@Retention(RetentionPolicy.CLASS)
	public @interface ClassRetention {
		// empty on purpose
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface RuntimeRetention {
		// empty on purpose
	}

	public @interface NoRetentionSpecified {
		// empty on purpose
	}
}
