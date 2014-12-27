package com.poorknight.testing.matchers.classes;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.poorknight.testing.matchers.utils.testclasses.RequestScopeTestClassThatIsProper;
import com.poorknight.testing.matchers.utils.testclasses.RequestScopeTestClassWithMissingNamedAnnotation;
import com.poorknight.testing.matchers.utils.testclasses.RequestScopeTestClassWithMissingScopeAnnotation;
import com.poorknight.testing.matchers.utils.testclasses.RequestScopeTestClassWithWrongScopeAnnotation;


@RunWith(JUnit4.class)
public class RequestScopedControllerMatcherTest {

	RequestScopedControllerMatcher matcher = RequestScopedControllerMatcher.isAProperRequestScopedController();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(RequestScopedControllerMatcher.class, hasFactoryMethod());
	}


	@Test
	public void passesIfReqeustScopedAndNamedAnnotationsExist() throws Exception {
		final boolean result = this.matcher.matches(RequestScopeTestClassThatIsProper.class);
		assertThat(result, is(true));
	}


	@Test
	public void failsIfWrongRequestScopeAnnotation() throws Exception {
		final boolean result = this.matcher.matches(RequestScopeTestClassWithWrongScopeAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsIfMissingRequestScopeAnnotation() throws Exception {
		final boolean result = this.matcher.matches(RequestScopeTestClassWithMissingScopeAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsIfMissingNamedAnnotation() throws Exception {
		final boolean result = this.matcher.matches(RequestScopeTestClassWithMissingNamedAnnotation.class);
		assertThat(result, is(false));
	}
}
