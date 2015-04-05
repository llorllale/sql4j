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

import java.sql.Connection;

/**
 *
 * @author George Aristy
 */
public class QueryBuilder {
  private final SqlBuilder context;

  public QueryBuilder(Connection connection) {
    this.context = new SqlBuilder(null, connection);
  }

  public Select select() {
    return new Select(context);
  }

  public Insert insertInto(String table){
    return new Insert(context, table);
  }

  public Update update(String table){
    return new Update(context, table);
  }
}
