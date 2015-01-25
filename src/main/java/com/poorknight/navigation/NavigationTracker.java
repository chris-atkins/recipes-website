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
		return lastPageExists() ? popLastPage() : null;
	}


	void registerNavigationTo(final String path, final String parameterString) {
		if (!path.contains("/home/index.jsf")) {
			System.out
					.println("********************************************************************************************************\nRegister Navigation Event: "
							+ path + " | " + parameterString);
		}

		this.navStack.push(new Location(path, parameterString));

		if (!path.contains("/home/index.jsf")) {
			System.out.println("New Navigation Stack: " + this.navStack);

		}
	}


	private boolean lastPageExists() {
		return this.navStack.size() > 1;
	}


	private String popLastPage() {
		this.navStack.pop(); // current page
		return this.navStack.pop().toUrl(); // the last page
	}
}