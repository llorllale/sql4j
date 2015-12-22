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

import java.util.List;
import org.sql4j.Condition.FinalizedCondition;
import org.sql4j.Join.LeftInnerJoin;
import org.sql4j.Join.LeftOuterJoin;
import org.sql4j.Join.RightInnerJoin;
import org.sql4j.Join.RightOuterJoin;

/**
 *
 * @author George Aristy
 */
public class Where implements QueryExpression {
  protected final SqlBuilder context;

  Where(SqlBuilder context) {
    this.context = context;
  }

  public AndOr where(FinalizedCondition condition){
    context.append("WHERE ").appendLine(condition.getContext().getParametrizedString());
    context.addParameters(condition.getContext().getParameters());
    return new AndOr(context);
  }

  public Join join(String table){
    return new Join(context, table);
  }

  public LeftInnerJoin leftInnerJoin(String table){
    return new LeftInnerJoin(context, table);
  }

  public LeftOuterJoin leftOuterJoin(String table){
    return new LeftOuterJoin(context, table);
  }

  public RightInnerJoin rightInnerJoin(String table){
    return new RightInnerJoin(context, table);
  }

  public RightOuterJoin rightOuterJoin(String table){
    return new RightOuterJoin(context, table);
  }

  public GroupBy groupBy(String... columns){
    return new GroupBy(context, columns);
  }

  public OrderBy orderBy(String column){
    return new OrderBy(context, column);
  }

  @Override
  public String toSqlString() {
    return context.getSql();
  }

  @Override
  public String toPreparedSqlString() {
    return context.getParametrizedString();
  }

  @Override
  public List<Object> getParameters() {
    return context.getParameters();
  }
}