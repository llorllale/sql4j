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
public class Join {
  private final SqlBuilder context;

  Join(SqlBuilder context, String table) {
    this.context = context;
    this.context.append(joinPrefix() + "JOIN ").append(table);
  }

  public final Where on(FinalizedCondition condition){
    this.context.append(" ON ").appendLine(condition.getContext().getParametrizedString());
    this.context.addParameters(condition.getContext().getParameters());
    return new Where(context);
  }

  protected String joinPrefix(){
    return "";
  }

  public static class LeftInnerJoin extends Join {
    LeftInnerJoin(SqlBuilder context, String table) {
      super(context, table);
    }

    @Override
    protected String joinPrefix(){
      return "LEFT INNER ";
    }
  }

  public static class LeftOuterJoin extends Join {
    LeftOuterJoin(SqlBuilder context, String table) {
      super(context, table);
    }

    @Override
    protected String joinPrefix(){
      return "LEFT OUTER ";
    }
  }

  public static class RightInnerJoin extends Join {
    RightInnerJoin(SqlBuilder context, String table) {
      super(context, table);
    }

    @Override
    protected String joinPrefix(){
      return "RIGHT INNER ";
    }
  }

  public static class RightOuterJoin extends Join {
    RightOuterJoin(SqlBuilder context, String table) {
      super(context, table);
    }

    @Override
    protected String joinPrefix(){
      return "RIGHT OUTER ";
    }
  }
}