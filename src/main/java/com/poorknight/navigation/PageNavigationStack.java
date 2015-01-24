package com.poorknight.navigation;

import java.util.Stack;


/**
 * Responsible for keeping a stack of navigation locations. JSF seems to navigate multiple times to the same page (maybe ajax calls?), and this class
 * holds rules for which of these it will keep or ignore. The result should be a stack that represents the user's concept of where they have navigated
 * to (in the correct order).
 */
public class PageNavigationStack extends Stack<Location> {

	private static final long serialVersionUID = -3170782906285679620L;

	private static final String EXPECTED_PREFIX = "/pages/";


	@Override
	public Location push(final Location location) {

		if (locationShouldBePushed(location)) {
			super.push(location);
		}

		if (locationShouldReplaceLastLocation(location)) {
			replaceLastLocationWith(location);
		}

		return isEmpty() ? null : peek();
	}


	private boolean locationShouldBePushed(final Location newLocation) {
		if (locationIsNotAPage(newLocation)) {
			return false;
		}

		if (isEmpty()) {
			return true;
		}

		final Location lastLocation = peek();

		if (lastLocation.equals(newLocation)) {
			return false;
		}

		return !(newLocation.isSimilarTo(lastLocation));
	}


	private boolean locationIsNotAPage(final Location newLocation) {
		return !(newLocation.getPath().startsWith(EXPECTED_PREFIX));
	}


	private boolean locationShouldReplaceLastLocation(final Location newLocation) {
		if (isEmpty()) {
			return false;
		}
		Location lastLocation = peek();
		return (newLocation.isSimilarTo(lastLocation) && newLocation.getParameterString() != null);
	}


	private void replaceLastLocationWith(final Location newLocation) {
		pop();
		super.push(newLocation);
	}
}