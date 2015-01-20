package com.poorknight.navigation;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import com.poorknight.listeners.servlet.HttpRequestListener;


@WebListener
public class NavigationRequestListener extends HttpRequestListener {

	@Inject
	private NavigationTracker navigator;


	@Override
	protected void onRequestInitialized(final HttpServletRequest request) {
		this.navigator.registerNavigationTo(request.getRequestURI(), request.getQueryString());
	}


	@Override
	protected void onRequestDestroyed(final HttpServletRequest request) {
		return;
	}
}
