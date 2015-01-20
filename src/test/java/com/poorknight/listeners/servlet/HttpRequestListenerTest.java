package com.poorknight.listeners.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class HttpRequestListenerTest {

	private final TestHttpListener listener = new TestHttpListener();

	private final ServletRequestEvent event = Mockito.mock(ServletRequestEvent.class);
	private final HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
	private final ServletRequest otherRequest = Mockito.mock(ServletRequest.class);


	@Test
	public void callsOnInitializedWithCorrectObject_IfRequestIsHttpRequest() throws Exception {
		when(this.event.getServletRequest()).thenReturn(this.httpRequest);

		this.listener.requestInitialized(this.event);
		assertThat(this.listener.onInitializedCalled, is(true));
		assertThat(this.listener.passedParameter, equalTo(this.httpRequest));
	}


	@Test
	public void doesNotCallOnInitialized_IfRequestIsNotAnHttpRequest() throws Exception {
		when(this.event.getServletRequest()).thenReturn(this.otherRequest);

		this.listener.requestInitialized(this.event);
		assertThat(this.listener.onInitializedCalled, is(false));
	}


	@Test
	public void callsOnDestroyedWithCorrectObject_IfRequestIsHttpRequest() throws Exception {
		when(this.event.getServletRequest()).thenReturn(this.httpRequest);

		this.listener.requestDestroyed(this.event);
		assertThat(this.listener.onDestroyedCalled, is(true));
		assertThat(this.listener.passedParameter, equalTo(this.httpRequest));
	}


	@Test
	public void doesNotCallOnDestroyed_IfRequestIsNotAnHttpRequest() throws Exception {
		when(this.event.getServletRequest()).thenReturn(this.otherRequest);

		this.listener.requestDestroyed(this.event);
		assertThat(this.listener.onInitializedCalled, is(false));
	}
}


class TestHttpListener extends HttpRequestListener {

	boolean onInitializedCalled = false;
	boolean onDestroyedCalled = false;

	HttpServletRequest passedParameter;


	@Override
	protected void onRequestInitialized(final HttpServletRequest request) {
		this.onInitializedCalled = true;
		this.passedParameter = request;
	}


	@Override
	protected void onRequestDestroyed(final HttpServletRequest request) {
		this.onDestroyedCalled = true;
		this.passedParameter = request;
	}

}