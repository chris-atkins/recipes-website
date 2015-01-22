package com.poorknight.navigation;

import lombok.Data;


/**
 * Responsible for managing the quirks of the URLs for this project. When given a path and query string (parameters - like 'recipeId=1'), it can
 * regenerate the location with the query string into a url that the system can navigate to.
 */
@Data
public class Location {

	final private String path;
	final private String parameterString;

	final static private String PREFIX = "/recipee7";
	final static private String NO_PARAM_SUFFIX = "?faces-redirect=true";
	final static private String WITH_PARAM_SUFFIX = "&faces-redirect=true";


	Location(final String path, final String parameterString) {
		if (path.startsWith(PREFIX)) {
			this.path = path.substring(PREFIX.length());
		} else {
			this.path = path;
		}
		this.parameterString = parameterString;
	}


	boolean isSimilarTo(final Location other) {
		return this.path.equals(other.getPath());
	}


	String toUrl() {
		if (this.parameterString == null) {
			return this.path + NO_PARAM_SUFFIX;
		}
		return this.path + "?" + this.parameterString + WITH_PARAM_SUFFIX;
	}
}
