package com.example;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@OpenAPIDefinition(info = @Info(title = "measurement-app", version = "0.0"))
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @Singleton
    static class DatabaseConnectionTester implements ApplicationEventListener<StartupEvent> {

        @Inject
        private DataSource dataSource;

        @Override
        public void onApplicationEvent(StartupEvent event) {
            // Add small delay to ensure datasource is fully initialized
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            testDatabaseConnection();
            testMeasurementsTable();
        }

        @Connectable
        protected void testDatabaseConnection() {
            LOG.info("=== Testing Database Connection ===");

            try (Connection connection = dataSource.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    DatabaseMetaData metaData = connection.getMetaData();

                    LOG.info("✅ Database connection successful!");
                    LOG.info("📊 Database: {} {}",
                            metaData.getDatabaseProductName(),
                            metaData.getDatabaseProductVersion());
                    LOG.info("🔗 URL: {}", metaData.getURL());
                    LOG.info("👤 User: {}", metaData.getUserName());
                    LOG.info("🚀 Driver: {} {}",
                            metaData.getDriverName(),
                            metaData.getDriverVersion());

                    // Test query execution
                    testBasicQuery(connection);

                } else {
                    LOG.error("❌ Database connection is null or closed");
                }

            } catch (SQLException e) {
                LOG.error("❌ Database connection failed: {}", e.getMessage());
                LOG.error("💡 Make sure MySQL server is running on localhost:3306");
                LOG.error("💡 Check database credentials in application.yml");
                LOG.error("💡 Verify database 'measurements_db' exists");
            }
        }

        private void testBasicQuery(Connection connection) {
            try {
                String testQuery = "SELECT 1 as test_value, NOW() as current_time";
                try (PreparedStatement stmt = connection.prepareStatement(testQuery);
                        ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        LOG.info("🔍 Test query successful: value={}, time={}",
                                rs.getInt("test_value"),
                                rs.getTimestamp("current_time"));
                    }
                }
            } catch (SQLException e) {
                LOG.error("❌ Test query failed: {}", e.getMessage());
            }
        }

        @Connectable
        protected void testMeasurementsTable() {
            LOG.info("=== Testing Measurements Table ===");

            try (Connection connection = dataSource.getConnection()) {

                // Check if measurements table exists
                if (tableExists(connection, "measurements")) {
                    LOG.info("✅ 'measurements' table exists");

                    // Test table structure
                    testTableStructure(connection);

                    // Count existing records
                    countRecords(connection);

                } else {
                    LOG.warn("⚠️  'measurements' table does not exist");
                    LOG.info("💡 Create the table using:");
                    LOG.info("   CREATE TABLE measurements (");
                    LOG.info("     id BIGINT AUTO_INCREMENT PRIMARY KEY,");
                    LOG.info("     uuid VARCHAR(36) UNIQUE NOT NULL,");
                    LOG.info("     patient_id BIGINT NOT NULL,");
                    LOG.info("     result DECIMAL(5,2) NOT NULL,");
                    LOG.info("     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
                    LOG.info("     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,");
                    LOG.info("     deleted BOOLEAN DEFAULT FALSE");
                    LOG.info("   );");
                }

            } catch (SQLException e) {
                LOG.error("❌ Table verification failed: {}", e.getMessage());
            }
        }

        private boolean tableExists(Connection connection, String tableName) throws SQLException {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, new String[] { "TABLE" })) {
                return tables.next();
            }
        }

        private void testTableStructure(Connection connection) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet columns = metaData.getColumns(null, null, "measurements", null)) {

                    LOG.info("📋 Table structure:");
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        String columnType = columns.getString("TYPE_NAME");
                        int columnSize = columns.getInt("COLUMN_SIZE");
                        String nullable = columns.getString("IS_NULLABLE");

                        LOG.info("   📝 {}: {} ({}) - Nullable: {}",
                                columnName, columnType, columnSize, nullable);
                    }
                }

                // Check for required indexes
                checkIndexes(connection);

            } catch (SQLException e) {
                LOG.error("❌ Table structure check failed: {}", e.getMessage());
            }
        }

        private void checkIndexes(Connection connection) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet indexes = metaData.getIndexInfo(null, null, "measurements", false, false)) {

                    LOG.info("🔍 Indexes:");
                    while (indexes.next()) {
                        String indexName = indexes.getString("INDEX_NAME");
                        String columnName = indexes.getString("COLUMN_NAME");
                        boolean unique = !indexes.getBoolean("NON_UNIQUE");

                        if (indexName != null && !indexName.equals("PRIMARY")) {
                            LOG.info("   📇 {}: {} (unique: {})", indexName, columnName, unique);
                        }
                    }
                }
            } catch (SQLException e) {
                LOG.warn("⚠️  Index check failed: {}", e.getMessage());
            }
        }

        private void countRecords(Connection connection) {
            try {
                // Count total records
                String countQuery = "SELECT COUNT(*) as total, COUNT(CASE WHEN deleted = false THEN 1 END) as active FROM measurements";
                try (PreparedStatement stmt = connection.prepareStatement(countQuery);
                        ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        int total = rs.getInt("total");
                        int active = rs.getInt("active");
                        LOG.info("📊 Records: {} total, {} active, {} deleted",
                                total, active, (total - active));
                    }
                }

            } catch (SQLException e) {
                LOG.error("❌ Record count failed: {}", e.getMessage());
            }
        }
    }
}