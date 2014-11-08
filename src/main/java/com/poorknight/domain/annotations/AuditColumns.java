package com.poorknight.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;


@InterceptorBinding
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditColumns {

	String createdOn() default "createdOn";


	String createdBy() default "createdBy";


	String lastUpdatedOn() default "lastUpdatedOn";


	String lastUpdatedBy() default "lastUpdatedBy";
}
