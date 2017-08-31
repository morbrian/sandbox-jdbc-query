package morbrian.sandbox.jdbcquery.database;

import morbrian.sandbox.jdbcquery.model.NamesAndRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hikethru08 on 11/15/15.
 */
@Stateless public class Database {

  @Resource(mappedName = "java:comp/env/DocumentDS") DataSource dataSource;

  @Resource(mappedName = "java:comp/env/ObjectDS") DataSource objectSource;

  Connection connection;

  @Inject private Logger logger;

  public Database() {
  }

  public Database(Connection connection) {
    logger = LoggerFactory.getLogger(Database.class);
    this.connection = connection;
  }

  public NamesAndRecords fetchData(String query) {
    if (objectSource != null) {
      try {
        fetchData(query, objectSource.getConnection());
      } catch(SQLException exc) {
        logger.error("failed to get object connection", exc);
      }
    } else {
      logger.error("Object connection is null");
    }
    try {
      return fetchData(query, getConnection());
    } catch(SQLException exc) {
      logger.error("failed to get data connection", exc);
      return null;
    }
  }

  private NamesAndRecords fetchData(String query, Connection conn) {
    // TODO: make this method take a closure so we can specify an operation to perform
    // TODO: explore fetch size performance
    // for a certain batch size of records.
    List<String> fieldNames = new ArrayList<>();
    List<List<Object>> records = new ArrayList<>();

    PreparedStatement stmt = null;
    ResultSet resultSet = null;
    ResultSetMetaData metaData = null;

    try {
      stmt = conn.prepareStatement(query);
      resultSet = stmt.executeQuery();

      metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      // remember that MetaData columns are indexed from 1

      for (int i = 1; i <= columnCount; i++) {
        fieldNames.add(metaData.getColumnName(i));
        logger.info(
            "Col " + metaData.getColumnName(i) + " MetaData: " + metaData.getColumnTypeName(i));
      }

      while (resultSet.next()) {
        List<Object> record = new ArrayList<>();
        // remember that MetaData columns are indexed from 1
        for (int i = 1; i <= columnCount; i++) {
          // TODO: handle types appropriately (probably don't want SQL types)
          // TODO: easiest way is probably to use template from OpenCSV to get case statement with all SQL types and handle
          Object obj = resultSet.getObject(i);
          logger.info("Col " + metaData.getColumnName(i) + " Java Type: " + (obj == null ?
              "null" :
              obj.getClass().getName()));
          record.add(obj);
        }
        records.add(record);
      }

    } catch (SQLException exc) {
      logger.error("", exc);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException exc2) {
        }
      }
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException exc2) {
        }
      }
    }

    return new NamesAndRecords(fieldNames, records);

  }

  private Connection getConnection() throws SQLException {
    return (dataSource != null) ? dataSource.getConnection() : connection;
  }


}
