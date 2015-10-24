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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.sql4j.Column.FinalizedColumn;

/**
 *
 * @author George Aristy
 */
public class CreateTable {
  protected final SqlBuilder builder;

  CreateTable(SqlBuilder builder, String tableName) {
    this.builder = builder;
    this.builder.appendLine("CREATE TABLE " + tableName + " (");
  }

  public FinalizedCreateTable column(FinalizedColumn column){
    return new FinalizedCreateTable(builder, column);
  }

  public static class FinalizedCreateTable implements DdlSql {
    private final SqlBuilder builder;
    private final List<FinalizedColumn> columns;

    private FinalizedCreateTable(SqlBuilder builder, FinalizedColumn column) {
      this.builder = builder;
      this.columns = new ArrayList<>();
      columns.add(column);
    }

    public FinalizedCreateTable column(FinalizedColumn column){
      columns.add(column);
      return this;
    }

    @Override
    public String toSqlString() {
      StringBuilder sql = new StringBuilder(builder.getSql());

      Iterator<FinalizedColumn> iter = columns.iterator();

      while(iter.hasNext()){
        sql.append(iter.next().getColumnString());

        if(iter.hasNext()){
          sql.append(",");
        }

        sql.append(builder.getNewLineChar());
      }

      sql.append(")");
      return builder.getSql();
    }
  }
}