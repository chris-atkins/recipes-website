package com.poorknight.testing.matchers;

import static com.poorknight.testing.matchers.CustomMatchers.hasFactoryMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.testing.matchers.utils.ReflectionUtils;
import com.poorknight.testing.matchers.utils.testclasses.AuditTestClasses;


@RunWith(MockitoJUnitRunner.class)
public class AuditColumnsMatcherTest {

	@InjectMocks
	AuditColumnsMatcher mockMatcher;

	@Mock
	SingleAuditFieldMatcher timestampFieldMatcher;

	@Mock
	SingleAuditFieldMatcher userFieldMatcher;


	@Before
	public void init() {
		setupDateFieldMatcher(true);
		setupStringFieldMatcher(true);
	}


	private void setupDateFieldMatcher(final boolean results) {
		when(this.timestampFieldMatcher.matchesSafely(any(Field.class), any(Description.class))).thenReturn(results);
	}


	private void setupStringFieldMatcher(final boolean results) {
		when(this.userFieldMatcher.matchesSafely(any(Field.class), any(Description.class))).thenReturn(results);
	}


	@Test
	public void usesFactoryMethod() throws Exception {
		assertThat(AuditColumnsMatcher.class, hasFactoryMethod());
	}


	@Test
	public void failsIfAuditColumnsAnnotationIsNotOnClass() throws Exception {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(Object.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfMissingCreatedOnColumn() throws Exception {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(AuditTestClasses.MissingCreatedOnColumn.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfMissingCreatedByColumn() throws Exception {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(AuditTestClasses.MissingCreatedByColumn.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfMissingUpdatedOnColumn() throws Exception {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(AuditTestClasses.MissingLastUpdatedOnColumn.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfMissingUpdatedByColumn() throws Exception {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(AuditTestClasses.MissingLastUpdatedByColumn.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfADateFieldFails() throws Exception {
		setupDateFieldMatcher(false);

		final boolean results = this.mockMatcher.matchesSafely(AuditTestClasses.ForMockTests.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfAStringFieldFails() throws Exception {
		setupStringFieldMatcher(false);

		final boolean results = this.mockMatcher.matchesSafely(AuditTestClasses.ForMockTests.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void failsIfVersionAnnotationDoesNotExistOnLastUpdateField() {
		final AuditColumnsMatcher matcher = AuditColumnsMatcher.correctlyImplementsAuditColumns();
		final boolean results = matcher.matchesSafely(AuditTestClasses.NoVersionAnnotationOnLastUpdate.class, Description.NONE);

		assertThat(results, equalTo(false));
	}


	@Test
	public void callsFieldMatcherOnceForEachFieldWithTrueResults() {
		final Class<?> classToUse = AuditTestClasses.ForMockTests.class;
		final Field createdOnField = ReflectionUtils.findFieldInClass(classToUse, "createdOn");
		final Field createdByField = ReflectionUtils.findFieldInClass(classToUse, "createdBy");
		final Field lastUpdatedOnField = ReflectionUtils.findFieldInClass(classToUse, "lastUpdatedOn");
		final Field lastUpdatedByField = ReflectionUtils.findFieldInClass(classToUse, "lastUpdatedBy");

		final boolean results = this.mockMatcher.matchesSafely(AuditTestClasses.ForMockTests.class, Description.NONE);

		verify(this.timestampFieldMatcher, times(1)).matchesSafely(Matchers.eq(createdOnField), Matchers.any(Description.class));
		verify(this.userFieldMatcher, times(1)).matchesSafely(Matchers.eq(createdByField), Matchers.any(Description.class));
		verify(this.timestampFieldMatcher, times(1)).matchesSafely(Matchers.eq(lastUpdatedOnField), Matchers.any(Description.class));
		verify(this.userFieldMatcher, times(1)).matchesSafely(Matchers.eq(lastUpdatedByField), Matchers.any(Description.class));
		assertThat(results, equalTo(true));
	}


	@Test
	public void returnsTrueWhenAllCriteriaAreMet() {
		final boolean results = this.mockMatcher.matchesSafely(AuditTestClasses.CorrectlyImplemented.class, Description.NONE);

		assertThat(results, equalTo(true));
	}
}
