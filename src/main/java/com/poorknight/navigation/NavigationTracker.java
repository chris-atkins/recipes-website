package com.poorknight.navigation;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * Responsible for keeping track of page navigation (as long as it is informed of the navigation), and returning to the last page when requested.
 */
@SessionScoped
@Named("navigationTracker")
public class NavigationTracker implements Serializable {

	private static final long serialVersionUID = -7482695445240115815L;

	private final PageNavigationStack navStack = new PageNavigationStack();


	public String lastPage() {
		return hasPreviousPage() ? popLastPage() : null;
	}


	public Boolean hasPreviousPage() {
		return this.navStack.size() > 1;
	}


	void registerNavigationTo(final String path, final String parameterString) {
		this.navStack.push(new Location(path, parameterString));
	}


	private String popLastPage() {
		this.navStack.pop(); // current page
		return this.navStack.pop().toUrl(); // the last page
	}
}