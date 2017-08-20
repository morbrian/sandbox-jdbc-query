package morbrian.sandbox.jdbctest;

import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseTestingUtility {
  private static final String DBUSER_PROPERTY = "j2eesandbox.jdbc.user";
  private static final String DBPASSWORD_PROPERTY = "j2eesandbox.jdbc.password";
  private static final String DBURL_PROPERTY = "j2eesandbox.jdbc.connectionUrl";

  private static Logger logger = LoggerFactory.getLogger(DatabaseTestingUtility.class);

  private TemporaryFolder tempFolder = new TemporaryFolder();
  private File dbFolder;
  private File sqlDataFolder;

  private Connection connection;
  private String connectionUrl;

  public DatabaseTestingUtility(File sqlDataFolder) {
    this.sqlDataFolder = sqlDataFolder;
  }

  private static Connection getConnection(String databaseUrl, String databaseUser,
      String databasePassword) throws SQLException {
    return DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
  }

  public String getDatabaseUser() {
    String user = getProperty(DBUSER_PROPERTY);
    return (user != null) ? user : "";
  }

  public String getDatabasePassword() {
    String password = getProperty(DBPASSWORD_PROPERTY);
    return (password != null) ? password : "";
  }

  public synchronized String getDatabaseUrl() {
    if (connectionUrl != null) {
      return connectionUrl;
    }
    String dbUrl = getProperty(DBURL_PROPERTY);
    if (dbUrl == null || dbUrl.isEmpty()) {
      try {
        tempFolder.create();
        dbFolder = tempFolder.newFolder();
        dbFolder.mkdirs();
        dbUrl = "jdbc:h2:" + dbFolder.getAbsolutePath();
        logger.debug("Using embedded database at " + dbUrl);
      } catch (IOException e) {
        e.printStackTrace();
        dbUrl = null;
      }
    }
    connectionUrl = dbUrl;
    return connectionUrl;
  }

  public synchronized Connection getConnection() throws SQLException {
    if (connection == null) {
      connection = DatabaseTestingUtility
          .getConnection(getDatabaseUrl(), getDatabaseUser(), getDatabasePassword());
    }
    return connection;
  }

  public synchronized void closeConnection() {
    if (connection == null) {
      return;
    }
    try {
      connection.close();
    } catch (Exception exc) {

    } finally {
      connection = null;
    }
  }

  public void createTable(String tablename) throws FileNotFoundException, SQLException {
    Connection connection = null;
    Statement stmt = null;
    try {
      connection = getConnection();
      stmt = connection.createStatement();
      stmt.execute("DROP TABLE IF EXISTS " + tablename);
      String schema = new Scanner(getSchema(tablename)).useDelimiter("\\Z").next();
      stmt.execute(schema);
    } catch (Exception exc) {
      throw exc;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (Exception exc2) {
        }
      }
    }
  }

  public void loadDataForTable(String tablename) throws FileNotFoundException, SQLException {
    Connection connection = null;
    Statement stmt = null;
    try {
      connection = getConnection();
      stmt = connection.createStatement();
      String data = new Scanner(getData(tablename)).useDelimiter("\\Z").next();
      stmt.execute(data);
    } catch (Exception exc) {
      throw exc;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (Exception exc2) {
        }
      }
    }
  }

  public void dropTable(String tablename) throws SQLException {
    Connection connection = null;
    Statement stmt = null;
    try {
      connection = getConnection();
      stmt = connection.createStatement();
      stmt.execute("DROP TABLE IF EXISTS " + tablename);
    } catch (Exception exc) {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (Exception exc2) {
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception exc2) {
        }
      }
      throw exc;
    }
  }

  private String getProperty(String propertyName) {
    String propertyValue = System.getProperty(propertyName);
    if (propertyValue != null && !propertyValue.isEmpty()) {
      logger.info("Using system property for " + propertyName);
    }
    return propertyValue;
  }


  private File getSchema(String tablename) {
    return new File(sqlDataFolder, "schema/" + tablename + ".sql");
  }

  private File getData(String tablename) {
    return new File(sqlDataFolder, tablename + ".sql");
  }

}
