package com.poorknight.initialization;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import com.google.common.collect.ImmutableList;


@RequestScoped
public class InitializationProcessesFactory {

	public List<InitializationProcess> getInitializationProcesses() {
		return new ImmutableList.Builder<InitializationProcess>().build();
	}

}