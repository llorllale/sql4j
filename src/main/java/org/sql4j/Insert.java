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

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author George Aristy
 */
public class Insert {
  private final SqlBuilder builder;

  Insert(SqlBuilder builder, String table){
    this.builder = builder;
    builder.append("INSERT INTO " + table);
  }

  public InsertValues columns(String... columns){
    builder.append(" (" + StringUtils.join(columns, ", ") + ")");
    return new InsertValues(builder);
  }

  public FinalizedInsert values(Object... values){
    InsertValues iv = new InsertValues(builder);
    return iv.values(values);
  }

  public static class InsertValues {
    private final SqlBuilder builder;

    private InsertValues(SqlBuilder builder){
      this.builder = builder;
      this.builder.append(" VALUES (");
    }

    public FinalizedInsert values(Object... values){
      builder.addParameters(Arrays.asList(values));
      builder.appendLine(StringUtils.repeat("?, ", values.length-1) + "?)");
      return new FinalizedInsert(builder);
    }
  }

  public static class FinalizedInsert implements DmlSql {
    private final SqlBuilder builder;

    private FinalizedInsert(SqlBuilder builder){
      this.builder = builder;
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