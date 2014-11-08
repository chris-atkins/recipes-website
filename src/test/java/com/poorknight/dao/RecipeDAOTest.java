package com.poorknight.dao;

import static com.poorknight.testing.matchers.CustomMatchers.hasCorrectTransactionLevelOnMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.math.RandomUtils;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.domain.Recipe;
import com.poorknight.exceptions.DaoException;
import com.poorknight.testing.matchers.MethodTransactionAnnotationMatcher.TransactionType;


@RunWith(MockitoJUnitRunner.class)
public class RecipeDAOTest {

	@InjectMocks
	private RecipeDAO recipeDAO;

	@Mock
	private EntityManager em;

	@Mock
	private CriteriaQuery<Recipe> query;

	@Mock
	private Root<Recipe> root;

	@Mock
	private TypedQuery<Recipe> typedQuery;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testCorrectTransactionLevelForSaveNewRecipe() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("saveNewRecipe", TransactionType.INSERT));
	}


	@Test
	public void testCorrectTransactionLevelForQueryAllRecipes() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("queryAllRecipes", TransactionType.QUERY));
	}


	@Test
	public void testCorrectTransactionLevelForQueryRecipeById() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("queryRecipeById", TransactionType.QUERY));
	}


	@Test
	public void testCorrectTransactionLevelForUpdateRecipeContents() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("updateRecipeContents", TransactionType.UPDATE));
	}


	@Test
	public void testEntityManagerCalledWithTheSameObjectOnSave() {
		final Recipe recipe = new Recipe();
		this.recipeDAO.saveNewRecipe(recipe);
		verify(this.em).persist(recipe);
	}


	@Test
	public void testQuerySetupForQueryAllRecipes() {

		final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
		when(this.em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(Recipe.class)).thenReturn(this.query);
		when(this.query.from(Recipe.class)).thenReturn(this.root);
		when(this.em.createQuery(this.query)).thenReturn(this.typedQuery);

		this.recipeDAO.queryAllRecipes();

		verify(this.query).select(this.root);
		verify(this.query).from(Recipe.class);
		verifyNoMoreInteractions(this.query);

		verify(this.em).createQuery(this.query);
		verify(this.typedQuery).getResultList();
	}


	@Test
	public void testQueryRecipeByIDUsesFindByPKOnEntityManager() {
		final Long recipeId = RandomUtils.nextLong();

		this.recipeDAO.queryRecipeById(recipeId);

		verify(this.em).find(Recipe.class, recipeId);
		verifyNoMoreInteractions(this.em);
	}


	@Test
	public void testEntityManagerCallFindCorrectly() {
		final Long id = Long.valueOf(RandomUtils.nextLong());

		this.recipeDAO.queryRecipeById(id);
		verify(this.em).find(Recipe.class, id);
	}


	@Test(expected = DaoException.class)
	public void exceptionOnSaveWhenIdAlreadyExists() throws Exception {
		final Recipe testRecipe = new Recipe("testName", "testContent");
		injectRandomRecipeId(testRecipe);

		this.recipeDAO.saveNewRecipe(testRecipe);
	}


	private void injectRandomRecipeId(final Recipe testRecipe) throws Exception {
		ReflectionUtils.setVariableValueInObject(testRecipe, "recipeId", RandomUtils.nextLong());
	}


	@Test
	public void saveIsNotAttemptedIfRecipeIdAlreadyExists() throws Exception {
		final Recipe testRecipe = new Recipe("testName", "testContent");
		injectRandomRecipeId(testRecipe);

		callSaveWhileSwallowingAnyExceptions(testRecipe);

		verifyZeroInteractions(this.em);
	}


	private void callSaveWhileSwallowingAnyExceptions(final Recipe testRecipe) {
		try {
			this.recipeDAO.saveNewRecipe(testRecipe);
		} catch (final DaoException e) {
			// do nothing - just swallow the exception
		}
	}


	@Test
	public void testUpdateRecipeContents() {
		final Recipe mockRecipe = mock(Recipe.class);
		final long mockId = RandomUtils.nextLong();
		when(this.em.find(Recipe.class, mockId)).thenReturn(mockRecipe);

		this.recipeDAO.updateRecipeContents(mockId, "newContent");

		final InOrder inOrder = Mockito.inOrder(this.em, mockRecipe);
		inOrder.verify(this.em).find(Recipe.class, mockId);
		inOrder.verify(this.em).persist(mockRecipe);
		inOrder.verify(mockRecipe).setRecipeContent("newContent");

		verifyNoMoreInteractions(this.em, mockRecipe);
	}
}
