package com.poorknight.navigation;

import java.io.Serializable;
import java.util.Stack;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@SessionScoped
@Named("navigationTracker")
public class NavigationTracker implements Serializable {

	private static final long serialVersionUID = -7482695445240115815L;

	private final Stack<Location> navStack = new Stack<>();


	public String lastPage() {
		return lastPageExists() ? popLastPage() : null;
	}


	void registerNavigationTo(final String path, final String parameterString) {
		final Location newLocation = new Location(path, parameterString);

		if (locationShouldBeSaved(newLocation)) {
			save(newLocation);
		}

		if (locationShouldReplaceLastLocation(newLocation)) {
			replaceLastLocationWith(newLocation);
		}
	}


	private boolean lastPageExists() {
		return this.navStack.size() > 1;
	}


	private String popLastPage() {
		this.navStack.pop(); // current page
		return this.navStack.pop().toUrl(); // the last page
	}


	private boolean locationShouldBeSaved(final Location newLocation) {
		if (this.navStack.isEmpty()) {
			return true;
		}

		final Location lastLocation = this.navStack.peek();

		if (lastLocation.equals(newLocation)) {
			return false;
		}

		return !(newLocation.isSimilarTo(lastLocation));
	}


	private void save(final Location newLocation) {
		this.navStack.push(newLocation);
	}


	private boolean locationShouldReplaceLastLocation(final Location newLocation) {
		final Location lastLocation = this.navStack.peek();
		return (newLocation.isSimilarTo(lastLocation) && newLocation.getParameterString() != null);
	}


	private void replaceLastLocationWith(final Location newLocation) {
		this.navStack.pop();
		save(newLocation);
	}
}
