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

import org.sql4j.Condition.FinalizedCondition;
import org.sql4j.Condition.GroupCondition;

/**
 *
 * @author George Aristy
 */
public class AndOr implements DmlSql {
  private final SqlBuilder context;

  AndOr(SqlBuilder context) {
    this.context = context;
  }

  public AndOr and(DmlSql query){
    context.append("AND ( ")
            .append(query.toPreparedSqlString())
            .append(" )");
    return this;
  }

  public AndOr or(DmlSql query){
    context.append("OR ( ")
            .append(query.toPreparedSqlString())
            .append(" )");
    return this;
  }

  public AndOr and(FinalizedCondition condition){
    context.append("AND ").appendLine(condition.getContext().getParametrizedString());
    context.addParameters(condition.getContext().getParameters());
    return this;
  }

  public AndOr or(FinalizedCondition condition){
    context.append("OR ").appendLine(condition.getContext().getParametrizedString());
    context.addParameters(condition.getContext().getParameters());
    return this;
  }

  public AndOr and(GroupCondition groupCondition){
    SqlBuilder ctx = groupCondition.getContext();
    context.append("AND ( ").append(ctx.getParametrizedString()).appendLine(" )");
    context.addParameters(ctx.getParameters());
    return this;
  }

  public AndOr or(GroupCondition groupCondition){
    SqlBuilder ctx = groupCondition.getContext();
    context.append("OR ( ").append(ctx.getParametrizedString()).appendLine(" )");
    context.addParameters(ctx.getParameters());
    return this;
  }

  public GroupBy groupBy(String... columns){
    return new GroupBy(context, columns);
  }

  public OrderBy orderBy(String... columns){
    return new OrderBy(context, columns);
  }

  @Override
  public String toSqlString() {
    return context.getSql();
  }

  @Override
  public String toPreparedSqlString() {
    return context.getParametrizedString();
  }
}
