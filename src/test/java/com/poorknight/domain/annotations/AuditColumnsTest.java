package com.poorknight.domain.annotations;

import static com.poorknight.testing.matchers.CustomMatchers.hasAnnotation;
import static com.poorknight.testing.matchers.CustomMatchers.hasAnnotationWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@AuditColumns
@RunWith(JUnit4.class)
public class AuditColumnsTest {

	@Test
	public void hasRuntimeRetention() throws Exception {
		assertThat(AuditColumns.class, hasAnnotationWithValue(Retention.class, RetentionPolicy.RUNTIME));
	}


	@Test
	public void hasTypeAsAnnotationTarget() throws Exception {
		assertThat(AuditColumns.class, hasAnnotationWithValue(Target.class, ElementType.TYPE));
	}


	@Test
	public void hasInterceptorBindingAnnotation() throws Exception {
		assertThat(AuditColumns.class, hasAnnotation(InterceptorBinding.class));
	}


	@Test
	public void defaultCreatedOn() throws Exception {
		final AuditColumns annotation = getDefaultAnnotation();
		assertThat(annotation.createdOn(), equalTo("createdOn"));
	}


	@Test
	public void defaultCreatedbY() throws Exception {
		final AuditColumns annotation = getDefaultAnnotation();
		assertThat(annotation.createdBy(), equalTo("createdBy"));
	}


	@Test
	public void defaultLastUpdatedOn() throws Exception {
		final AuditColumns annotation = getDefaultAnnotation();
		assertThat(annotation.lastUpdatedOn(), equalTo("lastUpdatedOn"));
	}


	@Test
	public void defaultLastUpdatedBy() throws Exception {
		final AuditColumns annotation = getDefaultAnnotation();
		assertThat(annotation.lastUpdatedBy(), equalTo("lastUpdatedBy"));
	}


	private AuditColumns getDefaultAnnotation() {
		final AuditColumns annotation = this.getClass().getAnnotation(AuditColumns.class);
		return annotation;
	}
}
