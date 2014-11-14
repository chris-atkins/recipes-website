package com.poorknight.testing.matchers.utils.testclasses;

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


@Entity
@Data
public class AuditClassWithoutAnnotation {

	@Transient
	private static final long serialVersionUID = 2123138070177295205L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.NONE)
	private Long primaryKey;

	@Setter(value = AccessLevel.NONE)
	@Column
	private Timestamp createdOn;

	@Setter(value = AccessLevel.NONE)
	@Column
	private String createdBy;

	@Version
	@Setter(value = AccessLevel.NONE)
	@Column
	private Timestamp lastUpdatedOn;

	@Setter(value = AccessLevel.NONE)
	@Column
	private String lastUpdatedBy;
}
