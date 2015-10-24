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

/**
 *
 * @author George Aristy
 */
public class ColumnConstraint {
  private ColumnConstraint() {
  }

  public static ColumnConstraintSpec constraint(String constraintName){
    return new ColumnConstraintSpec(constraintName);
  }

  public static class ColumnConstraintSpec {
    private final SqlBuilder builder;

    private ColumnConstraintSpec(String constraintName) {
      this.builder = new SqlBuilder();

      if(constraintName != null){
        this.builder.append("CONSTRAINT ").append(constraintName).append(" ");
      }
    }

    public FinalizedColumnConstraint notNull(){
      builder.append("NOT NULL");
      return new FinalizedColumnConstraint(builder);
    }

    public FinalizedColumnConstraint unique(){
      builder.append("UNIQUE");
      return new FinalizedColumnConstraint(builder);
    }

    public FinalizedColumnConstraint primaryKey(){
      builder.append("PRIMARY KEY");
      return new FinalizedColumnConstraint(builder);
    }
  }

  public static class FinalizedColumnConstraint {
    private final SqlBuilder builder;

    private FinalizedColumnConstraint(SqlBuilder builder) {
      this.builder = builder;
    }

    String getConstraintString(){
      return builder.getSql();
    }
  }
}