package com.poorknight.testing.matchers.utils.testclasses;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.Data;

import com.poorknight.domain.annotations.AuditColumns;


public class AuditTestClasses {

	@Data
	@AuditColumns
	public static class MissingCreatedOnColumn {

		private String createdBy;
		private String lastUpdatedOn;
		private String lastUpdatedBy;
	}

	@Data
	@AuditColumns
	public static class MissingCreatedByColumn {

		private String createdOn;
		private String lastUpdatedOn;
		private String lastUpdatedBy;
	}

	@Data
	@AuditColumns
	public static class MissingLastUpdatedOnColumn {

		private String createdOn;
		private String createdBy;
		private String lastUpdatedBy;
	}

	@Data
	@AuditColumns
	public static class MissingLastUpdatedByColumn {

		private String createdOn;
		private String createdBy;
		private String lastUpdatedOn;
	}

	@AuditColumns
	public class CorrectlyImplemented {

		private String createdOn;
		private String createdBy;

		@Version
		private String lastUpdatedOn;
		private String lastUpdatedBy;


		public String getCreatedOn() {
			return this.createdOn;
		}


		public String getCreatedBy() {
			return this.createdBy;
		}


		public String getLastUpdatedOn() {
			return this.lastUpdatedOn;
		}


		public String getLastUpdatedBy() {
			return this.lastUpdatedBy;
		}
	}

	@AuditColumns
	public class ForMockTests {

		private String createdOn;
		private String createdBy;

		@Version
		private String lastUpdatedOn;
		private String lastUpdatedBy;


		public String getCreatedOn() {
			return this.createdOn;
		}


		public String getCreatedBy() {
			return this.createdBy;
		}


		public String getLastUpdatedOn() {
			return this.lastUpdatedOn;
		}


		public String getLastUpdatedBy() {
			return this.lastUpdatedBy;
		}
	}

	@AuditColumns
	public class NoVersionAnnotationOnLastUpdate {

		private String createdOn;
		private String createdBy;
		private String lastUpdatedOn;
		private String lastUpdatedBy;


		public String getCreatedOn() {
			return this.createdOn;
		}


		public String getCreatedBy() {
			return this.createdBy;
		}


		public String getLastUpdatedOn() {
			return this.lastUpdatedOn;
		}


		public String getLastUpdatedBy() {
			return this.lastUpdatedBy;
		}
	}

	@AuditColumns
	public class FieldLevelTestClass {

		// ////////////////////////////////////////////////////
		@Column
		private int intFieldWithGetterAndColumn;


		public int getIntFieldWithGetterAndColumn() {
			return -1;
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////

		@Column
		private String noGetter;

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////

		@Column
		private String withSetter;


		public String getWithSetter() {
			return null;
		}


		public void setWithSetter(final String s) {
			// empty on purpose
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////
		private String noColumnAnnotation;


		public String getNoColumnAnnotation() {
			return null;
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////
		@Column
		private Timestamp missingTemporal;


		public Timestamp getMissingTemporal() {
			return null;
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////
		@Column
		@Temporal(TemporalType.DATE)
		private Timestamp wrongTemporalType;


		public Timestamp getWrongTemporalType() {
			return null;
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////
		@Column
		@Temporal(TemporalType.TIMESTAMP)
		private Timestamp lastUpdatedOn;


		public Timestamp getLastUpdatedOn() {
			return null;
		}

		// ////////////////////////////////////////////////////

		// ////////////////////////////////////////////////////
		@Column
		private String lastUpdatedBy;


		public String getLastUpdatedBy() {
			return this.lastUpdatedBy;
		}
		// ////////////////////////////////////////////////////
	}
}
