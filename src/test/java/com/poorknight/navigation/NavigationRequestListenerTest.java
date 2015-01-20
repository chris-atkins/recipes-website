package com.poorknight.navigation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NavigationRequestListenerTest {

	@InjectMocks
	private NavigationRequestListener listener;

	@Mock
	private NavigationTracker navigator;

	@Mock
	private HttpServletRequest request;

	@Mock
	private ServletRequestEvent event;

	private final String uri = RandomStringUtils.random(10);

	private final String queryString = RandomStringUtils.random(10);


	@Before
	public void initMocks() {
		when(this.event.getServletRequest()).thenReturn(this.request);
		when(this.request.getRequestURI()).thenReturn(this.uri);
		when(this.request.getQueryString()).thenReturn(this.queryString);
	}


	@Test
	public void passesUriAndQueryString_OnRequestInitialized() throws Exception {
		this.listener.requestInitialized(this.event);
		verify(this.navigator).registerNavigationTo(this.uri, this.queryString);
	}


	@Test
	public void noInteractions_OnRequestDetroyed() throws Exception {
		this.listener.requestDestroyed(this.event);
		verifyZeroInteractions(this.navigator);
	}

}
