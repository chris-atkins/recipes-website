package com.poorknight.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.poorknight.domain.Recipe;
import com.poorknight.exceptions.DaoException;


/**
 * Responsible for CRUD operations on the Recipe class.
 */
@Dependent
public class RecipeDAO {

	@PersistenceContext(unitName = "JPADB")
	EntityManager em;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveNewRecipe(final Recipe recipe) {
		throwExceptionIfGeneratedKeyAlreadyExists(recipe);
		this.em.persist(recipe);
	}


	private void throwExceptionIfGeneratedKeyAlreadyExists(final Recipe recipe) {
		if (recipe.getRecipeId() != null) {
			throw new DaoException("Cannot save a Recipe that already has a recipeId.  Maybe an update operation is more appropriate.");
		}
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Recipe> queryAllRecipes() {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		final Root<Recipe> recipe = query.from(Recipe.class);
		query.select(recipe);
		return this.em.createQuery(query).getResultList();
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Recipe queryRecipeById(final Long recipeId) {
		return this.em.find(Recipe.class, recipeId);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Recipe updateRecipeContents(final Long idOfRecipeToChange, final String newContents) {

		final Recipe recipe = queryRecipeById(idOfRecipeToChange);
		this.em.persist(recipe);
		recipe.setRecipeContent(newContents);
		return recipe;
	}


	/**
	 * @deprecated use queryRecipeById() instead
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Deprecated
	public Recipe queryRecipeByIdWithBadPerformance(final Long recipeId) {
		final CriteriaBuilder cb = this.em.getCriteriaBuilder();
		final CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		final Root<Recipe> recipe = query.from(Recipe.class);
		query.select(recipe);
		query.where(cb.equal(recipe.get("recipeId"), recipeId));
		return this.em.createQuery(query).getSingleResult();
	}
}
