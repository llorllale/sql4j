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

/**
 *
 * @author George Aristy
 */
public class Having implements FinalizedQuery {
  private final Context context;

  Having(Context context, FinalizedCondition condition) {
    this.context = context;
    this.context.append("HAVING ").append(condition.getContext().getParametrizedString());
    this.context.addParameters(condition.getContext().getParameters());
  }

  public Having having(FinalizedCondition condition){
    context.append(", ").append(condition.getContext().getParametrizedString());
    context.addParameters(condition.getContext().getParameters());
    return this;
  }

  public OrderBy orderBy(String column){
    context.newLine();
    return new OrderBy(context, column);
  }

  @Override
  public String toSqlString() {
    context.newLine();
    return context.getSql();
  }

  @Override
  public String toPreparedSqlString() {
    return context.getParametrizedString();
  }
}