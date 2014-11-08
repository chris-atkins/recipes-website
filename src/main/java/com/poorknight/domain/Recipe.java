package com.poorknight.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import com.poorknight.domain.annotations.AuditColumns;


/**
 * The high-level domain object, representing a recipe.
 */
@Data
@Entity
@AuditColumns
@Table(name = "C01_RECIPE")
public class Recipe implements Serializable {

	@Transient
	private static final long serialVersionUID = -5056112407610312836L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C01_RECIPE_ID")
	@Setter(value = AccessLevel.NONE)
	private Long recipeId;

	@Column(name = "C01_RECIPE_NAME")
	private String recipeName;

	@Column(name = "C01_RECIPE_CONTENT")
	private String recipeContent;

	@Setter(AccessLevel.NONE)
	@Column(name = "C01_CREATED_ON")
	private Timestamp createdOn;

	@Setter(AccessLevel.NONE)
	@Column(name = "C01_CREATED_BY")
	private String createdBy;

	@Version
	@Setter(AccessLevel.NONE)
	@Column(name = "C01_LAST_UPDATED_ON")
	private Timestamp lastUpdatedOn;

	@Setter(AccessLevel.NONE)
	@Column(name = "C01_LAST_UPDATED_BY")
	private String lastUpdatedBy;


	public Recipe(final String recipeName, final String recipeContent) {
		this.recipeName = recipeName;
		this.recipeContent = recipeContent;
	}


	public Recipe() {
		// empty on purpose
	}
}
