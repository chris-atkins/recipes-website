package com.poorknight.domain.entitylisteners;

import static com.poorknight.utils.ReflectionUtils.classHasAnnotation;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.poorknight.domain.annotations.AuditColumns;
import com.poorknight.utils.ReflectionUtils;
import com.poorknight.utils.TimestampGenerator;


/**
 * Responsible for adding audit columns fields to an entity before it is saved to the database. It will only add the createdOn/By fields the first
 * time an entity is saved. This is active for every entity object.
 *
 * TO STOP THIS LISTENER FROM BEING CALLED ON A PARTICULAR ENTITY TYPE, ANNOTATE THAT CLASS WITH @ExcludeDefaultListeners (or don't use the @AuditColumns
 * attribute)
 */
public class AuditColumnsListener {

	static final String DEFAULT_USER = "Anonymous";

	// can't use @Inject in an EntityListener (until jboss fixes this... this note written on v8.1.0.Final
	private TimestampGenerator timestampGenerator = null;


	public AuditColumnsListener() {
		this.timestampGenerator = new TimestampGenerator();
	}


	@PrePersist
	@PreUpdate
	public void addAuditFieldValues(final Object entity) {

		if (doesNotHaveAuditColumns(entity)) {
			return;
		}

		processAuditFields(entity);
	}


	private boolean doesNotHaveAuditColumns(final Object entity) {
		return !(classHasAnnotation(entity.getClass(), AuditColumns.class));
	}


	private void processAuditFields(final Object entity) {

		final Timestamp currentTimestamp = getCurrentTimestamp();
		final String currentUser = getCurrentUser();

		addValuesToAuditColumns(entity, currentTimestamp, currentUser);
	}


	private Timestamp getCurrentTimestamp() {
		return this.timestampGenerator.currentTimestamp();
	}


	private String getCurrentUser() {
		return DEFAULT_USER;
	}


	private void addValuesToAuditColumns(final Object entity, final Timestamp currentTimestamp, final String currentUser) {
		processCreatedByField(entity, currentUser);
		processCreatedOnField(entity, currentTimestamp);
		processLastUpdatedByField(entity, currentUser);
		processLastUpdatedOnField(entity, currentTimestamp);
	}


	private void processCreatedByField(final Object entity, final String currentUser) {
		final Field createdByField = findCreatedByField(entity);

		if (fieldIsNull(entity, createdByField)) {
			ReflectionUtils.setFieldInClass(entity, createdByField, currentUser);
		}
	}


	private void processCreatedOnField(final Object entity, final Timestamp currentTimestamp) {
		final Field createdOnField = findCreatedOnField(entity);

		if (fieldIsNull(entity, createdOnField)) {
			ReflectionUtils.setFieldInClass(entity, createdOnField, currentTimestamp);
		}
	}


	private void processLastUpdatedByField(final Object entity, final String currentUser) {
		final Field lastUpdatedByField = findLastUpdatedByField(entity);
		ReflectionUtils.setFieldInClass(entity, lastUpdatedByField, currentUser);
	}


	private void processLastUpdatedOnField(final Object entity, final Timestamp currentTimestamp) {
		final Field lastUpdatedOnField = findLastUpdatedOnField(entity);
		ReflectionUtils.setFieldInClass(entity, lastUpdatedOnField, currentTimestamp);
	}


	private Field findCreatedByField(final Object entity) {
		final AuditColumns auditColumns = findAuditColumns(entity);
		return ReflectionUtils.findFieldInClass(entity.getClass(), auditColumns.createdBy());
	}


	private Field findCreatedOnField(final Object entity) {
		final AuditColumns auditColumns = findAuditColumns(entity);
		return ReflectionUtils.findFieldInClass(entity.getClass(), auditColumns.createdOn());
	}


	private Field findLastUpdatedByField(final Object entity) {
		final AuditColumns auditColumns = findAuditColumns(entity);
		return ReflectionUtils.findFieldInClass(entity.getClass(), auditColumns.lastUpdatedBy());
	}


	private Field findLastUpdatedOnField(final Object entity) {
		final AuditColumns auditColumns = findAuditColumns(entity);
		return ReflectionUtils.findFieldInClass(entity.getClass(), auditColumns.lastUpdatedOn());
	}


	private AuditColumns findAuditColumns(final Object entity) {
		return entity.getClass().getAnnotation(AuditColumns.class);
	}


	private boolean fieldIsNull(final Object entity, final Field field) {
		return ReflectionUtils.getFieldFromObject(entity, field) == null;
	}
}