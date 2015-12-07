package com.poorknight.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.NotFoundException;
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

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.business.saverecipe.SaveRecipeService;
import com.poorknight.domain.Recipe;
import com.poorknight.domain.RecipeDAO;
import com.poorknight.utils.UnitTestSetupUtils;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEndpointTest {

	@InjectMocks
	private RecipeEndpoint recipeEndpoint;

	@Mock
	private SaveRecipeService recipeService;

	@Mock
	private RecipeDAO recipeDao;

	@Mock
	private Recipe recipe;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Response mockResponseFromResponseBuilder;

	@Test
	public void postRecipe_SavesAndReturnsResults() throws Exception {
		Recipe response = recipeEndpoint.postRecipe(recipe);

		verify(recipeService).saveNewRecipe(recipe);
		assertThat(response, equalTo(recipe));
	}

	@Test
	public void getRecipe_getsRecipeByPassedLong() throws Exception {
		long recipeId = RandomUtils.nextLong();
		when(recipeDao.queryRecipeById(recipeId)).thenReturn(recipe);

		Recipe response = recipeEndpoint.getRecipe(recipeId);

		assertThat(response, equalTo(recipe));
	}

	@Test(expected = NotFoundException.class)
	public void getRecipe_throwsNotFoundException_WhenNoRecipeIsFound() throws Exception {
		UnitTestSetupUtils.mockContainerToHandleWebServiceExceptions();
		when(recipeDao.queryRecipeById(-1L)).thenReturn(null);
		
		recipeEndpoint.getRecipe(-1L);
	}
}
