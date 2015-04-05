/* 
 * Copyright 2014 George Aristy.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author George Aristy
 */
class SqlBuilder {
  private Database database;
  private Connection connection;
  private final StringBuilder sql;
  private final List<Object> parameters;

  SqlBuilder(){
    this.sql = new StringBuilder();
    this.parameters = new ArrayList<>();
  }

  SqlBuilder(Database database, Connection connection) {
    this();
    this.database = database;
    this.connection = connection;
  }

  SqlBuilder(SqlBuilder prototype){
    this.sql = new StringBuilder(prototype.sql);
    this.database = prototype.database;
    this.connection = prototype.connection;
    this.parameters = new ArrayList<>(prototype.parameters);
  }

  SqlBuilder addParameter(Object parameter){
    parameters.add(parameter);
    return this;
  }

  SqlBuilder addParameters(List<Object> parameters){
    this.parameters.addAll(parameters);
    return this;
  }

  SqlBuilder append(String expression){
    sql.append(expression);
    return this;
  }

  SqlBuilder appendLine(String expression){
    sql.append(expression).append(System.getProperty("line.separator"));
    return this;
  }

  SqlBuilder newLine(){
    sql.append(System.getProperty("line.separator"));
    return this;
  }

  String getSql(){
    String sqlString = sql.toString();
    int paramIndex = 0;
    
    while(sqlString.contains("?")){
      sqlString = sqlString.replaceFirst("\\?", Condition.stringify(parameters.get(paramIndex)));
      paramIndex++;
    }

    return sqlString;
  }

  String getParametrizedString(){
    return sql.toString();
  }

  List<Object> getParameters(){
    return Collections.unmodifiableList(parameters);
  }

  <T> List<T> list(T type) throws SQLException {
    final List<T> list = new ArrayList<>();
    final String sqlString = sql.toString();
    int paramIndex = 1;
    int sqlParamIndex = 0;
    PreparedStatement stmt = connection.prepareStatement(sqlString);
    
    while((sqlParamIndex = sqlString.indexOf("?", sqlParamIndex)) > 0){
      stmt.setObject(paramIndex, parameters.get(paramIndex));
      paramIndex++;
      sqlParamIndex++;
    }

    ResultSet result = stmt.executeQuery();

    return list;
  }
}