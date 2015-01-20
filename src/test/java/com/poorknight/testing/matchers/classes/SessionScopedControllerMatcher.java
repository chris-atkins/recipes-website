package com.poorknight.testing.matchers.classes;

import javax.enterprise.context.SessionScoped;

import org.hamcrest.Factory;


public class SessionScopedControllerMatcher extends SerializableControllerMatcher {

	private SessionScopedControllerMatcher() {
		super();
	}


	@Factory
	public static SessionScopedControllerMatcher isAProperSessionScopedController() {
		return new SessionScopedControllerMatcher();
	}


	@Override
	String annotation() {
		return "@SessionScoped";
	}


	@Override
	String scopeDescription() {
		return "javax.enterprise.context.SessionScoped, not javax.faces.bean.SessionScoped";
	}


	@Override
	boolean doesNotHaveTheCorrectScopedAnnotation(final Class<?> classToInspect) {
		final SessionScoped annotation = classToInspect.getAnnotation(javax.enterprise.context.SessionScoped.class);
		return annotation == null;
	}


	@Override
	String wrongScopeDescription() {
		return "either the class is missing the @SessionScoped annotation, or it is using javax.faces.bean.SessionScoped (it should be using javax.enterprise.context.SessionScoped).";
	}
}
