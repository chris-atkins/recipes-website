package com.poorknight.testing.matchers.fields;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.poorknight.testing.matchers.utils.testclasses.ClassWithNoPrivatePKSetter;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPackagePrivatePKSetter;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPrivatePKSetter;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithProtectedPKSetter;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithPublicPKSetter;
import com.poorknight.utils.ReflectionUtils;


@RunWith(JUnit4.class)
@PrepareForTest(ReflectionUtils.class)
public class NoPublicSetterOnPrimaryKeyFieldsMatcherTest {

	final NoPublicSetterOnPrimaryKeyFieldsMatcher matcher = NoPublicSetterOnPrimaryKeyFieldsMatcher.doesNotHavePublicSetterForPrimaryKey();


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(NoPublicSetterOnPrimaryKeyFieldsMatcher.class, hasFactoryMethod());
	}


	@Test
	public void correctResultsForPublicVisibility() throws Exception {
		final boolean results = this.matcher.matches(ClassWithPublicPKSetter.class);
		assertThat(results, is(false));
	}


	@Test
	public void correctResultsForProtectedVisibility() throws Exception {
		final boolean results = this.matcher.matches(ClassWithProtectedPKSetter.class);
		assertThat(results, is(true));
	}


	@Test
	public void correctResultsForPackagePrivateVisibility() throws Exception {
		final boolean results = this.matcher.matches(ClassWithPackagePrivatePKSetter.class);
		assertThat(results, is(true));
	}


	@Test
	public void correctResultsForPrivateVisibility() throws Exception {
		final boolean results = this.matcher.matches(ClassWithPrivatePKSetter.class);
		assertThat(results, is(true));
	}


	@Test
	public void correctResultsForNoIdField() throws Exception {
		final boolean results = this.matcher.matches(Object.class);
		assertThat(results, is(true));
	}


	@Test
	public void correctResultsForIdFieldWithNoGetterOrSetter() throws Exception {
		final boolean results = this.matcher.matches(ClassWithNoPrivatePKSetter.class);
		assertThat(results, is(true));
	}
}
