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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author George Aristy
 */
public class OrderBy implements QueryExpression {
  private final SqlBuilder context;

  OrderBy(SqlBuilder context, String... columns) {
    this.context = context;
    this.context.append("ORDER BY ").append(StringUtils.join(columns, ", "));
  }

  public FinalizedOrderBy asc(){
    context.appendLine(" ASC");
    return new FinalizedOrderBy(context);
  }

  public FinalizedOrderBy desc(){
    context.appendLine(" DESC");
    return new FinalizedOrderBy(context);
  }

  @Override
  public String toSqlString() {
    return new SqlBuilder(context).newLine().getSql();
  }

  @Override
  public String toPreparedSqlString() {
    return new SqlBuilder(context).newLine().getParametrizedString();
  }

  @Override
  public List<Object> getParameters() {
    return context.getParameters();
  }

  public static class FinalizedOrderBy implements QueryExpression {
    private final SqlBuilder context;

    private FinalizedOrderBy(SqlBuilder context) {
      this.context = context;
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
}