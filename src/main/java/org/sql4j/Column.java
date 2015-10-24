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

import org.sql4j.ColumnConstraint.FinalizedColumnConstraint;

/**
 *
 * @author George Aristy
 */
public class Column {
  private Column() {
  }

  public static ColumnDataTypeChooser newColumn(String columnName) {
    return new ColumnDataTypeChooser(columnName);
  }

  public static class ColumnDataTypeChooser {
    private final SqlBuilder builder;

    private ColumnDataTypeChooser(String columnName) {
      this.builder = new SqlBuilder();
      this.builder.append(columnName);
    }

    public ColumnConstraintDef typeInteger(){
      builder.append(" INTEGER");
      return new ColumnConstraintDef(builder);
    }

    public ColumnConstraintDef typeString(int length){
      builder.append(" VARCHAR(" + length + ")");
      return new ColumnConstraintDef(builder);
    }
  }

  public static class ColumnConstraintDef extends FinalizedColumn {
    private ColumnConstraintDef(SqlBuilder builder) {
      super(builder);
    }

    public FinalizedColumn constraint(FinalizedColumnConstraint constraint){
      return new FinalizedColumn(builder);
    }
  }

  public static class FinalizedColumn {
    protected final SqlBuilder builder;

    private FinalizedColumn(SqlBuilder builder) {
      this.builder = builder;
    }

    protected String getColumnString(){
      return builder.getSql();
    }
  }
}