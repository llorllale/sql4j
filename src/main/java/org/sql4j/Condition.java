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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author George Aristy
 */
public class Condition {
  private final Context context;

  private Condition(Context context, String column) {
    this.context = context;
    this.context.append(column);
  }

  public static Condition column(String column){
    return new Condition(new Context(), column);
  }

  public static FinalizedCondition literal(Object object){
    return new FinalizedCondition(new Context().append(stringify(object)));
  }

  public FinalizedCondition like(String like){
    context.addParameter(like);
    context.append(" LIKE ?");
    return new FinalizedCondition(context);
  }

  public FinalizedCondition isNull(){
    return new FinalizedCondition(context.append(" IS NULL"));
  }

  public FinalizedCondition eq(Condition condition){
    return new FinalizedCondition(context.append(" = ").append(condition.getSql()));
  }

  public FinalizedCondition eq(Object object){
    context.addParameter(object);
    context.append(" = ?");
    return new FinalizedCondition(context);
  }

  public FinalizedCondition gt(Condition condition){
    return new FinalizedCondition(context.append(" > ").append(condition.getSql()));
  }

  public FinalizedCondition gt(Object object){
    context.addParameter(object);
    context.append(" > ?");
    return new FinalizedCondition(context);
  }

  public FinalizedCondition gteq(Condition condition){
    return new FinalizedCondition(context.append(" >= ").append(condition.getSql()));
  }

  public FinalizedCondition gteq(Object object){
    context.addParameter(object);
    context.append(" >= ?");
    return new FinalizedCondition(context);
  }

  public FinalizedCondition lt(Condition condition){
    return new FinalizedCondition(context.append(" < ").append(condition.getSql()));
  }

  public FinalizedCondition lt(Object object){
    context.addParameter(object);
    context.append(" < ?");
    return new FinalizedCondition(context);
  }

  public FinalizedCondition lteq(Condition condition){
    return new FinalizedCondition(context.append(" <= ").append(condition.getSql()));
  }

  public FinalizedCondition lteq(Object object){
    context.addParameter(object);
    context.append(" <= ?");
    return new FinalizedCondition(context);
  }

  static String stringify(Object object){
    String result = null;

    if(object instanceof Number){
      result = String.valueOf(object);
    }else if(object instanceof String){
      result = "'" + (String) object + "'";
    }else if(object instanceof Date){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      result = "TIMESTAMP'" + sdf.format((Date) object) + "'";
    }else{
      //TODO remove...
      throw new IllegalArgumentException("Unsupported type for value assignment: " + object.getClass().getName());
    }

    validate(result);
    return result;
  }

  private static void validate(String param) throws IllegalArgumentException {
    if("?".equals(param)) throw new IllegalArgumentException("'?' is not allowed to be assigned in a condition.");
  }

  private String getSql(){
    return context.getSql();
  }

  public static class FinalizedCondition {
    private final Context context;

    private FinalizedCondition(Context context) {
      this.context = context;
    }

    Context getContext(){
      return context;
    }
  }

  public static class GroupCondition {
    private final Context context;

    private GroupCondition() {
      this.context = new Context();
    }

    public static GroupCondition group(FinalizedCondition condition){
      GroupCondition group = new GroupCondition();
      group.context.append(condition.getContext().getParametrizedString());
      group.context.addParameters(condition.getContext().getParameters());
      return group;
    }

    public GroupCondition and(FinalizedCondition condition){
      context.append(" AND ").append(condition.getContext().getParametrizedString());
      context.addParameters(condition.getContext().getParameters());
      return this;
    }

    public GroupCondition or(FinalizedCondition condition){
      context.append(" OR ").append(condition.getContext().getParametrizedString());
      context.addParameters(condition.getContext().getParameters());
      return this;
    }

    Context getContext(){
      return context;
    }
  }
}