package com.poorknight.listeners.monitoring;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public class MonitoringListener implements HttpSessionAttributeListener, ServletContextAttributeListener, HttpSessionListener,
		HttpSessionActivationListener {

	private boolean shouldLog = false;


	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		log("ServletContext attributeAdded: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		log("ServletContext attributeRemoved: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		log("ServletContext attributeReplaced: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		String sessionId = event.getSession().getId();
		log("HttpSessionBinding(" + sessionId + ") attributeAdded: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		String sessionId = event.getSession().getId();
		log("HttpSessionBinding(" + sessionId + ") attributeRemoved: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		String attributeValue = "" + event.getValue();
		String sessionId = event.getSession().getId();
		log("HttpSessionBinding(" + sessionId + ") attributeReplaced: " + attributeName + " | " + attributeValue);
	}


	@Override
	public void sessionCreated(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		log("Session created: " + sessionId);
	}


	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		log("SESSION DESTROYED: " + sessionId);
	}


	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		log("Session passivating: " + sessionId);
	}


	@Override
	public void sessionDidActivate(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		log("Session activating: " + sessionId);
	}


	private void log(String logStatement) {
		if (shouldLog) {
			System.out.println(logStatement);
		}
	}
}
