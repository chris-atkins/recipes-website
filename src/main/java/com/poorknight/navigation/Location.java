package com.poorknight.navigation;

import lombok.Data;


@Data
public class Location {

	final private String path;
	final private String parameterString;

	final static private String PREFIX = "/recipee7";
	final static private String NO_PARAM_SUFFIX = "?faces-redirect=true";
	final static private String WITH_PARAM_SUFFIX = "&faces-redirect=true";


	public Location(final String path, final String parameterString) {
		if (path.startsWith(PREFIX)) {
			this.path = path.substring(PREFIX.length());
		} else {
			this.path = path;
		}
		this.parameterString = parameterString;
	}


	public boolean isSimilarTo(final Location other) {
		return this.path.equals(other.getPath());
	}


	public String toUrl() {
		if (this.parameterString == null) {
			return this.path + NO_PARAM_SUFFIX;
		}
		return this.path + "?" + this.parameterString + WITH_PARAM_SUFFIX;
	}
}
