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
public class ViewScopedControllerMatcherTest {

	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(ViewScopedControllerMatcher.class, hasFactoryMethod());
	}


	private boolean runTest(final Class<?> classToTest) {
		final ViewScopedControllerMatcher matcher = ViewScopedControllerMatcher.isAProperViewScopedController();
		return matcher.matches(classToTest);
	}


	@Test
	public void passesWithCorrectController() throws Exception {
		final boolean result = runTest(ClassThatIsGoodViewScopedBean.class);
		assertThat(result, is(true));
	}


	@Test
	public void failsOnMissingViewScopedAnnotation() throws Exception {
		final boolean result = runTest(ClassWithNoViewScopedAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsOnJavaxBeansViewScopedAnnotation() throws Exception {
		final boolean result = runTest(ClassWithWrongViewScopedAnnotation.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsIfNotDeclaredSerializable() throws Exception {
		final boolean result = runTest(ClassViewScopedButNotSerializable.class);
		assertThat(result, is(false));
	}


	@Test
	public void failsForMissingNamedAnnotation() throws Exception {
		final boolean result = runTest(ClassViewScopedButMissingNamedAnnotation.class);
		assertThat(result, is(false));
	}
}


class ClassWithNoViewScopedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}


@javax.faces.bean.ViewScoped
@Named
class ClassWithWrongViewScopedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}


@javax.faces.view.ViewScoped
@Named
class ClassThatIsGoodViewScopedBean implements Serializable {

	private static final long serialVersionUID = 1L;
}


@javax.faces.view.ViewScoped
@Named
class ClassViewScopedButNotSerializable {
	// empty on purpose
}


@javax.faces.view.ViewScoped
class ClassViewScopedButMissingNamedAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
}
