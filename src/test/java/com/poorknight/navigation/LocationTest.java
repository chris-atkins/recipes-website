package com.poorknight.navigation;

import static com.poorknight.testing.matchers.CustomMatchers.isTrulySerializable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.Serializable;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class LocationTest {

	private final String path = randomString();
	private final String params = randomString();

	private static final String LOCALHOST_PREFIX = "/recipee7";
	private static final String ALONE_REDIRECT_SUFFIX = "?faces-redirect=true";
	private static final String OTHER_PARAMS_REDIRECT_SUFFIX = "&faces-redirect=true";


	@Test
	public void returnsPathPlusRedirect_IfParamsAreNull() throws Exception {
		final Location location = new Location(this.path, null);
		assertThat(location.toUrl(), equalTo(this.path + ALONE_REDIRECT_SUFFIX));
	}


	@Test
	public void implementsSerializable() throws Exception {
		final boolean results = Serializable.class.isAssignableFrom(Location.class);
		assertThat(results, equalTo(true));
	}


	@Test
	public void trulySerializable() throws Exception {
		final Location location = new Location(this.path, this.params);
		assertThat(location, isTrulySerializable());
	}


	@Test
	public void returnsPathPlusParamsWithRedirect_ifParamsExist() throws Exception {
		final Location location = new Location(this.path, this.params);
		assertThat(location.toUrl(), equalTo(this.path + "?" + this.params + OTHER_PARAMS_REDIRECT_SUFFIX));
	}


	@Test
	public void stripsOffLocalhostPrefix_WithNullParams() throws Exception {
		final Location location = new Location(LOCALHOST_PREFIX + this.path, null);
		assertThat(location.toUrl(), equalTo(this.path + ALONE_REDIRECT_SUFFIX));
	}


	@Test
	public void stripsOffLocalhostPrefix_WithParams() throws Exception {
		final Location location = new Location(LOCALHOST_PREFIX + this.path, this.params);
		assertThat(location.toUrl(), equalTo(this.path + "?" + this.params + OTHER_PARAMS_REDIRECT_SUFFIX));
	}


	@Test
	public void isSimalarTo_IfPathsAreTheSame_Case1() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(this.path, this.params);

		assertThat(location.isSimilarTo(otherLocation), is(true));
	}


	@Test
	public void isSimalarTo_IfPathsAreTheSame_Case2() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(this.path, randomString());

		assertThat(location.isSimilarTo(otherLocation), is(true));
	}


	@Test
	public void isSimalarTo_IfPathsAreTheSame_Case3() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(this.path, null);

		assertThat(location.isSimilarTo(otherLocation), is(true));
	}


	@Test
	public void isSimalarTo_IfPathsAreTheSame_Case4() throws Exception {
		final Location location = new Location(this.path, null);
		final Location otherLocation = new Location(this.path, null);

		assertThat(location.isSimilarTo(otherLocation), is(true));
	}


	@Test
	public void isNotSimalarTo_IfPathsAreNotTheSame_Case1() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(randomString(), this.params);

		assertThat(location.isSimilarTo(otherLocation), is(false));
	}


	@Test
	public void isNotSimalarTo_IfPathsAreNotTheSame_Case2() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(randomString(), randomString());

		assertThat(location.isSimilarTo(otherLocation), is(false));
	}


	@Test
	public void isNotSimalarTo_IfPathsAreNotTheSame_Case3() throws Exception {
		final Location location = new Location(this.path, this.params);
		final Location otherLocation = new Location(randomString(), null);

		assertThat(location.isSimilarTo(otherLocation), is(false));
	}


	@Test
	public void isNotSimalarTo_IfPathsAreNotTheSame_Case4() throws Exception {
		final Location location = new Location(this.path, null);
		final Location otherLocation = new Location(randomString(), null);

		assertThat(location.isSimilarTo(otherLocation), is(false));
	}


	private String randomString() {
		return RandomStringUtils.random(RandomUtils.nextInt(100));
	}
}
