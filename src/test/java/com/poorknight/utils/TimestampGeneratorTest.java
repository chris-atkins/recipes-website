package com.poorknight.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TimestampGeneratorTest {

	private final TimestampGenerator generator = new TimestampGenerator();


	@Test
	public void returnsPrettyAccurateCurrentTime() throws Exception {
		final long currentTimeInMillis = System.currentTimeMillis();
		long generatedTime = this.generator.currentTimestamp().getTime();
		assertThat((double) generatedTime, closeTo(currentTimeInMillis, 2));
	}
}
