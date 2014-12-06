package com.poorknight.testing.matchers.fields;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ReadOnlyFieldMatcherTest {

	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(ReadOnlyFieldMatcher.class, hasFactoryMethod());
	}


	private boolean runTestForField(final String fieldName) {
		final ReadOnlyFieldMatcher matcher = ReadOnlyFieldMatcher.hasAReadOnlyField(fieldName);
		return matcher.matches(ReadOnlyFieldTestClass.class);
	}


	@Test
	public void failsForPublicField() throws Exception {
		final boolean result = runTestForField("publicField");
		assertThat(result, is(false));
	}


	@Test
	public void passesForProtectedField() throws Exception {
		final boolean result = runTestForField("protectedField");
		assertThat(result, is(true));
	}


	@Test
	public void passesForPackageField() throws Exception {
		final boolean result = runTestForField("packageField");
		assertThat(result, is(true));
	}


	@Test
	public void passesForPrivateField() throws Exception {
		final boolean result = runTestForField("privateField");
		assertThat(result, is(true));
	}


	@Test
	public void failsWithMissingGetter() throws Exception {
		final boolean result = runTestForField("missingGetter");
		assertThat(result, is(false));
	}


	@Test
	public void failsWithPrivateGetter() throws Exception {
		final boolean result = runTestForField("privateGetter");
		assertThat(result, is(false));
	}


	@Test
	public void failsWithPackageGetter() throws Exception {
		final boolean result = runTestForField("packageGetter");
		assertThat(result, is(false));
	}


	@Test
	public void failsWithProtectedGetter() throws Exception {
		final boolean result = runTestForField("protectedGetter");
		assertThat(result, is(false));
	}


	@Test
	public void passesWithPublicGetter() throws Exception {
		final boolean result = runTestForField("publicGetter");
		assertThat(result, is(true));
	}


	@Test
	public void passesWithBooleanGettersUsingIs() throws Exception {
		final boolean result = runTestForField("booleanGetter");
		assertThat(result, is(true));
	}


	@Test
	public void passesWithPrimitiveBooleanGettersUsingIs() throws Exception {
		final boolean result = runTestForField("primitiveBooleanGetter");
		assertThat(result, is(true));
	}


	@Test
	public void failsWithPublicSetter() throws Exception {
		final boolean result = runTestForField("publicSetter");
		assertThat(result, is(false));
	}


	@Test
	public void passesWithProtectedSetter() throws Exception {
		final boolean result = runTestForField("protectedSetter");
		assertThat(result, is(true));
	}


	@Test
	public void passesWithPackageSetter() throws Exception {
		final boolean result = runTestForField("packageSetter");
		assertThat(result, is(true));
	}


	@Test
	public void passesWithPrivateSetter() throws Exception {
		final boolean result = runTestForField("privateSetter");
		assertThat(result, is(true));
	}


	@Test
	public void passesWithMissingSetter() throws Exception {
		final boolean result = runTestForField("noSetter");
		assertThat(result, is(true));
	}
}


final class ReadOnlyFieldTestClass {

	@Getter
	private String privateField;

	@Getter
	String packageField;

	@Getter
	protected String protectedField;

	@Getter
	public String publicField;

	@Getter(AccessLevel.NONE)
	private String missingGetter;

	@Getter(AccessLevel.PRIVATE)
	private String privateGetter;

	@Getter(AccessLevel.PACKAGE)
	private String packageGetter;

	@Getter(AccessLevel.PROTECTED)
	private String protectedGetter;

	@Getter(AccessLevel.PUBLIC)
	private String publicGetter;

	private Boolean booleanGetter;

	private boolean primitiveBooleanGetter;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private String publicSetter;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PROTECTED)
	private String protectedSetter;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PACKAGE)
	private String packageSetter;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PRIVATE)
	private String privateSetter;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.NONE)
	private String noSetter;


	public Boolean isBooleanGetter() {
		return this.booleanGetter;
	}


	public boolean isPrimitiveBooleanGetter() {
		return this.primitiveBooleanGetter;
	}
}
