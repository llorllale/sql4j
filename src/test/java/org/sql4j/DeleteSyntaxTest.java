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
public class DeleteSyntaxTest {
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
  public void deleteNoConditions(){
    FinalizedQuery query = new QueryBuilder(null).deleteFrom("test");

    String expected = "DELETE FROM test" + NEW_LINE;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected, query.toPreparedSqlString());
  }

  @Test
  public void deleteWithConditions(){
    FinalizedQuery query  = new QueryBuilder(null).deleteFrom("clients")
            .where(column("last_name").eq("Doe"))
            ;

    String expected1 = new StringBuilder("DELETE FROM clients").append(NEW_LINE)
            .append("WHERE last_name = 'Doe'").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("DELETE FROM clients").append(NEW_LINE)
            .append("WHERE last_name = ?").append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }
}