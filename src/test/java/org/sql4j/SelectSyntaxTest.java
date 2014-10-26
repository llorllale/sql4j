/* * Copyright 2014 George Aristy.
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

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.sql4j.Condition.GroupCondition.group;
import static org.sql4j.Condition.column;

/**
 *
 * @author George Aristy
 */
public class SelectSyntaxTest {
  private static final String NEW_LINE = System.getProperty("line.separator");
  
  public SelectSyntaxTest() {
  }
  
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
  public void selectAllFromTable() {
    //SINGLE TABLE
    FinalizedQuery query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person");

    String expected = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected, query.toPreparedSqlString());

    //MULTIPLE TABLES
    query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person", "country", "city");

    expected = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person, country, city")
            .append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected, query.toPreparedSqlString());
  }

  @Test
  public void selectDistinctFromTable() {
    FinalizedQuery query = new QueryBuilder(null).select()
            .distinct()
            .columns("name", "age", "email")
            .from()
            .tables("contacts")
            ;

    String expected = new StringBuilder("SELECT DISTINCT name, age, email")
            .append(NEW_LINE)
            .append("FROM contacts")
            .append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected, query.toPreparedSqlString());
  }

  @Test
  public void selectForUpdate(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("name", "age", "email")
            .forUpdate()
            .from()
            .tables("contacts")
            ;

    String expected = new StringBuilder("SELECT name, age, email FOR UPDATE")
            .append(NEW_LINE)
            .append("FROM contacts")
            .append(NEW_LINE)
            .toString()
            ;

    assertEquals(expected, query.toSqlString());
    assertEquals(expected, query.toPreparedSqlString());
  }

  @Test
  public void selectAllFromTableWhere(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person")
            .where(column("age").gt(21))
            .and(column("creation_date").gt(new Date(83, 0, 25)))
            .or(column("balance").lt(1000))
            ;
    String expected1 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE age > 21").append(NEW_LINE)
            .append("AND creation_date > TIMESTAMP'1983-01-25 00:00:00'").append(NEW_LINE)
            .append("OR balance < 1000").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE age > ?").append(NEW_LINE)
            .append("AND creation_date > ?").append(NEW_LINE)
            .append("OR balance < ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .columns("name", "age")
            .from()
            .tables("person")
            .where(column("name").eq("George"))
            .and(group(column("age").gt(30)).or(column("email").eq("test@gmail.com")))
            .or(column("phone").eq("8095555555"))
            ;
    expected1 = new StringBuilder("SELECT name, age")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .append("WHERE name = 'George'")
            .append(NEW_LINE)
            .append("AND ( age > 30 OR email = 'test@gmail.com' )")
            .append(NEW_LINE)
            .append("OR phone = '8095555555'")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT name, age").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE name = ?").append(NEW_LINE)
            .append("AND ( age > ? OR email = ? )").append(NEW_LINE)
            .append("OR phone = ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person")
            .where(column("age").gteq(30))
            ;
    expected1 = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .append("WHERE age >= 30")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE age >= ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person")
            .where(column("age").lteq(30))
            ;
    expected1 = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .append("WHERE age <= 30")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE age <= ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person p", "city c")
            .where(column("p.city_id").eq(column("c.id")))
            .and(column("c.name").eq("New York"))
            ;
    expected1 = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person p, city c")
            .append(NEW_LINE)
            .append("WHERE p.city_id = c.id")
            .append(NEW_LINE)
            .append("AND c.name = 'New York'")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person p, city c").append(NEW_LINE)
            .append("WHERE p.city_id = c.id").append(NEW_LINE)
            .append("AND c.name = ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .columns("p.population", "c.name")
            .from()
            .tables("population p", "city c", "region r")
            .where(column("r.name").eq("Central"))
            .and(column("r.id").eq(column("c.region_id")))
            .and(column("c.id").eq(column("p.city_id")))
            .and(column("c.budget").lteq(1000000))
            .and(column("p.population").gt(400000))
            ;
    expected1 = new StringBuilder("SELECT p.population, c.name")
            .append(NEW_LINE)
            .append("FROM population p, city c, region r")
            .append(NEW_LINE)
            .append("WHERE r.name = 'Central'")
            .append(NEW_LINE)
            .append("AND r.id = c.region_id")
            .append(NEW_LINE)
            .append("AND c.id = p.city_id")
            .append(NEW_LINE)
            .append("AND c.budget <= 1000000")
            .append(NEW_LINE)
            .append("AND p.population > 400000")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT p.population, c.name").append(NEW_LINE)
            .append("FROM population p, city c, region r").append(NEW_LINE)
            .append("WHERE r.name = ?").append(NEW_LINE)
            .append("AND r.id = c.region_id").append(NEW_LINE)
            .append("AND c.id = p.city_id").append(NEW_LINE)
            .append("AND c.budget <= ?").append(NEW_LINE)
            .append("AND p.population > ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectColumnsFromTableWhereGroupBy(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("age", "sex")
            .from()
            .tables("person")
            .groupBy("sex")
            ;
    String expected1 = new StringBuilder("SELECT age, sex")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .append("GROUP BY sex")
            .append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected1, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .columns("age", "sex")
            .from()
            .tables("person")
            .where(column("last_name").eq("Smith"))
            .and(column("creation_date").gt(new Date(0, 0, 1)))
            .groupBy("sex")
            ;
    expected1 = new StringBuilder("SELECT age, sex")
            .append(NEW_LINE)
            .append("FROM person")
            .append(NEW_LINE)
            .append("WHERE last_name = 'Smith'")
            .append(NEW_LINE)
            .append("AND creation_date > TIMESTAMP'1900-01-01 00:00:00'")
            .append(NEW_LINE)
            .append("GROUP BY sex")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT age, sex").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE last_name = ?").append(NEW_LINE)
            .append("AND creation_date > ?").append(NEW_LINE)
            .append("GROUP BY sex").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectColumnsFromTableWhereGroupByOrderBy(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("id", "name", "email", "balance")
            .from()
            .tables("accounts")
            .where(column("balance").gt(950))
            .groupBy("name")
            .orderBy("balance")
            ;

    String expected1 = new StringBuilder("SELECT id, name, email, balance")
            .append(NEW_LINE)
            .append("FROM accounts")
            .append(NEW_LINE)
            .append("WHERE balance > 950")
            .append(NEW_LINE)
            .append("GROUP BY name")
            .append(NEW_LINE)
            .append("ORDER BY balance")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT id, name, email, balance").append(NEW_LINE)
            .append("FROM accounts").append(NEW_LINE)
            .append("WHERE balance > ?").append(NEW_LINE)
            .append("GROUP BY name").append(NEW_LINE)
            .append("ORDER BY balance").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectColumnsFromTableWhereGroupByOrderByWithOrder(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("number", "street", "city", "state")
            .from()
            .tables("region")
            .where(column("city").eq("Santo Domingo"))
            .groupBy("state")
            .orderBy("city")
            .asc()
            ;
    String expected1 = new StringBuilder("SELECT number, street, city, state")
            .append(NEW_LINE)
            .append("FROM region")
            .append(NEW_LINE)
            .append("WHERE city = 'Santo Domingo'")
            .append(NEW_LINE)
            .append("GROUP BY state")
            .append(NEW_LINE)
            .append("ORDER BY city ASC")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT number, street, city, state").append(NEW_LINE)
            .append("FROM region").append(NEW_LINE)
            .append("WHERE city = ?").append(NEW_LINE)
            .append("GROUP BY state").append(NEW_LINE)
            .append("ORDER BY city ASC").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .columns("number", "street", "city", "state")
            .from()
            .tables("region")
            .where(column("city").eq("Caribbean"))
            .groupBy("state")
            .orderBy("city")
            .desc()
            ;
    expected1 = new StringBuilder("SELECT number, street, city, state")
            .append(NEW_LINE)
            .append("FROM region")
            .append(NEW_LINE)
            .append("WHERE city = 'Caribbean'")
            .append(NEW_LINE)
            .append("GROUP BY state")
            .append(NEW_LINE)
            .append("ORDER BY city DESC")
            .append(NEW_LINE)
            .toString()
            ;
    expected2 = new StringBuilder("SELECT number, street, city, state").append(NEW_LINE)
            .append("FROM region").append(NEW_LINE)
            .append("WHERE city = ?").append(NEW_LINE)
            .append("GROUP BY state").append(NEW_LINE)
            .append("ORDER BY city DESC").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectColumnsFromTableWhereGroupByHaving(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("c.city", "c.state", "p.population")
            .from()
            .tables("city c", "population p")
            .where(column("c.id").eq(column("p.city_id")))
            .and(column("p.population_count").gt(100000))
            .groupBy("c.city")
            .having(column("p.population").lt(2000000))
            ;
    String expected1 = new StringBuilder("SELECT c.city, c.state, p.population")
            .append(NEW_LINE)
            .append("FROM city c, population p")
            .append(NEW_LINE)
            .append("WHERE c.id = p.city_id")
            .append(NEW_LINE)
            .append("AND p.population_count > 100000")
            .append(NEW_LINE)
            .append("GROUP BY c.city")
            .append(NEW_LINE)
            .append("HAVING p.population < 2000000")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT c.city, c.state, p.population").append(NEW_LINE)
            .append("FROM city c, population p").append(NEW_LINE)
            .append("WHERE c.id = p.city_id").append(NEW_LINE)
            .append("AND p.population_count > ?").append(NEW_LINE)
            .append("GROUP BY c.city").append(NEW_LINE)
            .append("HAVING p.population < ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectColumnsFromTableWhereGroupByOrderByHaving(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("street", "number", "sector", "city")
            .from()
            .tables("region")
            .where(column("city").eq("Santo Domingo"))
            .groupBy("sector")
            .having(column("number").gt(42))
            .orderBy("sector")
            .asc()
            ;
    String expected1 = new StringBuilder("SELECT street, number, sector, city")
            .append(NEW_LINE)
            .append("FROM region")
            .append(NEW_LINE)
            .append("WHERE city = 'Santo Domingo'")
            .append(NEW_LINE)
            .append("GROUP BY sector")
            .append(NEW_LINE)
            .append("HAVING number > 42")
            .append(NEW_LINE)
            .append("ORDER BY sector ASC")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT street, number, sector, city").append(NEW_LINE)
            .append("FROM region").append(NEW_LINE)
            .append("WHERE city = ?").append(NEW_LINE)
            .append("GROUP BY sector").append(NEW_LINE)
            .append("HAVING number > ?").append(NEW_LINE)
            .append("ORDER BY sector ASC").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectFromTableLeftInnerJoin(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .all()
            .from()
            .table("person p")
            .leftInnerJoin("account c").on(column("p.id").eq(column("c.person_id")))
            ;
    String expected1 = new StringBuilder("SELECT *")
            .append(NEW_LINE)
            .append("FROM person p")
            .append(NEW_LINE)
            .append("LEFT INNER JOIN account c ON p.id = c.person_id")
            .append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person p").append(NEW_LINE)
            .append("LEFT INNER JOIN account c ON p.id = c.person_id").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());

    query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("person p", "city c")
            .leftInnerJoin("region r")
            .on(column("r.id").eq(column("c.region_id")))
            ;
    expected1 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person p, city c").append(NEW_LINE)
            .append("LEFT INNER JOIN region r ON r.id = c.region_id").append(NEW_LINE)
            .toString()
            ;

    expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person p, city c").append(NEW_LINE)
            .append("LEFT INNER JOIN region r ON r.id = c.region_id").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectFromTableLeftOuterJoin(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .columns("name", "age")
            .from()
            .table("person p")
            .leftOuterJoin("orders o").on(column("p.id").eq(column("o.person_id")))
            .groupBy("o.creation_date")
            .orderBy("o.order_type")
            .desc()
            ;
    String expected1 = new StringBuilder("SELECT name, age").append(NEW_LINE)
            .append("FROM person p").append(NEW_LINE)
            .append("LEFT OUTER JOIN orders o ON p.id = o.person_id").append(NEW_LINE)
            .append("GROUP BY o.creation_date").append(NEW_LINE)
            .append("ORDER BY o.order_type DESC").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT name, age").append(NEW_LINE)
            .append("FROM person p").append(NEW_LINE)
            .append("LEFT OUTER JOIN orders o ON p.id = o.person_id").append(NEW_LINE)
            .append("GROUP BY o.creation_date").append(NEW_LINE)
            .append("ORDER BY o.order_type DESC").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectFromTableRightInnerJoin(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .distinct()
            .columns("p.name")
            .from()
            .table("person p")
            .rightInnerJoin("accounts a").on(column("p.id").eq(column("a.person_id")))
            ;
    String expected1 = new StringBuilder("SELECT DISTINCT p.name").append(NEW_LINE)
            .append("FROM person p").append(NEW_LINE)
            .append("RIGHT INNER JOIN accounts a ON p.id = a.person_id").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT DISTINCT p.name").append(NEW_LINE)
            .append("FROM person p").append(NEW_LINE)
            .append("RIGHT INNER JOIN accounts a ON p.id = a.person_id").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectFromTableRightOuterJoin(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .all()
            .from()
            .tables("contact c", "account a", "person p")
            .rightOuterJoin("user u").on(column("p.id").eq(column("u.person_id")))
            .where(column("a.person_id").eq(column("p.id")))
            .and(column("c.person_id").eq(column("p.id")))
            ;
    String expected1 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM contact c, account a, person p").append(NEW_LINE)
            .append("RIGHT OUTER JOIN user u ON p.id = u.person_id").append(NEW_LINE)
            .append("WHERE a.person_id = p.id").append(NEW_LINE)
            .append("AND c.person_id = p.id").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM contact c, account a, person p").append(NEW_LINE)
            .append("RIGHT OUTER JOIN user u ON p.id = u.person_id").append(NEW_LINE)
            .append("WHERE a.person_id = p.id").append(NEW_LINE)
            .append("AND c.person_id = p.id").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectCountFromTable(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .count()
            .column("name")
            .from()
            .table("person")
            .where(column("is_active").eq(1))
            ;
    String expected1 = new StringBuilder("SELECT COUNT(name)").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE is_active = 1").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT COUNT(name)").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE is_active = ?").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void selectCountDistinctFromTable(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .count()
            .distinct()
            .column("c.name")
            .from()
            .tables("country c", "user u")
            .where(column("u.is_active").eq(1))
            .and(column("u.country_id").eq(column("c.id")))
            ;
    String expected1 = new StringBuilder("SELECT COUNT(DISTINCT c.name)").append(NEW_LINE)
            .append("FROM country c, user u").append(NEW_LINE)
            .append("WHERE u.is_active = 1").append(NEW_LINE)
            .append("AND u.country_id = c.id").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT COUNT(DISTINCT c.name)").append(NEW_LINE)
            .append("FROM country c, user u").append(NEW_LINE)
            .append("WHERE u.is_active = ?").append(NEW_LINE)
            .append("AND u.country_id = c.id").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }

  @Test
  public void nullCondition(){
    FinalizedQuery query = new QueryBuilder(null).select()
            .all()
            .from()
            .table("person")
            .where(column("email").isNull())
            ;
    String expected1 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE email IS NULL").append(NEW_LINE)
            .toString()
            ;
    String expected2 = new StringBuilder("SELECT *").append(NEW_LINE)
            .append("FROM person").append(NEW_LINE)
            .append("WHERE email IS NULL").append(NEW_LINE)
            .toString()
            ;
    assertEquals(expected1, query.toSqlString());
    assertEquals(expected2, query.toPreparedSqlString());
  }
}