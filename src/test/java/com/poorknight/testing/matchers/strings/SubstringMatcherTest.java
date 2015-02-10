package com.poorknight.testing.matchers.strings;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class SubstringMatcherTest {

	private final static String ANY_STRING = RandomStringUtils.random(50);


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(SubstringMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithExactlyOneMatchingSubstring() throws Exception {
		String substring = RandomStringUtils.random(10);
		String testString = buildTestStringWithSubstringOccurring(substring, 1);

		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(substring);

		assertThat(matcher.matches(testString), is(true));
	}


	@Test
	public void failsWithNoMatchingSubstring() throws Exception {
		String substring = RandomStringUtils.random(10);
		String testString = buildTestStringWithSubstringOccurring(substring, 0);

		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(substring);

		assertThat(matcher.matches(testString), is(false));
	}


	@Test
	public void failsWithMoreThanOneMatchingSubstring() throws Exception {
		String substring = RandomStringUtils.random(10);
		String testString = buildTestStringWithSubstringOccurring(substring, 2);

		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(substring);

		assertThat(matcher.matches(testString), is(false));
	}


	@Test
	public void failsIfStringIsNull() throws Exception {
		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(ANY_STRING);
		assertThat(matcher.matches(null), is(false));
	}


	@Test
	public void failsIfStringIsEmpty() throws Exception {
		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(ANY_STRING);
		assertThat(matcher.matches(""), is(false));
	}


	@Test(expected = IllegalArgumentException.class)
	public void exceptionIfSubstringIsNull() throws Exception {
		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring(null);
		matcher.matches(ANY_STRING);
	}


	@Test(expected = IllegalArgumentException.class)
	public void exceptionIfSubstringIsEmpty() throws Exception {
		SubstringMatcher matcher = SubstringMatcher.hasExactlyOneOccurrenceOfSubstring("");
		matcher.matches(ANY_STRING);
	}


	private String buildTestStringWithSubstringOccurring(String substring, int occurrences) {
		StringBuilder sb = new StringBuilder();
		sb.append(randomString());
		for (int i = 0; i < occurrences; i++) {
			sb.append(substring);
			sb.append(randomString());
		}
		return sb.toString();
	}


	private String randomString() {
		return RandomStringUtils.random(RandomUtils.nextInt(20));
	}
}
