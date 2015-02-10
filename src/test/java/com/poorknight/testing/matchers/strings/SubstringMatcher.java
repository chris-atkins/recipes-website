package com.poorknight.testing.matchers.strings;

import org.apache.commons.lang.Validate;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;


public class SubstringMatcher extends TypeSafeDiagnosingMatcher<String> {

	private String substring;


	private SubstringMatcher(String substring) {
		Validate.notEmpty(substring, "The substring must be non-null and non-empty.");
		this.substring = substring;
	}


	@Override
	public void describeTo(Description description) {
		description.appendText("has exactly one occurrence of the substring '").appendText(this.substring).appendText("'");
	}


	@Override
	protected boolean matchesSafely(String stringToTest, Description mismatchDescription) {

		if (noSubstringExists(stringToTest)) {
			appendNoSubstringExistsMessage(mismatchDescription);
			return false;
		}

		if (moreThanOneSubstringExists(stringToTest)) {
			appendMoreThanOneSubstringExistsMessage(mismatchDescription);
			return false;
		}

		return true;
	}


	private boolean noSubstringExists(String stringToTest) {
		return stringToTest.indexOf(substring) == -1;
	}


	private boolean moreThanOneSubstringExists(String stringToTest) {
		return stringToTest.indexOf(substring) != stringToTest.lastIndexOf(substring);
	}


	private void appendNoSubstringExistsMessage(Description mismatchDescription) {
		mismatchDescription.appendText("'").appendText(substring).appendText("' does not exist in the string under test.");
	}


	private void appendMoreThanOneSubstringExistsMessage(Description mismatchDescription) {
		mismatchDescription.appendText("'").appendText(substring).appendText("' occurs more than one time in the string under test.");
	}


	@Factory
	public static SubstringMatcher hasExactlyOneOccurrenceOfSubstring(String substring) {
		return new SubstringMatcher(substring);
	}

}
