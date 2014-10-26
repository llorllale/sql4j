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

import org.apache.commons.lang3.StringUtils;
import org.sql4j.Condition.FinalizedCondition;

/**
 *
 * @author George Aristy
 */
public class GroupBy implements FinalizedQuery {
  private final Context context;

  GroupBy(Context context, String... columns) {
    this.context = context;
    this.context.append("GROUP BY ").appendLine(StringUtils.join(columns, ", "));
  }

  public Having having(FinalizedCondition condition){
    return new Having(context, condition);
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