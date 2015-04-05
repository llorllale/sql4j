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
public class Delete implements FinalizedQuery {
  private final SqlBuilder builder;

  Delete(SqlBuilder builder, String table){
    this.builder = builder;
    this.builder.append("DELETE FROM ").appendLine(table);
  }

  public AndOr where(FinalizedCondition condition){
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