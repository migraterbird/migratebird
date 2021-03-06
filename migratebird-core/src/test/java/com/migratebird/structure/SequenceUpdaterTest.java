/**
 * Copyright 2014 www.migratebird.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.migratebird.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.migratebird.database.Database;
import com.migratebird.database.Databases;
import com.migratebird.structure.sequence.SequenceUpdater;
import com.migratebird.structure.sequence.impl.DefaultSequenceUpdater;
import com.migratebird.util.SQLTestUtils;
import com.migratebird.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for the SequenceUpdater. Contains tests that can be implemented generally for all different database dialects.
 * Extended with implementations for each supported database dialect.
 * <p/>
 */
public class SequenceUpdaterTest {

    /* The logger instance for this class */
    private static Log logger = LogFactory.getLog(SequenceUpdaterTest.class);

    /* DataSource for the test database */
    private DataSource dataSource;

    /* Tested object */
    private SequenceUpdater sequenceUpdater;

    private Databases databases;
    private Database defaultDatabase;


    /**
     * Test fixture. Configures the implementation of the SequenceUpdater that matches the currenlty configured dialect.
     * Creates a test table and test sequence.
     */
    @Before
    public void setUp() {
        databases = TestUtils.getDatabases();
        defaultDatabase = databases.getDefaultDatabase();
        dataSource = defaultDatabase.getDataSource();
        sequenceUpdater = new DefaultSequenceUpdater(1000L, databases);

        cleanupTestDatabase();
        createTestDatabase();
    }


    /**
     * Clears the database, to avoid interference with other tests
     */
    @After
    public void tearDown() {
        cleanupTestDatabase();
    }


    /**
     * Tests the update sequences behavior
     */
    @Test
    public void testUpdateSequences() {
        if (!defaultDatabase.supportsSequences()) {
            logger.warn("Current dialect does not support sequences. Skipping test.");
            return;
        }
        assertCurrentSequenceValueBetween(0, 10);
        sequenceUpdater.updateSequences();
        assertCurrentSequenceValueBetween(1000, 1010);
    }


    /**
     * Verifies that if a sequence has a value already high enough, the value is not being set to a lower value
     */
    @Test
    public void testUpdateSequences_valueAlreadyHighEnough() {
        if (!defaultDatabase.supportsSequences()) {
            logger.warn("Current dialect does not support sequences. Skipping test.");
            return;
        }
        assertCurrentSequenceValueBetween(0, 10);
        sequenceUpdater.updateSequences();
        assertCurrentSequenceValueBetween(1000, 1010);
        sequenceUpdater.updateSequences();
        assertCurrentSequenceValueBetween(1000, 1010);
    }


    /**
     * Tests the update identity columns behavior
     */
    @Test
    public void testUpdateSequences_identityColumns() {
        if (!defaultDatabase.supportsIdentityColumns()) {
            logger.warn("Current dialect does not support identity columns. Skipping test.");
            return;
        }
        assertCurrentIdentityColumnValueBetween(0, 10);
        sequenceUpdater.updateSequences();
        assertCurrentIdentityColumnValueBetween(1000, 1010);
    }


    /**
     * Verifies that if a identity columns has a value already high enough, the value is not being set to a lower value
     */
    @Test
    public void testUpdateSequences_identityColumnsValueAlreadyHighEnough() {
        if (!defaultDatabase.supportsIdentityColumns()) {
            logger.warn("Current dialect does not support identity columns. Skipping test.");
            return;
        }
        assertCurrentIdentityColumnValueBetween(0, 10);
        sequenceUpdater.updateSequences();
        assertCurrentIdentityColumnValueBetween(1000, 1010);
        sequenceUpdater.updateSequences();
        assertCurrentIdentityColumnValueBetween(1000, 1010);
    }


    /**
     * Asserts that the current value for the test_sequence is between the given values
     *
     * @param minValue The minimum value (included)
     * @param maxValue The maximum value (included)
     */
    private void assertCurrentSequenceValueBetween(long minValue, long maxValue) {
        String correctCaseSequenceName = defaultDatabase.toCorrectCaseIdentifier("test_sequence");
        long currentValue = defaultDatabase.getSequenceValue(defaultDatabase.getDefaultSchemaName(), correctCaseSequenceName);
        assertTrue("Current sequence value is not between " + minValue + " and " + maxValue, (currentValue >= minValue && currentValue <= maxValue));
    }


    /**
     * Asserts that the current value for the identity column test_table.col1 is between the given values.
     *
     * @param minValue The minimum value (included)
     * @param maxValue The maximum value (included)
     */
    private void assertCurrentIdentityColumnValueBetween(long minValue, long maxValue) {
        SQLTestUtils.executeUpdate("delete from test_table1", dataSource);
        SQLTestUtils.executeUpdate("insert into test_table1(col2) values('test')", dataSource);
        long currentValue = SQLTestUtils.getItemAsLong("select col1 from test_table1 where col2 = 'test'", dataSource);
        assertTrue("Current sequence value is not between " + minValue + " and " + maxValue, (currentValue >= minValue && currentValue <= maxValue));
    }


    /**
     * Creates all test database structures (view, tables...)
     */
    private void createTestDatabase() {
        String dialect = defaultDatabase.getSupportedDatabaseDialect();
        if ("hsqldb".equals(dialect)) {
            createTestDatabaseHsqlDb();
        } else if ("mysql".equals(dialect)) {
            createTestDatabaseMySql();
        } else if ("oracle".equals(dialect)) {
            createTestDatabaseOracle();
        } else if ("postgresql".equals(dialect)) {
            createTestDatabasePostgreSql();
        } else if ("db2".equals(dialect)) {
            createTestDatabaseDb2();
        } else if ("derby".equals(dialect)) {
            createTestDatabaseDerby();
        } else if ("mssql".equals(dialect)) {
            createTestDatabaseMsSql();
        } else {
            fail("This test is not implemented for current dialect: " + dialect);
        }
    }


    /**
     * Drops all created test database structures (views, tables...)
     */
    private void cleanupTestDatabase() {
        String dialect = defaultDatabase.getSupportedDatabaseDialect();
        if ("hsqldb".equals(dialect)) {
            cleanupTestDatabaseHsqlDb();
        } else if ("mysql".equals(dialect)) {
            cleanupTestDatabaseMySql();
        } else if ("oracle".equals(dialect)) {
            cleanupTestDatabaseOracle();
        } else if ("postgresql".equals(dialect)) {
            cleanupTestDatabasePostgreSql();
        } else if ("db2".equals(dialect)) {
            cleanupTestDatabaseDb2();
        } else if ("derby".equals(dialect)) {
            cleanupTestDatabaseDerby();
        } else if ("mssql".equals(dialect)) {
            cleanupTestDatabaseMsSql();
        }
    }

    //
    // Database setup for HsqlDb
    //

    /**
     * Creates all test database structures
     */
    private void createTestDatabaseHsqlDb() {
        // create table containing identity
        SQLTestUtils.executeUpdate("create table test_table1 (col1 int not null identity, col2 varchar(12) not null)", dataSource);
        // create table without identity
        SQLTestUtils.executeUpdate("create table test_table2 (col1 int primary key, col2 varchar(12) not null)", dataSource);
        // create sequences
        SQLTestUtils.executeUpdate("create sequence test_sequence", dataSource);
    }


    /**
     * Drops all created test database structures
     */
    private void cleanupTestDatabaseHsqlDb() {
        SQLTestUtils.dropTestTables(defaultDatabase, "test_table1", "test_table2");
        SQLTestUtils.dropTestSequences(defaultDatabase, "test_sequence");
    }

    //
    // Database setup for MySql
    //

    /**
     * Creates all test database structures
     */
    private void createTestDatabaseMySql() {
        // create tables with auto increment column
        SQLTestUtils.executeUpdate("create table test_table1 (col1 int not null primary key AUTO_INCREMENT, col2 varchar(12) not null)", dataSource);
        // create table without increment column
        SQLTestUtils.executeUpdate("create table test_table2 (col1 int not null primary key, col2 varchar(12) not null)", dataSource);
    }


    /**
     * Drops all created test database structures
     */
    private void cleanupTestDatabaseMySql() {
        SQLTestUtils.dropTestTables(defaultDatabase, "test_table1", "test_table2");
    }

    //
    // Database setup for Oracle
    //

    /**
     * Creates all test database structures (view, tables...)
     */
    private void createTestDatabaseOracle() {
        // create sequence
        SQLTestUtils.executeUpdate("create sequence test_sequence", dataSource);
    }


    /**
     * Drops all created test database structures (views, tables...)
     */
    private void cleanupTestDatabaseOracle() {
        SQLTestUtils.dropTestSequences(defaultDatabase, "test_sequence");
    }

    //
    // Database setup for PostgreSql
    //

    /**
     * Creates all test database structures
     */
    private void createTestDatabasePostgreSql() {
        // create sequence
        SQLTestUtils.executeUpdate("create sequence test_sequence", dataSource);
    }


    /**
     * Drops all created test database structures
     */
    private void cleanupTestDatabasePostgreSql() {
        SQLTestUtils.dropTestSequences(defaultDatabase, "test_sequence");
    }

    //
    // Database setup for Db2
    //

    /**
     * Creates all test database structures (view, tables...)
     */
    private void createTestDatabaseDb2() {
        // create tables with auto increment column
        SQLTestUtils.executeUpdate("create table test_table1 (col1 int not null primary key generated by default as identity, col2 varchar(12) not null)", dataSource);
        // create table without increment column
        SQLTestUtils.executeUpdate("create table test_table2 (col1 int not null primary key, col2 varchar(12) not null)", dataSource);
        // create sequences
        SQLTestUtils.executeUpdate("create sequence test_sequence", dataSource);
    }


    /**
     * Drops all created test database structures (views, tables...)
     */
    private void cleanupTestDatabaseDb2() {
        SQLTestUtils.dropTestTables(defaultDatabase, "test_table1", "test_table2");
        SQLTestUtils.dropTestSequences(defaultDatabase, "test_sequence");
    }

    //
    // Database setup for Derby
    //

    /**
     * Creates all test database structures
     */
    private void createTestDatabaseDerby() {
        // create table containing identity
        SQLTestUtils.executeUpdate("create table test_table1 (col1 int not null primary key generated always as identity, col2 varchar(12) not null)", dataSource);
        // create table without identity
        SQLTestUtils.executeUpdate("create table test_table2 (col1 int not null primary key, col2 varchar(12) not null)", dataSource);
    }


    /**
     * Drops all created test database structures
     */
    private void cleanupTestDatabaseDerby() {
        SQLTestUtils.dropTestTables(defaultDatabase, "test_table1", "test_table2");
    }

    //
    // Database setup for MS-Sql
    //

    /**
     * Creates all test database structures
     */
    private void createTestDatabaseMsSql() {
        // create table containing identity
        SQLTestUtils.executeUpdate("create table test_table1 (col1 int not null primary key identity, col2 varchar(12) not null)", dataSource);
        // create table without identity
        SQLTestUtils.executeUpdate("create table test_table2 (col1 int not null primary key, col2 varchar(12) not null)", dataSource);
    }


    /**
     * Drops all created test database structures
     */
    private void cleanupTestDatabaseMsSql() {
        SQLTestUtils.dropTestTables(defaultDatabase, "test_table1", "test_table2");
    }
}
