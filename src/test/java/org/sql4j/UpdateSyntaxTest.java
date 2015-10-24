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
public class UpdateSyntaxTest {
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
  
  @Test
  public void updateNoConditions(){
    //SINGLE FIELD UPDATE
    DmlSql query = new QueryBuilder(null).update("clients")
            .set("salary", 70000)
            ;

    String expected1 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = 70000")
            .toString()
            ;
    String expected2 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = ?")
            .toString()
            ;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    //MULTIPLE FIELD UPDATE
    query = new QueryBuilder(null).update("clients")
            .set("salary", 70000)
            .set("department", "ACCOUNTING")
            ;

    expected1 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = 70000,").append(NEW_LINE)
            .append("department = 'ACCOUNTING'")
            .toString()
            ;
    expected2 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = ?,").append(NEW_LINE)
            .append("department = ?")
            .toString()
            ;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void updateWithConditions(){
    DmlSql query = new QueryBuilder(null).update("clients")
            .set("salary", 70000)
            .where(column("name").like("BOB%"))
            ;

    String expected1 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = 70000").append(NEW_LINE)
            .append("WHERE name LIKE 'BOB%'").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("UPDATE clients SET").append(NEW_LINE)
            .append("salary = ?").append(NEW_LINE)
            .append("WHERE name LIKE ?").append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }
}