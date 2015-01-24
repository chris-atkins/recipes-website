package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class SessionScopeMatcherTest {

	SessionScopeMatcher matcher = SessionScopeMatcher.isSessionScoped();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(SessionScopeMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesWithGoodClass() throws Exception {
		final boolean results = this.matcher.matches(GoodSessionScopedClass.class);
		assertThat(results, is(true));
	}


	@Test
	public void failsWith_WrongSessionScoped() throws Exception {
		final boolean results = this.matcher.matches(WrongSessionScopedClass.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWith_NoSessionScoped() throws Exception {
		final boolean results = this.matcher.matches(MissingSessionScopedClass.class);
		assertThat(results, is(false));
	}


	@Test
	public void failsWith_NoSerializable() throws Exception {
		final boolean results = this.matcher.matches(MissingSerializableClass.class);
		assertThat(results, is(false));
	}
}


@javax.enterprise.context.SessionScoped
class GoodSessionScopedClass implements Serializable {

	private static final long serialVersionUID = 461177547146000831L;
}


@javax.faces.bean.SessionScoped
class WrongSessionScopedClass implements Serializable {

	private static final long serialVersionUID = 461177547146000831L;
}


class MissingSessionScopedClass implements Serializable {

	private static final long serialVersionUID = 461177547146000831L;
}


@SuppressWarnings("cdi-not-passivation-capable")
@javax.enterprise.context.SessionScoped
class MissingSerializableClass {
	// empty on purpose
}
