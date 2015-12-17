package com.poorknight.domain;

import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.validator.constraints.NotEmpty;

import com.poorknight.exceptions.DaoException;

/**
 * Responsible for CRUD operations on the Recipe class.
 */
@RequestScoped
public class RecipeDAO {

	@PersistenceContext(unitName = "JPADB")
	EntityManager em;

	@TransactionAttribute(REQUIRED)
	public void saveNewRecipe(final Recipe recipe) {
		throwExceptionIfGeneratedKeyAlreadyExists(recipe);
		recipe.setSearchableRecipeText(generateSearchableRecipe(recipe.getRecipeName(), recipe.getRecipeContent()));
		this.em.persist(recipe);
	}

	private String generateSearchableRecipe(final String name, final String content) {
		return name.toLowerCase() + content.toLowerCase();
	}

	private void throwExceptionIfGeneratedKeyAlreadyExists(final Recipe recipe) {
		if (recipe.getRecipeId() != null) {
			throw new DaoException("Cannot save a Recipe that already has a recipeId.  Maybe an update operation is more appropriate.");
		}
	}

	@TransactionAttribute(SUPPORTS)
	public List<Recipe> queryAllRecipes() {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		final Root<Recipe> recipe = query.from(Recipe.class);
		query.select(recipe);
		return this.em.createQuery(query).getResultList();
	}

	@TransactionAttribute(SUPPORTS)
	public Recipe queryRecipeById(final Long recipeId) {
		return this.em.find(Recipe.class, recipeId);
	}

	@TransactionAttribute(REQUIRED)
	public Recipe updateRecipeContents(final Long idOfRecipeToChange, final String newContents) {

		final Recipe recipe = queryRecipeById(idOfRecipeToChange);
		this.em.persist(recipe);
		recipe.setRecipeContent(newContents);
		recipe.setSearchableRecipeText(generateSearchableRecipe(recipe.getRecipeName(), newContents));
		return recipe;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteRecipe(final Long recipeId) {
		em.remove(em.find(Recipe.class, recipeId));
	}

	/**
	 * @deprecated use queryRecipeById() instead
	 */
	@TransactionAttribute(REQUIRED)
	@Deprecated
	public Recipe queryRecipeByIdWithBadPerformance(final Long recipeId) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		final Root<Recipe> recipe = query.from(Recipe.class);
		query.select(recipe);
		query.where(cb.equal(recipe.get("recipeId"), recipeId));
		return this.em.createQuery(query).getSingleResult();
	}

	@TransactionAttribute(SUPPORTS)
	public List<Recipe> findRecipesContainingAnyOf(@NotEmpty final String... searchStrings) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		final Root<Recipe> recipe = query.from(Recipe.class);

		query.select(recipe);
		query.where(anyOfTheSearchStringsMatch(cb, recipe, searchStrings));
		return this.em.createQuery(query).getResultList();
	}

	private Predicate anyOfTheSearchStringsMatch(final CriteriaBuilder cb, final Root<Recipe> recipe, final String[] searchStrings) {
		final Predicate[] likeExpressions = buildLikeExpressions(cb, recipe, searchStrings);
		return cb.or(likeExpressions); // 'or's together all of the like
										// expressions
	}

	private Predicate[] buildLikeExpressions(final CriteriaBuilder cb, final Root<Recipe> recipe, final String[] searchStrings) {
		final Predicate[] likeExpressions = new Predicate[searchStrings.length];
		for (int i = 0; i < searchStrings.length; i++) {
			likeExpressions[i] = likeExpression(cb, recipe, searchStrings[i]);
		}
		return likeExpressions;
	}

	private Predicate likeExpression(final CriteriaBuilder cb, final Root<Recipe> recipe, final String searchString) {
		return cb.like(recipe.get(Recipe_.searchableRecipeText), createLikeString(searchString));
	}

	private String createLikeString(final String searchString) {
		return "%" + searchString.toLowerCase() + "%";
	}
}
