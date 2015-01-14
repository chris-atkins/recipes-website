package com.poorknight.initialization;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.google.common.collect.ImmutableList;


@RequestScoped
public class InitializationProcessesFactory {

	@Inject
	private RecipeSearchableTextPopulator recipeSearchableTextPopulator;


	public List<InitializationProcess> getInitializationProcesses() {
		return new ImmutableList.Builder<InitializationProcess>().add(//
				this.recipeSearchableTextPopulator).build();
	}

}
