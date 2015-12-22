/*
 * Copyright 2015 George Aristy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sql4j;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.sql4j.Condition.column;

/**
 *
 * @author George Aristy
 */
public class InsertSyntaxTest {
  private static final String NEW_LINE = System.getProperty("line.separator"); 

  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {

  }
  
  @After
  public void tearDown() {
  }

  @Test
  public void insertColumnsAndValues(){
    DmlSql query = new QueryBuilder(null).insertInto("test")
            .columns("column1", "column2", "column3")
            .values("value1", "value2", "value3")
            ;
    
    String expected1 = "INSERT INTO test (column1, column2, column3) VALUES ('value1', 'value2', 'value3')" + NEW_LINE;
    String expected2 = "INSERT INTO test (column1, column2, column3) VALUES (?, ?, ?)" + NEW_LINE;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void insertValues(){
    DmlSql query = new QueryBuilder(null).insertInto("test")
            .values("value1", "value2", "value3")
            ;
    
    String expected1 = "INSERT INTO test VALUES ('value1', 'value2', 'value3')" + NEW_LINE;
    String expected2 = "INSERT INTO test VALUES (?, ?, ?)" + NEW_LINE;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void insertQuery(){
    DmlSql query = new QueryBuilder(null)
            .insertInto("test_table")
            .query(new QueryBuilder(null)
                    .select()
                    .columns("name", "value")
                    .from()
                    .table("other_table")
            );

    String expected = "INSERT INTO test_table ( SELECT name, value" + NEW_LINE + "FROM other_table" + NEW_LINE + ")" + NEW_LINE;
    String expected2 = "INSERT INTO test_table ( SELECT name, value" + NEW_LINE + "FROM other_table" + NEW_LINE + ")" + NEW_LINE;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null)
            .insertInto("test_table")
            .columns("name", "value")
            .query(new QueryBuilder(null)
                    .select()
                    .columns("name", "value")
                    .from()
                    .table("other_table")
            );

    expected = "INSERT INTO test_table (name, value) VALUES ( SELECT name, value" + NEW_LINE + "FROM other_table" + NEW_LINE + ")" + NEW_LINE;
    expected2 = "INSERT INTO test_table (name, value) VALUES ( SELECT name, value" + NEW_LINE + "FROM other_table" + NEW_LINE + ")" + NEW_LINE;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null)
            .insertInto("test_table")
            .query(new QueryBuilder(null)
                    .select()
                    .all()
                    .from()
                    .table("other_table")
                    .where(column("id").eq(1))
            );

    expected = "INSERT INTO test_table ( SELECT *" 
            + NEW_LINE 
            + "FROM other_table" 
            + NEW_LINE 
            + "WHERE id = 1" 
            + NEW_LINE 
            + ")" 
            + NEW_LINE;
    expected2 = "INSERT INTO test_table ( SELECT *" 
            + NEW_LINE 
            + "FROM other_table" 
            + NEW_LINE 
            + "WHERE id = ?"
            + NEW_LINE 
            + ")" 
            + NEW_LINE;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }
}