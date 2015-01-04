package com.poorknight.domain;

import static com.poorknight.testing.matchers.CustomMatchers.hasCorrectTransactionLevelOnMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.exceptions.DaoException;
import com.poorknight.testing.matchers.CustomMatchers;
import com.poorknight.testing.matchers.methods.MethodTransactionAnnotationMatcher.TransactionType;


@RunWith(MockitoJUnitRunner.class)
public class RecipeDAOTest {

	private static final String MIXED_CASE_RECIPE_NAME = "Test Recipe Name";
	private static final String MIXED_CASE_RECIPE_CONTENT = "Test Contents";
	private static final String EXPECTED_SEARCHABLE_TEXT = "test recipe nametest contents";
	private static final String NEW_CONTENT = MIXED_CASE_RECIPE_CONTENT;

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

	@Captor
	private ArgumentCaptor<Recipe> recipeCaptor;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void requestScoped() {
		assertThat(RecipeDAO.class, CustomMatchers.isRequestScoped());
	}


	@Test
	public void correctTransactionLevel_ForSaveNewRecipe() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("saveNewRecipe", TransactionType.INSERT));
	}


	@Test
	public void correctTransactionLevel_ForQueryAllRecipes() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("queryAllRecipes", TransactionType.QUERY));
	}


	@Test
	public void correctTransactionLevel_ForQueryRecipeById() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("queryRecipeById", TransactionType.QUERY));
	}


	@Test
	public void correctTransactionLevel_ForUpdateRecipeContents() {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("updateRecipeContents", TransactionType.UPDATE));
	}


	@Test
	public void correctTransactionLevel_ForFindRecipesContainingAnyOf() throws Exception {
		assertThat(RecipeDAO.class, hasCorrectTransactionLevelOnMethod("findRecipesContainingAnyOf", TransactionType.QUERY));
	}


	@Test
	public void entityManagerCalledWithTheSameObjectOnSave() {
		final Recipe recipe = new Recipe("test", "recipe");
		this.recipeDAO.saveNewRecipe(recipe);
		verify(this.em).persist(recipe);
	}


	@Test
	public void testQuerySetup_ForQueryAllRecipes() {

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
	public void queryRecipeByID_UsesFindByPKOnEntityManager() {
		final Long recipeId = RandomUtils.nextLong();

		this.recipeDAO.queryRecipeById(recipeId);

		verify(this.em).find(Recipe.class, recipeId);
		verifyNoMoreInteractions(this.em);
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
	public void saveIsNotAttempted_IfRecipeIdAlreadyExists() throws Exception {
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
		final Recipe mockRecipe = setUpMockRecipe();
		final long mockId = RandomUtils.nextLong();
		when(this.em.find(Recipe.class, mockId)).thenReturn(mockRecipe);

		this.recipeDAO.updateRecipeContents(mockId, NEW_CONTENT);

		final InOrder inOrder = Mockito.inOrder(this.em, mockRecipe);
		inOrder.verify(this.em).find(Recipe.class, mockId);
		inOrder.verify(this.em).persist(mockRecipe);
		inOrder.verify(mockRecipe).setRecipeContent(NEW_CONTENT);
		inOrder.verify(mockRecipe).getRecipeName();
		inOrder.verify(mockRecipe).setSearchableRecipeText(EXPECTED_SEARCHABLE_TEXT);

		verifyNoMoreInteractions(this.em, mockRecipe);
	}


	private Recipe setUpMockRecipe() {
		final Recipe mock = mock(Recipe.class);
		when(mock.getRecipeName()).thenReturn(MIXED_CASE_RECIPE_NAME);
		when(mock.getRecipeContent()).thenReturn(MIXED_CASE_RECIPE_CONTENT);
		return mock;
	}


	@Test
	public void saveSetsTheSearchableTextFieldCorrectly() throws Exception {
		final Recipe testRecipe = new Recipe(MIXED_CASE_RECIPE_NAME, MIXED_CASE_RECIPE_CONTENT);

		this.recipeDAO.saveNewRecipe(testRecipe);

		verify(this.em).persist(this.recipeCaptor.capture());
		assertThat(this.recipeCaptor.getValue().getSearchableRecipeText(), equalTo(EXPECTED_SEARCHABLE_TEXT));
	}


	@Test
	public void updateUpdatesTheSearchableTextFieldCorrectly() throws Exception {
		final Long recipeId = RandomUtils.nextLong();
		final Recipe testRecipe = new Recipe(MIXED_CASE_RECIPE_NAME, "to be overwritten");
		when(this.em.find(Recipe.class, recipeId)).thenReturn(testRecipe);

		final Recipe result = this.recipeDAO.updateRecipeContents(recipeId, MIXED_CASE_RECIPE_CONTENT);

		assertThat(result.getSearchableRecipeText(), equalTo(EXPECTED_SEARCHABLE_TEXT));
	}
}
