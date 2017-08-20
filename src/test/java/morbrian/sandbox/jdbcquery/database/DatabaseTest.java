package morbrian.sandbox.jdbcquery.database;

import morbrian.sandbox.jdbcquery.model.NamesAndRecords;
import morbrian.sandbox.jdbctest.DatabaseTestingUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DatabaseTest {

  private static final String SAMPLE_TABLENAME = "sample";
  private static final String COMPANY_TABLENAME = "company";
  private static final int COMPANY_COUNT = 100;

  private Logger logger = LoggerFactory.getLogger(DatabaseTest.class);
  private DatabaseTestingUtility dbTestUtil =
      new DatabaseTestingUtility(new File("src/test/sql"));

  @Before public void setUp() throws Exception {
    try {
      dbTestUtil.createTable(SAMPLE_TABLENAME);
      dbTestUtil.loadDataForTable(SAMPLE_TABLENAME);
      dbTestUtil.createTable(COMPANY_TABLENAME);
    } catch (Exception exc) {
      fail("Unable to perform initial setup: " + exc.getMessage());
    }
  }

  @After public void tearDown() throws Exception {
    dbTestUtil.dropTable(SAMPLE_TABLENAME);
    dbTestUtil.dropTable(COMPANY_TABLENAME);
  }

  @Test public void testFetchData() throws Exception {
    // Creating a new database and connection here because fetchData() closes the connection
    // If that happens, @After will never successfully drop the tablename.
    Database database = new Database(dbTestUtil.getConnection());

    NamesAndRecords results = database.fetchData("SELECT * from " + SAMPLE_TABLENAME);
    List<String> headers = results.getFieldNames();
    assertEquals(4, headers.size());
    assertEquals("col1", headers.get(0).toLowerCase());
    assertEquals("col2", headers.get(1).toLowerCase());
    assertEquals("col3", headers.get(2).toLowerCase());
    assertEquals("col4", headers.get(3).toLowerCase());

    List<List<Object>> records = results.getRecords();
    assertEquals(4, records.size());
  }

}
