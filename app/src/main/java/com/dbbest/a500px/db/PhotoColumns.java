package com.dbbest.a500px.db;

import net.simonvt.schematic.annotation.DataType;

public interface PhotoColumns extends BaseColumns {

    @DataType(DataType.Type.TEXT)
    String USER_ID = "user_id";
}
