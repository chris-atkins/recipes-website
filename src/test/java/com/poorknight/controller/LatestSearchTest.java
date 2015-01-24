package com.poorknight.controller;

import static com.poorknight.testing.matchers.CustomMatchers.hasAReadOnlyField;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.CustomMatchers;


@RunWith(JUnit4.class)
public class LatestSearchTest {

	@Test
	public void sessionScoped() throws Exception {
		assertThat(LatestSearch.class, CustomMatchers.isSessionScoped());
	}


	@Test
	public void latestSearchIsReadOnly() throws Exception {
		assertThat(LatestSearch.class, hasAReadOnlyField("latestSearch"));
	}
}
