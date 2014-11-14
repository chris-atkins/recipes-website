package com.poorknight.testing.matchers.utils.testclasses;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import com.poorknight.domain.annotations.AuditColumns;


@Entity
@Data
@AuditColumns(createdBy = "alternateCreatedBy", createdOn = "alternateCreatedOn", lastUpdatedBy = "alternateLastUpdatedBy", lastUpdatedOn = "alternateLastUpdatedOn")
public class AlternateEntityTestClass implements Serializable {

	@Transient
	private static final long serialVersionUID = 2123138070177295205L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private Long primaryKey;

	@Setter(value = AccessLevel.NONE)
	@Column
	private Timestamp alternateCreatedOn;

	@Setter(value = AccessLevel.NONE)
	@Column
	private String alternateCreatedBy;

	@Version
	@Setter(value = AccessLevel.NONE)
	@Column
	private Timestamp alternateLastUpdatedOn;

	@Setter(value = AccessLevel.NONE)
	@Column
	private String alternateLastUpdatedBy;
}
