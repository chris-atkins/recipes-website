package com.poorknight.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class TextToHtmlTranslatorTest {

	private final TextToHtmlTranslator textToHtmlTranslator = new TextToHtmlTranslator();


	@Test
	public void testNewlineReplacedWithBrTags() {
		final String originalText = "hi\nthere";
		final String expectedTranslatedText = "hi<br/>there";

		final String translatedText = this.textToHtmlTranslator.translate(originalText);
		assertThat(translatedText, is(equalTo(expectedTranslatedText)));
	}


	@Test
	public void testMultipleNewlineReplacedWithBrTags() {
		final String originalText = "hi\nthere\nyou";
		final String expectedTranslatedText = "hi<br/>there<br/>you";

		final String translatedText = this.textToHtmlTranslator.translate(originalText);
		assertThat(translatedText, is(equalTo(expectedTranslatedText)));
	}


	@Test
	public void testAlternateNewlineReplacedWithBrTags() {
		final String originalText = "hi\r\nthere\r\nyou";
		final String expectedTranslatedText = "hi<br/>there<br/>you";

		final String translatedText = this.textToHtmlTranslator.translate(originalText);
		assertThat(translatedText, is(equalTo(expectedTranslatedText)));
	}

}
