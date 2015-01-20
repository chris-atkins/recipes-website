package com.poorknight.testing.matchers.classes;

import javax.faces.view.ViewScoped;

import org.hamcrest.Factory;


public class ViewScopedControllerMatcher extends SerializableControllerMatcher {

	private ViewScopedControllerMatcher() {
		super();
	}


	@Factory
	public static ViewScopedControllerMatcher isAProperViewScopedController() {
		return new ViewScopedControllerMatcher();
	}


	@Override
	String annotation() {
		return "@ViewScoped";
	}


	@Override
	String scopeDescription() {
		return "javax.faces.view.ViewScoped, not javax.faces.bean.ViewScoped";
	}


	@Override
	boolean doesNotHaveTheCorrectScopedAnnotation(final Class<?> classToInspect) {
		final ViewScoped annotation = classToInspect.getAnnotation(javax.faces.view.ViewScoped.class);
		return annotation == null;
	}


	@Override
	String wrongScopeDescription() {
		return "either the class is missing the @ViewScoped annotation, or it is using javax.faces.bean.ViewScoped (it should be using javax.faces.view.ViewScoped).";
	}
}
