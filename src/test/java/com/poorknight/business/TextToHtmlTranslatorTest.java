package com.poorknight.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class TextToHtmlTranslatorTest {

	@Inject
	private TextToHtmlTranslator textToHtmlTranslator;

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Deployment
	public static JavaArchive createDeployment() {

		// Maybe need to have a beans.xml that enables the interceptor
		return ShrinkWrap.create(JavaArchive.class).addClass(TextToHtmlTranslator.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}


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


	@Test
	public void testValidateForNonNullInput() {
		this.thrown.expect(ConstraintViolationException.class);
		this.textToHtmlTranslator.translate(null);
	}
}
