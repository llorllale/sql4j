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

/**
 *
 * @author George Aristy
 */
public class Select {
  private final Context context;

  Select(Context context) {
    this.context = context;
    context.append("SELECT ");
  }

  public SelectForUpdate all(){
    context.append("*");
    return new SelectForUpdate(context);
  }

  public SelectDistinct distinct(){
    return new SelectDistinct(context);
  }

  public SelectCount count(){
    return new SelectCount(context);
  }

  public SelectForUpdate columns(String... columns){
    context.append(StringUtils.join(columns, ", "));
    return new SelectForUpdate(context);
  }

  public static class SelectForUpdate extends SelectFinalizer {
    SelectForUpdate(Context context) {
      super(context);
    }
  
    public SelectFinalizer forUpdate(){
      super.context.append(" FOR UPDATE");
      return new SelectFinalizer(super.context);
    }
  }

  public static class SelectCount {
    private final Context context;
  
    SelectCount(Context context) {
      this.context = context;
      this.context.append("COUNT(");
    }
    
    public SelectFinalizer column(String column){
      context.append(column).append(")");
      return new SelectFinalizer(context);
    }

    public SelectCountDistinct distinct(){
      return new SelectCountDistinct(context);
    }
  }

  public static class SelectCountDistinct {
    private final Context context;
  
    SelectCountDistinct(Context context) {
      this.context = context;
      this.context.append("DISTINCT ");
    }
    
    public SelectFinalizer column(String column){
      context.append(column).append(")");
      return new SelectFinalizer(context);
    }
  }

  public static class SelectDistinct {
    private final Context context;
  
    SelectDistinct(Context context) {
      this.context = context;
      this.context.append("DISTINCT ");
    }
    
    public SelectForUpdate columns(String... columns){
      context.append(StringUtils.join(columns, ", "));
      return new SelectForUpdate(context);
    }
  }

  public static class SelectFinalizer {
    private final Context context;

    SelectFinalizer(Context context) {
      this.context = context;
    }

    public From from(){
      context.newLine();
      return new From(context);
    }
  }
}
