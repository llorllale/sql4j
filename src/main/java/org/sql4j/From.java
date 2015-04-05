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
public class From {
  private final SqlBuilder context;

  From(SqlBuilder context) {
    this.context = context;
    this.context.append("FROM ");
  }

  public Where table(String table){
    context.appendLine(table);
    return new Where(context);
  }

  public Where tables(String... tables){
    context.appendLine(StringUtils.join(tables, ", "));
    return new Where(context);
  }
}