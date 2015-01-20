package com.poorknight.listeners.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;


public abstract class HttpRequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(final ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			onRequestDestroyed((HttpServletRequest) servletRequest);
		}
	}


	@Override
	public void requestInitialized(final ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			onRequestInitialized((HttpServletRequest) servletRequest);
		}
	}


	protected abstract void onRequestInitialized(final HttpServletRequest request);


	protected abstract void onRequestDestroyed(final HttpServletRequest request);
}
