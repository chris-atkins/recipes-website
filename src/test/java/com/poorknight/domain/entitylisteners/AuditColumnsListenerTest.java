package com.poorknight.domain.entitylisteners;

import static com.poorknight.testing.matchers.CustomMatchers.hasPreSaveEntityMethod;
import static com.poorknight.testing.matchers.CustomMatchers.hasPreUpdateEntityMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

import lombok.Data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.poorknight.testing.matchers.utils.testclasses.AlternateEntityTestClass;
import com.poorknight.testing.matchers.utils.testclasses.AuditClassWithoutAnnotation;
import com.poorknight.testing.matchers.utils.testclasses.ProperEntityTestClass;
import com.poorknight.utils.ReflectionUtils;
import com.poorknight.utils.TimestampGenerator;


@RunWith(MockitoJUnitRunner.class)
public class AuditColumnsListenerTest {

	private static final String METHOD_NAME = "addAuditFieldValues";
	private static final String USER_NAME = AuditColumnsListener.DEFAULT_USER;
	private static final Timestamp CURRENT_TIMESTAMP = new Timestamp(RandomUtils.nextInt(1000));

	@InjectMocks
	private AuditColumnsListener listener;

	@Mock
	private TimestampGenerator timestampGenerator;


	@Before
	public void initMocks() {
		Mockito.when(this.timestampGenerator.currentTimestamp()).thenReturn(CURRENT_TIMESTAMP);
	}


	@Test
	public void methodAnnotatedWithPrePersist() throws Exception {
		assertThat(AuditColumnsListener.class, hasPreSaveEntityMethod(METHOD_NAME));
	}


	@Test
	public void methodAnnotatedWithPreUpdate() throws Exception {
		assertThat(AuditColumnsListener.class, hasPreUpdateEntityMethod(METHOD_NAME));
	}


	@Test
	public void listenerRegisteredInPersistenceXml() throws Exception {
		final String persistenceContent = getMetaInfResourceFile("persistence.xml");
		assertThat(persistenceContent, containsString("<mapping-file>META-INF/listeners.xml</mapping-file>"));

		final String listenersContent = getMetaInfResourceFile("listeners.xml");
		assertThat(listenersContent, containsString(buildExpectedListenerContentString()));
	}


	private String getMetaInfResourceFile(final String fileName) throws IOException {
		final File file = FileUtils.getFile("src", "main", "resources", "META-INF", fileName);
		final String contents = new String(Files.readAllBytes(Paths.get(file.toURI())));
		assertThat(contents, notNullValue());
		return contents;
	}


	private String buildExpectedListenerContentString() {
		final String expected = "<entity-listener class=\"PUTPACKAGEHERE.AuditColumnsListener\"";
		return expected.replace("PUTPACKAGEHERE", findClassPathString());

	}


	private String findClassPathString() {
		return AuditColumnsListener.class.getPackage().getName();
	}


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void insertsCreatedByIfItIsNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();  // all fields are null
		long startTime;
		long endTime;
		startTime = System.nanoTime();

		this.listener.addAuditFieldValues(testEntity);

		endTime = System.nanoTime();
		System.out.println("Total Milli time: " + ((endTime - startTime) / 1000000.0));
		System.out.println("Total Nano time: " + (endTime - startTime));

		assertThat(testEntity.getCreatedBy(), equalTo(USER_NAME));
	}


	@Test
	public void doesNotInsertCreatedByIfItIsNotNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();
		ReflectionUtils.setFieldInClass(testEntity, "createdBy", "alteredValue");

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedBy(), equalTo("alteredValue"));
	}


	@Test
	public void insertsCreatedByWithNonDefaultFieldName() {
		final AlternateEntityTestClass testEntity = new AlternateEntityTestClass();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getAlternateCreatedBy(), equalTo(USER_NAME));
	}


	@Test
	public void doesNotInsertCreatedByIfNoAuditColumnsAnnotation() {
		final AuditClassWithoutAnnotation testEntity = new AuditClassWithoutAnnotation();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedBy(), nullValue());
	}


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void insertsCreatedOnIfItIsNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();  // all fields are null
		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedOn(), equalTo(CURRENT_TIMESTAMP));
	}


	@Test
	public void doesNotInsertCreatedOnIfItIsNotNull() {
		final Timestamp alteredTimestamp = new Timestamp(555);
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();
		ReflectionUtils.setFieldInClass(testEntity, "createdOn", alteredTimestamp);

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedOn(), equalTo(alteredTimestamp));
	}


	@Test
	public void insertsCreatedOnWithNonDefaultFieldName() {
		final AlternateEntityTestClass testEntity = new AlternateEntityTestClass();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getAlternateCreatedOn(), equalTo(CURRENT_TIMESTAMP));
	}


	@Test
	public void doesNotInsertCreatedOnIfNoAuditColumnsAnnotation() {
		final AuditClassWithoutAnnotation testEntity = new AuditClassWithoutAnnotation();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedOn(), nullValue());
	}


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void insertsLastUpdatedByIfItIsNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();  // all fields are null
		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedBy(), equalTo(USER_NAME));
	}


	@Test
	public void insertsLastUpdatedByIfItIsNotNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();
		ReflectionUtils.setFieldInClass(testEntity, "lastUpdatedBy", "alteredUserName");

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedBy(), equalTo(USER_NAME));
	}


	@Test
	public void insertsLastUpdatedByWithNonDefaultFieldName() {
		final AlternateEntityTestClass testEntity = new AlternateEntityTestClass();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getAlternateLastUpdatedBy(), equalTo(USER_NAME));
	}


	@Test
	public void doesNotInsertLastUpdatedByIfNoAuditColumnsAnnotation() {
		final AuditClassWithoutAnnotation testEntity = new AuditClassWithoutAnnotation();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedBy(), nullValue());
	}


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void insertsLastUpdatedOnIfItIsNull() {
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();  // all fields are null
		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedOn(), equalTo(CURRENT_TIMESTAMP));
	}


	@Test
	public void insertsLastUpdatedOnIfItIsNotNull() {
		final Timestamp alteredTimestamp = new Timestamp(1001);
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();
		ReflectionUtils.setFieldInClass(testEntity, "lastUpdatedOn", alteredTimestamp);

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedOn(), equalTo(CURRENT_TIMESTAMP));
	}


	@Test
	public void insertsLastUpdatedOnWithNonDefaultFieldName() {
		final AlternateEntityTestClass testEntity = new AlternateEntityTestClass();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getAlternateLastUpdatedOn(), equalTo(CURRENT_TIMESTAMP));
	}


	@Test
	public void doesNotInsertLastUpdatedOnIfNoAuditColumnsAnnotation() {
		final AuditClassWithoutAnnotation testEntity = new AuditClassWithoutAnnotation();

		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getLastUpdatedOn(), nullValue());
	}


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void createdOnAndLastUpdatedOnHaveSameValue() {
		Mockito.when(this.timestampGenerator.currentTimestamp()).thenReturn(CURRENT_TIMESTAMP, new Timestamp(1001));
		final ProperEntityTestClass testEntity = new ProperEntityTestClass();
		this.listener.addAuditFieldValues(testEntity);
		assertThat(testEntity.getCreatedOn(), equalTo(testEntity.getLastUpdatedOn()));
	}

}


@Data
class NonAnnotatedClass {

	private Timestamp createdOn;
	private String createdBy;
	private Timestamp lastUpdatedOn;
	private String lastUpdatedBy;
}