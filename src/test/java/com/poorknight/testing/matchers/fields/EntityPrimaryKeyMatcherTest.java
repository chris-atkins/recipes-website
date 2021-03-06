package com.poorknight.testing.matchers.fields;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.testing.matchers.utils.testclasses.ClassWithAutoGeneratedAutoGeneratedIDAttributeWithWrongStrategy;
import com.poorknight.testing.matchers.utils.testclasses.ClassWithIDAttributeButNoAutoGenerated;
import com.poorknight.testing.matchers.utils.testclasses.FieldOfTypelong;
import com.poorknight.testing.matchers.utils.testclasses.ProperEntityTestClass;


@RunWith(MockitoJUnitRunner.class)
public class EntityPrimaryKeyMatcherTest {

	private static final Class<?> GOOD_CLASS_TO_TEST = ProperEntityTestClass.class;
	private static final Class<?> BAD_CLASS_TO_TEST = FieldOfTypelong.class;
	private static final Class<?> NON_AUTO_GENERATED_PK_CLASS = ClassWithIDAttributeButNoAutoGenerated.class;
	private static final Class<?> AUTO_GENERATED_PK_CLASS_WITH_WRONG_STRATEGY = ClassWithAutoGeneratedAutoGeneratedIDAttributeWithWrongStrategy.class;

	@InjectMocks
	private EntityPrimaryKeyMatcher primaryKeyMatcher;

	@Mock
	private AnnotatedFieldsMatcher idAttributeVerifier;


	@Test
	public void usesFactoryMethod() {
		assertThat(EntityPrimaryKeyMatcher.class, hasFactoryMethod());
	}


	@Test
	public void usesIdAttributeVerifierResultsForTrue() {
		when(this.idAttributeVerifier.matchesSafely(Matchers.eq(GOOD_CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(true);
		final Description mockDescription = Mockito.mock(Description.class);

		final boolean results = this.primaryKeyMatcher.matchesSafely(GOOD_CLASS_TO_TEST, mockDescription);

		verify(this.idAttributeVerifier).matchesSafely(GOOD_CLASS_TO_TEST, mockDescription);
		assertThat(results, is(true));
	}


	@Test
	public void usesIdAttributeVerifierResultsForFalse() {
		when(this.idAttributeVerifier.matchesSafely(Matchers.eq(GOOD_CLASS_TO_TEST), Matchers.any(Description.class))).thenReturn(false);
		final Description mockDescription = Mockito.mock(Description.class);

		final boolean results = this.primaryKeyMatcher.matchesSafely(GOOD_CLASS_TO_TEST, mockDescription);

		verify(this.idAttributeVerifier).matchesSafely(GOOD_CLASS_TO_TEST, mockDescription);
		assertThat(results, is(false));
	}


	@Test
	public void checkThatLongValuesAreAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(GOOD_CLASS_TO_TEST, Description.NONE);
		assertThat(results, is(true));
	}


	@Test
	public void checkThatNonLongValuesAreNotAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(BAD_CLASS_TO_TEST, Description.NONE);
		assertThat(results, is(false));
	}


	@Test
	public void checkThatAutoGeneratedValuesAreAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(GOOD_CLASS_TO_TEST, Description.NONE);
		assertThat(results, is(true));
	}


	@Test
	public void checkThatNonAutoGeneratedValuesAreNotAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(NON_AUTO_GENERATED_PK_CLASS, Description.NONE);
		assertThat(results, is(false));
	}


	@Test
	public void checkThatAutoGeneratedValuesWithWrongStrateghyAreNotAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(AUTO_GENERATED_PK_CLASS_WITH_WRONG_STRATEGY,
				Description.NONE);
		assertThat(results, is(false));
	}


	@Test
	public void checkThatAutoGeneratedValuesWithCorrectStrategyAreAccepted() {
		final boolean results = EntityPrimaryKeyMatcher.hasValidPrimaryKey().matchesSafely(GOOD_CLASS_TO_TEST, Description.NONE);
		assertThat(results, is(true));
	}
}
