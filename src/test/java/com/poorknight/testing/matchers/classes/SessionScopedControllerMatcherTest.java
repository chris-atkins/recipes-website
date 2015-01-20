package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class SessionScopedControllerMatcherTest {

	final SessionScopedControllerMatcher matcher = SessionScopedControllerMatcher.isAProperSessionScopedController();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(SessionScopedControllerMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithCorrectController() throws Exception {
		final boolean result = this.matcher.matches(ClassThatIsGoodSessionScopedBean.class);
		assertThat(result, is(true));
	}


	@Test
	public void failsOnMissingSessionScopedAnnotation() throws Exception {
		final boolean result = this.matcher.matches(ClassWithNoSessionScopedAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsOnJavaxBeansSessionScopedAnnotation() throws Exception {
		final boolean result = this.matcher.matches(ClassWithWrongSessionScopedAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsIfNotDeclaredSerializable() throws Exception {
		final boolean result = this.matcher.matches(ClassSessionScopedButNotSerializable.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsForMissingNamedAnnotation() throws Exception {
		final boolean result = this.matcher.matches(ClassSessionScopedButMissingNamedAnnotation.class);
		assertThat(result, is(false));
	}
}


@Named
class ClassWithNoSessionScopedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}


@javax.faces.bean.SessionScoped
@Named
class ClassWithWrongSessionScopedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}


@javax.enterprise.context.SessionScoped
@Named
class ClassThatIsGoodSessionScopedBean implements Serializable {

	private static final long serialVersionUID = 1L;
}


@SuppressWarnings("cdi-not-passivation-capable")
@javax.enterprise.context.SessionScoped
@Named
class ClassSessionScopedButNotSerializable {
	// empty on purpose
}


@javax.enterprise.context.SessionScoped
class ClassSessionScopedButMissingNamedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}