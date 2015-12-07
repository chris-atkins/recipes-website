package com.poorknight.utils;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.mockito.Mockito;

public class UnitTestSetupUtils {

	private UnitTestSetupUtils() {
		//private to avoid instantiation
	}
	
	public static void mockContainerToHandleWebServiceExceptions() {

        RuntimeDelegate.setInstance(new RuntimeDelegate() {
			
			@Override
			public VariantListBuilder createVariantListBuilder() {
				return null;
			}
			
			@Override
			public UriBuilder createUriBuilder() {
				return null;
			}
			
			@Override
			public ResponseBuilder createResponseBuilder() {
				
				return new Response.ResponseBuilder() {
					
					@Override
					public ResponseBuilder variants(List<Variant> arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder variants(Variant... arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder variant(Variant arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder type(String arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder type(MediaType arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder tag(String arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder tag(EntityTag arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder status(int arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder replaceAll(MultivaluedMap<String, Object> arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder location(URI arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder links(Link... arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder link(String arg0, String arg1) {
						return this;
					}
					
					@Override
					public ResponseBuilder link(URI arg0, String arg1) {
						return this;
					}
					
					@Override
					public ResponseBuilder lastModified(Date arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder language(Locale arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder language(String arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder header(String arg0, Object arg1) {
						return this;
					}
					
					@Override
					public ResponseBuilder expires(Date arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder entity(Object arg0, Annotation[] arg1) {
						return this;
					}
					
					@Override
					public ResponseBuilder entity(Object arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder encoding(String arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder cookie(NewCookie... arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder contentLocation(URI arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder clone() {
						return this;
					}
					
					@Override
					public ResponseBuilder cacheControl(CacheControl arg0) {
						return this;
					}
					
					@Override
					public Response build() {
						Response mockResponseFromResponseBuilder = Mockito.mock(Response.class, RETURNS_DEEP_STUBS);
						when(mockResponseFromResponseBuilder.getStatusInfo().getFamily()).thenReturn(Response.Status.Family.CLIENT_ERROR);
						return mockResponseFromResponseBuilder;
					}
					
					@Override
					public ResponseBuilder allow(Set<String> arg0) {
						return this;
					}
					
					@Override
					public ResponseBuilder allow(String... arg0) {
						return this;
					}
				};
			}
			
			@Override
			public Builder createLinkBuilder() {
				return null;
			}
			
			@Override
			public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> arg0) throws IllegalArgumentException {
				return null;
			}
			
			@Override
			public <T> T createEndpoint(Application arg0, Class<T> arg1)
					throws IllegalArgumentException, UnsupportedOperationException {
				return null;
			}
		});
	}
}
