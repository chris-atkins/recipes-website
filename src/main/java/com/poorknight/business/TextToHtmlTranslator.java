package com.poorknight.business;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.validation.constraints.NotNull;


@Dependent
public class TextToHtmlTranslator implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String HTML_NEWLINE = "<br/>";
	private static final String[] NEWLINE_REGEXS = { "\\r\\n", "\\n" }; // order matters


	public String translate(@NotNull final String originalText) {

		String results = originalText;
		for (final String regex : NEWLINE_REGEXS) {
			results = results.replaceAll(regex, HTML_NEWLINE);
		}
		return results;
	}

}
