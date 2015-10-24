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

import org.sql4j.Condition.FinalizedCondition;

/**
 *
 * @author George Aristy
 */
public class Update {
  private final SqlBuilder builder;

  Update(SqlBuilder builder, String table) {
    this.builder = builder;
    this.builder.append("UPDATE " + table);
  }

  public FinalizedUpdate set(String column, Object value){
    builder.appendLine(" SET");
    builder.append(column + " = ?");
    builder.addParameter(value);
    return new FinalizedUpdate(builder);
  }

  public static class FinalizedUpdate implements DmlSql {
    private final SqlBuilder builder;

    private FinalizedUpdate(SqlBuilder builder) {
      this.builder = builder;
    }

    public FinalizedUpdate set(String column, Object value){
      builder.appendLine(",");
      builder.append(column + " = ?");
      builder.addParameter(value);
      return this;
    }

    public AndOr where(FinalizedCondition condition){
      builder.newLine();
      return new Where(builder).where(condition);
    }

    @Override
    public String toSqlString() {
      return builder.getSql();
    }

    @Override
    public String toPreparedSqlString() {
      return builder.getParametrizedString();
    }
  }
}